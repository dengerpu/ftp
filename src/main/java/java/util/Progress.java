package main.java.java.util;

import main.java.client.FtpClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;

public class Progress {
    static long allFileSize = 0; // 所有需要复制的文件大小
    static long currentFileSizeCopied = 0;// 已复制的文件总大小
    public Progress(String op, String filePath,String name){
        JFrame f = new JFrame(op+"进度条");
        f.setSize(450, 140);
        f.setLocation(200, 200);
        f.setLayout(new FlowLayout());


        JButton bStartCopy = new JButton("开始"+op);
        bStartCopy.setPreferredSize(new Dimension(100, 30));

        JLabel l = new JLabel("文件"+op+"进度：");
        JProgressBar pb = new JProgressBar();
        pb.setMaximum(100);
        pb.setStringPainted(true);

        f.add(bStartCopy);
        f.add(l);
        f.add(pb);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.setVisible(true);
        FtpClient ftpClient = new FtpClient();

        // 计算需要复制的文件的总大小
        String srcPath = filePath;
        File folder = new File(srcPath);
        calclateAllFilesize(folder);
        // 点击开始复制
        bStartCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentFileSizeCopied = 0;
                copyFolder(filePath);
                //开始操作
                bStartCopy.setEnabled(false);
            }
            public void copyFolder(String srcPath) {
                File srcFolder = new File(srcPath);

                File[] files = srcFolder.listFiles();
                for (File srcFile : files) {

                    if (!(srcFile.isDirectory())) {

                        //这一步执行下载或者上传操作
                        ftpClient.DownFun(name,filePath);
                        currentFileSizeCopied += srcFile.length();

                        double current = (double) currentFileSizeCopied / (double) allFileSize;
                        int progress = (int) (current * 100);
                        pb.setValue(progress);
                        if (progress == 100) {
                            JOptionPane.showMessageDialog(f, op+"完成");
                            bStartCopy.setEnabled(true);
                        }

                    }

                    if (srcFile.isDirectory()) {
                        copyFolder(srcFile.getAbsolutePath());
                    }

                }
            }
        });
    }


    /**
     * 遍历文件夹获取文件夹内容总大小
     *
     * @param file
     */
    public static void calclateAllFilesize(File file) {   //获取文件的总大小
        allFileSize = 0;
        if (file.isFile()) {
            allFileSize += file.length();
        }
        if (file.isDirectory()) {
            File[] fs = file.listFiles();
            for (File f : fs) {
                calclateAllFilesize(f);
            }
        }

    }
}
