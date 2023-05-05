package com.yingsheng.blektx.core

/**
 * 一个mtu数据
 */
class Data(var byteData: MutableList<Byte> = mutableListOf()) {

    var nextData: Data? = null

    fun isEmpty(): Boolean {
        return byteData.isEmpty() && nextData == null
    }
}



class Datas(var byteData: MutableList<Char> = mutableListOf()) {

    var nextData: Datas? = null

    fun isEmpty(): Boolean {
        return byteData.isEmpty() && nextData == null
    }
}