package com.example.koribackend.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionFactory {

    private static HikariDataSource dataSource;
    private static final Dotenv dotenv = Dotenv.load();
    private static final String url = dotenv.get("DB_URL");
    private static final String user = dotenv.get("DB_USER");
    private static final String password = dotenv.get("DB_PASSWORD");

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setConnectionTestQuery("SELECT 1");
        config.setConnectionTimeout(20000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1740000);
        config.setAutoCommit(true);
        config.setPoolName("KoriPool");

        dataSource = new HikariDataSource(config);
    }

    private ConnectionFactory() {
        // Prevent its instantiation.
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
