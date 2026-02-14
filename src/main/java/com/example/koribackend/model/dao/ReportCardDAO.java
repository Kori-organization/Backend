package com.example.koribackend.model.dao;

import com.example.koribackend.model.entity.Grade;
import com.example.koribackend.model.entity.ReportCard;
import com.example.koribackend.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReportCardDAO {

    public ReportCard selectReportCard(int idStudent) {
        ReportCard reportCard = null;

        // SQL query
        String sql1 = "SELECT sj.id, sj.name, g.grade1, g.grade2, g.rec FROM subjects sj LEFT JOIN ( SELECT g.* FROM grades g JOIN grade_rep gr ON gr.grade_id = g.id JOIN report_card rc ON rc.id = gr.rep_id WHERE rc.student_id = ?) g ON g.subject_id = sj.id ORDER BY sj.id";
        String sql2 = "SELECT rc.id, s.name, rc.final_situation, s.serie FROM students s JOIN report_card rc ON s.enrollment = rc.student_id WHERE enrollment = ?";

        // Execute query
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt1 = conn.prepareStatement(sql1);
                PreparedStatement stmt2 = conn.prepareStatement(sql2)
        ) {
            stmt1.setInt(1,idStudent);
            stmt2.setInt(1,idStudent);
            ResultSet rs1 = stmt1.executeQuery();
            ResultSet rs2 = stmt2.executeQuery();
            ArrayList<Grade> grades = new ArrayList<>();
            while (rs1.next()) {
                Grade grade = new Grade();
                grade.setId(rs1.getInt(1));
                grade.setSubject(rs1.getString(2));
                grade.setGrade1(rs1.getDouble(3));
                if (rs1.wasNull()) { grade.setGrade1(-1); }
                grade.setGrade2(rs1.getDouble(4));
                if (rs1.wasNull()) { grade.setGrade2(-1); }
                grade.setRec(rs1.getDouble(5));
                if (rs1.wasNull()) { grade.setRec(-1); }
                grades.add(grade);
            }
            if (rs2.next()) {
                reportCard = new ReportCard(rs2.getInt(1),grades,rs2.getString(2),rs2.getString(3),rs2.getInt(4));
            }
        } catch (SQLException e) {
            // Handle error
            e.printStackTrace();
        }
        return reportCard;
    }

}