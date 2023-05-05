package com.yingsheng.blektx.utils

import android.app.Application
import android.content.pm.PackageManager
import android.os.Looper
import com.yingsheng.blektx.exception.BleNotSupported
import com.yingsheng.blektx.ext.missingSystemFeature

@Throws(BleNotSupported::class)
fun assertBle(app: Application) {
    app.packageManager.takeIf {
        it.missingSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }?.also { throw BleNotSupported() }
}

fun assertMainThread() {
    if (Looper.getMainLooper() != Looper.myLooper()) {
        throw RuntimeException("it is not main thread")
    }
}