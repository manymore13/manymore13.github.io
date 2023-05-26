package com.yingsheng.blektx.core

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import androidx.annotation.Keep
import java.util.*

/**
 * ble data解析
 */
@Keep
abstract class DataParser {

    val receiverData: MutableList<Byte> = mutableListOf()

    lateinit var filterUUID: UUID

    abstract fun parseData(gatt: BluetoothGatt, charter: BluetoothGattCharacteristic): Boolean

}