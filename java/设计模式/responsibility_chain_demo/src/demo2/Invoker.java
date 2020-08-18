package demo2;

/**
 * @ClassNmae: Invoker
 * @Description: java类作用描述
 * @Author: zhulin
 * @CreateDate: 2020/8/4$ 23:37$
 * @UpdateUser: zhulin
 * @UpdateDate: 2020/8/4$ 23:37$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class Invoker {
    private  Command[] onCommand;
    private  Command[] offCommand;

    public Invoker() {
        this.onCommand = new Command[2];
        this.offCommand = new Command[2];
    }

    public  void setOnCommand(Command command, int index){
        this.onCommand[index]=command;
    }
    public  void setOffCommand(Command command,int index){
        this.offCommand[index]=command;
    }
    public void doOnCommand(int index){
        this.onCommand[index].execute();
    }
    public void doOffCommand(int index){
        this.offCommand[index].execute();
    }

}
