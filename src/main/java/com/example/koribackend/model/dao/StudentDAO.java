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

        String sql = "SELECT enrollment, email, issueDate, password, name, serie FROM students";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Student stud = new Student();
                stud.setEnrollment(rs.getInt("enrollment"));
                stud.setEmail(rs.getString("email"));
                stud.setIssueDate(rs.getDate("issueDate"));
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

    // Delete student by enrollment
    public boolean deleteStudent(int enrollment) {

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
}
