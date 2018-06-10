# Concurrent

## Java

### Description

- 并发提高在阻塞时的性能
- 函数式语言处理并发
- Java 线程机制是抢占式
- 并发上下文切换代价高Description
- 实现：多线程

### Implementation

#### Runnable

- 定义任务
- 通常写成无限循环的形式，除非有条件使得终止

#### Thread

- start 启动任务
- yield 向线程调度器生命发出已经执行完最重要的任务、可以切换线程的信号
- registerNatives 线程注册了自己，在 run 没有执行完成之前无法被垃圾回收器清除

#### Executor