package com.yingsheng.blektx.core

import androidx.annotation.Keep

@Keep
enum class BleStatus {
    /**
     * 蓝牙连接上
     */
    CONNECTED,

    /**
     * 蓝牙断开
     */
    DISCONNECTED,

    NONE

}

data class DeviceInfo(val mac: String = "", val name: String? = "", val bleStatus: BleStatus = BleStatus.NONE)