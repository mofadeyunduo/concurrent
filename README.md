# Concurrent

## Java

### Description

- 并发提高在阻塞时的性能
- 函数式语言处理并发
- Java 线程机制是抢占式
- 并发上下文切换代价高
- 实现：多线程

### Priority

- 大多数情况下试图操作线程优先级是种错误
- 优先级在各个操作系统实现不同，可靠的是 MAX_PRIORITY、MIN_PRIORITY、NORM_PRIORITY

### KeyWord finally

- 在设置后台进程时， **后台线程的 finally 会不执行**

### 终结任务

- 小心谨慎

### 线程状态

1. new
1. runnable
1. blocked 
1. dead

#### blocked 触发条件

- sleep
- wait 挂起（notify 再次进入就绪状态）
- 等待输入输出
- 试图 synchronized，被其他线程锁住

### 死锁

- 潜伏期长，很难复现

#### 产生条件

- 互斥
- 一个资源者持有资源
- 资源不可以被抢占
- 循环等待

### 免锁容器

- 读取和写入同时发生
- 写入时复制建立读取内容副本，读取操作读源数组，写入数据写副本，完成之后以原子性的操作将副本换入源数组
- 只能保证最终一致性
- CopyOnWriteXXX

### Implementation

#### Runnable

- 定义任务
- 通常写成无限循环的形式，除非有条件使得终止

#### Callable

- 可以返回 <T> 类型的值，返回 Future<T> 对象
- Future<T> isDone 可以检查任务是否完成
- get 获取结果，如果结果为准备就绪，会阻塞

#### Thread

- 可以继承实现 start 方法，也可以利用构造器设置 Runnable 的实现类的实例来执行任务
- start 启动任务
- yield 向线程调度器生命发出已经执行完最重要的任务、可以切换线程的信号，不能依赖
- registerNatives 线程注册了自己，在 run 没有执行完成之前无法被垃圾回收器清除
- sleep 休眠
- setPriority 设置优先级
- setDaemon 设置后台进程
- join 等待线程结束，可以设置超时时间
- setUncaughtExceptionHandler 设置异常处理器
- interrupted 可以查询是否产生中断，并清除中断状态
- wait 线程进入等待状态，**释放锁**，可以从 notify 恢复，建议用 while 而不是 if 执行 wait 操作，因为别的操作可能又让 wait 条件满足
- wait 会释放 synchronized 的锁
- notify 唤醒一个等待的线程，保证所有线程 wait 条件相同，在一个类中可能有很多任务，只唤醒当前任务相关的线程
- notifyAll 唤醒所有等待的线程

#### FutureTask

- 可取消
- 异步计算

#### ThreadFactory

- Thread 工厂，生成 Thread

#### Executor

- 管理 Thread 对象
- 接收 ThreadFactory 作为参数
- execute 接受参数是实现 Runnable 类的实例，submit 接受参数是 Callable 类的实例 
- shutdown 阻止后续提交任务
- CachedThreadPool 缓存
- FixedThreadPool 固定大小
- SingleThreadExecutor 单线程，多任务排队

#### Lock

- 锁对象，相当于 synchronized
- 在 finalize 中 unlock
- 实现锁的高级功能，如超时
- lockInterruptibly 产生中断
- 性能好（线性增长），可读性略低

#### AtomicXXX

- 原子操作
- 锁更安全一些，Atomic 系列类是为 java.util.concurrent 服务
- 乐观锁，性能一般很高，但并发量大时，CPU 消耗资源多

#### ThreadLocal

- 线程本地存储

#### BlockingQueue

- 阻塞队列
- 配合 Enum 使用，组建流程

#### PipedReader & PipedWriter

- 管道读写同步数据

#### Semaphore

- 信号量，允许多个任务同时访问一个资源

#### Exchanger

- 两个任务交换对象的栅栏

#### synchronized

- 设置域为 private，保证只有方法可以访问该字段的值
- 共享域需要同步
- 锁方法：当前对象只有一个线程能访问该方法，效率低
- 锁对象：用于临界区，锁方法中的部分代码片段，效率高
- 不具备超时等特性
- 具有锁的对象可以访问其他该对象加锁的方法
- wait, notify, notifyAll 必须在 synchronized 下使用，如果不这么使用，[可能会丢失 notify()](https://leokongwq.github.io/2017/02/24/java-why-wait-notify-called-in-synchronized-block.html)
- 数据量大性能低，但可读性好

#### volatile

- 立即写入主存中，所有线程都看得到，避免缓存影响
- 保证 long、double 赋值操作的原子性
- 只有一个值会改变的情况下使用 
- 不能保证线程安全

#### InterruptedException

- 中断线程
- 注意清理资源
- IO、synchronized 不可中断
- NIO 提供了新中断方式

#### CountDownLatch

- 同步多个任务使用，首先新建 CountDownWatch 确定任务大小，各个任务 countDown，再 await 等待其他任务完成

#### CyclicBarrier

- 创建一组任务，并行工作，在所有所有任务完成之前等待
- 相比于 CountDownLatch，可以多次触发
- 构造函数参数包括所需任务数、所有任务完成之后执行的操作

#### DelayedQueue

- 延迟队列

#### PriorityBlockingQueue

- 优先级队列

#### ConcurrentHashMap

- 写入机制非写时复制，比写时复制快

#### ScheduledExecutorService

- 定时器
- schedule 执行一次
- scheduleAtFixedRate 多次执行

#### ReadWriteLock

- 读锁可以在没有写锁的时候被多个线程同时持有，写锁是独占的
- 多读少写性能高

#### Fork/Join

##### ForkJoinPool

- invoke 启动任务

##### RecursiveTask

- 定义子任务
- invokeAll 调用子任务

#### TransferQueue

- 队列满时，阻塞生产者

### 有意思的问题：为什么 System.out.println() 不会被中断？

《Java 编程思想》提了一句 “System.out.println() 不会被中断”，疑惑的我去看源码，恍然大悟。

```
    public void println(boolean x) {
        synchronized (this) {
            print(x);
            newLine();
        }
    }
```

System 包的 out 是静态对象，只有一个实例，在执行 println，锁住自己，下个线程想用 System.out 的方法，只能等当前操作结束。
这在多线程是个性能天坑。每个线程如果都有 System.out.println 方法，互相阻塞。可以参考项目下 com.example.concurrent.number.NumberMain 测试。

### 三个线程互相打印

三个线程按顺序打印 A、B、C，参见 com.example.concurrent.print.PrinterTest。

### 承诺升级理论

线程组的启示：继续错误的代价由别人来承担，而承认错误的代价由自己来承担。

### 线程很简单？

如果某个人表示线程机制很容易或者很简单，那么请确保这个人没有对你的项目作出重要的决策。如果这个人已经在做了，那么你就已经陷入麻烦中了。

### 双检锁

双重检查，加锁。双重检查防止多次实例化。

- 多线程下需要加锁
- 在方法上加锁影响性能
- 由于 JIT 编译不确定性，需要在资源上加上 volatile 防止编译器优化，导致获取到的 obj 为空的问题

正确做法：

```
class SomeClass {
  private volatile Resource resource = null;
  public Resource getResource() {
    if (resource == null) {
      synchronized {
        if (resource == null)
          resource = new Resource();
      }
    }
    return resource;
  }
}
```

参考[双重检查锁定原理详解](https://blog.csdn.net/li295214001/article/details/48135939/)。