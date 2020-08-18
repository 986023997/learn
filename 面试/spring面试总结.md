# spring面试题总结



![image-20200817170944650](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200817170944650.png)

#### 1.sprin的了解

spring是一个java开发框架，spring中有两个重要的功能，ioc和aop

ioc即控制反转，对象的管理交由spring容器进行管理，实现代码的解耦

aop即面向切面编程，通过代理的方式给业务代码实现增强

spring的特点：

体积小

非侵入式

便于与其他的框架集成

功能丰富，对许多的功能都进行了封装，如对jdbc的封装

#### 2.列举一些重要的Spring模块？

**spring core**:spring的核心模块，其他模块都需要依赖于此模块，核心工具包

**Spring Aspects**： 该模块为与AspectJ的集成提供支持。

**Spring AOP** ：提供了面向切面的编程实现。

**Spring JDBC** : Java数据库连接。 

**Spring JMS** ：Java消息服务。

**Spring ORM** : 用于支持Hibernate等ORM工具。

**Spring Web** : 为创建Web应用程序提供支持。

**Spring Test** : 提供了对 JUnit 和 TestNG 测试的支持。

#### 3.什么是ioc

官方：IoC（控制反转）也称为依赖注入（DI）。在此过程中，对象仅通过构造函数参数，工厂方法的参数或在构造或从工厂方法返回后在对象实例上设置的属性来定义其依赖项（即，与它们一起使用的其他对象） 。然后，容器在创建bean时注入那些依赖项。此过程从根本上讲是通过使用类的直接构造或诸如服务定位器模式之类的控件来控制其依赖项的实例化或位置的bean本身的逆过程（因此称为Control的倒置）。

通俗的讲，IoC（Inverse of Control:控制反转）是一种**设计思想**，就是 **将原本在程序中手动创建对象的控制权，交由Spring框架来管理 **IoC 容器是 Spring 用来实现 IoC 的载体， IoC 容器实际上就是个Map（key，value）,Map 中存放的是各种对象。ioc容器就像是一个工厂，当我们需要构建一个对象时，只需要配置好配置文件或注解，就完成不需要对象是如何被构建出来

#### 4.spring ioc的初始化过程

#### 5.什么是Aop

概念

实现方式

#### 6.spring的常用创建bean对象的方式

1.通过默认构造函数

2.通过普通工厂方法

3.使用工厂中的静态方法

#### 7.spring的常用注入方式

1.构造函数

2.set方法注入

3.注解

#### 8.bean的作用范围

1.singleton:单例

2.prototype:多例

3.session:作用于session范围

4.request：作用于web应用的request范围

5.globe-session：用于分布式的session范围

#### 9.spring中的bean是安全的吗？

spring中默认是单例的，当单例中有竞态条件时，不能保证线程安全

#### 10.spring自动装配bean的方式有哪些？

1.不自动装配

2.通过类型装配

3.通过名称进行装配

4.通过构造函数进行装配

#### 11.spring事务实现方式有哪些？

声明式事务

编程式事务

#### 12.@Component 和 @Bean 的区别是什么？

1. 作用对象不同: `@Component` 注解作用于类，而`@Bean`注解作用于方法。
2. `@Component`通常是通过类路径扫描来自动侦测以及自动装配到Spring容器中（我们可以使用 `@ComponentScan` 注解定义要扫描的路径从中找出标识了需要装配的类自动装配到 Spring 的 bean 容器中）。`@Bean` 注解通常是我们在标有该注解的方法中定义产生这个 bean,`@Bean`告诉了Spring这是某个类的示例，当我需要用它的时候还给我。
3. `@Bean` 注解比 `Component` 注解的自定义性更强，而且很多地方我们只能通过 `@Bean` 注解来注册bean。比如当我们引用第三方库中的类需要装配到 `Spring`容器时，则只能通过 `@Bean`来实现。

#### 13.将一个类声明为Spring的 bean 的注解有哪些?

@component:通用的注解，可标注任意类为 `Spring` 组件。如果一个Bean不知道属于哪个层，可以使用`@Component` 注解标注。

@Repository:对应持久层，用于操作数据库

@service:对应服务层，设计到业务逻辑

@Controller:控制层，用于接受用户请求

#### 14.spring bean的生命周期

- Bean容器找到配置文件中Spring Bean的定义。
- Bean容器利用Java Reflection API创建一个Bean的实例。
- 如果涉及到一些属性值 利用set方法设置一些属性值。
- 如果Bean实现了BeanNameAware接口，调用setBeanName()方法，传入Bean的名字。
- 如果Bean实现了BeanClassLoaderAware接口，调用setBeanClassLoader()方法，传入ClassLoader对象的实例。
- 如果Bean实现了BeanFactoryAware接口，调用setBeanClassLoader()方法，传入ClassLoader对象的实例。
- 与上面的类似，如果实现了其他*Aware接口，就调用相应的方法。
- 如果有和加载这个Bean的Spring容器相关的BeanPostProcessor对象，执行postProcessBeforeInitialization()方法
- 如果Bean实现了InitializingBean接口，执行afterPropertiesSet()方法。
- 如果Bean在配置文件中的定义包含`init-method`属性，执行指定的方法。
- 如果有和加载这个Bean的Spring容器相关的BeanPostProcessor对象，执行postProcessAfterInitialization()方法
- 当要销毁Bean的时候，如果Bean实现了DisposableBean接口，执行destroy()方法。
- 当要销毁Bean的时候，如果Bean在配置文件中的定义包含`destroy-method`属性，执行指定的方法。

#### 15.Spring AOP 和 AspectJ AOP 有什么区别？

**Spring AOP 属于运行时增强，而 AspectJ 是编译时增强。** Spring AOP 基于代理(Proxying)，而 AspectJ 基于字节码操作(Bytecode Manipulation)。