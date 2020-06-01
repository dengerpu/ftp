package main.java.util;


import javax.swing.*;
import java.awt.*;


public class Progress {
    public static JProgressBar pb;
    public static JFrame f;
    public Progress(){
        f = new JFrame("进度条");
        f.setSize(400, 300);
        f.setLocation(200, 200);

        f.setLayout(new FlowLayout());

         pb = new JProgressBar();

        //进度条最大100
        pb.setMaximum(100);
        //当前进度是0
        pb.setValue(1);
        //显示当前进度
        pb.setStringPainted(true);
        f.add(pb);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

    }
    public static void setP(int i){
        pb.setValue(i);
    }
    public static void close(){
        f.dispose();
    }
}
