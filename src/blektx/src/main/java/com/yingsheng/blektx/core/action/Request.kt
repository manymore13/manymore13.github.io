package com.yingsheng.blektx.core.action

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.util.Log
import com.yingsheng.blektx.core.BleHandler
import com.yingsheng.blektx.core.RequestCallBack
import java.util.concurrent.atomic.AtomicBoolean

abstract class Request(
    val gatt: BluetoothGatt?,
    val character: BluetoothGattCharacteristic?,
    val delay: Long = 0,
    val bleHandler: BleHandler
) : IBleAction {

    companion object {
        const val TAG = "BleService"
    }

    /**
     * 该request是否已经销毁
     */
    private val isDestroy: AtomicBoolean = AtomicBoolean(false)

    var callBack: RequestCallBack? = null

    var name: String? = ""
        get() {
            if (field.isNullOrBlank()) {
                return javaClass.simpleName
            }
            return field
        }

    override fun onNotify(gatt: BluetoothGatt, characte: BluetoothGattCharacteristic) {}

    /**
     * 该request是否已经销毁
     */
    fun isDestroy():Boolean{
        return isDestroy.get()
    }

    /**
     * request销毁时调用
     */
    open fun onDestroy() {
        isDestroy.set(true)
        callBack?.onDestroy()
    }

}