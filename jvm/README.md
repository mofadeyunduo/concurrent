[TOC]
# JVM

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
- 指定栈大小，-Xss128k 

### 本地方法栈（Native Method Stack）

- 与虚拟机栈基本相同，执行的事本地方法
- 线程私有
- 会 OOM、StackOverflow
- 指定栈大小，-Xss128k 

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

## 对象存活算法

### 引用计数法（Reference Counting）

- 对象包含一个引用计数器，如果有引用该对象的对象，计数器 + 1；引用失效时，计数器 - 1
- 如果引用计数器为 0，对象可被回收
- 简单，效率高
- 不能解决循环引用的问题

### 可达性算法

- 从 GC roots 开始，根据结点向下搜索，没搜索到的就可以被回收
  - stack reference
  - static
  - final
  - native

## 回收

- 回收对象：会调用一次 finalize 方法，多次被回收，仅仅调用一次
- 回收方法区
  - String Pool 等：和普通对象一致
  - 类
    - 该类的实例是否被回收
    - ClassLoader 是否被回收
    - Class 对象没有被引用
    
## 回收算法

### 标记-清除（Mark-Sweep）

- 标记为可回收的，某一时刻统一回收
- 标记和清除效率不高
- 会产生大量的内存碎片，分配大对象时，内存不足

### 复制（Copying）

- 空间对半分，只用一块；一块用完时，将存活的对象复制到另外一块，按顺序划分内存
- 不会产生磁盘碎片
- 内存空间代价较高，一半是空闲的
- 适合存活率较低的空间

#### 改进

由于 98% 的对象都是朝生夕死的，不需要对半分空间。

- 首先分成三个空间，一块 Eden、两块 Survivor，比例 8:1:1，只使用一块 Eden、一块 Survivor
- 回收时，将 Eden 和 Survivor 存活的对象复制到另一块 Survivor
- 只有 10% 内存会浪费

### 标记-复制（Mark-Compact）

- 和标记-清楚类似，最后不清理内存，直接将活的对象复制
- 适合存活率较高的空间

## 分代收集（Generational Collection）

- 新生代：每次垃圾手机都会有大量对象死去，适合使用复制算法
- 老年代：存活率高，没有额外空间可以使用，适合使用标记-清除或标记复制算法
- Eden：新生代空间的一部分
- Survivor： 分为 from、to，为新生代空间一部分，GC 收集 Eden 和 from，转移非垃圾到 to 中或者老年代空间中

### 分代原因

新生代存活率低，老年代存活率高，不同的特点决定了不同的算法：新生代存活率低注定要频繁进行 Minor GC，而老年代存活率高会使 Major GC 频率降低。

## GC 类型

- Minor GC：针对新生代 GC
- Major GC：针对老年代 GC，通常会变成 Full GC
- Full GC：Major GC + Minor GC

## Java 收集器

### 枚举根节点

OopMap 保存了类型信息，包括引用，方便 GC roots 找引用链

### Safepoint、Safe Region

- 到达 Safepoint，才能进行 GC
- 常见 Safepoint
  - 方法调用
  - 循环跳转
  - 异常跳转
- 如何让所有代码跑到 Safepoint
  - 抢断式中断(Preemptive Suspension)：GC 发生时，强制中断；如果代码不在 Safepoint，让其跑到 Safepoint，一般不采用
  - 主动式中断（Voluntary Suspension）：各个线程主动去轮训该标志，如 test 指令
- Safe Region 是指代码引用不会发生变化的区域，GC 时安全

### 比较

| 名称 | 新生代 or 老年代 | 特点 | 适用场景 | 相关参数（前面加上 -XX:） |
| --- | --- | --- | --- | --- |
| Serial | 新生代 | 单线程 | 在客户端简单而高效 | UseSerialGC(Serial + Serial Old)|
| ParNew | 新生代 | Serial 多线程版本 | 适合于多核 CPU | UseParNewGC(ParNew + Serial Old) |
| Parallel Scavenge | 新生代 | 注重于吞吐量（吞吐量 = 运行用户代码时间 / (运行用户代码时间 + 垃圾收集时间)） | 注重吞吐量的场景 | UseParallelGC(PS + Serial Old) |  
| Serial Old | 老年代 | Serial 老年代版本 | CMS 后备方案，配合 Parallel Scavenge | |
| Parallel Old | 老年代 | ParNew 老年代版本 | 配合 Parallel Scavenge 使用 | |
| CMS(Concurrent Mark Sweep) | 老年代 | 响应时间最短为目标 | 响应时间比较短，吞吐量相对于 G1 大一些 | UseConcMarkSweepGC(ParNew + CMS + Serial Old) |
| G1(Garbage first) | - | 可预测的停顿，可以规定停顿时间 | 对响应时间有要求的，吞吐量要求不高的 | UseG1GC |

