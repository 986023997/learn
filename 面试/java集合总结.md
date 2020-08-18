

# java集合总结

#### 1.Collection和Collections区别

colection是集合接口

collections是一个操作集合的工具类

#### 2.Set和List区别？map

list和set都继承自collection接口

list有序，可重复

set无须不可重复

map使用键值对存储，无序，键不可重复，值可重复

#### 3.ArrayList和LinkedList和Vector的区别

ArrayList:使用Object数组存储数据，有序，可通过下标访问元素效率为O(1)，插入删除受到元素位置的影响(O(n-i))，如：在元素的最后位置插入删除，则得到元素的下标直接添加删除即可，但是当位置在中间时，插入删除后涉及大该元素之后的元素位置的移动；在内存分配方面总会有一部分空间无法得到使用

LinkedList:使用双向链表的数据结构，jdk1.6之前使用的是循环链表，无法随机访问，查找速率较慢O（n）,插入删除较快，但是在指定位置插入删除效率也会收到元素位置的影响，因为先要找到i-1的为位置，再进行插入删除；

Vectory：同ArrayList，但是vectory是线程安全的，用synchronized修饰的，需要同步效率较低，是过时的容器，

#### 4.说一说 ArrayList 的扩容机制吧

1.ArrayList的构造方法

arraylist的无参构造函数，构造了一个空数组，只有当对数组添加元素的时候，才会初始化容量，添加第一个元素时，扩容为10

2.扩容机制

首先看add方法

```
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    elementData[size++] = e;
    return true;
}
```



首先通过ensureCapacityInternal判断是否需要扩容，

```
private void ensureCapacityInternal(int minCapacity) {
    ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
}
```

//判断传到的数据和默认的初始扩容大小10,取其中的最大值作为第一次扩容的大小

```
private static int calculateCapacity(Object[] elementData, int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        return Math.max(DEFAULT_CAPACITY, minCapacity);
    }
    return minCapacity;
}
```

随着添加元素，每次添加之前需要判断是否需要扩容了

```
private void ensureExplicitCapacity(int minCapacity) {
    modCount++;

    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}
```

grow:

```
private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```

```
int newCapacity = oldCapacity + (oldCapacity >> 1);
```

可以看到上面这句就是得到扩容之后数组的大小大约为1.5倍左右，若扩容后的大小小于最小的容量则设置为最小容量，若小于最大值即Integer.MAX_VALUE - 8，则分配最大值，然后将数据copy到扩容后的数组。

#### 5.SynchronizedList和Vector的区别

Vector是java.util包中的一个类。 SynchronizedList是java.util.Collections中的一个静态内部类。

在多线程的场景中可以直接使用Vector类，也可以使用Collections.synchronizedList(List list)方法来返回一个线程安全的List。

synchronizedLIst利用synchronized使用同步代码块对传入的list进行包装，Vector使用同步方法实现，两者的扩充数组容量方式不一样

vectory自动增长原来一倍的数组长度，ArrayList是原来的50%，

synchronizedLIst利用通过代码块比使用同步方法锁定范围更小，效率更高；

但是，SynchronizedList中实现的类并没有都使用synchronized同步代码块，其中有listIterator和listIterator(int index)并没有做同步处理，因此在并发情况下做遍历，需要手动加锁；

#### 6.Set如何保证元素不重复

根据实现方式不同主要分为两大类。HashSet和TreeSet。

HashSet的底层也是使用了hashmap的结构，即数组+链表，对于添加的元素，先计算hashcode,得到数组的下标，找到该下标的链表，和链表上的元素进行比较若不同则在链表末尾添加，若相同则不进行添加

TreeSet的底层是TreeMap的keySet()，而TreeMap是基于红黑树实现的，红黑树是一种平衡二叉查找树，它能保证任何一个节点的左右子树的高度差不会超过较矮的那棵的一倍。

#### 7.HashMap和HashTable的区别

线程安全：

HashTable 中的方法是同步的，而HashMap中的方法在默认情况下是非同步的。在多线程并发的环境下，可以直接使用HashTable，但是要使用HashMap的话就要自己增加同步处理了。

允不允许null值： HashTable中，key和value都不允许出现null值，否则会抛出NullPointerException异常。 HashMap中，null可以作为键，这样的键只有一个；可以有一个或多个键所对应的值为null。

默认初始容量和扩容机制： HashTable中的hash数组初始大小是11，增加的方式是 old*2+1。HashMap中hash数组的默认大小是16，而且一定是2的指数；

Hashtable是遗留类，很多映射的常用功能与HashMap类似，不同的是它承自Dictionary类，并且是线程安全的，任一时间只有一个线程能写Hashtable，并发性不如ConcurrentHashMap，因为ConcurrentHashMap引入了分段锁。Hashtable不建议在新代码中使用，不需要线程安全的场合可以用HashMap替换，需要线程安全的场合可以用ConcurrentHashMap替换。





##### HashMap的原理

参考https://zhuanlan.zhihu.com/p/21673805

###### 1.存储结构-字段

从结构实现来讲，HashMap是数组+链表+红黑树（JDK1.8增加了红黑树部分）实现的，如下如所示。

