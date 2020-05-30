package main.java.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class FtpServer {
    static String ftpPath = "C:/";  //Ftp共享的目录，设置默认为C盘
    //初始化FTP服务器
    public ServerSocket init(){
        ServerSocket server = null;
        String path = null;  //FTP共享路径

        try{
            server = new ServerSocket(6500);//建立FTP服务器绑定6500端口
            BufferedReader readPath = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("/*********************************************************/");
            System.out.println("The Ftp is started successful!");
            System.out.println("The first thing need to do is set the shared filePath!");
            System.out.println("For example: c:/");
            System.out.println("/*********************************************************/");
           // System.out.print("==>Please Input the Shared Path:");
           // if(!(path=readPath.readLine()).equals("")){
              //  ftpPath = path;
            //}

            //  System.out.println("文件上传根目录："+ftpPath);
            //初始化上传文件存放文件夹"upload"
            //File.separator代表系统目录中的间隔线，可以解决兼容问题
            File downDirctory = new File(ftpPath + File.separator + "Upload");
            if(!downDirctory.exists()){
                downDirctory.isDirectory();
                downDirctory.mkdir();
            }

            System.out.println(downDirctory);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("The port has been used或者FtpServer39行发生错误");
        }
        return server;

    }

    /**
     * FTP服务器端运行主程序
     * */

    public void  run(ServerSocket serverSocket) throws Exception{
        boolean judge = true;
        while (judge){
            //接收多个客户端为每个客户端建立连接
            Socket client = serverSocket.accept();
            Thread thread = new Thread(new ClientConnect(client, FtpServer.ftpPath));
            thread.start();
            System.out.println("one Client Login!");
        }
        serverSocket.close();
    }

}
