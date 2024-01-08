# MotionLayout 学习
[motionlayout 谷歌官方](https://developer.android.com/training/constraint-layout/motionlayout?hl=zh-cn)

[MotionLayout介绍 掘金翻译](https://juejin.cn/post/6844903746896396295)

[MotionLayout 使用说明书（入门级）](https://juejin.cn/post/6860854727874363405#heading-22)

[MotionLayout 使用说明书（进阶+实战）](https://blog.csdn.net/knight1996/article/details/109678361)

代码控制播放转场动效
```java
motionLayout.setTransition(R.id.s1, R.id.s2) //blue to orange transition
motionLayout.transitionToEnd()
```

[MotionLayout sample](https://github.com/android/platform-samples/tree/main/samples/user-interface/constraintlayout)

[android:visibility changes to children of MotionLayout](https://stackoverflow.com/questions/57168071/androidvisibility-changes-to-children-of-motionlayout)

高斯模糊(毛玻璃)
[Blurry](https://github.com/wasabeef/Blurry)

# 按钮点击时增加动画效果
1. 点击时播放动画,监听事件执行对应动画，扩展性不好
  [安卓点击按钮实现缩放效果，点击完成恢复原状](https://blog.csdn.net/lgz0921/article/details/119353183)
2. Android StateListAnimator 方式实现效果    
  [聊聊 Android StateListAnimator](https://www.jianshu.com/p/4ad49e78e0b6)     
  [设置控件按下点击时的动画](https://blog.csdn.net/u013718730/article/details/88862624)  
   
