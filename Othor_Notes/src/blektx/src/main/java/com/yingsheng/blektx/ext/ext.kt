package com.yingsheng.blektx.ext

import android.util.Log
import com.blankj.utilcode.util.ToastUtils
import com.yingsheng.blektx.BuildConfig

fun toast(msg: String) {
    if (BaseDatas.isTest) {
        ToastUtils.showLong(msg)
    }
}

fun dLog(tag: String, msg: String) {
//    if (BuildConfig.DEBUG) {
        Log.d(tag, msg)
//    }
}