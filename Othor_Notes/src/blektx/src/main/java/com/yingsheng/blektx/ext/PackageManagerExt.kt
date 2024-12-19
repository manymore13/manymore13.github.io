package com.yingsheng.blektx.ext

import android.content.pm.PackageManager

fun PackageManager.missingSystemFeature(name: String): Boolean = !hasSystemFeature(name)
