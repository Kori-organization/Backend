package com.example.koribackend.model.dao;

import com.example.koribackend.model.entity.Professor;
import com.example.koribackend.model.entity.Student;
import com.example.koribackend.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO {

    // Gets all professors
    public List<Professor> selectProfAll() {

        List<Professor> professors = new ArrayList<>();

        // SQL query
        String sql = "SELECT id, username, password_hash FROM professors";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Professor prof = new Professor();
                prof.setId(rs.getInt("id"));
                prof.setUsername(rs.getString("username"));
                prof.setPassword(rs.getString("password_hash"));
                professors.add(prof);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return professors;
    }

    // Gets professor by subject
    public Professor selectProfessorBySubject(String subject) {

        // SQL query
        String sql = "SELECT p.id, p.username, p.password_hash FROM professors p " +
                "JOIN subjects s ON s.id = p.subject_id WHERE s.name ILIKE ?";
        Professor professor = null;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, "%" + subject + "%");
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                professor = new Professor();
                professor.setId(rs.getInt("id"));
                professor.setUsername(rs.getString("username"));
                professor.setPassword(rs.getString("password_hash"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return professor;
    }

    // Gets professor by user
    public Professor selectProfessorByUser(String username) {
        Professor professor = null;

        // SQL query
        String sql = "SELECT p.id, p.username, p.password_hash, p.name, s.name AS subject_name FROM professors p JOIN subjects s ON p.subject_id = s.id WHERE p.username = ?";
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                professor = new Professor();
                professor.setId(rs.getInt("id"));
                professor.setUsername(rs.getString("username"));
                professor.setPassword(rs.getString("password_hash"));
                professor.setName(rs.getString("name"));
                professor.setSubjectName(rs.getString("subject_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return professor;
    }

    // Deletes by ID
    public boolean deleteById(int id) {

        // SQL query
        String sql = "DELETE FROM professors WHERE id = ?";

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
    public boolean loginValid(String user, String password) {

        // SQL query
        String sql = "SELECT id FROM professors WHERE username = ? AND password_hash = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, user);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean createAccount(Professor professor) {
        String sql1 = "INSERT INTO subjects(name) VALUES(?)";
        String sql2 = "INSERT INTO professors(username, password_hash, subject_id, name) VALUES (?,?,?,?)";

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            int id = -1;
            try (PreparedStatement stmt1 = conn.prepareStatement(sql1,PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt1.setString(1,professor.getSubjectName());
                stmt1.execute();
                try (ResultSet keys = stmt1.getGeneratedKeys()) {
                    if (keys.next()) {
                        id = keys.getInt(1);
                    }
                }
            }
            if (id == -1) { return false; }
            try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
                stmt2.setString(1,professor.getUsername());
                stmt2.setString(2,professor.getPassword());
                stmt2.setInt(3,id);
                stmt2.setString(4,professor.getName());
                stmt2.execute();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}



