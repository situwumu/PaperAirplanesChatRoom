package component;

import jdk.internal.dynalink.beans.StaticClass;
import util.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 登录验证
 */
public class UsersDao {
    public static Users Login(Connection connection,Users user) throws Exception{
        Users resultUser = null;
        String sql = "SELECT * FROM users WHERE name = ? AND password = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1,user.getUsername());
        pstmt.setString(2,user.getPassword());

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()){
            resultUser = new Users();
            resultUser.setUsername(rs.getString("name"));
            resultUser.setPassword(rs.getString("password"));
        }
        pstmt.close();
        return  resultUser;
    }
    //注册页面
   public static void Regist(Connection connection,Users user) throws Exception {

       String sql = "INSERT INTO users VALUES(NULL,?,?,?,?,?)";

       PreparedStatement pstmt = connection.prepareStatement(sql);
       pstmt.setString(1,user.getUsername());
       pstmt.setString(2,user.getPassword());
       pstmt.setString(3,user.getTelephone());
       pstmt.setString(4,user.getGender());
       pstmt.setInt(5,user.getStatus());

       pstmt.execute();
       pstmt.close();
   }
    //修改密码
    public static void ChangePassword(Connection connection,String username,String password) throws Exception {
        String sql = "UPDATE users SET password = ? WHERE name = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1,password);
        pstmt.setString(2,username);

        pstmt.execute();
        pstmt.close();
    }
   //通过用户名查找用户是否存在
   public static boolean ContainsUser(Connection connection,String username)throws Exception{
       boolean flag = false;
       String sql = "SELECT * FROM users WHERE name = ? ";
       PreparedStatement pstmt = connection.prepareStatement(sql);
       pstmt.setString(1,username);

       ResultSet rs = pstmt.executeQuery();
       if (rs.next()){
          flag = true;
       }
       pstmt.close();
       return  flag;
   }
    //通过用户名及密码查找用户是否存在
    public static boolean ContainsByUserAndPassword(Connection connection,String username,String password)throws Exception{
        boolean flag = false;
        String sql = "SELECT * FROM users WHERE name = ? AND password = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1,username);
        pstmt.setString(2,password);

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()){
            flag = true;
        }
        pstmt.close();
        return  flag;
    }
    //通过用户名更改状态
    public static void setSatus(Connection connection,String username ,int status)throws Exception{

        String sql = "UPDATE users SET status = ? WHERE name = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1,status);
        pstmt.setString(2,username);
        pstmt.execute();
        pstmt.close();

    }


//       // Sql 查询测试
//    public static void main(String[] args) throws Exception {
//        Connection connection = DatabaseUtils.getConnection();
//        Users user = new Users("梅花十三","13","13","女",0);
//        UsersDao.ChangePassword(connection,"梅花十三","567");
//
//
//    }



}


