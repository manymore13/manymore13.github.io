package com.yingsheng.blektx.utils

/**
 * kotlin 代理 ，只能接口代理
 */
interface Base {
    fun print()
}

class BaseImpl(val x: Int) : Base {
    override fun print() {
        print(x)
    }
}

class Derived(b: Base) : Base by b{
//    override fun print() {
//        print("Derived")
//    }

    fun test(){
        print("test")
    }
}

fun main() {

    val b = BaseImpl(10)

    val r = Derived(b)
    r.print()
    r.test()

    var intArray = intArrayOf(0,1,2,3,4,5,6,7,8,9)
    print(intArray.toList().subList(3,10))
}