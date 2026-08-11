package com.esports.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/esports_manager";
    private static final String USER = "root";
    private static final String PASSWORD = System.getenv().getOrDefault("ESPORTS_DB_PASSWORD", "changeme"); // set ESPORTS_DB_PASSWORD
    
    // Important: Do not use static connection, get new connection each time (重要：不要使用静态connection，每次获取新连接)
    public static Connection getConnection() {
        try {
            System.out.println("🔄 Connecting to database... (正在连接数据库...)");
            System.out.println("📋 URL: " + URL);
            System.out.println("👤 User: " + USER);
            
            // MySQL 8.0+ and 9.0+ driver class name (MySQL 8.0+ 和 9.0+ 的驱动类名)
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL driver loaded successfully (MySQL驱动加载成功)");
            
            // Add timezone parameters to avoid timezone errors (very important!) (添加时区参数，避免时区错误（非常重要！）)
            String urlWithParams = URL + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            System.out.println("🔗 Connection URL (连接URL): " + urlWithParams);
            
            Connection connection = DriverManager.getConnection(urlWithParams, USER, PASSWORD);
            System.out.println("🎉 Database connection successful! (数据库连接成功！)");
            return connection;
            
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL driver not found! (MySQL驱动未找到！)");
            System.out.println("Please check if mysql-connector-j-9.5.0.jar is added to project library (请检查是否添加了mysql-connector-j-9.5.0.jar到项目库中)");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed! (数据库连接失败！)");
            System.out.println("Error message (错误信息): " + e.getMessage());
            System.out.println("Please check (请检查)：");
            System.out.println("1. Is MySQL service running? (MySQL服务是否正在运行？)");
            System.out.println("2. Is username and password correct? (用户名密码是否正确？)");
            System.out.println("3. Does database 'esports_manager' exist? (数据库'esports_manager'是否存在？)");
            e.printStackTrace();
            return null;
        }
    }
    
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("🔒 Database connection closed! (数据库连接已关闭！)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
