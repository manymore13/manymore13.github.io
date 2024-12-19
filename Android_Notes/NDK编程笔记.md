# NDK编程笔记
## [FFMpeg国内镜像](https://gitee.com/mirrors/ffmpeg)
## 参考链接
1. [NDK指南](https://developer.android.google.cn/ndk/guides)
2. [NDK库Api](https://developer.android.google.cn/ndk/reference)
3. [官方例子](https://developer.android.google.cn/ndk/samples)
4. [Android NDK 开发-韩曙亮](https://blog.csdn.net/han1202012/category_9493908.html)
5. [NDK r21编译FFmpeg 4.2.2（x86、x86_64、armv7、armv8）](https://blog.csdn.net/u011609853/article/details/105648099)
6. [ffmpeg-android-maker](https://github.com/Javernaut/ffmpeg-android-maker),[wiki](https://github.com/Javernaut/ffmpeg-android-maker/wiki/Setting-the-FFmpeg-version)
推荐使用这个脚本编译FFmpeg
```shell
./ffmpeg-android-maker.sh -abis=x86_64 -android=21 --source-git-branch=release/4.2.3
```
7. [wikiCompilationGuide/Android](http://trac.ffmpeg.org/wiki/CompilationGuide/Android) 
8. [FFmpeg官方WIKI](https://trac.ffmpeg.org/wiki/CompilationGuide/Generic)
9. [Android gradle SourceSets介绍与使用](https://blog.csdn.net/lbcab/article/details/72771729)
10. [Android：JNI调用C++自定义类的详细方法](https://blog.csdn.net/chaoqiangscu/article/details/83023762)
## 概念
1. NDK是什么？
一套可以帮助你把原生代码(c/c++)嵌入到Android应用中的工具
2. 适用性
重用已经有的原生库，计算密集型应用用原生代码可以提高性能，比如游戏
3. [JNI ORACLE官方介绍](https://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/jniTOC.html)
4. [了解JNI动态注册native方法](https://blog.csdn.net/u013278099/article/details/52003759)   
## 编译流程

```mermaid
graph LR
A(预处理)-->B(编译)
B-->C(汇编)
C-->D(链接)
```   
- **预处理(Preprocessing)**
   完成宏替换，文件引入，以及去除空行，注释等，为下一步编译做准备。也就是对各种预处理命令进行处理，包括头文件的包含，宏定义的扩展，条件编译的选择等。
    ```shell
    gcc -E test.c -o test.i
    ```
选项-E让gcc在预处理结束后停止编译，test.i文件为预处理后输出的文件。-o指定输出文件
- **编译(Compilation)**
![编译](../Othor_Notes/img/编译.png)  
    ```shell
    gcc -S test.i -o test.s
    ```
选项-S让gcc在编译结束后停止编译过程,test.s文件为编译后生成的汇编代码。

- **汇编**
  汇编就是把编译阶段生成的.s文件转成二进制**目标代码**，也就是机器代码(01序列)。
    ```shell
    gcc -c test.s -o test.o
    ```
选项-c让gcc在汇编结束后停止编译过程，test.o文件为生成的机器码目标文件。

- **链接**
链接就是将多个目标文件以及所需的库文件链接生成可执行目标文件的过程。
    ```shell
    gcc test.o -o test
    ```
    -o本质上是一个重命名选项。不使用-o选项时，默认生成的是a.out文件
- **一次性生成可执行文件**
  ```shell
  gcc -o test test.c
  ```
## 静态库与动态库
[静态库和动态库的区别](https://www.cnblogs.com/codingmengmeng/p/6046481.html)


## CMake使用
[cmake学习笔记之add_library、target_link_libraries和link_directories](https://blog.csdn.net/bigdog_1027/article/details/79113342)
[FFmpeg（四）：CMake移植FFmpeg的静态库到AndroidStudio中](https://www.jianshu.com/p/e51547d288fd)

## 编译工具(推荐使用CMake)
1. [CMake,Ndkbuild不同配置方法以及区别](https://blog.csdn.net/xiaoyu_93/article/details/53082088)   
2. [配置CMake-官方](https://developer.android.google.cn/studio/projects/configure-cmake#add-ndk-api)
3. [如何添加NDK API](https://developer.android.google.cn/studio/projects/configure-cmake#add-ndk-api)
添加ndk中的日志打印库
```java
cmake_minimum_required(VERSION 3.4.1)
include_directories( src/main/cpp/include/ )

add_library(hello-jni SHARED
            src/main/cpp/Hello.c)

add_library(callC SHARED
        src/main/cpp/javaCallC.c)

add_library(calljava SHARED
        src/main/cpp/cCallJava.cpp)

find_library(log-lib
        log)

# Include libraries needed for hello-jni lib
target_link_libraries(hello-jni
                      android
                      log)
target_link_libraries( calljava  ${log-lib} )
```
具体配置介绍参考 [配置CMake-官方](https://developer.android.google.cn/studio/projects/configure-cmake#add-ndk-api)

## 命令
1. 生成头文件:javah -d 存放的目录　完整类
2. 查看方法签名:javap　-s 完整类
3. kotlin类头文件生成必须要到 路径(app/build/tmp/kotlin-classes/debug)下javah 
```java
javap -s com.cn.katefm.jni.CcallJava
```   

## java调用C
直接看源码
java或者kotlin要调用ｃ/c++函数，必须声明native方法
他们用的关键字不一样。
```kotlin
package com.cn.katefm.jni

/**
 * c代码调用java代码
 * 查看该类所有方法签名
 * ~/AndroidStudioProjects/KateFM/app/build/tmp/kotlin-classes/debug$ javap -s com.cn.katefm.jni.CcallJava
 */
class CcallJava {

    init {
        System.loadLibrary("calljava")
    }

    external fun cCallmultiply(a: Int, b: Int):Int

    /**
     * 乘法运算
     */
    fun multiply(a: Int, b: Int) = a * b

}
```
```java
package com.cn.katefm.jni;

/**
 * java代码调用C代码
 */
public class JavaCallC {

    static {
        System.loadLibrary("callC");
    }

    /**
     * 两数想家
     * @param x
     * @param y
     * @return
     */
    public native int add(int x, int y);

    /**
     * 字符串拼接
     * @param s
     * @return
     */
    public native String sayHello(String s);

    /**
     * 修改字符串值并返回
     * @param intArray
     * @return
     */
    public native int[] increaseArrayEles(int[] intArray);

    /**
     * 密码检验
     * @param pwd 密码
     * @return
     */
    public native int checkPwd(String pwd);
}

```
```c++
//
// Created by manymore13 on 20-6-15.
//
// https://developer.android.com/studio/projects/configure-cmake
#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <string.h>
#include "com_cn_katefm_jni_JNI.h"


jint Java_com_cn_katefm_jni_JavaCallC_add
        (JNIEnv *env, jobject jobj, jint x, jint y) {
    return x + y;
};

jstring Java_com_cn_katefm_jni_JavaCallC_sayHello
        (JNIEnv *env, jobject jobj, jstring str) {
    char *jniStr = "我是jni";
    const char *jStr = (*env)->GetStringUTFChars(env, str, 0);
    jniStr = strcat(jniStr, jStr);
    return (*env)->NewStringUTF(env, jniStr);
};

JNIEXPORT jintArray JNICALL Java_com_cn_katefm_jni_JavaCallC_increaseArrayEles
        (JNIEnv *env, jobject jobj, jintArray jArray) {
    int len = (*env)->GetArrayLength(env, jArray);
    jint *jIntArray = (*env)->GetIntArrayElements(env, jArray, JNI_FALSE);
    jintArray resultArray = (*env)->NewIntArray(env, len);
    for (int i = 0; i < len; i++) {
        jint *current = jIntArray + i;
        *current = +10;
    }
    (*env)->SetIntArrayRegion(env, resultArray, 0, len, jIntArray);
    return resultArray;
};

JNIEXPORT jint JNICALL Java_com_cn_katefm_jni_JavaCallC_checkPwd
        (JNIEnv *env, jobject jobj, jstring passWord) {
    const char *origin = "123456";
    const char *userPassWord = (*env)->GetStringUTFChars(env, passWord, JNI_FALSE);
    int code = strcmp(origin, userPassWord);
    if (code == 0) {
        return 200;
    } else {
        return 400;
    }
};




```
## C调用java

```c++
//
// Created by manymore13 on 20-6-16.
//

#include "com_cn_katefm_jni_CcallJava.h"
#include <iostream>
#include <android/log.h>

#define  LOG_TAG  "test"
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGD(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jint JNICALL Java_com_cn_katefm_jni_CcallJava_cCallmultiply
        (JNIEnv *env, jobject jobj, jint a, jint b) {
    //1. 得到字节码
    jclass jclazz = (env)->FindClass("com/cn/katefm/jni/CcallJava");
    //2. 得到方法
    jmethodID jmethodId = env->GetMethodID(jclazz, "multiply", "(II)I");
    //3.实例化该类语言打印日志
    //jobject  jobject = env->AllocObject(jclazz);
    //4.调用该方法
    LOGD("我是日志打印");
    return env->CallIntMethod(jobj,jmethodId,a,b);

}
```

native层调用java层弹Toast
```java
jclass jclazz = env->FindClass("com/cn/katefm/MainActivity");
    jmethodID jmethodId = env->GetMethodID(jclazz, "showToast", "(Ljava/lang/String;)V");
    env->CallVoidMethod(thiz, jmethodId, str);
```

## BUG
1. [解决Error: undefined reference to `__android_log_print'](https://blog.csdn.net/guyuealian/article/details/78310025)
