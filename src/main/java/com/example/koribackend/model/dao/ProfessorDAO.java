package com.example.koribackend.model.dao;

import com.example.koribackend.model.entity.Professor;
import com.example.koribackend.config.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProfessorDAO {

    // Gets all professors
    public ArrayList<Professor> selectProfessorAll() {

        ArrayList<Professor> professors = new ArrayList<>();

        // SQL query
        String sql = "SELECT p.id, p.username, p.password_hash, s.name, p.name  FROM professors p join subjects s on s.id = p.subject_id";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Professor prof = new Professor();
                prof.setId(rs.getInt(1));
                prof.setUsername(rs.getString(2));
                prof.setPassword(rs.getString(3));
                prof.setSubjectName(rs.getString(4));
                prof.setName(rs.getString(5));
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
        String sql1 = "SELECT subject_id FROM professors WHERE id = ?";
        String sql2 = "DELETE FROM professors WHERE id = ?";
        String sql3 = "DELETE FROM subjects WHERE id = ?";

        Connection conn = null;

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            int subjectId = -1;
            try (PreparedStatement psGet = conn.prepareStatement(sql1)) {
                psGet.setInt(1, id);
                try (ResultSet rs = psGet.executeQuery()) {
                    if (rs.next()) {
                        subjectId = rs.getInt("subject_id");
                    }
                }
            }
            if (subjectId == -1) { return false; }

            try (PreparedStatement psDeleteProfessor = conn.prepareStatement(sql2)) {
                psDeleteProfessor.setInt(1, id);
                psDeleteProfessor.executeUpdate();
            }

            try (PreparedStatement psDeleteSubject = conn.prepareStatement(sql3)) {
                psDeleteSubject.setInt(1, subjectId);
                psDeleteSubject.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {

            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception ignored) {}
        }
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

    public boolean updateProfessor(Professor professor) {
        String sql1 = "INSERT INTO subjects(name) VALUES(?)";
        String sql2 = "SELECT subject_id FROM professors WHERE id = ?";
        String sql3 = "UPDATE professors SET username = ?, password_hash = ?, subject_id = ?, name = ? WHERE id = ?";
        String sql4 = "DELETE FROM subjects WHERE id = ?";
        String sql5 = "UPDATE professors SET username = ?, password_hash = ?, name = ? WHERE id = ?";


        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            if (!professor.getSubjectName().isEmpty()) {
                int idSubjectEdit = -1;
                try (PreparedStatement stmt1 = conn.prepareStatement(sql1,PreparedStatement.RETURN_GENERATED_KEYS)) {
                    stmt1.setString(1, professor.getSubjectName());
                    stmt1.execute();
                    try (ResultSet keys = stmt1.getGeneratedKeys()) {
                        if (keys.next()) {
                            idSubjectEdit = keys.getInt(1);
                        }
                    }
                }
                if (idSubjectEdit == -1) { return false; }

                int idSubjectDelete = -1;
                try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
                    stmt2.setInt(1, professor.getId());
                    try (ResultSet keys = stmt2.executeQuery()) {
                        if (keys.next()) {
                            idSubjectDelete = keys.getInt(1);
                        }
                    }
                }
                if (idSubjectDelete == -1) { return false; }

                try (PreparedStatement stmt3 = conn.prepareStatement(sql3)) {
                    stmt3.setString(1,professor.getUsername());
                    stmt3.setString(2,professor.getPassword());
                    stmt3.setInt(3,idSubjectEdit);
                    stmt3.setString(4,professor.getName());
                    stmt3.setInt(5,professor.getId());
                    stmt3.execute();
                }

                try (PreparedStatement stmt4 = conn.prepareStatement(sql4)) {
                    stmt4.setInt(1,idSubjectDelete);
                    stmt4.execute();
                }
            } else {
                try (PreparedStatement stmt5 = conn.prepareStatement(sql5)) {
                    stmt5.setString(1,professor.getUsername());
                    stmt5.setString(2,professor.getPassword());
                    stmt5.setString(3,professor.getName());
                    stmt5.setInt(4,professor.getId());
                    stmt5.execute();
                }
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



