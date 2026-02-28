package com.example.koribackend.model.dao;

import com.example.koribackend.model.entity.Administrator;
import com.example.koribackend.config.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Administrator entities.
 * Manages database operations including retrieval, deletion, and authentication.
 */
public class AdministratorDAO {

    // Retrieve a complete list of all administrators from the database
    public List<Administrator> selectAdministratorAll() {

        List<Administrator> admins = new ArrayList<>();
        String sql = "SELECT id, username, password_hash FROM administrators";

        // Use try-with-resources to ensure the connection and statement are closed automatically
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            // Iterate through the results and map each row to an Administrator object
            while (rs.next()) {
                Administrator admin = new Administrator();
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password_hash"));
                admins.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admins;
    }

    // Remove an administrator record based on its unique primary key
    public boolean deleteAdministrator(int id) {

        String sql = "DELETE FROM administrators WHERE id = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            // Return true if at least one row was affected by the delete operation
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Validate administrator credentials during the login process
    public boolean loginValid(String username, String passwordHash) {

        // Use SELECT 1 as an efficient way to check for existence without fetching data
        String sql = "SELECT 1 FROM administrators WHERE username = ? AND password_hash = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);

            ResultSet rs = stmt.executeQuery();
            // If rs.next() is true, a matching user was found
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Fetch a single administrator profile using their username
    public Administrator selectAdministratorForUsername(String username) {

        Administrator admin = null;
        String sql = "SELECT id, username, password_hash FROM administrators WHERE username LIKE ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                admin = new Administrator();
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password_hash"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }
}