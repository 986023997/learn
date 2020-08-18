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
public class LightOffCommand implements Command {
    private Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.off();
    }
}
