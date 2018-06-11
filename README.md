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

- 在设置后台进程时， **最后一个后台线程的 finally 会不执行**

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

#### Executor

- 管理 Thread 对象
- 接收 ThreadFactory 作为参数
- execute 接受参数是实现 Runnable 类的实例，submit 接受参数是 Callable 类的实例 
- shutdown 阻止后续提交任务
- CachedThreadPool 缓存
- FixedThreadPool 固定大小
- SingleThreadExecutor 单线程，多任务排队

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