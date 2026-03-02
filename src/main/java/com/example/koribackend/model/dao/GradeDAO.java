package com.example.koribackend.model.dao;

import com.example.koribackend.config.ConnectionFactory;
import com.example.koribackend.model.entity.Grade;

import java.sql.*;
import java.util.ArrayList;

/**
 * Data Access Object for Grade management.
 * Handles complex logic for updating, inserting, and linking grades to report cards.
 */
public class GradeDAO {

    /**
     * Updates N1 and N2 grades. If the grade record doesn't exist for that subject,
     * it creates a new entry and links it to the student's report card.
     */
    public boolean updateGrades(double n1, double n2, int enrollment, String subject) {
        // SQL to update an existing grade by joining several tables to find the correct subject/student link
        String sql1 = "UPDATE grades g SET grade1 = ?, grade2 = ? WHERE g.id = (SELECT g.id FROM students sd JOIN report_card rc ON rc.student_id = sd.enrollment JOIN grade_rep gr ON gr.rep_id = rc.id JOIN grades g ON gr.grade_id = g.id JOIN subjects s ON g.subject_id = s.id WHERE g.subject_id = (SELECT s.id FROM subjects s WHERE s.name ILIKE ?) AND sd.enrollment = ?)";
        // SQL to insert a new grade entry
        String sql2 = "INSERT INTO grades(grade1, grade2, rec, subject_id) values(?,?,null, (SELECT s.id FROM subjects s WHERE s.name ILIKE ? ) )";
        // SQL to link the new grade entry to the report card association table
        String sql3 = "INSERT INTO grade_rep(rep_id,grade_id) VALUES( (SELECT rc.id FROM report_card rc JOIN students s ON rc.student_id = s.enrollment WHERE s.enrollment = ?) , ?)";

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            // Start transaction
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql1)) {
                // Set n1/n2 values or NULL if the input is -1
                if (n1 != -1) { stmt.setDouble(1,n1); } else { stmt.setNull(1, Types.DOUBLE); }
                if (n2 != -1) { stmt.setDouble(2,n2); } else { stmt.setNull(2, Types.DOUBLE); }
                stmt.setString(3,subject);
                stmt.setInt(4,enrollment);

                // If update fails (zero rows affected), the grade doesn't exist; perform insertion
                if (stmt.executeUpdate() < 1) {
                    int createId = -1;
                    try (PreparedStatement stmt2 = conn.prepareStatement(sql2,PreparedStatement.RETURN_GENERATED_KEYS)) {
                        if (n1 != -1) { stmt2.setDouble(1,n1); } else { stmt2.setNull(1, Types.DOUBLE); }
                        if (n2 != -1) { stmt2.setDouble(2,n2); } else { stmt2.setNull(2, Types.DOUBLE); }
                        stmt2.setString(3,subject);
                        stmt2.execute();

                        // Capture the auto-generated ID of the new grade
                        try (ResultSet rs = stmt2.getGeneratedKeys()) {
                            if (rs.next()) {
                                createId = rs.getInt(1);
                            }
                        }
                    }

                    if (createId == -1) { return false; }

                    // Link the newly created grade ID to the report card
                    try (PreparedStatement stmt3 = conn.prepareStatement(sql3)) {
                        stmt3.setInt(1,enrollment);
                        stmt3.setInt(2,createId);
                        if (stmt3.executeUpdate() < 1) {
                            return false;
                        }
                    }
                }
            }
            // Commit all changes if everything succeeded
            conn.commit();
            return true;
        } catch (SQLException e) {
            // Rollback changes on error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            // Trigger an update to the final situation (passed/failed) after a failure
            new ReportCardDAO().updateSituation(enrollment);
            e.printStackTrace();
        } finally {
            // Clean up connection
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            new ReportCardDAO().updateSituation(enrollment);
        }
        return false;
    }

    /**
     * Internal version of updateGrades that participates in an external transaction.
     */
    public boolean updateGrades(Connection conn, double n1, double n2, int enrollment, String subject) throws SQLException {
        String sql1 = "UPDATE grades g SET grade1 = ?, grade2 = ? WHERE g.id = (SELECT g.id FROM students sd JOIN report_card rc ON rc.student_id = sd.enrollment JOIN grade_rep gr ON gr.rep_id = rc.id JOIN grades g ON gr.grade_id = g.id JOIN subjects s ON g.subject_id = s.id WHERE g.subject_id = (SELECT s.id FROM subjects s WHERE s.name ILIKE ?) AND sd.enrollment = ?)";
        String sql2 = "INSERT INTO grades(grade1, grade2, rec, subject_id) values(?,?,null, (SELECT s.id FROM subjects s WHERE s.name ILIKE ? ) )";
        String sql3 = "INSERT INTO grade_rep(rep_id,grade_id) VALUES( (SELECT rc.id FROM report_card rc JOIN students s ON rc.student_id = s.enrollment WHERE s.enrollment = ?) , ?)";

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
                if (createId == -1) { throw new SQLException("Error"); }
                try (PreparedStatement stmt3 = conn.prepareStatement(sql3)) {
                    stmt3.setInt(1,enrollment);
                    stmt3.setInt(2,createId);
                    if (stmt3.executeUpdate() < 1) {
                        throw new SQLException("Error");
                    }
                }
            }
        } finally {
            new ReportCardDAO().updateSituation(enrollment);
        }
        return true;
    }

    /**
     * Updates only the recovery (rec) grade for a student.
     */
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
            // Recalculate student situation after update
            new ReportCardDAO().updateSituation(enrollment);
        }
        return false;
    }

    /**
     * Internal version of updateRec that participates in an external transaction.
     */
    public boolean updateRec(Connection conn, double rec, int enrollment, String subject) throws SQLException {
        String sql1 = "UPDATE grades g SET rec = ? WHERE g.id = (SELECT g.id FROM students sd JOIN report_card rc ON rc.student_id = sd.enrollment JOIN grade_rep gr ON gr.rep_id = rc.id JOIN grades g ON gr.grade_id = g.id JOIN subjects s ON g.subject_id = s.id WHERE g.subject_id = (SELECT s.id FROM subjects s WHERE s.name ILIKE ?) AND sd.enrollment = ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql1)) {
            if (rec != -1) { stmt.setDouble(1, rec); } else { stmt.setNull(1, Types.DOUBLE); }
            stmt.setString(2, subject);
            stmt.setInt(3, enrollment);
            stmt.execute();
        } finally {
            new ReportCardDAO().updateSituation(enrollment);
        }
        return true;
    }

    /**
     * Bulk updates all grades (N1, N2, and Rec) for a student across all subjects.
     * Uses an atomic transaction for the entire list.
     */
    public boolean updateAllGrades(ArrayList<Grade> grades, int enrollment) {
        Connection conn = null;

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            for (Grade g : grades) {
                // Process standard grades
                boolean success = updateGrades(conn, g.getGrade1(), g.getGrade2(), enrollment, g.getSubject());
                if (!success) {
                    conn.rollback();
                    return false;
                }
                // Process recovery grades
                success = updateRec(conn, g.getRec(), enrollment, g.getSubject());
                if (!success) {
                    conn.rollback();
                    return false;
                }
            }
            conn.commit();
            return true;
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // Ensure the final pass/fail status is refreshed after bulk update
            new ReportCardDAO().updateSituation(enrollment);
        }
        return false;
    }
}