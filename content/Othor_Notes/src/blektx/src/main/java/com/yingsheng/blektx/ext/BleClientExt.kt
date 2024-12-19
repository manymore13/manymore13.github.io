package com.yingsheng.blektx.ext

import com.yingsheng.blektx.core.Data
import com.yingsheng.blektx.core.Datas

fun ByteArray.splite(dataSize: Int): MutableList<MutableList<Byte>> {
    val dataList = toList()
    val resultList = mutableListOf<MutableList<Byte>>()
    var index = 0
    val len = dataList.size
    while (index < len) {
        val toIndex = Math.min(index + dataSize, len)
        val tempList = dataList.subList(index, toIndex).toMutableList()
        resultList.add(tempList)
        index += dataSize
    }

    return resultList
}

fun ByteArray.spliteData(dataSize: Int): Data {
    val dataList = toList()
    val resultData = Data()
    var index = 0
    val len = dataList.size
    var curData = resultData
    while (index < len) {
        val toIndex = Math.min(index + dataSize, len)
        val tempList = dataList.subList(index, toIndex).toMutableList()
        if(curData.isEmpty()){
            curData.byteData = tempList
        }else{
            val tempData = Data(tempList)
            curData.nextData = tempData
            curData = tempData
        }
//        resultList.add(tempList)
        index += dataSize
    }

    return resultData
}

fun CharArray.spliteData(dataSize: Int): Datas {
    val dataList = toList()
    val resultData = Datas()
    var index = 0
    val len = dataList.size
    var curData = resultData
    while (index < len) {
        val toIndex = Math.min(index + dataSize, len)
        val tempList = dataList.subList(index, toIndex).toMutableList()
        if(curData.isEmpty()){
            curData.byteData = tempList
        }else{
            val tempData = Datas(tempList)
            curData.nextData = tempData
            curData = tempData
        }
//        resultList.add(tempList)
        index += dataSize
    }

    return resultData
}

fun main() {
    val charData = "abcdefghijklmnopqrstuvwxyz".toCharArray().spliteData(10)

    println("")

}