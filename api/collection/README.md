# Collection

## Java

### Collection

- 添加、删除等操作时可选操作，如 Arrays.asList，会产生固定大小的集合,会抛出 UnsupportedOperationException

### Set

- HashSet、TreeSet、LinkedHashSet
- HashSet、LinkedHashSet 注意需要对其中的元素定义 hashcode()
- SortedSet 有序集合
- NavigableSet 可导航集合，拥有 lower 等方法

### Queue

- LinkedList、PriorityQueue，性能无差别
- DeQue，双端队列
- add 不能插入抛出异常，offer 不会
- remove 移除空会抛出异常，poll 不会

### Map

- HashMap、TreeMap、LinkedHashMap、WeakHashMap、ConcurrentHashMap、IdentityHashMap
- LinkedHashMap，遍历按照插入顺序
- WeakHashMap 弱键映射，允许释放映射所指的对象
- IdentityHashMap 用 == 进行比较
- SortedMap，排序 Map

### Collections

- 对 Collection 的操作
- fill 填充对象数组
- newSetFromMap 用法：实现 ConcurrentHashSet，newSetFromMap(new ConcurrentHashMap)
- disjoint 不相交集合
- checkedXXX 元素会检查类型
- synchronizedXXX 同步集合
- unmodified 不可修改集合
- rotate 循环向后移动，最后的元素往前移
- shuffle 乱序
- sort 排序
- XXXBinarySearch 二分搜索

### Arrays

- 对数组的操作
- sort 排序
- binarySearch 二分查找元素
- stream 流式处理

### System

- arrayCopy 内存级复制，浅复制

### Comparator、Comparable

- Comparator 比较器，一般类直接实现
- Comparable 可比较的，一般作为参数传入

### XXXReference

- WeakReference System.GC() 就可以回收
- SoftReference 内存不足回收
- PhantomReference 调用 clear 方法才会清除

### Spliterator

- splitable iterator，可分割迭代器
- 接口是Java为了并行遍历数据源中的元素而设计的迭代器

## 一些设计原则

- 将保持不变的事物和会改变的事物分离

## 正确的 equals 方法

- 自反性、对称性、传递性、一致性、为 null 结果为 false
- 默认 equals 比较地址

## hash

### 正确的 hashcode

1. 给 result 赋予某个非 0 常量
1. 对于每一个域，计算出一个散列码 c
1. 合并计算， result = result * 37 + c
1. 返回 result

### 散列码 c 计算公式

|域类型|计算公式|
|:---:|:---:|
|boolean|0 1|
|byte char short int|(int)|
|long|(int) f >> 32|
|float|Float.floatToIntBits|
|double|(int)Double.doubleToIntBits >> 32|
|Object|hashcode|
|数组|每一项运用|

### hash 原理

- 先用 hashcode 计算，无冲突直接使用得到的值
- 有冲突，遍历冲突所在的 list，equals 计算得出值

### HashMap 的性能

- 负载因子，当前存储 / 容量，默认 0.75
- 如果知道需要存储多少数据，设置合适的容量

## 快速报错

- 当非并发集合进行并发操作时，会快速抛出 ConcurrentModificationException