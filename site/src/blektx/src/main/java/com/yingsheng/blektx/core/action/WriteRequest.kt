package com.yingsheng.blektx.core.action

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.util.Log
import com.blankj.utilcode.util.ConvertUtils
import com.yingsheng.blektx.core.BleHandler
import com.yingsheng.blektx.core.Data
import com.yingsheng.blektx.core.DataParser
import com.yingsheng.blektx.exception.WriteTimeOutException
import java.util.*
import kotlin.concurrent.timer

internal class WriteRequest(
    gatt: BluetoothGatt?,
    character: BluetoothGattCharacteristic?,
    val data: Data,
    delay: Long = 0,
    var dataParser: DataParser,
    bleHandler: BleHandler,
) : Request(gatt, character, delay, bleHandler) {

    private var curData: Data? = data

    /**
     * 超时定时器
     */
    private var timeOut: Timer? = null

    override fun doAction(): Boolean {
        if (delay >= 1000L) {
            timeOut = timer(name = name, initialDelay = delay, period = 100) {
                timeOut?.cancel()
                callBack?.onThrowException(WriteTimeOutException(""))
            }
        }

        return doWrite()
    }

    override fun onAction(status: Int) {
        when (status) {
            BluetoothGatt.GATT_SUCCESS -> {
                doWrite()
            }
            else -> {
                Log.d(TAG, "$name write status = $status")
            }
        }
    }

    override fun onNotify(gatt: BluetoothGatt, charter: BluetoothGattCharacteristic) {
        val recvComplete = dataParser.parseData(gatt, charter)
        // 收完了
        if (recvComplete) {
            onDestroy()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        curData = null
        timeOut?.cancel()
    }

    private fun doWrite(): Boolean {
        if (character == null) return false
        if (curData == null) {
            Log.d(TAG, "$name write end")
            return false
        }
        return curData?.run {
            if (!character.setValue(this.byteData.toByteArray())) {
                return false
            }
            val result = gatt?.writeCharacteristic(character) ?: false
            if (result) {
                Log.d(TAG, "doWrite "+ConvertUtils.bytes2HexString(character.value))
                curData = curData?.nextData
            }
            result
        } ?: false
    }
}
