# Java 内存

本文基于 JDK 8。

## 分类

### 程序计数器（Program Counter Register）

- 内存空间较小，保存了当前线程执行代码的位置
- 线程私有
- 不会 OOM（Out Of Memory）

### 虚拟机栈（VM Stack）

- 保存了对象引用等
- 线程私有
- 会 OOM、StackOverflow
- - -Xss128k 指定栈大小

### 本地方法栈（Native Method Stack）

- 与虚拟机栈基本相同，执行的事本地方法
- 线程私有
- 会 OOM、StackOverflow
- - -Xss128k 指定栈大小

### 堆（Heap） 

- 对象的内容会在堆上分配，包括运行时常量池（Runtime Constant Pool）
- 线程公有
- 会 OOM
- 是 GC 管理的主要区域
- 用 Xmx、Xms 分别控制最大堆内存、最小堆内存，-Xms20m -Xmx20m，指定 OOM 时 dump，-XX:+HeapDumpOnOutOfMemory -XX:HeapDumpPath=e:/

### 方法区（Method Area）

- 存储了类信息、静态变量等
- 线程公有
- 内存分配在 JVM 之中，会 OOM
- 俗称永久代（Permanent Generation，简称 Perm Gen）
- 指定大小，-XX:PermSize=10M -XX:MaxPermSize=10m
- JDK 7 将永久代转移到堆中
- JDK 8 已移除永久代，转移到 Metaspace

### Metaspace

- 原来的 MethodArea
- 内存分配在系统内存中，超过系统可用内存，会 OOM
- 指定大小，-XX:MetaspaceSize=10m -XX:MaxMetaspaceSize=10m

### 直接内存

- 不是虚拟机运行的一部分，DirectByteBuffer 会用，主要是从内存中直接读取文件
- 内存分配在系统内存中，超过系统可用内存，会 OOM
- 指定大小，-XX:MaxDirectMemorySize=10m

## 内存分配

### 对象创建流程

- 遇到 new 关键字，查找对应类的符号引用，没有就进行类加载
- 分配内存
- 初始化为零值
- 设置对象相应的属性
- 执行对象的构造函数

### 内存分配方式

- 指针碰撞：计算出内存大小，指针向下移动
- 空闲列表：维护内存可用部分，进行分配

### 并发

- CAS 同步处理：并发大容易产生冲突，降低效率
- TLAB（Thread Local Allocation Buffer）：在线程所处空间中分配内存

### 内存存储

- 对象头（Header）：一部分是自身运行数据，考虑到空间效率，空间大小非固定，主要存储哈希码、GC 年代等；另一部分类型指针，存储对象类型信息，数组还要存储数组大小
- 实例数据（Instance Data）：存储实例数据，一般相同宽度的数据会被分配到一起，父类会在子类之前（CompactField 参数为 true，子类较窄的也可能插入到父类空隙之中）
- 对齐填充（Padding）：占位符，对象大小必须是 8 字节的整数倍

### 访问定位方式

- 句柄访问：栈 reference -> 句柄池 对象实例数据指针 -> 实例池，优点是移动对象时只需要改实例指针
- 直接指针访问： 栈 reference -> 对象实例数据指针（包括实例数据），HotSpot 采用，优点是访问速度快（减少了一次指针访问）
