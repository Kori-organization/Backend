package com.example.koribackend.model.dao;

import com.example.koribackend.model.entity.Admnistrator;
import com.example.koribackend.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdmnistratorDAO {

    // Gets all administrators
    public List<Admnistrator> selectAdministratorAll() {

        List<Admnistrator> administrators = new ArrayList<>();

        // SQL query
        String sql = "SELECT id, username, password_hash FROM administrators";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Admnistrator admin = new Admnistrator();
                admin.setId(rs.getInt("id"));
                admin.setName(rs.getString("username"));
                admin.setPassword(rs.getString("password_hash"));

                administrators.add(admin);
            }
        } catch (SQLException e) {
            // SQL error
            e.printStackTrace();
        }

        return administrators;
    }

    // Deletes by ID
    public boolean deleteById(int id) {

        // SQL query
        String sql = "DELETE FROM administrators WHERE id = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            // SQL error
            e.printStackTrace();
        }

        return false;
    }
}
