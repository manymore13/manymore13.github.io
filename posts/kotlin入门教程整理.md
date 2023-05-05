# Kotlin入门教程整理

----

## Kotlin基础语法

>在 Kotlin 中，所有东西都是对象

### 注释

与大多数现代语言一样，Kotlin 支持单行（或行末）与多行（块）注释。

```kotlin
// 这是单行注释

/* 这是多行
   块注释。 */

// Kotlin 中的块注释可以嵌套
/* 注释从这里开始
/* 包含嵌套的注释 */ 

并且在这里结束。 */
   
```

### 类型声明

- **使用关键字val 定义只读局部变量，只能为其赋值一次**

```kotlin
val a: Int = 1  // 立即赋值
val b = 2   // 自动推断出 `Int` 类型
val c: Int  // 如果没有初始值类型不能省略
c = 3       // 明确赋值

```

- **使用 var 关键字, 可重新赋值的变量**

```kotlin
// 自动推断出 `Int` 类型

var x = 5 

x += 1
```

### 字符串模板
```kotlin
var a = 1
// 模板中的简单名称：
val s1 = "a is $a" 

a = 2
// 模板中的任意表达式：
val s2 = "${s1.replace("is", "was")}, but now is $a"
```

### 声明函数返回值类型
1. 使用关键字**fun**定义**sum**函数，两个**Int**参数，返回**Int**函数
```kotlin
fun sum(x:Int, y:Int): Int {return x+y}
```
2. 将表达式作为函数体、返回值类型自动推断的函数
```kotlin
fun sum(a: Int, b: Int) = a + b
```
3. 函数返回无意义的值
```kotlin
fun printSum(a: Int, b: Int): Unit {
    println("sum of $a and $b is ${a + b}")
}
```
Unit 返回类型可以省略
``` kotlin
fun printSum(a: Int, b: Int) {
    println("sum of $a and $b is ${a + b}")
}
```

### 条件表达式
```kotlin
fun maxof(a: Int, b: Int): Int {
    if (a > b) {
        return a
    } else {
        return b
    }
}
```
也可以这么写
```kotlin
fun maxOf(a: Int, b: Int) = if (a > b) a else b
```
### 空值与 null 检测
当某个变量的值可以为 null 的时候，必须在声明处的类型后添加?来标识该引用可为空。

```kotlin
// 如果 str 的内容不是数字返回 null
fun parseInt(str: String): Int? {
    // ……
}
// 使用返回可空值的函数
fun printProduct(arg1: String, arg2: String) {
    val x = parseInt(arg1)
    val y = parseInt(arg2)

    // 直接使用 `x * y` 会导致编译错误，因为它们可能为 null
    if (x != null && y != null) {
        // 在空检测后，x 与 y 会自动转换为非空值（non-nullable）
        println(x * y)
    }
    else {
        println("'$arg1' or '$arg2' is not a number")
    }    
}

// 或者这么使用
if (x == null) {
    println("Wrong number format in arg1: '$arg1'")
    return
}
if (y == null) {
    println("Wrong number format in arg2: '$arg2'")
    return
}

// 在空检测后，x 与 y 会自动转换为非空值
println(x * y)
```

### 类型检测与自动类型转换
is 运算符检测一个表达式是否某类型的一个实例。 如果一个不可变的局部变量或属性已经判断出为某类型，那么检测后的分支中可以直接当作该类型使用，无需显式转换。
```kotlin
// eg1
fun getStringLength(obj: Any): Int? {
    if (obj is String) {
        // `obj` 在该条件分支内自动转换成 `String`
        return obj.length
    }

    // 在离开类型检测分支后，`obj` 仍然是 `Any` 类型
    return null
}
// eg2
fun getStringLength(obj: Any): Int? {
    if (obj !is String) return null

    // `obj` 在这一分支自动转换为 `String`
    return obj.length
}
// eg3
fun getStringLength(obj: Any): Int? {
    // `obj` 在 `&&` 右边自动转换成 `String` 类型
    if (obj is String && obj.length > 0) {
      return obj.length
    }

    return null
}

```
### 循环
1. for循环

```kotlin
val items = listOf("apple", "banana", "kiwifruit")
for (item in items) {
    println(item)
}
```
```kotlin
val items = listOf("apple", "banana", "kiwifruit")
for (index in items.indices) {
    println("item at $index is ${items[index]}")
}
```
2. while循环

