package com.yingsheng.blektx.core.action

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import com.yingsheng.blektx.core.BleHandler

internal class ReadRequest(
    gatt: BluetoothGatt?,
    character: BluetoothGattCharacteristic?,
    delay: Long = 0,
    bleHandler: BleHandler
) : Request(gatt, character, delay, bleHandler) {

    override fun doAction(): Boolean {
        return character?.run {
            gatt?.readCharacteristic(this)
        } ?: false
    }

    override fun onAction(status: Int) {
        when (status) {
            BluetoothGatt.GATT_SUCCESS -> {
                bleHandler.clearRequest(this)
            }
        }
    }
}