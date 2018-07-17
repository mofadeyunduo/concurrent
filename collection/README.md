# Collection

## Java

### Collection

- 添加、删除等操作时可选操作，如 Arrays.asList，会产生固定大小的集合,会抛出 UnsupportedOperationException

### Set

- HashSet、TreeSet、LinkedHashSet
- HashSet、LinkedHashSet 注意需要对其中的元素定义 hashcode()
- SortedSet 有序集合

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

### Arrays

- 对数组的操作
- sort 排序
- binarySearch 二分查找元素

### System

- arrayCopy 内存级复制，浅复制

### Comparator、Comparable

- Comparator 比较器，一般类直接实现
- Comparable 可比较的，一般作为参数传入

## 一些设计原则

- 将保持不变的事物和会改变的事物分离

## 正确的 equals 方法

- 自反性、对称性、传递性、一致性、为 null 结果为 false
- 默认 equals 比较地址


