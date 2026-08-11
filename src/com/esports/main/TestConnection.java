package com.esports.main;

import java.sql.Connection;
import com.esports.util.DatabaseConnection;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("Testing database connection... (测试数据库连接...)");
        Connection conn = DatabaseConnection.getConnection();
        
        if (conn != null) {
            System.out.println("Connection successful! (连接成功！)");
            // Now closeConnection needs Connection parameter (现在closeConnection需要Connection参数)
            DatabaseConnection.closeConnection(conn);
        } else {
            System.out.println("Connection failed! (连接失败！)");
        }
    }
}