package util;

import java.sql.*;

/**
 * 数据库工具类
 * @author 张洋
 */
public class DatabaseUtils {
    private static String dbURL = "jdbc:mysql://localhost:3306/db1?serverTimezone= Asia/Shanghai";
    //用户名、密码
    private static String dbUsername = "root";
    private static String dbPassword = "019230";
    //驱动名称
    private static String jdbcName = "com.mysql.cj.jdbc.Driver";
    //获取数据库连接
    public static Connection getConnection()throws Exception{
        Class.forName(jdbcName);
        Connection connection = DriverManager.getConnection(dbURL,dbUsername,dbPassword);
        return connection;
    }
    //关闭数据库连接
    public static void closeConnection(Connection connection) throws Exception{
        if (connection != null){
            connection.close();
        }
    }

}

