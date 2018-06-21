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

### AtomicXXX

- 原子操作
- 锁更安全一些，Atomic 系列类是为 java.util.concurrent 服务

#### synchronized

- 设置域为 private，保证只有方法可以访问该字段的值
- 共享域需要同步
- 锁方法：当前对象只有一个线程能访问该方法，效率低
- 锁对象：用于临界区，锁方法中的部分代码片段，效率高
- 不具备超时等特性

#### volatile

- 立即写入主存中，所有线程都看得到，避免缓存影响
- 保证 long、double 赋值操作的原子性
- 只有一个值会改变的情况下使用 
- 不能保证线程安全

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
这在多线程是个性能天坑。每个线程如果都有 System.out.println 方法，互相阻塞。可以参考项目下 NumberMain 测试。

### 承诺升级理论

线程组的启示：继续错误的代价由别人来承担，而承认错误的代价由自己来承担。

### 线程很简单

如果某个人表示线程机制很容易或者很简单，那么请确保这个人没有对你的项目作出重要的决策。如果这个人已经在做了，那么你就已经陷入麻烦中了。