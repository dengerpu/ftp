package main.client;

/*
* 该线程累的作用就是实时监听FTP服务器的反馈信息，并显示在客户端界面
* */

import java.io.*;

public class Receive implements Runnable  {
    String synObject=null;
    BufferedReader sendFromServer;
    PrintWriter sentToServer;
    InputStream is;
    OutputStream os;
    boolean isStop=false;//控制线程的状态变量
    public Receive(InputStream is,OutputStream os,String object){
        this.is = is;
        this.os = os;
        this.synObject = object;
    }
    //将线程暂停运行
    public void stopNow(boolean isStart){
        this.isStop = isStart;
    }
    //将线程恢复运行
    public void startNow(boolean isStart)
    {
        this.isStop=!isStart;
    }
    public void run(){
        try
        {
            String echo=null;
            sendFromServer=new BufferedReader(new InputStreamReader(is));
            sentToServer=new PrintWriter(os,true);//发送给服务端的控制流的封装
            synchronized(synObject)
            {
                while(true)
                {
                    if(!isStop)
                    {
                        echo=sendFromServer.readLine();//接收服务器的控制信息
                        if(echo==null||echo.equals(""))
                        {
                            break;
                        }
                        else if(echo.equals("accept"))
                        {
                            isStop=true;
                            sentToServer.println("pardon");//客户端要求服务器重新发送第一轮握手协议
                        }
                        else
                        {
                            System.out.println(echo);//显示服务器的反馈信息
                        }
                    }
                    else if(isStop)
                    {
                        System.out.println("Data Channel started!");//显示服务器的控制信息
                        synObject.wait();     //如果转换成数据流的信号开始了就将线程等待
                    }
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("BYE!");
        }
    }
}

