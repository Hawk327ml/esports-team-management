package com.esports.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = System.getenv().getOrDefault(
            "ESPORTS_DB_URL",
            "jdbc:mysql://localhost:3306/esports_manager");
    private static final String USER = System.getenv().getOrDefault("ESPORTS_DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("ESPORTS_DB_PASSWORD", "changeme");
    private static final boolean VERBOSE = "1".equals(System.getenv("ESPORTS_DB_VERBOSE"));

    // Prefer a fresh connection per call (avoid a long-lived static Connection).
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String urlWithParams = URL + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            if (VERBOSE) {
                System.out.println("Connecting: " + urlWithParams + " as " + USER);
            }
            return DriverManager.getConnection(urlWithParams, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL driver not found. Ensure mysql-connector-j is on the classpath (Maven dependency).");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            System.err.println("Check: MySQL running, ESPORTS_DB_PASSWORD, and that database esports_manager exists (sql/schema.sql).");
            e.printStackTrace();
            return null;
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
