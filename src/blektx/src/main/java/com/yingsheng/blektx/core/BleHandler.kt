package com.yingsheng.blektx.core

import android.bluetooth.*
import android.os.SystemClock
import androidx.annotation.Keep
import com.blankj.utilcode.util.ConvertUtils
import com.yingsheng.blektx.core.action.Request
import com.yingsheng.blektx.ext.dLog
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

class BleHandler : BluetoothGattCallback() {

    private val mQueue: LinkedList<Request> = LinkedList()

    private val mCallbackList = LinkedList<BluetoothGattCallback>()

    private var curRequest: Request? = null

    private val isProcessIng: AtomicBoolean = AtomicBoolean(false)

    private val reqCurTime: AtomicLong = AtomicLong(0)

    var blescope: CloseableCoroutineScope? = null

    var mtu: Int = 20

    companion object {

        @Keep
        const val TAG = "BleService"

        const val DEFAILT_TIME_OUT_MIS: Long = 15000
    }

    private fun forEachCallback(callback: (BluetoothGattCallback) -> Unit) {
        mCallbackList.forEach {
            callback(it)
        }
    }

    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        when (newState) {
            BluetoothProfile.STATE_CONNECTED -> {
                dLog(TAG, "Connected to GATT server.")
                gatt.requestMtu(257)
//                gatt.discoverServices()
            }
            else -> {
                dLog(TAG, "Disconnected from GATT server. status = $status newState=$newState")
                release()
            }
        }

        forEachCallback {
            it.onConnectionStateChange(gatt, status, newState)
        }

    }

    override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
        super.onMtuChanged(gatt, mtu, status)
        when (status) {
            BluetoothGatt.GATT_SUCCESS, 4 -> {
                this.mtu = Math.max(mtu, 20)
                gatt?.discoverServices()
            }
            else ->{
//                gatt?.requestMtu(mtu)
            }
        }
        dLog(TAG, "onMtuChanged $mtu $status")

    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        when (status) {
            BluetoothGatt.GATT_SUCCESS -> {
                dLog(TAG, "onServicesDiscovered received: $status")
//                val coroutineDispatcher = Executors.newFixedThreadPool(1).asCoroutineDispatcher()
                blescope = CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
//                for(i in 0..100)
//                blescope?.launch {
//                    println(i)
//                }

            }
            else -> dLog(TAG, "onServicesDiscovered received: $status")
        }
        forEachCallback { it.onServicesDiscovered(gatt, status) }
    }

    override fun onCharacteristicRead(
        gatt: BluetoothGatt, characte: BluetoothGattCharacteristic, status: Int
    ) {
        blescope?.launch {
            curRequest?.onAction(status)
            forEachCallback { it.onCharacteristicRead(gatt, characte, status) }
        }

    }

    override fun onCharacteristicWrite(
        gatt: BluetoothGatt, characte: BluetoothGattCharacteristic, status: Int
    ) {
//        dLog(
//            TAG,
//            "onCharw ${ConvertUtils.bytes2HexString(characte.value)}  status = $status "+ Thread.currentThread().name
//        )
        blescope?.launch {
//            dLog(
//                TAG,
//                "write：status = $status ${ConvertUtils.bytes2HexString(characte.value)}"
//            )
//            delay(30)
            curRequest?.onAction(status)
            forEachCallback { it.onCharacteristicWrite(gatt, characte, status) }
        }
    }

    override fun onDescriptorWrite(
        gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int
    ) {
        dLog(
            TAG,
            "descriptor write：${ConvertUtils.bytes2HexString(descriptor.value)} status = $status"
        )

        blescope?.launch {
            curRequest?.onAction(status)
            forEachCallback { it.onDescriptorWrite(gatt, descriptor, status) }
        }
    }

    /**
     * 资源释放
     */
    fun release() = blescope?.launch {
        mQueue.clear()
        updateProcessState(false, null)
        blescope?.close()
        blescope = null
        isProcessIng.set(false)
        reqCurTime.set(0)
        curRequest = null
    }

    /**
     * Characteristic notification
     */
    override fun onCharacteristicChanged(
        gatt: BluetoothGatt, characte: BluetoothGattCharacteristic
    ) {
        dLog(
            TAG,
            "notify uuid = ${characte.uuid}, (0x)${
                ConvertUtils.bytes2HexString(characte.value)
            }"
        )
        blescope?.launch {
            curRequest?.onNotify(gatt, characte)
            forEachCallback { it.onCharacteristicChanged(gatt, characte) }
        }
    }

    fun addRequest(request: Request, isAddFirst: Boolean = false){
        if (isAddFirst) {
            mQueue.addFirst(request)
        } else {
            mQueue.add(request)
        }
        doNextRequest()
    }

    /**
     * 外部清除request
     */
    fun clearRequest(request: Request?) = blescope?.launch {
        mQueue.remove(request)
        request?.run {
            if (!this.isDestroy()) {
                this.onDestroy()
            }
        }
        updateProcessState(false, request)
    }

    fun registBleCallBack(callback: BluetoothGattCallback) {
        mCallbackList.add(callback)
    }

    fun unRegistBleCallBack(callback: BluetoothGattCallback) {
        mCallbackList.remove(callback)
    }

    /**
     * 正在处理中
     */
    private fun isProcessIng(): Boolean {
        return isProcessIng.get()
    }

    /**
     * 请求是否超时
     */
    private fun reqIsTimeOut(): Boolean {
        return SystemClock.elapsedRealtime() - reqCurTime.get() > DEFAILT_TIME_OUT_MIS
    }

    /**
     * 设置处理的状态
     */
    private fun updateProcessState(processIng: Boolean, request: Request? = null) =
        blescope?.launch {
            if (processIng) {
                // 设置处理中
                if (curRequest != request) {
                    curRequest = request
                    isProcessIng.set(processIng)
                    reqCurTime.set(SystemClock.elapsedRealtime())

                }
            } else {
                // 设置空闲中
                if (curRequest == request) {
                    isProcessIng.set(processIng)
                    reqCurTime.set(0)
                    curRequest = null
                }
            }
        }

    fun doNextRequest() = blescope?.launch {
//        dLog(TAG, "isProcessIng = ${isProcessIng()}")
        if (isProcessIng() && !reqIsTimeOut()) {
            return@launch
        }
        val request = mQueue.poll() ?: return@launch
        dLog(TAG, "-->> cmd: " + request.name + " queueSize = ${mQueue.size}")
        updateProcessState(true, request)
        val result = request.doAction()
        if (!result) {
            delay(1000)
            request.doAction()
        }
    }
}

@DelicateCoroutinesApi
fun main() = runBlocking {

    val coroutineDispatcher = Executors.newFixedThreadPool(1).asCoroutineDispatcher()
    for (i in 0..1000) {
        GlobalScope.launch(SupervisorJob() + coroutineDispatcher) {
            println("$i ")
        }
    }
    delay(5000)
    println("end")
}