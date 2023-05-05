package com.yingsheng.blektx.core.action

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic

/**
 * ble操作
 */
interface IBleAction {

    /**
     * 动作，干活的,比如 WriteRequest 这里面做写操作
     */
    fun doAction():Boolean

    /**
     * onCharacteristicRead, onCharacteristicWrite，onDescriptorWrite回调
     */
    fun onAction(status: Int)

    /**
     * notify
     */
    fun onNotify(gatt: BluetoothGatt, characte: BluetoothGattCharacteristic)
}