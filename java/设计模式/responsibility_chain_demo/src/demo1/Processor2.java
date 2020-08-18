package demo1;

/**
 * @ClassNmae: Processor1
 * @Description: java类作用描述
 * @Author: zhulin
 * @CreateDate: 2020/8/4$ 22:30$
 * @UpdateUser: zhulin
 * @UpdateDate: 2020/8/4$ 22:30$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class Processor2 extends Handler {

    protected Processor2(Handler handler) {
        super(handler);
    }


    @Override
    public void doFilter(Integer num) {
        if (num > 50) {
            System.out.println(this.getClass().getName() + "处理了");
        }else if(handler!=null){
            handler.doFilter(num);
        }
    }
}