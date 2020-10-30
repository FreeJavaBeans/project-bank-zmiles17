package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

    private static ConnectionUtil singleton = new ConnectionUtil();

    private Connection connection;

    private ConnectionUtil() {
        super();
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");
        String url = System.getenv("DB_URL");
        try {
            DriverManager.getConnection(url, username, password);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static ConnectionUtil getSingleton() {
        return singleton;
    }

    public Connection getConnection() {
        return connection;
    }
}
