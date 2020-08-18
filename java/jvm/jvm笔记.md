# jvm笔记

## **堆**

## 1.jvm中默认堆的比列为：

年轻代和老年代的比例为1：2，年轻代中Eden和survivor0和survivor1的比例为 8：1：1，

![image-20200810211847480](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200810211847480.png)

eden满了触发ygc,在survivor区中的对象达到阈值（15）之后会保存到老年代

对于幸存者区：复制之后有交换，谁空谁是to

关于垃圾回收：频繁在新生代收集，很少在老年代收集，几乎不在元空间收集

对象分配的过程

![image-20200810212807986](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200810212807986.png)

### Minor GC、Major GC、Full GC:

gc按回收区域分为两大类型：一种是部分收集、一种是整堆收集（Full GC）

部分收集：不hi完整收集整个java堆的垃圾收集。可分为：

新生代收集：Minor GC

老年代收集：Major GC

混合收集：

整堆收集：收集整个java堆和方法区的

### **Minor GC的触发时机：**

eden区域满了的时候触发Minor GC，顺便会把幸存者区域回收，Minor GC非常频繁，会引发STW,z暂停用户的线程，等待垃圾回收结束，用户线程才能恢复运行。  

### **老年代GC(Major GC/Full GC)**触发机制：

发生在老年代的gc,出现major gc 经常会先尝试触发minor gc(并非绝对，parallel scavenge收集器的收集策略里就有直接进行major gc的策略选择过程)。

major gc的速度一般会比minor gc慢10倍以上，stw的时间更长。

如果major gc后，还内存不足就会报OOM了。



### Full GC触发机制：

执行的情况：

1.调用system.gc()时，

2.老年代空间不足

3.方法区空间不足

说明：开发中尽量避免full gc



![image-20200811173638274](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811173638274.png)



![image-20200811173608743](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811173608743.png)



## 内存分配策略：



![image-20200811173917149](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811173917149.png)

![image-20200811174048689](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811174048689.png)

## 空间分配担保

![image-20200811181801606](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811181801606.png)







## TLAB

![image-20200811175429857](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811175429857.png)

![image-20200811175711585](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811175711585.png)

![image-20200811175736312](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811175736312.png)

![image-20200811180031252](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811180031252.png)

![image-20200811180357485](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811180357485.png)









![image-20200811182007435](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811182007435.png)

## 逃逸分析

![image-20200811182338773](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811182338773.png)

开发中能使用局部变量的，就不要再方法外定义

![image-20200811183235443](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811183235443.png)

![image-20200811183333361](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811183333361.png)

![image-20200811184020809](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811184020809.png)

![image-20200811184443617](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811184443617.png)

![image-20200811184825453](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811184825453.png)

![image-20200811185158032](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811185158032.png)

![image-20200811185234056](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811185234056.png)

![image-20200811185504643](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811185504643.png)

## 方法区





![image-20200811191307663](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811191307663.png)

![image-20200811191615062](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811191615062.png)



![image-20200811192354398](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811192354398.png)



![image-20200811192710166](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811192710166.png)





![image-20200811193424432](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811193424432.png)



![image-20200811193439102](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811193439102.png)





![image-20200811193547944](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811193547944.png)

![image-20200811194653129](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811194653129.png)





![image-20200811200238940](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200811200238940.png)