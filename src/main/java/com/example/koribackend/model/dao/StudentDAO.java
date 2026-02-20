package com.example.koribackend.model.dao;

import com.example.koribackend.model.entity.Student;
import com.example.koribackend.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // Get all students
    public List<Student> selectStudentAll() {

        List<Student> students = new ArrayList<>();

        // SQL query
        String sql = "SELECT enrollment, email, issue_date, password, name, serie FROM students";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Student stud = new Student();
                stud.setEnrollment(rs.getInt("enrollment"));
                stud.setEmail(rs.getString("email"));
                stud.setIssueDate(rs.getDate("issue_date"));
                stud.setPassword(rs.getString("password"));
                stud.setName(rs.getString("name"));
                stud.setSerie(rs.getInt("serie"));

                students.add(stud);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    public Student selectStudentForEmail(String email) {
        Student student = null;

        String sql = "SELECT enrollment, email, issue_Date, password, name, serie FROM students WHERE email LIKE ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);

        ) {
            stmt.setString(1,email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                student = new Student();
                student.setEnrollment(rs.getInt("enrollment"));
                student.setEmail(rs.getString("email"));
                student.setIssueDate(rs.getDate("issue_Date"));
                student.setPassword(rs.getString("password"));
                student.setName(rs.getString("name"));
                student.setSerie(rs.getInt("serie"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return student;
    }

    // Delete student by enrollment
    public boolean deleteStudent(int enrollment) {

        // SQL query
        String sql = "DELETE FROM students WHERE enrollment = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, enrollment);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Login
    public boolean loginValid(String email, String password) {
        // SQL query
        String sql = "SELECT enrollment FROM students WHERE email LIKE ? AND password LIKE ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            // SQL error
            e.printStackTrace();
            return false;
        }
    }

    public boolean newPasswordStudent(int enrollment, String newPassword) {
        String sql = "UPDATE students set password = ? WHERE enrollment = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1,newPassword);
            stmt.setInt(2, enrollment);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createAccount(Student student) {
        String sql1 = "INSERT INTO students(email, issue_Date, password, name, serie) VALUES(?,?,?,?,?)";
        String sql2 = "INSERT INTO report_card(final_situation,student_id) VALUES(?,?)";

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            int enrollment = -1;
            try (PreparedStatement stmt1 = conn.prepareStatement(sql1,PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt1.setString(1, student.getEmail());
                stmt1.setDate(2, student.getIssueDate());
                stmt1.setString(3, student.getPassword());
                stmt1.setString(4, student.getName());
                stmt1.setInt(5, student.getSerie());
                stmt1.execute();
                try (ResultSet keys = stmt1.getGeneratedKeys()) {
                    if (keys.next()) {
                        enrollment = keys.getInt(1);
                    }
                }
            }
            if (enrollment == -1) { return false; }
            try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
                stmt2.setString(1, "Em processo");
                stmt2.setInt(2, enrollment);
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

    public boolean accountExists(String email) {
        String sql = "SELECT enrollment FROM students WHERE email LIKE ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1,email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
