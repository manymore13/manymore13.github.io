package com.yingsheng.lib_bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import androidx.annotation.Keep
import com.yingsheng.blektx.core.BleClient
import com.yingsheng.blektx.core.DataParser
import com.yingsheng.lib_bluetooth.cmd.HaylouCommand
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import java.util.*

@Keep
object HaylouBleClient : BleClient() {

    val serviceUUID: UUID = UUID.fromString("0000a00a-0000-1000-8000-00805f9b34fb")
    val writeUUID: UUID = UUID.fromString("0000b001-0000-1000-8000-00805f9b34fb")
    val writeAckUUID: UUID = UUID.fromString("0000b002-0000-1000-8000-00805f9b34fb")
    val notifyACKUUID: UUID = UUID.fromString("0000b005-0000-1000-8000-00805f9b34fb")
    val notifyUUID: UUID = UUID.fromString("0000b004-0000-1000-8000-00805f9b34fb")

    init {
        val needRegistNotify = mutableListOf(
            Pair(serviceUUID, notifyACKUUID),
            Pair(serviceUUID, notifyUUID)
        )
        needRegistNotify.forEach {
            addAllNotify(it)
        }

        registBleCallBack(object : BluetoothGattCallback() {
            override fun onCharacteristicChanged(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic
            ) {
                super.onCharacteristicChanged(gatt, characteristic)
                if (characteristic.uuid.equals(notifyACKUUID)) {
                    nextRequest(true)
                }
            }
        })
    }

    /**
     *
     */
    @ExperimentalCoroutinesApi
    fun write(data: ByteArray): Flow<ByteArray> {
        return write(serviceUUID, writeUUID, data, 0, notifyUUID, "")
    }

    fun writeCmd(cmd: HaylouCommand, delay: Long = 0): Flow<ByteArray> {
        return write(
            serviceUUID,
            writeUUID,
            cmd.getData(),
            delay,
            notifyUUID,
            cmd.javaClass.simpleName
        )
    }

    fun writeCmdNoResp(cmd: HaylouCommand, delay: Long = 3000): Flow<ByteArray> {
        return write(
            serviceUUID,
            writeUUID,
            cmd.getData(),
            delay,
            notifyACKUUID,
            cmd.javaClass.simpleName
        )
    }


    /**
     * 不需要返回值
     */
    @ExperimentalCoroutinesApi
    suspend fun writeOnly(data: ByteArray) {
        write(serviceUUID, writeUUID, data, 0, notifyUUID,"")
            .catch { e ->
                e.printStackTrace()
            }.collect { }
    }

    @ExperimentalCoroutinesApi
    fun read(characteristic: UUID): Flow<ByteArray> {
        return read(serviceUUID, characteristic)
    }

    override fun getDataparse(): DataParser {
        return HaylouDataParser()
    }

    override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {

//        val service = gatt.getService(serviceUUID)

        return true
    }

}