package main.client;

/*
 * FTP客户端程序
 * */


import main.java.ClientUI;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class FtpClient {
    /*
     * 初始化FTP客户端
     * 参数： args[0]:ip   args[1]:端口
     * 返回：clientSocket:客户端Socket
     * */
    public static  String[][] files = null;
    public static int total = 0;
    public static String options = "ls";
    public Socket init(String ip,int port){
        Socket clientSocket = null;
        if(ip!=null){
            try{
                //接收参数，传递服务器ip和port
                clientSocket = new Socket(ip,port);
                //初始化下载文件存放文件夹“Download”
                File downDirctory = new File("c:\\Download");
                if(!downDirctory.exists()){         //如果文件不存在就创建文件
                    downDirctory.isDirectory();
                    downDirctory.mkdir();
                }

            }catch (Exception e){
                System.out.println("初始化客户端发生错误");
                e.printStackTrace();
            }
        }else{
            System.out.println("ip或者端口号为空");
        }
        return clientSocket;
    }

    /**
     * 从Ftp服务器上退出
     * 参数： sentToServer 发送给FTP服务端的控制信息
     * */

    public void quit(PrintWriter sentToServer){
        sentToServer.println("quit");  //向Ftp服务端发送“quit”命令
    }
    /**
     * 列表显示FTP服务器端的共享文件
     * 参数：sentToServer 发送给Ftp服务端的控制信息
     * **/
    public void listFiles(PrintWriter sentToServer){
        sentToServer.println("ls");  //向Ftp服务器端发送“ls”命令
    }

    /**
     * 从FTP服务器端下载文件
     * 参数：is            FTP客户端套接字输入流
     * 参数：recv          监听FTP服务器反馈信息的线程
     * 参数：sentToServer  发送给FTP服务端的控制信息
     * 参数：synObject     线程同步的对象标识
     * 参数：keyBoardIn    FTP客户端键盘的输入控制信息
     * 参数：serverIn      接收FTP服务器端关于文件传输的控制信息
     * **/
    public void downFromServer(InputStream is, Receive recv, PrintWriter sentToServer, String synObject,String filename, BufferedReader serverIn,String savePath){
        sentToServer.println("get");  //向服务器端发送"get"命令
        recv.stopNow(true);//停止接收线程
        sentToServer.println("ready"); //发送第一轮握手标志“ready”
        //下面的一个同步块就是用来控制信息传输流转换成数据传输流
        synchronized (synObject){
            while(true){
                try{
                    //双方达成协议 开始传发文件
                    if((serverIn.readLine()).equalsIgnoreCase("accept")){
                        System.out.println("Protocol：" + "accept");
                        String fileName = null;
                        sentToServer.println(fileName = filename);
                        String handShake = serverIn.readLine();
                        if(handShake == null || handShake.equalsIgnoreCase("stop")){
                            JOptionPane.showMessageDialog(null, "该文件类型不支持下载！","消息提示",JOptionPane.ERROR_MESSAGE);
                            System.out.println("**warnning**:This file can'n be download! ");
                        }else{
                            int fileLength = Integer.parseInt(handShake);
                            System.out.println("待下载文件大小："+fileLength);
                          //  FileOutputStream fileCopy = new FileOutputStream("c:"+ File.separator + "Download" + File.separator + fileName);//文件下载目录
                            FileOutputStream fileCopy = new FileOutputStream(savePath);//文件下载目录
                            TransferInfo transferInfo = new TransferInfo(); //文件传输时输出"."的线程
                            Thread transferThread = new Thread(transferInfo);
                            transferInfo.startNow(true);
                            transferThread.start();
                            //通过文件大小实现文件的同步传输
                            for (int i = 0; i < fileLength; i++) {
                                fileCopy.write(is.read());   //将文件下载到本地
                            }
                            fileCopy.close();  //关闭文件
                            sentToServer.println("finished");  //发送文件下载完毕信号
                            transferInfo.stopNow(true);  //传输完毕，终止输出"."

                        }
                        synObject.notify();   //将数据流又切换到控制流
                        recv.startNow(true);  //打开接收线程
                        Thread.sleep(1000);
                        break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从FTP客户端上传文件
     * 参数：os            FTP客户端套接字输出流
     * 参数：recv          监听FTP服务器反馈信息的线程
     * 参数：sentToServer  发送给FTP服务器端的控制信息
     * 参数：synObject     线程同步的对象标识
     * 参数：keyBoardIn    FTP客户端键盘的输入控制信息
     * 参数：serverIn      接收FTP服务器端关于文件传输的控制信息
     * */
    public void upToServer(OutputStream os, Receive recv, PrintWriter sentToServer, String synObject, String filename, BufferedReader serverIn){
        sentToServer.println("put");   //向FTP服务器端发送"get"命令
        recv.stopNow(true);    //停止接受线程
        sentToServer.println("ready");//发送第一轮握手标志
        //下面的一个同步块就是用来控制信息传输流转化成数据传输流
        synchronized (synObject){
            while(true){
                try{
                    //双方达成协议，开始传发文件
                    if ((serverIn.readLine()).equalsIgnoreCase("accept")){
                        System.out.println("Protocol：" + "accept");
                        int content;
                        String fileName = null;
                        sentToServer.println(fileName = filename);
                        FileInputStream checkFile = new FileInputStream(fileName);
                            try{
                                int fileLength = 0;
                                //读写文件的大小
                                while((content = checkFile.read())!=-1){
                                    fileLength++;
                                }
                                sentToServer.println(fileLength);  //获取待上传文件的大小并发送到服务器端
                                checkFile.close(); //关闭文件
                                Thread.sleep(500); //次数线程等待是为了保证读取同一个文件时不会出现重写错误
                                FileInputStream upFile = new FileInputStream(fileName);
                                TransferInfo transferInfo = new TransferInfo();// 文件传输时输出"."的线程
                                Thread transferThread = new Thread(transferInfo);
                                transferInfo.startNow(true);
                                transferThread.start();
                                //按字节的形式将文件传送到服务器
                                while((content = upFile.read())!=-1){
                                    os.write(content);
                                }
                                upFile.close();  //关闭文件
                                transferInfo.stopNow(true);  //传输完毕，终止输出
                            }catch (Exception e){
                                JOptionPane.showMessageDialog(null, "该文件类型不支持上传！","消息提示",JOptionPane.ERROR_MESSAGE);
                                System.out.println("**warnning**:This file can'n be uploaded! ");
                            }
                        }
                    //}
                    sentToServer.println("finished");  //发送文件下载完毕信号
                    synObject.notify(); //将数据流又切换到控制流
                    recv.startNow(true);  //打开接收线程
                    Thread.sleep(1000);
                    break;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * FTP客户端运行主程序
     * 参数：clientSocket      FTP客户端套接字
     *
     * */
    public static OutputStream os;
    public static  InputStream is;
    public static PrintWriter sentToServer;
    public static  BufferedReader serverIn;
    public static Receive recv;
    public static Thread recvThread;
    public static String synObject = "";
    public static Socket clientSocket1 = null;
    public static BufferedReader keyBoardIn1 = null;
    public void run(Socket clientSocket){
        try{
            clientSocket1 = clientSocket;
           // String synObject="";   //线程同步资源
            BufferedReader keyBoardIn=new BufferedReader(new InputStreamReader(System.in));
            keyBoardIn1 = keyBoardIn;
            String instruction=null; // 客户端的控制指令
            os=clientSocket.getOutputStream();// 套接字输出流
            is = clientSocket.getInputStream();// 套接字输入流
            sentToServer=new PrintWriter(os,true);//发送给服务端的控制流的封装
            serverIn=new BufferedReader(new InputStreamReader(is));//接收服务器发来的信息
            recv=new Receive(is,os,synObject);//FTP服务器的监听线程
            recvThread=new Thread(recv); // 建立接收服务数据的线程
            recvThread.setDaemon(true);//将线程设置成后台的以便于随主程序终止而终止
            recvThread.start();
                options = "ls";
                if(options.equalsIgnoreCase("ls"))
                {
                    Thread.sleep(1000);

                    //显示FTP服务器端的共享文件
                    listFiles(sentToServer);
                   // Thread.sleep(1000);
                    byte[] bytes = new byte[1024*10];
                    int len = is.read(bytes);
                    String listString = new String(bytes, 0, len);
                    System.out.println("获取"+listString);
                    String FileList = listString.substring(1,listString.length()-2);
                    String[] split = FileList.split(",,");
                    files = new String[100][100];
                    int i = 0;
                    for (String s : split) {
                        String[] split1 = null;
                        split1 = s.substring(2, s.length() - 1).split(",");
                        int j = 0;
                        for (String s1 : split1) {
                            files[i][j++] = s1.substring(1,s1.length()-1);
                        }
                        i++;
                    }
                    total = i;  //记录数据总数
                    new ClientUI();

                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //运行下载的方法
    public  void DownFun(String filename,String savePath){
        downFromServer(is,recv,sentToServer,synObject,filename,serverIn,savePath);
    }
    //运行上传的方法
    public void UpLoadFun(String filename){
        //从FTP客户端上传文件
        upToServer(os,recv,sentToServer,synObject,filename,serverIn);
    }
    public void TrashFun(){
        //系统资源回收
        trash(keyBoardIn1,serverIn,sentToServer,clientSocket1);
    }

    /**
     * 系统资源回收
     * 参数： keyBoardIn   FTP客户端键盘的输入控制信息
     * 参数： serverIn     接受FTP服务器端关于文件传输的控制信息
     * 参数： sentToServer 发送给FTP服务端的控制信息
     * 参数： clientSocket 客户端套接字
     */
    public void  trash(BufferedReader keyBoardIn,BufferedReader serverIn,PrintWriter sentToServer,Socket clientSocket) {
        try
        {
            keyBoardIn.close();
            serverIn.close();
            sentToServer.close();
            clientSocket.close();
        } catch (IOException e)
        {
            System.out.println("Resources recycled failed!");
            e.printStackTrace();
        }

    }

}