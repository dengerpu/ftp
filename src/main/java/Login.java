package main.java;

import main.client.RunClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JFrame {
    JButton jb1, jb2;  //按钮
    JTextField jtf;   //文本框
    JLabel jlb1, jlb2, jlb3,jlb4; //标签
    JPasswordField jpf; //密码框
    String name;
    String password;
    String order;
    int flag=0;
    public Login(){
        jb1 =new JButton("登陆");   //添加两个按钮
        jb2 =new JButton("重置");
        jb1.setBackground(Color.blue);
        jb2.setBackground(Color.blue);
        jlb1=new JLabel("用户名");   //添加两个标签
        jlb2=new JLabel("  密  码");
        jlb1.setForeground(Color.red);
        jlb2.setForeground(Color.red);
        jtf = new JTextField(10);	//创建文本框和密码框
        jpf = new JPasswordField(10);

        jlb3=new JLabel("注册账户");
        jlb4=new JLabel("忘记密码");


        jlb3.setBounds(5,140,60,30);jlb4.setBounds(240,140,60,30);
        jlb3.setForeground(Color.white);jlb4.setForeground(Color.white);

        jlb1.setBounds(40,30,50,25);jtf.setBounds(80,30,150,25);
        jlb2.setBounds(40,65,50,25);jpf.setBounds(80,65,150,25);
        jb1.setBounds(80,100,60,30);jb2.setBounds(170,100,60,30);
        this.add(jb1);this.add(jb2);this.add(jlb1);this.add(jlb2);this.add(jtf);this.add(jpf);
        this.add(jlb3);this.add(jlb4);
        ImageIcon image=new ImageIcon("src//img//bg.jpg");
        JLabel background=new JLabel(image);
        // 把背景图片添加到分层窗格的最底层作为背景
        this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
        background.setBounds(0,0,300,200);
        // 把内容窗格转化为JPanel，否则不能用方法setOpaque()来使内容窗格透明
        JPanel imagePanel = (JPanel) this.getContentPane();
        imagePanel.setOpaque(false);
        imagePanel.add(background);


        this.setIconImage((new ImageIcon("src//img//ftp.png").getImage()));   //修改窗体图标
        this.setTitle("用户登录");
        this.setLayout(null);
        this.setSize(300, 200);   //设置窗体大小
        this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);  //设置仅关闭当前窗口
        this.setVisible(true);  //设置可见
        this.setResizable(false);	//设置不可拉伸大小
        jb1.addActionListener(new MyListener());    //为登陆按钮添加事件
        jb2.addActionListener(new MyListener());    //为重置按钮添加事件

        //为注册按钮添加点击事件
        jlb3.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON1)
                    new Register();
            }
        });
        jlb4.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON1)
                    JOptionPane.showMessageDialog(null, "联系管理员","消息提示",JOptionPane.WARNING_MESSAGE);
            }
        });
    }
    //为登陆动作监听事件执行处理
    class MyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand()=="登陆")
            {
                try{
                    login();
                }catch(HeadlessException e1) {
                    e1.printStackTrace();
                }}
            else if(e.getActionCommand()=="重置")
                clear();
        }
    }


    //重置方法，清空账号和密码
    public void clear() {      //清除账号和密码
        jtf.setText("");       //设置为空
        jpf.setText("");
    }

    //验证登录信息

    public void login() throws HeadlessException{
        char[] a=jpf.getPassword();
        String password=String.valueOf(a);   //将字符数组转化为字符串
        String accountname=jtf.getText();
        Check check = new Check();
        if(jtf.getText().isEmpty()&&password.isEmpty())
            JOptionPane.showMessageDialog(null, "账号密码为空，请输入！","消息提示",JOptionPane.WARNING_MESSAGE);
        else if(jtf.getText().isEmpty())
            JOptionPane.showMessageDialog(null,"账号为空，请输入！","消息提示",JOptionPane.WARNING_MESSAGE);
        else if(password.isEmpty())
            JOptionPane.showMessageDialog(null,"密码为空，请输入！","消息提示",JOptionPane.WARNING_MESSAGE);
        else if(check.check2(accountname, password))
        {
            JOptionPane.showMessageDialog(null,"登陆成功","消息提示",JOptionPane.WARNING_MESSAGE);
           new RunClient();
            dispose();   //使窗口消失
        }
        else {
            JOptionPane.showMessageDialog(null, "账号密码错误请重新输入！","消息提示",JOptionPane.ERROR_MESSAGE);
            clear();  //调用清除函数
        }
    }

}
