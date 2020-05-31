package main.java.client;


/**
 * 该线程的作用：文件传输过程中，每隔0.5秒输出一个
 * */
public class TransferInfo implements Runnable {
    boolean isStop=false;//控制线程的状态变量
    public TransferInfo()
    {

    }
    //将线程暂停运行
    public void stopNow(boolean isStop)
    {
        this.isStop=isStop;
    }
    //将线程恢复运行
    public void startNow(boolean isStart)
    {
        this.isStop=!isStart;
    }
    public void run()
    {
        try
        {
            //每隔0.5秒，输出一个"."号
            while(true)
            {
                if(!isStop)
                {
                    Thread.sleep(500);
                    System.out.print(".");
                }
                else if(isStop)
                {
                    System.out.println("DONE!");
                    break;    //文件传输完毕则终止循环
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