### 参数

- UseParallelOldGC(PS + Parallel Old) 
- SurvivorRatio(Eden、Survivor 比例)
- PretenureSizeThreShold(直接晋升到老年代大小)
- MaxTenuringThreshold(晋升老年代年龄)
- UseAdaptiveSizePolicy(动态调整区域大小、进入老年代年龄)
- HandlePromotionFailure(是否允许担保失败)
- ParallelGCThreads(GC 内存回收线程数)
- GCTimeRatio(GC 占用总时间比率，在 PS 收集器有效)
- MaxGCPauseMillis(GC 最长停顿时间，在 PS 收集器有效)
- CMSInitiatingOccupancyFraction（CMS，空间使用多少开始 GC）
- UseCMSCompactAtFullCollection（CMS，是否在 CMS 完成之后整理内存碎片）
- CMSFullGCsBeforeCompaction(CMS，在若干次垃圾收集后启动一次碎片整理)

[更多参数](https://www.oracle.com/technetwork/articles/java/vmoptions-jsp-140102.html)

### CMS

STW：stop the world，停止所有线程

#### 流程

1. 初始标记（CMS initial mark），STW，时间很短，找出所有 GC roots 可以关联的对象
1. 并发标记（CMS concurrent mark），不会 STW，时间很长，并发执行，找出所有需要清理的对象
1. 重新标记（CMS remark），STW，时间比较短，修正并发阶段可能导致标记错误的对象
1. 并发清除（CMS concurrent sweep），不会 STW，清理

#### 缺点

- 对于单核 CPU 不友好，GC 时候占用很多 CPU 时间，降低吞吐量
- 产生浮动垃圾（Floating Garbage），由于 CMS 和线程是并行执行，所以不能等到空间满了之后才 GC，此时产生的垃圾叫做 Floating Garbage；CMS 运行期间可能由于 Floating Garbage 导致内存占满，会触发 Concurrent Mode Failure，启动 Serial Old 收集器
- 由于采用 Mark Sweep 算法导致内存碎片，需要 Full GC 去整理内存

### G1

#### 算法

- 分为多个 Region，计算出每个 Region 回收价值，进行回收
- Region 保存了 Remembered Set，记录了引用关系；在写操作进行时增加了屏障，

#### 流程

1. 初始标记（Initial Marking），STW，时间很短，找出所有 GC roots 可以关联的对象
1. 并发标记（Concurrent Marking），不会 STW，时间很长，并发执行，找出所有需要清理的对象
1. 最终标记（Final Marking），STW，时间比较短，修正并发阶段可能导致标记错误的对象，根据Remembered Set Logs修正 根据Remembered Set
1. 筛选回收（Live Data Counting And Evacuation），STW，时间短，根据 Region 的价值回收垃圾

#### 优点

- 并行与并发
- 分代收集，内部区分年轻代和老年代，不需要配合其他收集器
- 空间整合，将内存分为多个 Region，Region 之间是基于复制算法
- 可预测的停顿，可以指定 M 毫秒内垃圾收集时间不超过 N 毫秒

## GC 日志

```
[GC (Allocation Failure) [Tenured: 6144K->6593K(10240K), 0.0030056 secs] 8201K->6593K(19456K), [Metaspace: 2994K->2994K(1056768K)], 0.0030760 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[Full GC (Allocation Failure) [Tenured: 6593K->6577K(10240K), 0.0034362 secs] 6593K->6577K(19456K), [Metaspace: 2994K->2994K(1056768K)], 0.0034777 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
```

- GC 类型 + 老年代回收情况 + 总内存回收情况 + Metaspace 回收情况 + 时间（用户态 + 内核态 + 墙钟时间）
- 不同的收集器会用不同的字段，基本上保持一致

## 内存分配、回收

- 对象优先在 Eden 分配
- 大对象直接进入老年代（尽量不要有短命的大对象，Major GC 频繁）
- 长期存活的对象进入老年代
- 动态对象年龄判断（如果相同年龄的对象大小占用 Survivor（from） 空间的 1/2，直接进入老年代）

### 空间担保分配

- 如果老年代剩余空间比年轻代占用空间大（说明装得下所有的年轻代，没有风险），Minor GC 安全
- 否则，如果担保失败且老年代剩余空间比历次晋升到老年代的对象大小总和大，尝试进行 Minor GC
- 否则，进行 Full GC
- 如果尝试进行的 Minor GC 失败，进行一次 Full GC

## JVM 工具

### 命令行

命令行少用，费时。

- jps: 显示虚拟机进程
- jstat: 虚拟机运行数据
- jinfo: 虚拟机配置信息
- jmap： 内存转储快照（heapdump）
- jhat：分析 heapdump 文件，不建议使用，功能简陋，占用资源
- jstack：显示虚拟机进程快照

### GUI

- jconsole 老版本
- jvisualvm 新版本，推荐使用，插件化，功能更多，比如 btrace（扩展代码

## JVM 调优案例

- 高性能硬件上的程序部署策略：每个 JVM 分配大内存，GC 时间长，STW 时间过长，影响用户体验；需要分成很多 JVM，每个 JVM 占小内存。
- 堆外内存导致溢出：OOM 原因是 null；Direct Memory 只有在 Full GC 才会回收
- 外部命令导致系统缓慢：调用 shell 脚本导致系统缓慢；删除 shell 脚本，改成 java 命令
- JVM 进程崩溃，线程过多：异步方式调用接口等待返回；改用生产者消费者模式

## Java 内存模型

### 线程数据存储

- 所有变量都存在主内存（Main Memory）
- 每个线程都有自己的工作内存（Main Memory）

### volatile

- 可见性：修改之后的结果会立即刷新到主内存中，并不代表并发安全（如 i++），尽量使用原子性的操作来操作 volatile 数据
- 有序性：禁止编译器重排序优化
- 轻量

### 原则

- 原子性、可见性、有序性
- 先行发生关系（happens-before）
  - 程序次序原则：在一个线程内，按照代码顺序执行
  - 管程锁定原则：unlock 一定发生在 lock 之前
  - volatile 变量规则：写优先于读
  - 线程启动规则：start 优先
  - 线程终止规则：所有操作先行于对此线程的终止
  - 线程中断规则：interrupt() 优先于检测到中断事件
  - 对象终结原则：初始化优先于 finalize()
  - 传递性：A 比 B 先，B 比 C 先，A 一定比 C 线

### 线程实现（操作系统方面）

- 轻量级进程，即线程
- 调度方式：抢占式

### 线程状态

- New：刚刚创建
- Runnable：正在运行
- Waiting：等待，如 Object.wait()、Thread.join()
- Timed Waiting：限期等待，在一定时间唤醒，设置了 timeout、Thread.sleep() 方法
- Blocked，阻塞，如 synchronized
- Terminated：终止

## 线程安全

- 不可变对象：共享数据为 final，类为 final，要想修改陈胜新对象
- 绝对线程安全：绝对安全，不可能
- 相对线程安全：普通的 Java 类所处的水平
- 线程兼容：使用同步手段保证线程安全
- 线程对立：怎么都线程不安全，应该避免

### 线程安全实现
 
- 阻塞
  - synchronized 同步
  - RentrantLock 可重入锁（等待可中断、可以实现公平锁、可以绑定多个解锁条件）
- 非阻塞（乐观锁）
  - CAS

### 锁优化

- 适应性自旋锁（Adaptive Spining）：进入 synchronized 之前，如果对象的锁是轻量级锁，会忙循环一段时间（自适应），这么做是因为在实践中，持有锁的线程会很快释放锁，减少切换上下文开销
- 锁消除（Lock Elimination）：一些代码上需要同步，但是没有共享数据的，消除锁
- 锁粗化（Lock Coarsening）：对同一个对象反复加锁、解锁，会把加锁操作范围扩大，例如原来在循环里，后放在循坏外
- 轻量锁（Lightweight Locking）：当有两个线程竞争的时候使用，第一个线程通过 CAS 获得锁，如果成功，执行代码；第二个线程通过 CAS 未获得锁，会自旋，如果自旋之后再次获得锁还是失败，膨胀为重量级锁
- 偏向锁（Baised Locking）：只有一个线程时使用，只需要 CAS 部分字段（线程 ID），如果成功，获得锁，之后只需要比较线程 ID 即可；如果失败，说明不是偏向该线程，检查存在线程是否活着，如果存活，撤回偏向锁，膨胀为轻量级锁；如果死去，撤回偏向锁，偏向当前线程。线程冲突较多建议禁用偏向锁，-XX:-UseBiasedLocking

