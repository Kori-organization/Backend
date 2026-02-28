package com.example.koribackend.config;

// Import HikariCP classes for high-performance connection pooling
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
// Import Dotenv to manage environment variables from a .env file
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.SQLException;

// Use a final class to prevent inheritance for this utility factory
public final class ConnectionFactory {

    // Define a static data source to be shared across the application
    private static HikariDataSource dataSource;
    // Load environment variables once during class initialization
    private static final Dotenv dotenv = Dotenv.load();
    // Retrieve database credentials and URL from the .env configuration
    private static final String url = dotenv.get("DB_URL");
    private static final String user = dotenv.get("DB_USER");
    private static final String password = dotenv.get("DB_PASSWORD");

    // Static block to initialize the connection pool when the class is loaded
    static {
        try {
            // Manually load the PostgreSQL driver class
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Configure HikariCP connection pool settings
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);

        // Set pool size and behavior constraints
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        // Define a simple query to validate if the connection is still alive
        config.setConnectionTestQuery("SELECT 1");
        // Set timeout limits for obtaining and maintaining connections
        config.setConnectionTimeout(20000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1740000);
        // Ensure transactions are committed automatically by default
        config.setAutoCommit(true);
        // Assign a custom name to the pool for debugging and logging
        config.setPoolName("KoriPool");

        // Instantiate the data source with the defined configuration
        dataSource = new HikariDataSource(config);
    }

    // Private constructor to enforce the utility class pattern and prevent instantiation
    private ConnectionFactory() {
        // Prevent its instantiation.
    }

    // Provide a public static method to borrow a connection from the pool
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}