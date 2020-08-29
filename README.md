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


## 3. `wait()` `notify()` `notifyAll()`
`wait()`,`notify()`和`notifyAll()`必须要与`synchronized(resource)`一起使用。
* `wait()`,`notify()`和`notifyAll()`方法是本地方法，并且为`final`方法，无法被重写。
* 调用某个对象的`wait()`方法能让当前线程阻塞，并且当前线程必须拥有此对象的monitor（即锁，或者叫管程）
* 调用某个对象的`notify()`方法能够唤醒一个正在等待这个对象的monitor的线程，如果有多个线程都在等待这个对象的monitor，则只能唤醒其中一个线程；
* 调用`notifyAll()`方法能够唤醒所有正在等待这个对象的monitor的线程；

## 4. synchronized

* 使用this

 当两个并发线程访问同一个对象object中的synchronized(this)同步代码块时，一段时间内只能有一个线程被执行，另一个线程必须等待当前线程执行完这个同步代码块以后才能执行该代码块。
 当一个线程访问object的一个synchronized同步代码块时，另一个线程仍然可以访问该object对象中的非synchronized(this)同步代码块。

* 将任意对象作为对象监视器

多个线程调用同一个对象中的不同名称的synchronized同步方法或synchronized(this)同步代码块时，调用的效果就是按顺序执行，也就是同步的，阻塞的。
这说明synchronized同步方法或synchronized(this)同步代码块分别有两种作用。

      （1）synchronized同步方法

       ①对其他synchronized同步方法或synchronized（this）同步代码块呈阻塞状态。

       ②同一时间只有一个线程可以执行synchronized同步方法中的代码

      （2）synchronized(this)同步代码块

       ①对其他synchronized同步方法或synchronized（this）同步代码块呈阻塞状态。

       ②同一时间只有一个线程可以执行synchronized(this)同步代码块中的代码。

锁非this对象具有一定的优点：如果一个类中有很多个synchronized方法，这时虽然能实现同步，但会受到阻塞，所以影响效率；
但如果使用同步代码块锁非this对象，则synchronized(非this)代码块的程序与同步方法是异步的。不与其他锁this同步方法争抢this锁，则可以大大提高运行效率。

使用synchronized(非this对象x)同步代码块 进行同步操作时，对象监视器必须是同一个对象。如果不是同一个对象监视器，运行的结果就是异步调用了，就会交叉运行。