# 蓝牙

## 开源库

1. [Android-Scanner-Compat-Library](https://github.com/NordicSemiconductor/Android-Scanner-Compat-Library)
The Scanner Compat library solves the problem with scanning for Bluetooth Low Energy devices on Android.

2. [Android-BLE-Library](https://github.com/NordicSemiconductor/Android-BLE-Library)
An Android library that solves a lot of Android's Bluetooth Low Energy problems. The BleManager class exposes high level API for connecting and communicating with Bluetooth LE peripherals. The API is clean and easy to read.

##　相关概念

1. [GATT协议及蓝牙核心系统结构](https://blog.csdn.net/ohyeahhhh/article/details/52175596)
2. [BLE4.0教程二 蓝牙协议之服务与特征值分析](https://www.cnblogs.com/asam/p/8676369.html)

蓝牙4.0是以参数来进行数据传输的，即服务端定好一个参数，客户端可以对这个参数进行读，写，通知等操作，这个东西我们称之为特征值（characteristic），
但一个参数不够我们用，比如我们这个特征值是电量的值，另一个特征值是设备读取的温度值。那这时候会有多个特征值，并且我们还会对它们分类，分出来的类我们称之为服务（service）。
一个设备可以有多个服务，每一个服务可以包含多个特征值。为了方便操作，每个特征值都有他的属性，例如长度（size）,权限（permission），值（value）,描述（descriptor），如下图。
![img](https://img-blog.csdn.net/20160810192733658)

3. [蓝牙【GATT】协议介绍](http://murata.eetrend.com/article/2017-11/1000980.html)
GATT 的全名是 Generic Attribute Profile（姑且翻译成：普通属性协议），它定义两个 BLE 设备通过叫做 Service 和 Characteristic 的东西进行通信。GATT 就是使用了 ATT（Attribute Protocol）协议，ATT 协议把 Service, Characteristic遗迹对应的数据保存在一个查找表中，次查找表使用 16 bit ID 作为每一项的索引。

4. [Android 蓝牙BLE开发详解](https://juejin.cn/post/6844903636930134030)

5. [Android BLE 蓝牙开发入门](https://www.jianshu.com/p/3a372af38103)