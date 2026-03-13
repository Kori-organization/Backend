package com.example.koribackend.model.dao;

import com.example.koribackend.dto.StudentDTO;
import com.example.koribackend.model.entity.Professor;
import com.example.koribackend.config.ConnectionFactory;
import com.example.koribackend.model.entity.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Professor entities.
 * Manages database operations including role-based authentication and
 * synchronized updates between the professors and subjects tables.
 */
public class ProfessorDAO {

    // Retrieve a list of all professors joined with their respective subjects
    public ArrayList<Professor> selectProfessorAll() {

        ArrayList<Professor> professors = new ArrayList<>();

        // SQL query joining professors with subjects to include the subject name
        String sql = "SELECT p.id, p.username, p.password_hash, s.name, p.name FROM professors p join subjects s on s.id = p.subject_id";

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

    // Find a specific professor based on the subject they teach (case-insensitive)
    public Professor selectProfessorBySubject(String subject) {

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

    // Retrieve a professor profile by their unique username
    public Professor selectProfessorByUser(String username) {
        Professor professor = null;

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

    // Delete a professor and their associated subject record using a transaction
    public boolean deleteById(int id) {

        String sql1 = "SELECT subject_id FROM professors WHERE id = ?";
        String sql2 = "DELETE FROM professors WHERE id = ?";
        String sql3 = "DELETE FROM subjects WHERE id = ?";

        Connection conn = null;

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            int subjectId = -1;
            // First, find the subject ID associated with this professor
            try (PreparedStatement psGet = conn.prepareStatement(sql1)) {
                psGet.setInt(1, id);
                try (ResultSet rs = psGet.executeQuery()) {
                    if (rs.next()) {
                        subjectId = rs.getInt("subject_id");
                    }
                }
            }
            if (subjectId == -1) { return false; }

            // Delete the professor record
            try (PreparedStatement psDeleteProfessor = conn.prepareStatement(sql2)) {
                psDeleteProfessor.setInt(1, id);
                psDeleteProfessor.executeUpdate();
            }

            // Delete the subject record (assuming a 1:1 relationship)
            try (PreparedStatement psDeleteSubject = conn.prepareStatement(sql3)) {
                psDeleteSubject.setInt(1, subjectId);
                psDeleteSubject.executeUpdate();
            }

            conn.commit(); // Finalize all deletions
            return true;
        } catch (Exception e) {
            // Undo changes if any deletion fails
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Cleanup and reset connection state
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception ignored) {}
        }
    }

    // Verify login credentials against the database
    public boolean loginValid(String user, String password) {

        String sql = "SELECT id FROM professors WHERE username = ? AND password_hash = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, user);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if a match is found

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Create a new professor account along with a new subject entry
    public int createAccount(Professor professor) {
        String sql1 = "SELECT id FROM subjects WHERE name LIKE ?";
        String sql2 = "INSERT INTO subjects(name) VALUES(?)";
        String sql3 = "INSERT INTO professors(username, password_hash, subject_id, name) VALUES (?,?,?,?)";

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt1 = conn.prepareStatement(sql1)) {
                stmt1.setString(1,professor.getSubjectName());
                try (ResultSet rs = stmt1.executeQuery()) {
                    if (rs.next()) {
                        return 2;
                    }
                }
            }

            int id = -1;
            // Create the subject first and retrieve its generated primary key
            try (PreparedStatement stmt2 = conn.prepareStatement(sql2,PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt2.setString(1,professor.getSubjectName());
                stmt2.execute();
                try (ResultSet keys = stmt2.getGeneratedKeys()) {
                    if (keys.next()) {
                        id = keys.getInt(1);
                    }
                }
            }

            if (id == -1) { return 0; }

            // Create the professor linking to the new subject ID
            try (PreparedStatement stmt3 = conn.prepareStatement(sql3)) {
                stmt3.setString(1,professor.getUsername());
                stmt3.setString(2,professor.getPassword());
                stmt3.setInt(3,id);
                stmt3.setString(4,professor.getName());
                stmt3.execute();
            }
            conn.commit();
            return 1;
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
        return 0;
    }

    // Update existing professor details and manage subject re-assignment
    public int updateProfessor(Professor professor) {
        String sql1 = "INSERT INTO subjects(name) VALUES(?)";
        String sql2 = "SELECT subject_id FROM professors WHERE id = ?";
        String sql3 = "UPDATE professors SET username = ?, password_hash = ?, subject_id = ?, name = ? WHERE id = ?";
        String sql4 = "DELETE FROM subjects WHERE id = ?";
        String sql5 = "UPDATE professors SET username = ?, password_hash = ?, name = ? WHERE id = ?";


        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            // If a new subject name is provided, perform a complex replacement
            if (!professor.getSubjectName().isEmpty()) {
                int idSubjectEdit = -1;
                // 1. Create the new subject
                try (PreparedStatement stmt1 = conn.prepareStatement(sql1,PreparedStatement.RETURN_GENERATED_KEYS)) {
                    stmt1.setString(1, professor.getSubjectName());
                    stmt1.execute();
                    try (ResultSet keys = stmt1.getGeneratedKeys()) {
                        if (keys.next()) {
                            idSubjectEdit = keys.getInt(1);
                        }
                    }
                }
                if (idSubjectEdit == -1) { return 0; }

                // 2. Identify the old subject ID to be removed later
                int idSubjectDelete = -1;
                try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
                    stmt2.setInt(1, professor.getId());
                    try (ResultSet keys = stmt2.executeQuery()) {
                        if (keys.next()) {
                            idSubjectDelete = keys.getInt(1);
                        }
                    }
                }
                if (idSubjectDelete == -1) { return 0; }

                // 3. Update professor with the new details and new subject reference
                try (PreparedStatement stmt3 = conn.prepareStatement(sql3)) {
                    stmt3.setString(1,professor.getUsername());
                    stmt3.setString(2,professor.getPassword());
                    stmt3.setInt(3,idSubjectEdit);
                    stmt3.setString(4,professor.getName());
                    stmt3.setInt(5,professor.getId());
                    stmt3.execute();
                }

                // 4. Remove the old orphaned subject
                try (PreparedStatement stmt4 = conn.prepareStatement(sql4)) {
                    stmt4.setInt(1,idSubjectDelete);
                    stmt4.execute();
                }
            } else {
                // If no subject update is needed, perform a simpler profile update
                try (PreparedStatement stmt5 = conn.prepareStatement(sql5)) {
                    stmt5.setString(1,professor.getUsername());
                    stmt5.setString(2,professor.getPassword());
                    stmt5.setString(3,professor.getName());
                    stmt5.setInt(4,professor.getId());
                    stmt5.execute();
                }
            }

            conn.commit();
            return 1;
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
        return 0;
    }

    public ArrayList<StudentDTO> StudentsFilterDTO(String situation, int serie, String subjectName) {
        ArrayList<StudentDTO> listResult = new ArrayList<>();
        String sql = "SELECT s.enrollment, s.email, s.issue_date, s.password, s.name, s.serie, g.grade1, g.grade2, g.rec, g.subject_id, sub.name AS subject FROM students s " +
                "JOIN report_card rc " +
                "ON s.enrollment = rc.student_id " +
                "JOIN grade_rep gr " +
                "ON rc.id = gr.rep_id " +
                "JOIN grades g " +
                "ON g.id = gr.grade_id " +
                "JOIN subjects sub " +
                "ON sub.id = g.subject_id " +
                "WHERE rc.final_situation = ? AND s.serie = ? AND sub.name = ?";
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, situation);
            stmt.setInt(2, serie);
            stmt.setString(3, subjectName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                StudentDTO stud = new StudentDTO();
                stud.setEnrollment(rs.getInt("enrollment"));
                stud.setEmail(rs.getString("email"));
                stud.setIssueDate(rs.getDate("issue_date"));
                stud.setPassword(rs.getString("password"));
                stud.setName(rs.getString("name"));
                stud.setSerie(rs.getInt("serie"));
                stud.setGrade1(rs.getDouble("grade1"));
                if (rs.wasNull()) { stud.setGrade1(-1); }
                stud.setGrade2(rs.getDouble("grade2"));
                if (rs.wasNull()) { stud.setGrade2(-1); }
                stud.setRec(rs.getDouble("rec"));
                if (rs.wasNull()) { stud.setRec(-1); }
                stud.setSubject(rs.getString("subject") == null ? "" : rs.getString("subject"));
                listResult.add(stud);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listResult;
    }

    public ArrayList<Student> StudentsFilter(String situation, int grade, String subjectName) {
        ArrayList<Student> listResult = new ArrayList<>();
        String sql = "SELECT s.enrollment, s.name, s.email, s.issue_date " +
                "FROM students s " +
                "JOIN report_card rc " +
                "ON s.enrollment = rc.student_id " +
                "JOIN grade_rep gr " +
                "ON rc.id = gr.rep_id " +
                "JOIN grades g " +
                "ON g.id = gr.grade_id " +
                "JOIN subjects sub " +
                "ON sub.id = g.subject_id " +
                "WHERE rc.final_situation = ? AND s.serie = ? AND sub.name = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, situation);
            stmt.setInt(2, grade);
            stmt.setString(3, subjectName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Student student = new Student();
                student.setEnrollment(rs.getInt("enrollment"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setIssueDate(rs.getDate("issue_date"));
                listResult.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listResult;
    }
}