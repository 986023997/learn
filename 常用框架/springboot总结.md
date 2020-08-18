# springboot总结

## springboot自动配置加载的原理

springboot通过启动类@SpringBootApplication注解内的@EnableAutoConfiguration注解，在这个注解内

![image-20200731093133780](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200731093133780.png![image-20200731093202134](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200731093202134.png)

可以看到引入 了这个类，进到这个类里

![image-20200731095227054](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200731095227054.png)

主要看这个方法

![image-20200731095315543](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200731095315543.png)

里面调用了getCandidateConfigurations()这个方法得到自动配置的类有132，进入到这个方法

![image-20200731095437678](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200731095437678.png)

这里调用了springFactoriesLoader.loadFactoryNames()方法进入到这个类

![image-20200731095857018](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200731095857018.png)

调用了loadSpringFactories这个方法，进入到这个方法，可以看到这个方法调用了加载器，去加载位于META-INF/spring.factories下的全限定的类型，这些类就是用于自动配置的类

![image-20200731222319030](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200731222319030.png)

我们进入到某一个自动配置类如redis的自动配置类，在类上@EnableConfigurationProperties这个注解标注了属性类，



![image-20200731225743214](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200731225743214.png)

进入到这个属性类，可以看到提供了一些默认的属性，因此我们可以不用配置这些属性，采用默认的配置即可

![image-20200731225915916](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20200731225915916.png)