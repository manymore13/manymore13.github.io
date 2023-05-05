package com.yingsheng.blektx.core

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {

    override val coroutineContext: CoroutineContext = context

    override fun close() {
        Log.i("BleService", "coroutineContext cancel")
        coroutineContext.cancel()
    }
}
const val MAC_SEPARATOR = ':'

fun getBleMac(originMac: String): String {
    if(originMac.contains(Regex(MAC_SEPARATOR.toString()))){
        return originMac
    }
    val sb = StringBuilder()
    originMac.forEachIndexed { index, char ->
        sb.append(char)
        if ((index != 0) && (index != originMac.length - 1)
            && ((index+1) % 2 == 0)) {
            sb.append(MAC_SEPARATOR)
        }
    }
    return sb.toString()
}

fun main() {
    var originMac = "78:25:38:02:07:4A"

    println(getBleMac(originMac))

    originMac = "78253802074A"

    println(getBleMac(originMac))
}