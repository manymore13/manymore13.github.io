package com.yingsheng.blektx.core

import android.app.Activity
import android.app.Application
import android.bluetooth.*
import android.bluetooth.BluetoothGatt.GATT_SUCCESS
import android.bluetooth.le.ScanCallback
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import androidx.annotation.Keep
import com.yingsheng.blektx.core.action.NotifyRequest
import com.yingsheng.blektx.core.action.ReadRequest
import com.yingsheng.blektx.core.action.WriteRequest
import com.yingsheng.blektx.exception.BleNotConnectedException
import com.yingsheng.blektx.exception.RemoteDeviceNotFoundExceprion
import com.yingsheng.blektx.ext.dLog
import com.yingsheng.blektx.ext.spliteData
import com.yingsheng.blektx.utils.assertBle
import com.yingsheng.blektx.utils.assertMainThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * ble客户端
 */
abstract class BleClient {

    private val allNotify = mutableListOf<Pair<UUID, UUID>>()

    private var bleHandler: BleHandler = BleHandler()

    private var mBluetoothGatt: BluetoothGatt? = null

    private var mDevice: BluetoothDevice? = null

    /**
     * 是否已经连接上设备
     */
    private var isConnected = false

    private val _bleState = MutableStateFlow(DeviceInfo())

    val bleInfo: StateFlow<DeviceInfo> get() = _bleState

    private val blescope: CloseableCoroutineScope?
        get() {
            return bleHandler.blescope
        }

    /**
     * mac地址
     */
    private var mac: String? = null
        set(value) {
            if (value == field) {
                return
            } else {
                disconnect()
                field = value
            }
        }

    private val mtu: Int
        get() {
            return bleHandler.mtu
        }

    lateinit var app: Application

    companion object {

        const val TAG = "BleClient"

        @JvmStatic
        val CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID: UUID =
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }


    private var bluetoothAdapter: BluetoothAdapter? = null

    private val BluetoothAdapter.isDisabled: Boolean
        get() = !isEnabled

    private val mBleSearcher: BleSearcher by lazy(LazyThreadSafetyMode.NONE) {
        BleSearcher(bluetoothAdapter, Handler(Looper.getMainLooper()))
    }

