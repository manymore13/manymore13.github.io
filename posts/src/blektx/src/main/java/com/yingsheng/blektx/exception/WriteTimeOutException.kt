package com.yingsheng.blektx.exception

import androidx.annotation.Keep

@Keep
class WriteTimeOutException(msg: String) : RuntimeException(msg)