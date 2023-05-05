package com.yingsheng.blektx.exception

import androidx.annotation.Keep

@Keep
class BleNotConnectedException(msg: String) : RuntimeException(msg)