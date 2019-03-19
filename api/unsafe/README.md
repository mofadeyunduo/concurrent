# Unsafe

## 优点

- 效率高，调用 cmpxchg 进行操作

## 缺点

- 直接对内存操作不安全，不建议代码中使用

## 用途

- ConcurrentHashmap 使用 CAS 保证并发安全，设置 mapping 的时候，使用 Unsafe.compareAndSwapObject 进行操作

## 示例

- com/example/unsafe/UnsafeTest.java 利用 Unsafe 以 CAS 操作数组