![preview](https://pic4.zhimg.com/8db4a3bdfb238da1a1c4431d2b6e075c_r.jpg)

(1) 从源码可知，HashMap类中有一个非常重要的字段，就是 Node[] table，即哈希桶数组，明显它是一个Node的数组。我们来看Node[JDK1.8]是何物。

```text
static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;    //用来定位数组索引位置
        final K key;
        V value;
        Node<K,V> next;   //链表的下一个node

        Node(int hash, K key, V value, Node<K,V> next) { ... }
        public final K getKey(){ ... }
        public final V getValue() { ... }
        public final String toString() { ... }
        public final int hashCode() { ... }
        public final V setValue(V newValue) { ... }
        public final boolean equals(Object o) { ... }
}
```

Node是HashMap的一个内部类，实现了Map.Entry接口，本质是就是一个映射(键值对)。上图中的每个黑色圆点就是一个Node对象。

HashMap就是使用哈希表来存储的。哈希表为解决冲突，可以采用开放地址法和链地址法等来解决问题，Java中HashMap采用了链地址法。链地址法，简单来说，就是数组加链表的结合。在每个数组元素上都一个链表结构，当数据被Hash后，得到数组下标，把数据放在对应下标元素的链表上。例如程序执行下面代码：

```text
map.put("美团","小美");
```

系统将调用"美团"这个key的hashCode()方法得到其hashCode 值（该方法适用于每个Java对象），然后再通过Hash算法的后两步运算（高位运算和取模运算，下文有介绍）来定位该键值对的存储位置，有时两个key会定位到相同的位置，表示发生了Hash碰撞。当然Hash算法计算结果越分散均匀，Hash碰撞的概率就越小，map的存取效率就会越高。

在理解Hash和扩容流程之前，我们得先了解下HashMap的几个字段。从HashMap的默认构造函数源码可知，构造函数就是对下面几个字段进行初始化，源码如下：

```text
int threshold;             // 所能容纳的key-value对极限 
     final float loadFactor;    // 负载因子
     int modCount;  
     int size;
```



Node[] table的初始化长度length(默认值是16)，Load factor为负载因子(默认值是0.75)，threshold是HashMap所能容纳的最大数据量的Node(键值对)个数。threshold = length * Load factor。

结合负载因子的定义公式可知，threshold就是在此Load factor和length(数组长度)对应下允许的最大元素数目，超过这个数目就重新resize(扩容)，扩容后的HashMap容量是之前容量的两倍。默认的负载因子0.75是对空间和时间效率的一个平衡选择，

这里存在一个问题，即使负载因子和Hash算法设计的再合理，也免不了会出现拉链过长的情况，一旦出现拉链过长，则会严重影响HashMap的性能。于是，在JDK1.8版本中，对数据结构做了进一步的优化，引入了红黑树。而当链表长度太长（默认超过8）时，链表就转换为红黑树，利用红黑树快速增删改查的特点提高HashMap的性能，其中会用到红黑树的插入、删除、查找等算法。

###### 2. hash确定索引位置

hash函数

```
static final int hash(Object key) {
    int h;
    //得到hashcoed,并将hashcode与hashcode的高16位进行异或
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

并且在存值的时候会对得到hash与数组取模运算得到下标

put源码：

```
 public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }


