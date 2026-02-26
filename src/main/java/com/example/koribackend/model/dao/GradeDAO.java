package com.example.koribackend.model.dao;

import com.example.koribackend.config.ConnectionFactory;

import java.sql.*;

public class GradeDAO {

    public boolean updateGrades(double n1, double n2, int enrollment, String subject) {
        String sql1 = "UPDATE grades g SET grade1 = ?, grade2 = ? WHERE g.id = (SELECT g.id FROM students sd JOIN report_card rc ON rc.student_id = sd.enrollment JOIN grade_rep gr ON gr.rep_id = rc.id JOIN grades g ON gr.grade_id = g.id JOIN subjects s ON g.subject_id = s.id WHERE g.subject_id = (SELECT s.id FROM subjects s WHERE s.name ILIKE ?) AND sd.enrollment = ?)";
        String sql2 = "INSERT INTO grades(grade1, grade2, rec, subject_id) values(?,?,null, (SELECT s.id FROM subjects s WHERE s.name ILIKE ? ) )";
        String sql3 = "INSERT INTO grade_rep(rep_id,grade_id) VALUES( (SELECT rc.id FROM report_card rc JOIN students s ON rc.student_id = s.enrollment WHERE s.enrollment = ?) , ?)";

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(sql1)) {
                if (n1 != -1) { stmt.setDouble(1,n1); } else { stmt.setNull(1, Types.DOUBLE); }
                if (n2 != -1) { stmt.setDouble(2,n2); } else { stmt.setNull(2, Types.DOUBLE); }
                stmt.setString(3,subject);
                stmt.setInt(4,enrollment);
                if (stmt.executeUpdate() < 1) {
                    int createId = -1;
                    try (PreparedStatement stmt2 = conn.prepareStatement(sql2,PreparedStatement.RETURN_GENERATED_KEYS)) {
                        if (n1 != -1) { stmt2.setDouble(1,n1); } else { stmt2.setNull(1, Types.DOUBLE); }
                        if (n2 != -1) { stmt2.setDouble(2,n2); } else { stmt2.setNull(2, Types.DOUBLE); }
                        stmt2.setString(3,subject);
                        stmt2.execute();
                        try (ResultSet rs = stmt2.getGeneratedKeys()) {
                            if (rs.next()) {
                                createId = rs.getInt(1);
                            }
                        }
                    }
                    if (createId == -1) { return false; }
                    try (PreparedStatement stmt3 = conn.prepareStatement(sql3)) {
                        stmt3.setInt(1,enrollment);
                        stmt3.setInt(2,createId);
                        if (stmt3.executeUpdate() < 1) {
                            return false;
                        }
                    }
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
            new ReportCardDAO().updateSituation(enrollment);
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

    public boolean updateRec(double rec, int enrollment, String subject) {
        String sql1 = "UPDATE grades g SET rec = ? WHERE g.id = (SELECT g.id FROM students sd JOIN report_card rc ON rc.student_id = sd.enrollment JOIN grade_rep gr ON gr.rep_id = rc.id JOIN grades g ON gr.grade_id = g.id JOIN subjects s ON g.subject_id = s.id WHERE g.subject_id = (SELECT s.id FROM subjects s WHERE s.name ILIKE ?) AND sd.enrollment = ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql1)
        ) {
            if (rec != -1) { stmt.setDouble(1, rec); } else { stmt.setNull(1, Types.DOUBLE); }
            stmt.setString(2, subject);
            stmt.setInt(3, enrollment);
            if (stmt.executeUpdate() >= 1) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            new ReportCardDAO().updateSituation(enrollment);
        }
        return false;
    }


}
