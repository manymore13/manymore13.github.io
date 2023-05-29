Title: Android开源库
Date: 2020-2-3 11:00
Category: Android
Tags:Android,开源

# Android开源库

## [Retrofit](https://square.github.io/retrofit/)

>A type-safe HTTP client for Android and Java

## [RxJava](https://github.com/ReactiveX/RxJava)

>RxJava – Reactive Extensions for the JVM – a library for composing asynchronous and event-based programs using observable sequences for the Java VM.

[给 Android 开发者的 RxJava 详解](https://gank.io/post/560e15be2dca930e00da1083)

## [AndroidAutoSize](https://github.com/JessYanCoding/AndroidAutoSize)

>今日头条屏幕适配方案终极版，一个极低成本的 Android 屏幕适配方案

## UI

[QMUI_Android](https://github.com/Tencent/QMUI_Android)
>提高 Android UI 开发效率的 UI 库

[JKeyboardPanelSwitch](https://github.com/Jacksgong/JKeyboardPanelSwitch)
>For resolve the layout conflict when keybord & panel are switching (Android键盘面板冲突 布局闪动处理方案)

[MultiType](https://github.com/drakeet/MultiType)

>An Android library makes it easier and more flexible to create multiple types for RecyclerViews.

## 优化
[Android卡顿优化--卡顿检测](https://blog.csdn.net/gs344937933/article/details/89815855)
[AndroidPerformanceMonitor](https://github.com/markzhai/AndroidPerformanceMonitor/blob/master/README_CN.md) [框架原理](http://blog.zhaiyifan.cn/2016/01/16/BlockCanaryTransparentPerformanceMonitor/)
[leakcanary](https://square.github.io/leakcanary/getting_started/)
>SoftReference类、WeakReference类和PhantomReference类，它们分别代表软引用、弱引用和虚引用。ReferenceQueue类表示引用队列，它可以和这三种引用类联合使用，比如说：如果弱引用所引用的对象被垃圾回收，Java虚拟机就会把这个弱引用放到与之关联的ReferenceQueue中，所以我们如果在对象该回收的时候去检测队列如果发现队列中有这个对象的若应用说明这个对象被正常回收了，如果检测是发现没有对象的弱引用不在说明对象没有被正常回收就是发生了内存泄漏。但是实际操作中在我们认为对象该被回收的时候实际上可能有一定的延迟或者并没有发生GC，所以需要我们给他一定的回收时间和重试时间或者手动触发一下GC(不一定执行)。
[Android内存泄漏-LeakCanary源码原理分析](https://blog.csdn.net/u011148116/article/details/106762665)

[Stetho官网](http://facebook.github.io/stetho/) [Github源码](https://github.com/facebook/stetho)
Stetho是一个Android应用调试工具。集成后，开发人员可以通过Chrome的开发工具查看App相关的信息和调试。[chrome://inspect](chrome://inspect)

## 架构
[gradle配置优化及dependencies中各种依赖方式说明](https://blog.csdn.net/jinfulin/article/details/80421927)
>gradle使用，变量使用
[gradle使用技巧](https://blog.csdn.net/u012982629/article/details/81121717)