final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    //tab为空则创建
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
        //得到hash取模得到下标
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        Node<K,V> e; K k;
        //存在key,覆盖
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        else if (p instanceof TreeNode)
        //为红黑树
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
        //若为链表
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    //若长度大于8，转化为红黑树
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
                //若存在，直接覆盖
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    ++modCount;
    //超过阈值，扩容
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
```

###### 3. 分析HashMap的put方法

![preview](https://pic4.zhimg.com/58e67eae921e4b431782c07444af824e_r.jpg)

###### 4. 扩容机制

resize()源码

```
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    if (oldCap > 0) {
    //扩容前已经达到了最大，2^30
        if (oldCap >= MAXIMUM_CAPACITY) {
        //修改阈值为int的最大值(2^31-1)，这样以后就不会扩容了
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        //newCap=oldCap*2,扩容2倍
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                 oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // double threshold
    }
    else if (oldThr > 0) // initial capacity was placed in threshold
        newCap = oldThr;
    else {               // zero initial threshold signifies using defaults
        newCap = DEFAULT_INITIAL_CAPACITY;
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    if (newThr == 0) {
    //设定新的阈值
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                  (int)ft : Integer.MAX_VALUE);
    }
    threshold = newThr;
    @SuppressWarnings({"rawtypes","unchecked"})
    //初始化一个新的Entry数组
    Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    if (oldTab != null) {
   
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                if (e.next == null)
                //重新计算下标，并赋值
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode)
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else { // preserve order
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    do {
                        next = e.next;
                        //原索引
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        //原索引+oldCap
                        else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                     // 原索引放到bucket里
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                     // 原索引+oldCap放到bucket里
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return newTab;
}
```

jdk1.8对扩容的优化经过观测可以发现，我们使用的是2次幂的扩展(指长度扩为原来2倍)，所以，元素的位置要么是在原位置，要么是在原位置再移动2次幂的位置。看下图可以明白这句话的意思，n为table的长度，图（a）表示扩容前的key1和key2两种key确定索引位置的示例，图（b）表示扩容后key1和key2两种key确定索引位置的示例，其中hash1是key1对应的哈希与高位运算结果。

![img](https://pic3.zhimg.com/80/a285d9b2da279a18b052fe5eed69afe9_720w.png)

元素在重新计算hash之后，因为n变为2倍，那么n-1的mask范围在高位多1bit(红色)，因此新的index就会发生这样的变化：

![img](https://pic1.zhimg.com/80/b2cb057773e3d67976c535d6ef547d51_720w.png)

因此，我们在扩充HashMap的时候，不需要像JDK1.7的实现那样重新计算hash，只需要看看原来的hash值新增的那个bit是1还是0就好了，是0的话索引没变，是1的话索引变成“原索引+oldCap”

###### 5.线程安全

在jdk1.7中，在resize的时候采用头插法，在多线程使用场景中，应该尽量避免使用线程不安全的HashMap，而使用线程安全的ConcurrentHashMap。那么为什么说HashMap是线程不安全的，下面举例子说明在并发的多线程使用场景中使用HashMap可能造成死循环并且会丢失数据。在jdk1.8之后改用尾插法，不会死循环，但是由于不是线程安全的所以会导致更新丢失

#### 8.HashMap 和 ConcurrentHashMap 的区别？

 JDK1.7的 ConcurrentHashMap 底层采用 **分段的数组+链表** 实现，JDK1.8 采用的数据结构跟HashMap1.8的结构一样，数组+链表/红黑二叉树。Hashtable 和 JDK1.8 之前的 HashMap 的底层数据结构类似都是采用 **数组+链表** 的形式，数组是 HashMap 的主体，链表则是主要为了解决哈希冲突而存在的；

 ① **在JDK1.7的时候，ConcurrentHashMap（分段锁）** 对整个桶数组进行了分割分段(Segment)，每一把锁只锁容器其中一部分数据，多线程访问容器里不同数据段的数据，就不会存在锁竞争，提高并发访问率。 **到了 JDK1.8 的时候已经摒弃了Segment的概念，而是直接用 Node 数组+链表+红黑树的数据结构来实现，并发控制使用 synchronized 和 CAS 来操作。整个看起来就像是优化过且线程安全的 HashMap，虽然在JDK1.8中还能看到 Segment 的数据结构，但是已经简化了属性，只是为了兼容旧版本；② **Hashtable(同一把锁)** :使用 synchronized 来保证线程安全，效率非常低下。当一个线程访问同步方法时，其他线程也访问同步方法，可能会进入阻塞或轮询状态，如使用 put 添加元素，另一个线程不能使用 put 添加元素，也不能使用 get，竞争会越来越激烈效率越低。

#### 9.HashMap的长度为什么是2的幂次方

为了能让 HashMap 存取高效，尽量较少碰撞，也就是要尽量把数据分配均匀。我们上面也讲到了过了，Hash 值的范围值-2147483648到2147483647，前后加起来大概40亿的映射空间，只要哈希函数映射得比较均匀松散，一般应用是很难出现碰撞的。但问题是一个40亿长度的数组，内存是放不下的。所以这个散列值是不能直接拿来用的。用之前还要先做对数组的长度取模运算，得到的余数才能用来要存放的位置也就是对应的数组下标。这个数组下标的计算方法是“ `(n - 1) & hash`”。（n代表数组长度）。这也就解释了 HashMap 的长度为什么是2的幂次方。

**这个算法应该如何设计呢？**

我们首先可能会想到采用%取余的操作来实现。但是，重点来了：**“取余(%)操作中如果除数是2的幂次则等价于与其除数减一的与(&)操作（也就是说 hash%length==hash&(length-1)的前提是 length 是2的 n 次方；）。”** 并且采用二进制位操作 &，相对于%能够提高运算效率，这就解释了 HashMap 的长度为什么是2的幂次方。

所以，return h & (length-1);只要保证length的长度是2^n 的话，就可以实现取模运算了。

所以，因为位运算直接对内存数据进行操作，不需要转成十进制，所以位运算要比取模运算的效率更高，所以HashMap在计算元素要存放在数组中的index的时候，使用位运算代替了取模运算。之所以可以做等价代替，前提是要求HashMap的容量一定要是2^n 。

###### 1.为什么默认大小是16？

猜测：太小了就有可能频繁发生扩容，影响效率。太大了又浪费空间，不划算。

###### 2.为什么转载因子是0.75?

loadFactor是装载因子，表示HashMap满的程度，默认值为0.75f，设置成0.75有一个好处，那就是0.75正好是3/4，而capacity又是2的幂。所以，两个数的乘积都是整数。

默认负载因子（0.75）在时间和空间成本上提供了很好的折衷。较高的值会降低空间开销，但提高查找成本（体现在大多数的HashMap类的操作，包括get和put）。设置初始大小时，应该考虑预计的entry数在map及其负载系数，并且尽量减少rehash操作的次数

