package com.example.koribackend.model.dao;

import com.example.koribackend.model.entity.Admnistrator;
import com.example.koribackend.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class admnistratorDAO {

    // Get all administrators
    public List<Admnistrator> selectAdministratorAll() {

        List<Admnistrator> administrators = new ArrayList<>();

        // SQL query
        String sql = "SELECT id, username, password_hash FROM administrators";

        // Execute query
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            // Map results
            while (rs.next()) {
                Admnistrator admin = new Admnistrator();
                admin.setId(rs.getInt("id"));
                admin.setName(rs.getString("username"));
                admin.setPassword(rs.getString("password_hash"));

                administrators.add(admin);
            }
        } catch (SQLException e) {
            // Handle error
            e.printStackTrace();
        }

        return administrators;
    }

    // Delete admin by ID
    public int deleteById(int id) {

        // SQL query
        String sql = "DELETE FROM administrators WHERE id = ?";

        // Execute deletion
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            // Handle error
            e.printStackTrace();
        }

        return id;
    }

}
