package com.example.koribackend.model.dao;

import com.example.koribackend.model.entity.Admnistrator;
import com.example.koribackend.model.entity.Student;
import com.example.koribackend.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class studentDAO {

    // Get all administrators
    public List<Student> selectStudentAll() {

        List<Student> students = new ArrayList<>();

        // SQL query
        String sql = "SELECT enrollment, email, issueDate, password, name, serie FROM student";

        // Execute query
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            // Map results
            while (rs.next()) {
                Student stud = new Student();
                stud.setEnrollment(rs.getInt("id"));
                stud.setEmail(rs.getString("email"));
                stud.setIssueDate(rs.getDate("issueDate"));
                stud.setPassword(rs.getString("password"));
                stud.setName(rs.getString("name"));
                stud.setSerie(rs.getInt("serie"));

                students.add(stud);
            }
        } catch (SQLException e) {
            // Handle error
            e.printStackTrace();
        }

        return students;
    }

}
