package com.example.koribackend.model.dao;

import com.example.koribackend.model.entity.Grade;
import com.example.koribackend.model.entity.ReportCard;
import com.example.koribackend.config.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Data Access Object for Report Cards.
 * Manages the retrieval of student grades and the logic for calculating
 * the final academic situation.
 */
public class ReportCardDAO {

    /**
     * Retrieves a complete ReportCard for a specific student.
     * Combines student details with a list of all subjects and their respective grades.
     */
    public ReportCard selectReportCard(int idStudent) {
        ReportCard reportCard = null;

        // SQL1: Fetches all subjects and joins with grades specifically for this student
        String sql1 = "SELECT sj.id, sj.name, g.grade1, g.grade2, g.rec FROM subjects sj " +
                "LEFT JOIN (SELECT g.* FROM grades g JOIN grade_rep gr ON gr.grade_id = g.id " +
                "JOIN report_card rc ON rc.id = gr.rep_id WHERE rc.student_id = ?) g " +
                "ON g.subject_id = sj.id ORDER BY sj.id";

        // SQL2: Fetches basic report card info and student identity
        String sql2 = "SELECT rc.id, s.name, rc.final_situation, s.serie FROM students s " +
                "JOIN report_card rc ON s.enrollment = rc.student_id WHERE enrollment = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt1 = conn.prepareStatement(sql1);
                PreparedStatement stmt2 = conn.prepareStatement(sql2)
        ) {
            stmt1.setInt(1, idStudent);
            stmt2.setInt(1, idStudent);

            ResultSet rs1 = stmt1.executeQuery();
            ResultSet rs2 = stmt2.executeQuery();

            ArrayList<Grade> grades = new ArrayList<>();

            // Map result set to Grade entities
            while (rs1.next()) {
                Grade grade = new Grade();
                grade.setId(rs1.getInt(1));
                grade.setSubject(rs1.getString(2));

                // Handle null grades by assigning -1 to represent "Not Graded"
                grade.setGrade1(rs1.getDouble(3));
                if (rs1.wasNull()) { grade.setGrade1(-1); }

                grade.setGrade2(rs1.getDouble(4));
                if (rs1.wasNull()) { grade.setGrade2(-1); }

                grade.setRec(rs1.getDouble(5));
                if (rs1.wasNull()) { grade.setRec(-1); }

                grades.add(grade);
            }

            // If the report card exists, assemble the final object
            if (rs2.next()) {
                reportCard = new ReportCard(rs2.getInt(1), grades, rs2.getString(2), rs2.getString(3), rs2.getInt(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportCard;
    }

    /**
     * Calculates and updates the student's final situation (Aprovado, Reprovado, or Em andamento).
     * This method uses a complex SQL CASE statement to evaluate all subject averages at once.
     */
    public void updateSituation(int idStudent) {
        // SQL1 logic:
        // 1. If any subject is missing N1 or N2 -> 'Em andamento'
        // 2. If average < 7 and no recovery -> 'Recuperação'
        // 3. If recovery exists, check new average -> 'Aprovado' or 'Reprovado'
        // 4. Finally, aggregate all results: if one subject is 'Em andamento', the whole card is 'Em andamento'
        String sql1 = "UPDATE report_card SET final_situation = (SELECT CASE WHEN COUNT(*) FILTER (WHERE situacao = 'Em andamento') > 0 THEN 'Em andamento' WHEN COUNT(*) FILTER (WHERE situacao = 'Recuperação') > 0 THEN 'Em andamento' " +
        "WHEN COUNT(*) FILTER (WHERE situacao = 'Reprovado') > 0 THEN 'Reprovado' WHEN COUNT(*) FILTER (WHERE situacao = 'Aprovado por recuperação') > 0 THEN 'Aprovado por recuperação' ELSE 'Aprovado' END FROM (SELECT CASE WHEN g.grade1 IS NULL OR g.grade2 IS NULL THEN 'Em andamento' WHEN (g.grade1 + g.grade2) / 2 >= 7 THEN 'Aprovado' " +
        "WHEN g.rec IS NOT NULL THEN CASE WHEN ((g.grade1 + g.grade2) / 2 + g.rec) / 2 >= 7 THEN 'Aprovado por recuperação' ELSE 'Reprovado' END ELSE 'Recuperação' END AS situacao FROM subjects sub LEFT JOIN LATERAL (SELECT g.grade1, g.grade2, g.rec " +
        "FROM report_card rc JOIN grade_rep gr ON gr.rep_id = rc.id JOIN grades g ON g.id = gr.grade_id WHERE rc.student_id = ? AND g.subject_id = sub.id ORDER BY g.id DESC LIMIT 1) g ON TRUE) t) WHERE student_id = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql1)
        ) {
            stmt.setInt(1,idStudent);
            stmt.setInt(2,idStudent);
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}