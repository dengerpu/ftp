package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class Register extends JFrame implements ActionListener {

    JButton jb1, jb2;  //按钮
    JLabel jlb1, jlb2, jlb3,jlb4, jlb6;  //标签
    JTextField jtf1,jtf4;   //文本框
    JPasswordField jpw1,jpw2;  //密码框
    JPasswordField jpf; //密码框
    JPanel jp1,jp2,jp3, jp4,jp6,jp7;		//面板

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


        ImageIcon bz=new ImageIcon();
        jlb6 = new JLabel("注册信息");

        jlb6.setFont(new   java.awt.Font("Dialog",   1,   20));   //设置字体类型，加粗，大小为20
        //文本信息
        jtf1 = new JTextField(13);
        jpw1 = new JPasswordField(13);
        jpw2 = new JPasswordField(13);
        jtf4 = new JTextField(13);

        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();
        jp4 = new JPanel();
        jp6 = new JPanel();
        jp7 = new JPanel();
        //将对应信息加入面板中
        jp1.add(jlb1);
        jp1.add(jtf1);

        jp2.add(jlb2);
        jp2.add(jpw1);

        jp3.add(jlb3);
        jp3.add(jpw2);

        jp4.add(jlb4);
        jp4.add(jtf4);

        jp6.add(jb1);
        jp6.add(jb2);

        jp7.add(jlb6);

        //将JPane加入JFrame中
        this.add(jp7);  //先加入提示语

        this.add(jp1);
        this.add(jp2);
        this.add(jp3);
        this.add(jp4);
        this.add(jp6);

        //设置布局
        this.setTitle("注册信息");
        this.setLayout(new GridLayout(7, 1));
        this.setSize(350, 350);   //设置窗体大小
        this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);  //设置仅关闭当前窗口
        this.setIconImage((new ImageIcon("src//img//ftp.png").getImage()));   //修改窗体图标
        this.setVisible(true);  //设置可见
        this.setResizable(false);	//设置不可拉伸大小

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getActionCommand()=="确定") {
            try {
                register();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        else if (e.getActionCommand()=="重置")
        {
            clear();
        }

    }
    //验证注册信息，并做处理
    public void register() throws IOException, SQLException
    {
        char[] password = jpw1.getPassword();
        String pw1 = String.valueOf(password);
        char[] password2 = jpw2.getPassword();
        String pw2 = String.valueOf(password2);
        //判断信息是否补全
        if (jtf1.getText().isEmpty()||pw1.isEmpty()||
                pw2.isEmpty()||jtf4.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "信息有空缺，请补全！","消息提示",JOptionPane.WARNING_MESSAGE);
        }
        else if(!pw1.equals(pw2)){
            JOptionPane.showMessageDialog(null, "两次输入密码不一致","消息提示",JOptionPane.WARNING_MESSAGE);
        }
        //判断账户名和密码是否包含中文
        else if (new Check().checkcountname(jtf1.getText())||new Check().checkcountname(pw1))
        {
            JOptionPane.showMessageDialog(null, "用户名或密码存在中文，不合法!","消息提示",JOptionPane.WARNING_MESSAGE);
        }
        //判断邮箱是否正确
        else if (!new Check().isemail(jtf4.getText()))
        {
            JOptionPane.showMessageDialog(null, "邮箱地址不合法，请重新输入！","消息提示",JOptionPane.WARNING_MESSAGE);
        }

        //满足要求
        else if (!jtf1.getText().isEmpty()&&!pw1.isEmpty()&&
                !pw2.isEmpty()&&!jtf4.getText().isEmpty())
        {
            boolean register = new Check().register(jtf1.getText(), pw1, jtf4.getText());
            if (register){
                JOptionPane.showMessageDialog(null, "注册成功！","消息提示",JOptionPane.WARNING_MESSAGE);
                dispose();   //使窗口消失
            }else{
                JOptionPane.showMessageDialog(null, "注册失败！","消息提示",JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    //清空账号和密码框
    private void clear() {
        jtf1.setText("");    //设置为空
        jpw1.setText("");
        jpw2.setText("");
        jtf4.setText("");
    }


}