package demo2;

import java.lang.reflect.InvocationHandler;
import java.util.LinkedHashSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassNmae: Client
 * @Description: java类作用描述
 * @Author: zhulin
 * @CreateDate: 2020/8/4$ 23:42$
 * @UpdateUser: zhulin
 * @UpdateDate: 2020/8/4$ 23:42$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class Client {
    public static void main(String[] args){
        Invoker invoker=new Invoker();
        Tv tv=new Tv();
        TvOnCommand tvOnCommand=new TvOnCommand(tv);
        TvOffCommand tvOffCommand=new TvOffCommand(tv);
        invoker.setOnCommand(tvOnCommand,0);
        invoker.setOffCommand(tvOffCommand,0);
        Light light=new Light();
        LightOnCommand lightOnCommand=new LightOnCommand(light);
        LightOffCommand lightOffCommand=new LightOffCommand(light);
        invoker.setOnCommand(lightOnCommand,1);
        invoker.setOffCommand(lightOffCommand,1);
        invoker.doOnCommand(0);
        invoker.doOnCommand(1);
        invoker.doOffCommand(1);
        invoker.doOffCommand(0);
        Thread

    }
}
