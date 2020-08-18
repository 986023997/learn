package demo2;

/**
 * @ClassNmae: TvOnCommand
 * @Description: java类作用描述
 * @Author: zhulin
 * @CreateDate: 2020/8/4$ 23:36$
 * @UpdateUser: zhulin
 * @UpdateDate: 2020/8/4$ 23:36$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class TvOnCommand implements Command {
    private Tv tv;

    public TvOnCommand(Tv tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.on();
    }
}
