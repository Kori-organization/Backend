package com.example.koribackend.model.dao;

import com.example.koribackend.model.entity.Administrator;
import com.example.koribackend.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministratorDAO {

    // Get all administrators
    public List<Administrator> selectAdministratorAll() {

        List<Administrator> admins = new ArrayList<>();
        String sql = "SELECT id, username, password_hash FROM administrators";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
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

    // Delete by ID
    public boolean deleteAdministrator(int id) {

        String sql = "DELETE FROM administrators WHERE id = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Login
    public boolean loginValid(String username, String passwordHash) {

        String sql = "SELECT 1 FROM administrators WHERE username = ? AND password_hash = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Administrator selectAdministratorForUsername(String username) {

        Administrator admin = null;
        String sql = "SELECT id, username, password_hash FROM administrators WHERE username LIKE ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1,username);
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
