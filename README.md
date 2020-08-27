# java-learn

## 1. JVM
### 1.1 虚拟机栈的异常状况

在Java虚拟机规范中，对Java虚拟机栈规定了两种异常的状况：

* 如果线程请求的栈深度大于虚拟机所允许的深度，将抛出StackOverflowError异常；
* 如果虚拟机栈可以动态扩展，如果扩展时无法申请到足够的内存，将抛出OutOfMemoryError异常。

#### 1.1.1 StackOverflowError

JVM会为每个线程的虚拟机栈分配一定的内存大小（-Xss1024k，默认为1024k），因此虚拟机栈能够容纳的栈帧数量是有限的，若栈帧不断进栈而不出栈，最终会导致当前线程虚拟机栈的内存空间耗尽，典型如一个无结束条件的递归函数调用。

#### 1.1.2 OutOfMemoryError

不同于StackOverflowError，OutOfMemoryError指的是当整个虚拟机栈内存耗尽，并且无法再申请到新的内存时抛出的异常。

JVM未提供设置整个虚拟机栈占用内存的配置参数。虚拟机栈的最大内存大致上等于“JVM进程能占用的最大内存（依赖于具体操作系统） - 最大堆内存 - 最大方法区内存 - 程序计数器内存（可以忽略不计） - JVM进程本身消耗内存”。当虚拟机栈能够使用的最大内存被耗尽后，便会抛出OutOfMemoryError。



### JVM属性调优

* 1. -Xms 为JVM启动时分配的内存，比如-Xms200m，表示分配200M。
* 2. -Xmx 为JVM运行过程中分配的最大内存，比如-Xms500m，表示JVM进程最多只能够占用500M内存。
* 3. -Xss 为JVM启动的每个线程分配的内存大小，默认JDK1.8中是1M





## 2. String主意事项
`==`比较的是两个字符串的地址是否为相等（同一个地址），`equals()`方法比较的是两个字符串对象的内容是否相同

* `String`是`final`对象，不会被修改，每次使用 `+` 进行拼接都会创建新的对象，而不是改变原来的对象，也属于线程安全的；
* `StringBuffer`可变字符串，主要用于字符串的拼接，属于线程安全的；（`StringBuffer`的`append`操作用了`synchronized`）
* `StringBuilde`r可变字符串，主要用于字符串的拼接，属于线程不安全的；

`String a = "ab";`内存会去查找永久代(常量池) ，如果没有的话，在永久代中中开辟一块儿内存空间，把地址付给栈指针，如果已经有了"ABC"的内存，直接把地址赋给栈指针。
```java
String a = "a" + "b";
String b = "ab";
String c = new String("ab");
System.out.println(a == b); // true
System.out.println(a == c); // false
```
而`String c = new String("ab");`是根据`"ab"`这个String对象再次构造一个String对象;在堆中从新new一块儿内存，把指针赋给栈，将新构造出来的String对象的引用赋给c。 因此 只要是new String()，栈中的地址都是指向最新的new出来的堆中的地址。
