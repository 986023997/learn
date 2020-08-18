# jvm总结

jvm是java虚拟机，java的跨平台主要是依靠于jvm,虽然java是跨平台的，当jvm却不是，主流的jvm主要有hotspot、jrocket、j9等，主要使用的还是hotspot,hostspot是一个采用栈指令集的架构，具有指令集小，指令多的特点，解释执行和编译执行共存的，hotspot中有一个jit的即时编译器，主要编译在程序中多次执行的代码编译为机器指令，提高速度，jit对于代码的优化有：热点探测，公共子表达式的消除，逃逸分析等，

## 类加载过程

## 运行时数据区域

## jvm执行引擎

## 垃圾回收器

## 垃圾回收算法

## JMM(java memory model)java内存模型