# ConcurrentHashMap

## 100个字介绍

ConcurrentHashMap 是一个并发安全的 HashMap。它的优点首先是并发安全， 其次性能较高。
并发安全且快速的原理是利用 CAS 算法进行元素的替换，利用汇编原子性的指令 cmpxchg 快速替换。

## 解析

## 相关概念

相关概念如桶在 HashMap 源码解析已经叙述了，就不在此叙述了。

### 关键参数

和 HashMap 相同的参数就不再叙述了。

### 关键方法

#### put

该方法的主要功能是将一个 mapping 以并发安全的形式放入 HashMap 中。

```
    public V put(K key, V value) {
        return putVal(key, value, false); // false 表明如果不存在才放入
    }
```

```
    final V putVal(K key, V value, boolean onlyIfAbsent) {
        if (key == null || value == null) throw new NullPointerException(); // key 和 value 不能为空
        int hash = spread(key.hashCode()); // 将 hashCode() 的高位传播到低位，原因请看 HashMap 源码分析
        int binCount = 0;
        for (Node<K,V>[] tab = table;;) {
            Node<K,V> f; int n, i, fh; K fk; V fv;
            if (tab == null || (n = tab.length) == 0) // case1. 如果 hash 表不存在，创建
                tab = initTable();
            else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {  // case2. 如果桶为空，直接 CAS 替换
                if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value)))
                    break;                   // 空桶不用 synchronized
            }
            else if ((fh = f.hash) == MOVED)  // case3. todo
                tab = helpTransfer(tab, f);
            else if (onlyIfAbsent // case4. 检查第一个结点，不需要 synchronized，hashCode 相同且 key 相同（地址或 equals == true)，直接设置值
                     && fh == hash  // 普遍来说（官方已证），每一个桶中基本只有一个元素，所以直接检查第一个元素很大几率会直接命中
                     && ((fk = f.key) == key || (fk != null && key.equals(fk)))
                     && (fv = f.val) != null)
                return fv;
            else { // case5. 桶中第一个元素没有命中，需要检查后续元素
                V oldVal = null;
                synchronized (f) { // 锁住这一个桶
                    if (tabAt(tab, i) == f) {  // 双检锁，两个 put 操作同时到达了 synchronized 处，第一个 put 把 tab[i] 修改了，第二个 put 操作跳出，从 for 循环再次进入
                        if (fh >= 0) {
                            binCount = 1;
                            for (Node<K,V> e = f;; ++binCount) { // 是一个链表结点
                                K ek;
                                if (e.hash == hash &&
                                    ((ek = e.key) == key ||
                                     (ek != null && key.equals(ek)))) { // 相同的 key，替换
                                    oldVal = e.val;
                                    if (!onlyIfAbsent)
                                        e.val = value;
                                    break;
                                }
                                Node<K,V> pred = e;
                                if ((e = e.next) == null) { // 遍历结束，是新元素，增加到链表尾部
                                    pred.next = new Node<K,V>(hash, key, value);
                                    break;
                                }
                            }
                        }
                        else if (f instanceof TreeBin) { // 是一个红黑树结点
                            Node<K,V> p;
                            binCount = 2;
                            if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                           value)) != null) { // 直接放入红黑树结构
                                oldVal = p.val;
                                if (!onlyIfAbsent)
                                    p.val = value;
                            }
                        }
                        else if (f instanceof ReservationNode) // todo 给 putIfAbsent 使用
                            throw new IllegalStateException("Recursive update");
                    }
                }
                if (binCount != 0) { // binCount 不为 0，说明是链表或者红黑树结构，在大于 TREEIFY_THRESHOLD 需要将结构红黑树变成提升查询效率
                    if (binCount >= TREEIFY_THRESHOLD)
                        treeifyBin(tab, i);
                    if (oldVal != null)
                        return oldVal;
                    break;
                }
            }
        }
        addCount(1L, binCount); // todo
        return null;
    }
```
