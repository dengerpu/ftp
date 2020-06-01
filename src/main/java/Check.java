package main.java;

import main.java.util.JDBCUtils;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Check {
    public Object check;
    //在注册时 验证账号是否存在

    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    public boolean  check1(String countname){  //判断账户是否存在
        try{
            String sql = "select * from ftp where user = ?";
            Map<String, Object> map = template.queryForMap(sql, countname);
            if(map!=null){
                return true;
            }
        }catch(Exception e) {
            return false;
        }
        return false;
    }
   public boolean check2(String user,String pwd){   //判断用户名密码是否正确
        String sql = "select * from  ftp where user = ? and pwd = ?";
       try{
           Map<String, Object> map = template.queryForMap(sql, user, pwd);

       }catch (Exception e){
           return false;
       }
       return true;

   }
   public boolean register(String user,String pwd,String email){
        String sql = "insert into ftp values(?,?,?,?)";
        try {
            int update = template.update(sql, null, user, pwd, email);
        }catch (Exception e){
            return false;
        }
       return true;
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
    @Test
    public static void main(String[] args) {
        //正则表达式
        String email = "123456qq.com";
        String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        System.out.println(email.matches(regex));
    }

}
