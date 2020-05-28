package main.java.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class Register extends JFrame implements ActionListener {

    JButton jb1, jb2;  //按钮
    JLabel jlb1, jlb2, jlb3,jlb4,jlb5, jlb6;  //标签
    JTextField jtf1,jtf2,jtf3,jtf4, jtf5;   //文本框
    JPasswordField jpf; //密码框
    JPanel jp1,jp2,jp3, jp4,jp5,jp6,jp7;		//面板

    public Register() {
        //按钮
        jb1 = new JButton("确定");
        jb2 = new JButton("重置");
        //设置按钮监听
        jb1.addActionListener(this);
        jb2.addActionListener(this);
        //标签信息


        jlb1 = new JLabel("        账号");
        jlb2 = new JLabel("        密码");
        jlb3 = new JLabel("确认密码");
        jlb4 = new JLabel("       邮箱");
        jlb5 = new JLabel("   验证码");

        jlb6 = new JLabel("注册信息");

        jlb6.setFont(new   java.awt.Font("Dialog",   1,   20));   //设置字体类型，加粗，大小为20
        //文本信息
        jtf1 = new JTextField(13);
        jtf2 = new JTextField(13);
        jtf3 = new JTextField(13);
        jtf4 = new JTextField(13);
        jtf5 = new JTextField(13);

        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();
        jp4 = new JPanel();
        jp5 = new JPanel();
        jp6 = new JPanel();
        jp7 = new JPanel();
        //将对应信息加入面板中
        jp1.add(jlb1);
        jp1.add(jtf1);

        jp2.add(jlb2);
        jp2.add(jtf2);

        jp3.add(jlb3);
        jp3.add(jtf3);

        jp4.add(jlb4);
        jp4.add(jtf4);

        jp5.add(jlb5);
        jp5.add(jtf5);

        jp6.add(jb1);
        jp6.add(jb2);

        jp7.add(jlb6);

        //将JPane加入JFrame中
        this.add(jp7);  //先加入提示语

        this.add(jp1);
        this.add(jp2);
        this.add(jp3);
        this.add(jp4);
        this.add(jp5);
        this.add(jp6);

        //设置布局
        this.setTitle("注册信息");
        this.setLayout(new GridLayout(7, 1));
        this.setSize(350, 350);   //设置窗体大小
        this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);  //设置仅关闭当前窗口
        this.setIconImage((new ImageIcon("src//img//银行卡.png").getImage()));   //修改窗体图标
        this.setVisible(true);  //设置可见
        this.setResizable(false);	//设置不可拉伸大小

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getActionCommand()=="确定")
        {
//            try {
////                register();
////            } catch (IOException | SQLException e1) {
////                // TODO Auto-generated catch block
////                e1.printStackTrace();
////            }
            System.out.println("确定事件");
        }
        else if (e.getActionCommand()=="重置")
        {
            clear();
        }

    }
    //验证注册信息，并做处理
    public void register() throws IOException, SQLException
    {
        //判断信息是否补全
        if (jtf1.getText().isEmpty()||jtf2.getText().isEmpty()||
                jtf3.getText().isEmpty()||jtf4.getText().isEmpty()||jtf5.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "信息有空缺，请补全！","消息提示",JOptionPane.WARNING_MESSAGE);
        }
        //判断账户名和密码是否包含中文
        else if (new Check().checkcountname(jtf1.getText())||new Check().checkcountname(jtf2.getText()))
        {
            JOptionPane.showMessageDialog(null, "用户名或密码存在中文，不合法!","消息提示",JOptionPane.WARNING_MESSAGE);
        }
        //判断邮箱是否正确
        else if (new Check().isemail(jtf4.getText()))
        {
            JOptionPane.showMessageDialog(null, "邮箱地址不合法，请重新输入！","消息提示",JOptionPane.WARNING_MESSAGE);
        }

        //满足要求
        else if (!jtf1.getText().isEmpty()&&!jtf2.getText().isEmpty()&&
                !jtf3.getText().isEmpty()&&!jtf4.getText().isEmpty()&&!jtf5.getText().isEmpty())
        {
//            //注册成功， 打包为信息数组传递给UserMessage进行更新操作
//            String []message = new String[5];
//            message[0] = jtf1.getText();   //获取输入的文本信息
//            message[1] = jtf2.getText();
//            message[2] = jtf3.getText();
//            message[3] = jtf4.getText();
//            message[4] = jtf5.getText();
//            if (!new Check().check1(message[2]))   //调用Check的check方法检测用户是否存在， 如果不存在执行
//            {
//               // new usemysql().updateaccount(message[0],message[1],message[2],message[3],message[4]);
//                JOptionPane.showMessageDialog(null,"注册成功！","提示消息",JOptionPane.WARNING_MESSAGE);
//                dispose();  //使窗口消失
//            }
//            else
//            {
//                JOptionPane.showMessageDialog(null,"账号已存在，请重新输入！","提示消息",JOptionPane.WARNING_MESSAGE);
//                //dispose();
//            }
            System.out.println("注册成功");
        }
    }

    //清空账号和密码框
    private void clear() {
        jtf1.setText("");    //设置为空
        jtf2.setText("");
        jtf3.setText("");
        jtf4.setText("");
        jtf5.setText("");

    }


}