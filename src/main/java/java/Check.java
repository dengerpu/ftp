package main.java.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Check {
    public Object check;
    //在注册时 验证账号是否存在
    String url="jdbc:mysql://localhost:3306/bank?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8"; //通过DriverManagr获取数据连接
    String username="root";
    String password="123";
    public boolean  check1(String countname) throws IOException, SQLException{  //判断账户是否存在
        Connection conn=null;
        java.sql.Statement stmt=null;
        ResultSet rs=null;
        String name1[] = new String[1000];
        int j=0;
        try{
            //Class.forName("com.mysql.jdbc.Driver");  //加载数据库驱动
            conn=DriverManager.getConnection(url,username,password);
            stmt=conn.createStatement();//通过Connection对象获取Statement对象
            String sql="select * from account"; //使用Statement语句执行SQL语句
            rs=((java.sql.Statement) stmt).executeQuery(sql);
            while(rs.next()) {
                name1[j]+=rs.getString("bank_account");
                name1[j]=name1[j].substring(4,name1[j].length());
                j++;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(rs!=null)rs.close();
            if(stmt!=null)stmt.close();
            if(conn!=null)conn.close();
        }
        for(int i=0;i<j;i++) {
            if(countname.equals(name1[i]))
                return true;
        }
        return false;
    }
    //在挂失是 找回密码需要验证， 姓名，身份证号，和账户是否与注册时保持一致
    public String  check2(String name, String userid,String countname) throws IOException, SQLException{
        Connection conn=null;
        java.sql.Statement stmt=null;
        ResultSet rs=null;
        String a[]=new String[1000];
        int j=0;
        try{
            //Class.forName("com.mysql.jdbc.Driver");  //加载数据库驱动
            conn=DriverManager.getConnection(url,username,password);
            stmt=conn.createStatement();//通过Connection对象获取Statement对象
            String sql="select * from account"; //使用Statement语句执行SQL语句
            rs=((java.sql.Statement) stmt).executeQuery(sql);
            while(rs.next()) {
                a[j]=rs.getString("name")+rs.getString("name_id")+rs.getString("bank_account")+rs.getString("bank_password");
                j++;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(rs!=null)rs.close();
            if(stmt!=null)stmt.close();
            if(conn!=null)conn.close();
        }
        for(int i=0;i<j;i++)
            if ((a[i].substring(0, a[i].length()-6)).equals(name+userid+countname))
            {
                return a[i].substring(a[i].length()-6,a[i].length());
            }
        return null;
    }
    public boolean  check3(String countname) throws IOException, SQLException{  //判断账户是否被锁定
        Connection conn=null;
        java.sql.Statement stmt=null;
        ResultSet rs=null;
        String a=null;
        try{
            conn=DriverManager.getConnection(url,username,password);
            stmt=conn.createStatement();//通过Connection对象获取Statement对象
            String sql="select * from account where bank_account=?"; //使用Statement语句执行SQL语句
            //创建PreparedStatement对象
            PreparedStatement prestmt = conn.prepareStatement(sql);
            prestmt.setString(1,countname);
            //如果查询的结果集中有超过一条的记录，则登陆成功
            rs=prestmt.executeQuery();
            while(rs.next()) {
                a+=rs.getString("sd");
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(rs!=null)rs.close();
            if(stmt!=null)stmt.close();
            if(conn!=null)conn.close();
        }
        String sd="0";
        if(a.substring(4,5).equals(sd))
            return true;
        else  return false;
    }

    //验证用户名和密码是否为中文
    public boolean checkcountname(String countname)
    {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(countname);
        if (m.find()) {
            return true;
        }
        return false;
    }
    //验证姓名是否为中文
    public boolean checkname(String name)
    {
        int n = 0;
        for(int i = 0; i < name.length(); i++) {
            n = (int)name.charAt(i);
            if(!(19968 <= n && n <40869)) {
                return false;
            }
        }
        return true;
    }
    //验证邮箱是否合法
    public boolean isemail(String email) {
        //正则表达式
         String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
         return email.matches(regex);
    }

}
