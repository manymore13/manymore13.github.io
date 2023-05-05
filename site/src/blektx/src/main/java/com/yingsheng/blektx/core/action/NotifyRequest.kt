package com.yingsheng.blektx.core.action

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import com.yingsheng.blektx.core.BleClient
import com.yingsheng.blektx.core.BleHandler

internal class NotifyRequest(
    gatt: BluetoothGatt?,
    character: BluetoothGattCharacteristic?,
    delay: Long = 0,
    bleHandler: BleHandler,
    val enable: Boolean = true
) : Request(gatt, character, delay, bleHandler) {

    override fun doAction(): Boolean {
        return character?.run {
            gatt?.setCharacteristicNotification(this, enable)
            this.getDescriptor(BleClient.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID)?.run {
                val writeValue =
                    if (enable) BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
                if (!setValue(writeValue)) {
                    return false
                }
                return gatt?.writeDescriptor(this)?:false
            } ?: false
        } ?: false
    }

    override fun onAction(status: Int) {
        onDestroy()
    }
}