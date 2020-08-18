package demo1;

/**
 * @ClassNmae: Handler
 * @Description: java类作用描述
 * @Author: zhulin
 * @CreateDate: 2020/8/4$ 22:24$
 * @UpdateUser: zhulin
 * @UpdateDate: 2020/8/4$ 22:24$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public abstract class Handler {
    protected Handler handler;
    protected Handler(Handler handler){
        this.handler=handler;
    }
    public abstract void doFilter(Integer num);
}
