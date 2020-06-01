package main.server;

import main.client.FtpClient;

import java.net.ServerSocket;

/*
* FTP服务器运行主程序
* */
public class RunServer {
    public static void main(String[] args)
    {
        FtpServer server = new FtpServer();  //生成FTP服务器端
        ServerSocket serverSocket = server.init(); //初始化并生成服务器套接字
        try {
            server.run(serverSocket);   //运行FTP服务器端程序
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