    @Keep
    private val bluetoothReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)) {
                BluetoothAdapter.STATE_ON -> {
                    retryConnect()
                    dLog(TAG, "BluetoothAdapter.STATE_ON ")
                }
                BluetoothAdapter.STATE_OFF -> {
                    dLog(TAG, "BluetoothAdapter.STATE_OFF ")
                    disconnect()
                }
            }
        }

    }

    fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = blescope?.launch(context, start, block)

    protected open fun registBleCallBack(bleCallBack: BluetoothGattCallback) {
        bleHandler.registBleCallBack(bleCallBack)
    }

    /**
     * 初始化
     */
    @Keep
    fun init(application: Application) {
        assertMainThread()
        this.app = application

        val bluetoothManager =
            this.app.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        assertBle(this.app)
        registBluetoothChangeReceiver(application)
        bleHandler.registBleCallBack(object : BluetoothGattCallback() {
            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                when (status) {
                    GATT_SUCCESS -> {
                        isConnected = true

                        if (isRequiredServiceSupported(gatt)) {
                            initAction()
//                            toast("设备已连接 ")
                            val device = gatt.device
                            _bleState.value =
                                DeviceInfo(device.address, device.name, BleStatus.CONNECTED)
                        } else {
                            disconnect()
                        }
                    }
                }
            }

            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                when (newState) {
                    BluetoothProfile.STATE_CONNECTED -> {

                    }
                    else -> {
                        disconnect()
                    }
                }

            }
        })
    }

    private fun registBluetoothChangeReceiver(application: Application) {
        val filter = IntentFilter().apply { addAction(BluetoothAdapter.ACTION_STATE_CHANGED) }
        application.registerReceiver(bluetoothReceiver, filter)
    }

    private fun unRegistBluetoothChangeReceiver(application: Application) {
        application.unregisterReceiver(bluetoothReceiver)
    }

    open fun initAction() {
        registNotify()
    }

    /**
     * 注册notify
     */
    private fun registNotify() {

        allNotify.forEach {
            val service = it.first
            val characteristic = it.second

            val notifyRequest = NotifyRequest(
                mBluetoothGatt,
                getCharacteristic(service, characteristic),
                bleHandler = bleHandler,
                enable = true
            )

            notifyRequest.callBack = object : RequestCallBack() {
                override fun onDestroy() {
                    bleHandler.clearRequest(notifyRequest)
                    nextRequest()
                }
            }

            bleHandler.addRequest(notifyRequest, true)
        }
    }

    @Keep
    fun addAllNotify(notifyPair: Pair<UUID, UUID>) {
        this.allNotify.add(notifyPair)
    }

    @Keep
    fun isConnected(): Boolean {
        return mBluetoothGatt != null && isConnected
    }

    /**
     * 通过mac地址连接设备
     */
    @Keep
    fun connectWithMac(autoConnect: Boolean, mac: String) {
        assertMainThread()
        if (TextUtils.isEmpty(mac)) {
            dLog(TAG, "mac is empty")
            return
        }

        this.mac = getBleMac(mac)

        if (isConnected()) {
            dLog(TAG, "蓝牙已连接")
            return
        }

        try {
            if (mBluetoothGatt?.device?.address.equals(this.mac)) {
                mBluetoothGatt?.connect()
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mDevice = bluetoothAdapter?.getRemoteDevice(this.mac)

        val remoteDevice =
            mDevice ?: throw RemoteDeviceNotFoundExceprion()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBluetoothGatt =
                remoteDevice.connectGatt(app, autoConnect, bleHandler, BluetoothDevice.TRANSPORT_LE)
        } else {
            mBluetoothGatt = remoteDevice.connectGatt(app, autoConnect, bleHandler)
        }
    }

    /**
     * 连接设备
     */
    @Keep
    fun connectWithDevice(autoConnect: Boolean, remoteDevice: BluetoothDevice) {
        assertMainThread()
        this.mac = remoteDevice.address
        if (isConnected()) {
            dLog(TAG, "蓝牙已连接")
            return
        }

        try {
            if (mBluetoothGatt?.device?.address.equals(this.mac)) {
                mBluetoothGatt?.connect()
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mDevice = remoteDevice


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBluetoothGatt =
                remoteDevice.connectGatt(app, autoConnect, bleHandler, BluetoothDevice.TRANSPORT_LE)
        } else {
            mBluetoothGatt = remoteDevice.connectGatt(app, autoConnect, bleHandler)
        }
    }

    private fun retryConnect() {
        mac?.let { connectWithMac(true, it) }
    }


    fun write(service: UUID, characte: UUID, values: ByteArray): Boolean {
        return getCharacteristic(service, characte)?.run {
            val storeSuccess = this.setValue(values)
            if (storeSuccess) {
                return mBluetoothGatt?.writeCharacteristic(this) ?: false
            }
            return false
        } ?: false
    }

    /**
     * @param service 服务的UUID
     * @param characte 服务里面的特征值UUID
     * @param values 需要写入蓝牙的数据
     * @param delay 分包延迟,单位毫秒，默认不延迟
     * @param notifyUUID 写入数据后，有数据从Notify通道返回，通道的UUID，如果是NULL,数据发送成功即刻返回
     */
    @Keep
    @ExperimentalCoroutinesApi
    fun write(
        service: UUID, characte: UUID, values: ByteArray,
        delay: Long = 0, filterUUID: UUID, tag: String = ""
    ): Flow<ByteArray> {
        return callbackFlow {
            if (!isConnected()) {
                throw BleNotConnectedException("")
            }
            val data = values.spliteData(mtu - 3)
            val request = WriteRequest(
                mBluetoothGatt,
                character = getCharacteristic(service, characte),
                data = data,
                delay = delay,
                dataParser = getDataparse(),
                bleHandler = bleHandler
            ).apply {
                dataParser.filterUUID = filterUUID
                name = tag
                callBack = object : RequestCallBack() {
                    override fun onDestroy() {
                        trySend(dataParser.receiverData.toByteArray())
                        bleHandler.clearRequest(this@apply)
                        nextRequest()
                        Log.d(TAG,"cmd ${name} onDestroy")
                        close()
                    }

                    override fun onThrowException(e: Exception) {
                        close(e)
                    }
                }

            }

            bleHandler.addRequest(request)
            awaitClose {
                bleHandler.clearRequest(request)
                dLog(TAG, "${request.name} write callback close")
            }
        }
    }

    @Keep
    @ExperimentalCoroutinesApi
    fun read(service: UUID, characte: UUID): Flow<ByteArray> {
        if (!isConnected()) {
            return emptyFlow()
        }
        return callbackFlow<ByteArray> {

//            val bleResponse: BluetoothGattCallback = object : BluetoothGattCallback() {
//                override fun onCharacteristicRead(
//                    gatt: BluetoothGatt?,
//                    characteristic: BluetoothGattCharacteristic,
//                    status: Int
//                ) {
//                    trySend(characteristic.value)
//                }
//            }
            val readRequest =
                ReadRequest(
                    mBluetoothGatt,
                    getCharacteristic(service, characte),
                    bleHandler = bleHandler
                )
            readRequest.callBack = object : RequestCallBack() {
                override fun onDestroy() {
                    close()
                }
            }
            bleHandler.addRequest(readRequest)
            awaitClose {
                bleHandler.clearRequest(readRequest)
                dLog(TAG, "read callback close")
            }
        }

    }

    private fun getCharacteristic(service: UUID, characte: UUID): BluetoothGattCharacteristic? {
        return mBluetoothGatt?.getService(service)?.getCharacteristic(characte)
    }

    protected fun nextRequest(forceNext: Boolean = false) {
        bleHandler.doNextRequest()
    }

    @Keep
    fun disconnect(device: BluetoothDevice? = null) {

        bleHandler.release()
        mBluetoothGatt?.close()
        mBluetoothGatt = null
        isConnected = false

        val tDevice: BluetoothDevice? = device ?: mDevice

        if (tDevice != null) {
            _bleState.value =
                DeviceInfo(tDevice.address, tDevice.name, BleStatus.DISCONNECTED)
        }
        mDevice = null

    }


    /**
     * 搜索附近设备
     */
    @Keep
    fun search(scanMillis: Long, bleScanCallback: ScanCallback) {
        mBleSearcher.scanBLeDevice(scanMillis, bleScanCallback)
    }

    /**
     * 停止搜索附近设备
     */
    @Keep
    fun stopSearch() {
        mBleSearcher.stopLeScanDevice()
    }

    /**
     * 开启蓝牙
     */
    @Keep
    fun checkBluetooth(act: Activity, requestEnableBtCode: Int) {
        bluetoothAdapter?.takeIf { it.isDisabled }?.apply {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            act.startActivityForResult(enableBtIntent, requestEnableBtCode)
        }
    }

    /**
     * 返回true，连接成功，返回false 断开连接
     */
    abstract fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean


    /**
     * 获取 data 解析
     */
    @Keep
    abstract fun getDataparse(): DataParser

}