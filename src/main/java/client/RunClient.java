package main.java.client;

/*
* FTP客户端运行的主程序
*
* */

import java.net.Socket;

public class RunClient {
    public static void main(String[] args)
        {
        FtpClient client = new FtpClient();  //生成客户端
        Socket clientSocket = client.init("127.0.0.1",6500);   //初始化并生成客户端套接字
        try{
            client.run(clientSocket);   //运行FTP客户端
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
