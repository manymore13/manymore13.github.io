package com.yingsheng.blektx.core

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic

/**
 * 外部回调，提供给外部使用
 */
abstract class RequestCallBack {

    /**
     * request销毁
     */
    open fun onDestroy() {}

    /**
     * notify回调
     */
    open fun onNotify(gatt: BluetoothGatt, charter: BluetoothGattCharacteristic) {}

    open fun onThrowException(e: Exception) {}

}