```kotlin
val items = listOf("apple", "banana", "kiwifruit")
var index = 0
while (index < items.size) {
    println("item at $index is ${items[index]}")
    index++
}
```
3. when表达式
```kotlin
fun describe(obj: Any): String =
    when (obj) {
        1          -> "One"
        "Hello"    -> "Greeting"
        is Long    -> "Long"
        !is String -> "Not a string"
        else       -> "Unknown"
    }
```
4. 使用区间（range）
使用 in 运算符来检测某个数字是否在指定区间内
```kotlin
val x = 10
val y = 9
if (x in 1..y+1) {
    println("fits in range")
}
```
检测某个数字是否在指定区间外
```kotlin
val list = listOf("a", "b", "c")

if (-1 !in 0..list.lastIndex) {
    println("-1 is out of range")
}
if (list.size !in list.indices) {
    println("list size is out of valid list indices range, too")
}
```
区间迭代
```kotlin
for (x in 1..5) {
    print(x)
}
```
数列迭代
```kotlin
for (x in 1..10 step 2) {
    print(x)
}
println()
for (x in 9 downTo 0 step 3) {
    print(x)
}
```
### 集合
1. 对集合进行迭代
```kotlin
for (item in items) {
    println(item)
}
```
2. 使用 in 运算符来判断集合内是否包含某实例
```kotlin
when {
    "orange" in items -> println("juicy")
    "apple" in items -> println("apple is fine too")
}
```
3. 使用 lambda 表达式来过滤（filter）与映射（map）集合
```kotlin
val fruits = listOf("banana", "avocado", "apple", "kiwifruit")
fruits
  .filter { it.startsWith("a") }
  .sortedBy { it }
  .map { it.toUpperCase() }
  .forEach { println(it) }
```
### 创建基本类及其实例
```kotlin
val rectangle = Rectangle(5.0, 2.0)
val triangle = Triangle(3.0, 4.0, 5.0)
```
1. 类的修饰符
```kotlin
// 类属性修饰符
abstract    // 抽象类  
final       // 类不可继承，默认属性
enum        // 枚举类
open        // 类可继承，类默认是final的
annotation  // 注解类

// 访问权限修饰符
private    // 仅在同一个文件中可见
protected  // 同一个文件中或子类可见
public     // 所有调用的地方都可见
internal   // 同一个模块中可见
```
2. 内部类与嵌套类
- 构造器

在kotlin中有主，次两种构造器，写在类名后面的是**主构造器**，在类中的是**次构造器**，次构造器必须调用主构造器
，一个类最多只能有1个主构造器（也可以没有），而次构造器是没有个数限制。

主构造器中的参数在类的属性中使用，也可以在init代码块中使用。

```kotlin
class User constructor(var name: String) {
    
    init{
        this.name = name
    }
    constructor(name: String, id: Int) :this(name){
  
    }
    
}
```

主构造器里直接声明属性

```kotlin
class User(var name: String) {

}
// 等价于：
class User(name: String) {
  var name: String = name
}
```
- 嵌套类

```kotlin
class Outer {                  // 外部类
    private val bar: Int = 1
    class Nested {             // 嵌套类
        fun foo() = 2
    }
}

fun main(args: Array<String>) {
    val demo = Outer.Nested().foo() // 调用格式：外部类.嵌套类.嵌套类方法/属性
    println(demo)    // == 2
}
```
- 内部类
>内部类使用 inner 关键字来表示。内部类会带有一个对外部类的对象的引用，所以内部类可以访问外部类成员属性和成员函数
```kotlin
class Outer {
    private val bar: Int = 1
    var v = "成员属性"
    /**嵌套内部类**/
    inner class Inner {
        fun foo() = bar  // 访问外部类成员
        fun innerTest() {
            var o = this@Outer //获取外部类的成员变量
            println("内部类可以引用外部类的成员，例如：" + o.v)
        }
    }
}

fun main(args: Array<String>) {
    val demo = Outer().Inner().foo()
    println(demo) //   1
    val demo2 = Outer().Inner().innerTest() 
    println(demo2)   // 内部类可以引用外部类的成员，例如：成员属性
}
```

为了消除歧义，要访问来自外部作用域的 this，我们使用this@label，其中 @label 是一个 代指 this 来源的标签。

- 匿名内部类

使用对象表达式来创建匿名内部类：

