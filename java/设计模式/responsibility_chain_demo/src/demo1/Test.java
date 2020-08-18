package demo1;

/**
 * @ClassNmae: Test
 * @Description: java类作用描述
 * @Author: zhulin
 * @CreateDate: 2020/8/4$ 22:41$
 * @UpdateUser: zhulin
 * @UpdateDate: 2020/8/4$ 22:41$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class Test {
    public static void main(String[] args) {
        Integer num=40;
        Integer num2=60;
        Handler handler2=new Processor2(null);
        Handler handler1=new Processor1(handler2);
        handler1.doFilter(num);
        handler1.doFilter(num2);
    }
}
