package main.java.server;

import main.java.java.util.FileBean;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
* 实现多客户连接的线程类编写
* */
public class ClientConnect implements Runnable {
    Socket clientConnectSocket;    //客户端连接套接字
    BufferedReader receiveFormClient;   //服务器控制信息输入流
    PrintWriter sentToClient;            //服务器端控制信息输出流
    String ftpPath;                     //服务器共享目录
    public ClientConnect(Socket clientConnectSocket,String ftpPath){
        this.clientConnectSocket = clientConnectSocket;
        this.ftpPath = ftpPath;
    }
    /**
     * 显示共享目录下的所有文件
     * */
    public void listFiles(String ftpPath) {
        File menu = new File(ftpPath);  //传递共享的路径名
        String[] subFile = menu.list();
        File[] file = menu.listFiles();
        List<FileBean> list = new ArrayList<FileBean>();
        sentToClient.println("/***************文件开始展示***************/");
        for (int i = 0; i < subFile.length; i++) {
            FileBean fileBean = new FileBean();
            fileBean.setFilename(file[i].getName());
            fileBean.setFileLength(file[i].length());
            fileBean.setFileType(file[i].getName().substring(file[i].getName().lastIndexOf(".") + 1));
            fileBean.setUpdateTime(file[i].lastModified());
            list.add(fileBean);
        }
        try {
            clientConnectSocket.getOutputStream().write(list.toString().getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }

        sentToClient.println("/***************The shared files show ended***************/");
    }
    /**
     * 发送给客户端的帮助信息
     * */
    public void tips(){
        sentToClient.println("/************************************************************/");
        sentToClient.println("/**********************||HELP||******************************/");
        sentToClient.println("/**  ls:   To show the shared files!                      **/");
        sentToClient.println("/**  get:  To download the file from server to client!    **/");
        sentToClient.println("/**  put:  To Upload the file from client to server!      **/");
        sentToClient.println("/**  quit: To exit the ftpServer!                         **/");
        sentToClient.println("/**  help: To show all commands!                          **/");
        sentToClient.println("/************************************************************/");
    }


    /**
    * 下载文件到客服端
    * */
    public void downToClient(OutputStream os,BufferedReader receiveFormClient)throws Exception{
        int content;
        String fileName = null;
        FileInputStream checkFile = null;
        sentToClient.println("==>Please Input the file name:");
        //接收客服端的准备标志“ready”
        String handShake = receiveFormClient.readLine();
        System.out.println("Protocol: " + handShake);
        if(handShake.equalsIgnoreCase("ready")){
            Thread.sleep(1000);  //主线程休眠1秒钟，为了与客户端保持传输同步
            sentToClient.println("accept"); //告诉客户端已经准备好传输文件
            handShake = receiveFormClient.readLine();
            //如果客户端没有收到“accept”控制命令，则服务器重新发送一次
            if(handShake.equalsIgnoreCase("pardon")){
                sentToClient.println("accept");  //重发一次确认信息
                fileName = receiveFormClient.readLine();
            }else{
                fileName = handShake;
            }
            System.out.println("Ready to download：" + fileName);
            String pathName = ftpPath + fileName;
            try{
                checkFile = new FileInputStream(pathName);
                int fileLength = 0;
                //读写文件的大小
                while((content = checkFile.read()) != -1){
                    fileLength++;
                }
                sentToClient.println(fileLength);  //获取待下载文件的大小并发送到客户端
                System.out.println("文件大小: " + fileLength);
                checkFile.close();
                Thread.sleep(500);  //此处线程等待是为了保证读取同一个文件时不会出现重写错误
                FileInputStream downFile = new FileInputStream(pathName);
                //按字节的形式将文字传送到客户端
                while ((content = downFile.read()) != -10) {
                    os.write(content);
                }
                downFile.close(); //关闭文件
                handShake = receiveFormClient.readLine();  //接收客户端的接收完毕信号
               if(handShake.equalsIgnoreCase("finished")) {  //发送文件下载完毕信号
                   System.out.println("DownLoad successful!");
                   sentToClient.println("The file:" + "\" "+ fileName + "\""+ "is download completely!");
               }

            }catch (Exception e){
                //当待下载的文件
                sentToClient.println("stop");
                System.out.println("Wrong fileName!");
            }
        }

    }

    /**
     * 客户端上传文件到服务器
     * */

    public void upFromClient(InputStream is,BufferedReader receiveFormClient) throws  Exception{
        String fileName = null;
        sentToClient.println("==>Please Input the upload file name:");
        //接收客户端的准备标志“ready”
        String handShake = receiveFormClient.readLine();
        System.out.println("Protocal: " + handShake);
        if(handShake.equalsIgnoreCase("ready")){
            Thread.sleep(1000);  //主线程休眠1秒钟，是为了与客户端把持输出同步
            sentToClient.println("accept");  //告诉客户端已经准备好传输文件
            handShake = receiveFormClient.readLine();
            while (true){
                if(handShake.equalsIgnoreCase("pardon")){
                    sentToClient.println("accept");  //重发一次
                    handShake = receiveFormClient.readLine();
                }else{
                    break;
                }
            }
            fileName = handShake;
            System.out.println("Ready to upload：" + fileName);
            if(receiveFormClient.readLine().equalsIgnoreCase("continue")){
                int fileLength = Integer.parseInt(receiveFormClient.readLine());
                System.out.println("上传文件大小：" + fileLength);
                try{
                    //在服务器端生成客户端上传的文件
                    FileOutputStream fileCopy = new FileOutputStream(ftpPath);
                    for (int i = 0; i < fileLength; i++) {
                        fileCopy.write(is.read());  //将文件上传到服务器
                    }
                    fileCopy.close();//关闭文件
                    handShake = receiveFormClient.readLine(); //接收客户端的接收完毕信号
                    if(handShake.equalsIgnoreCase("finished")){  //发送文件上传信号
                        System.out.println("Upload successful!");
                        sentToClient.println("The file:" + "\" "+ fileName + "\""+ "is uploaded completely!");
                    }

                }catch (Exception e){
                    //e.printStackTrace();
                    sentToClient.println("**warnning**:This file can'n be uploaded! ");
                }
            }else{
                System.out.println(fileName + "upload interrupted!");
                handShake = receiveFormClient.readLine();// 接收客户端的接收完毕信号
                if (handShake.equalsIgnoreCase("finished"))// 发送文件上传完毕信号{
                    sentToClient.println("The file:" + "\" "+ fileName + "\""+ "can'n be uploaded!");
                }
            }
        }
        /*
        * 线程运行类
        * */
    @Override
    public void run() {
        try{
            //服务器与客户端实现输入输出流的链接
            InputStream is = clientConnectSocket.getInputStream();
            OutputStream os = clientConnectSocket.getOutputStream();
            receiveFormClient = new BufferedReader(new InputStreamReader(is));
            // PrintWriter 的println会产生适合系统的换行符 比PrintStream好
            sentToClient = new PrintWriter(os, true);
            sentToClient.println("/**************Ftp login successful!****************/");
            tips();// 显示所有的命令提示信息
            while(true){
                sentToClient.println("==>Please input command:");
                String text = receiveFormClient.readLine();
                System.out.println("Command：" + text);
                //退出Ftp服务器
                if(text.equalsIgnoreCase("quit")){
                    System.out.println("One Client logout!");
                    sentToClient.println("/**************Ftp logout successful!****************/");
                    break;
                }else if (text.equalsIgnoreCase("help")){
                    tips();  //显示所有的命令提示信息
                }else if(text.equalsIgnoreCase("ls")) {
                    listFiles(ftpPath);  //显示共享的文件
                }

                else if (text.equalsIgnoreCase("get"))
                {
                    downToClient(os, receiveFormClient); // 传递文件流到客户端
                }
                // 传递文件流到服务器端
                else if (text.equalsIgnoreCase("put"))
                {
                    upFromClient(is, receiveFormClient);
                }
                sentToClient.println("==>command\"" + text+ "\"is done successfully!");
            }
            receiveFormClient.close();
            sentToClient.close();
            clientConnectSocket.close();
        }catch (IOException e){

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
