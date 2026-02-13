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
        String sql = "INSERT INTO students(email, issue_Date, password, name, serie) VALUES(?,?,?,?,?)";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1,student.getEmail());
            stmt.setDate(2, student.getIssueDate());
            stmt.setString(3,student.getPassword());
            stmt.setString(4,student.getName());
            stmt.setInt(5,student.getSerie());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