```kotlin
class Test {
    var v = "成员属性"

    fun setInterFace(test: TestInterFace) {
        test.test()
    }
}

/**
 * 定义接口
 */
interface TestInterFace {
    fun test()
}

fun main(args: Array<String>) {
    var test = Test()

    /**
     * 采用对象表达式来创建接口对象，即匿名内部类的实例。
     */
    test.setInterFace(object : TestInterFace {
        override fun test() {
            println("对象表达式创建匿名内部类的实例")
        }
    })
}
```

### 接口

使用 interface 关键字定义接口，允许方法有默认实现

```kotlin
interface MyInterface {
    fun bar()    // 未实现
    fun foo() {  //已实现
      // 可选的方法体
      println("foo")
    }
}
```

实现接口
一个类或者对象可以实现一个或多个接口

```kotlin
class Child : MyInterface {
    override fun bar() {
        // 方法体
    }
}
```

- 函数重写

实现多个接口时，可能会遇到同一方法继承多个实现的问题

```kotlin
interface A {
    fun foo() { print("A") }   // 已实现
    fun bar()                  // 未实现，没有方法体，是抽象的
}

interface B {
    fun foo() { print("B") }   // 已实现
    fun bar() { print("bar") } // 已实现
}

class C : A {
    override fun bar() { print("bar") }   // 重写
}

class D : A, B {
    override fun foo() {
        super<A>.foo()
        super<B>.foo()
    }

    override fun bar() {
        super<B>.bar()
    }
}

fun main(args: Array<String>) {
    val d =  D()
    d.foo();
    d.bar();
}
```

### 匿名函数和高阶函数
```kotlin
/**
 * 匿名函數
 */
val stringLengFunc: (String) -> Int = { input ->
    input.length
}

/**
 * 高阶函数
 */
fun stringMapper(str: String, mapper: (String) -> Int): Int {
    // Invoke function
    return mapper(str)
}


fun main() {
    val stringLength = stringLengFunc("Android")

    val length = stringMapper("Android1", stringLengFunc)
    println(length)

}
```

### [操作符](http://www.kotlincn.net/docs/reference/operator-overloading.html)

代码例子：
```kotlin
fun main() {
    val counter1 = Counter(2)
    val counter2 = Counter(3)

    println(counter1 + counter2)

    val clock = Clock {
        100L
    }
}

data class Counter(val dayIndex: Int) {
    operator fun plus(increment: Counter): Counter {
        return Counter(dayIndex + increment.dayIndex)
    }

    operator fun minus(decrement: Int): Counter {
        return Counter(dayIndex - decrement)
    }

}

interface Clock {

    fun uptimeMillis(): Long

    companion object {
        inline operator fun invoke(crossinline block: () -> Long): Clock =
                object : Clock {
                    override fun uptimeMillis(): Long = block()
                }
    }
}
```

###　[作用域函数](https://www.kotlincn.net/docs/reference/scope-functions.html)


| 函数  | 对象引用 | 返回值            | 是否时扩展函数             |
| ----- | -------- | ----------------- | -------------------------- |
| let   | it       | Lambda 表达式结果 | 是                         |
| run   | this     | Lambda 表达式结果 | 是                         |
| with  | this     | Lambda 表达式结果 | 不是：把上下文对象当作参数 |
| apply | this     | 上下文对象        | 是                         |
| also  | it       | 上下文对象        | 是                         |




## 范型
[Java 泛型 <? super T> 中 super 怎么 理解？与 extends 有何不同？](https://www.zhihu.com/question/20400700)
out 相当于java里面的 <? extend>
in 相当于java里面的 <? super>

## 零散点记

### 注解
@file:JvmName("BarChartKit")

@JvmStatic

[Kotlin中@JvmOverloads 注解](https://www.jianshu.com/p/72d1959a7c56)

[kotlin 双冒号](https://blog.csdn.net/lv_fq/article/details/72869124)
Kotlin 中 双冒号操作符 表示把一个方法当做一个参数，传递到另一个方法中进行使用，通俗的来讲就是引用一个方法。

## 参考链接
1. [Kotlin 的变量、函数和类型](https://kaixue.io/kotlin-basic-1/)
2. [Kotlin 里那些「不是那么写的」](https://kaixue.io/kotlin-basic-2/)
3. [Kotlin 里那些「更方便的](https://kaixue.io/kotlin-basic-3/)
4. [Kotlin StateFlow 搜索功能的实践 DB + NetWork](https://blog.csdn.net/deng_hui_long/article/details/108969021)
