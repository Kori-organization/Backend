package com.example.koribackend.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionFactory {
    private static final Dotenv dotenv = Dotenv.load();

    private static final String url = dotenv.get("DB_URL");
    private static final String user = dotenv.get("DB_USER");
    private static final String password = dotenv.get("DB_PASSWORD");

    private ConnectionFactory() {
        // Prevent its instantiation.
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
