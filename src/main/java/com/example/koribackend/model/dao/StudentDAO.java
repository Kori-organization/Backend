package com.example.koribackend.model.dao;

import com.example.koribackend.dto.RakingDTO;
import com.example.koribackend.dto.StudentDTO;
import com.example.koribackend.model.entity.Student;
import com.example.koribackend.config.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Student entities.
 * Handles CRUD operations, authentication, and ranking statistics for students.
 */
public class StudentDAO {

    /**
     * Retrieves all student records from the database.
     * @return List of Student objects.
     */
    public List<Student> selectStudentAll() {

        List<Student> students = new ArrayList<>();

        // SQL query to fetch all student data
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

    /**
     * Retrieves all students belonging to a specific grade (serie).
     * @param grade The grade level to filter by.
     * @return List of Student objects.
     */
    public List<Student> selectStudentAllByGrade(int grade) {

        List<Student> students = new ArrayList<>();

        // SQL query filtered by grade level
        String sql = "SELECT enrollment, email, issue_date, password, name, serie FROM students WHERE serie = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, grade);
            ResultSet rs = pstmt.executeQuery();

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

    /**
     * Retrieves a detailed DTO list of students in a grade including grades for a specific subject.
     * Uses LEFT JOINs to ensure students are listed even if they don't have grades yet.
     */
    public List<StudentDTO> selectStudentDTOAllByGrade(int grade, String subject) {

        List<StudentDTO> students = new ArrayList<>();

        // Complex query joining students with their specific grades and subject info
        String sql = "SELECT s.enrollment, s.email, s.issue_date, s.password, s.name, s.serie, g.grade1, g.grade2, g.rec, g.subject_id, sjc.name AS subject FROM students s " +
                "LEFT JOIN ( " +
                "   SELECT g.*, rc.student_id " +
                "   FROM grades g " +
                "   JOIN grade_rep gr ON gr.grade_id = g.id " +
                "   JOIN report_card rc ON rc.id = gr.rep_id " +
                ") g ON g.student_id = s.enrollment AND g.subject_id = ( " +
                "       SELECT id FROM subjects WHERE name = ? " +
                ")  " +
                "LEFT JOIN subjects sjc ON sjc.id = g.subject_id " +
                "WHERE s.serie = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, subject);
            pstmt.setInt(2, grade);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                StudentDTO stud = new StudentDTO();
                stud.setEnrollment(rs.getInt("enrollment"));
                stud.setEmail(rs.getString("email"));
                stud.setIssueDate(rs.getDate("issue_date"));
                stud.setPassword(rs.getString("password"));
                stud.setName(rs.getString("name"));
                stud.setSerie(rs.getInt("serie"));

                // Normalizing results: if value is 0.0/null, set to -1 to signify "No Grade"
                stud.setGrade1(rs.getDouble("grade1") == 0.0 ? -1 : rs.getDouble("grade1"));
                stud.setGrade2(rs.getDouble("grade2") == 0.0 ? -1 : rs.getDouble("grade2"));
                stud.setRec(rs.getDouble("rec") == 0.0 ? -1 : rs.getDouble("rec"));
                stud.setSubject(rs.getString("subject") == null ? "" : rs.getString("subject"));

                students.add(stud);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    /**
     * Fetches a single student based on their enrollment ID.
     */
    public Student selectStudentByEnrollment(int enrollment) {

        Student stud = new Student();

        // SQL query targeting a specific enrollment ID
        String sql = "SELECT enrollment, email, issue_date, password, name, serie FROM students WHERE enrollment = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, enrollment);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                stud.setEnrollment(rs.getInt("enrollment"));
                stud.setEmail(rs.getString("email"));
                stud.setIssueDate(rs.getDate("issue_date"));
                stud.setPassword(rs.getString("password"));
                stud.setName(rs.getString("name"));
                stud.setSerie(rs.getInt("serie"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stud;
    }

    /**
     * Fetches a list of students matching an enrollment ID.
     */
    public List<Student> selectStudentsByEnrollment(int enrollment) {

        List<Student> students = new ArrayList<>();

        // SQL query targeting a specific enrollment ID
        String sql = "SELECT enrollment, email, issue_date, password, name, serie FROM students WHERE enrollment = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, enrollment);
            ResultSet rs = pstmt.executeQuery();

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

    /**
     * Search students by name or email within a specific grade using case-insensitive partial matching.
     */
    public List<Student> selectStudentsByNameOrEmail(String nameOrEmail, int serie) {

        List<Student> students = new ArrayList<>();

        // SQL query with ILIKE for partial case-insensitive matching
        String sql = "SELECT enrollment, email, issue_date, password, name, serie FROM students WHERE serie = ? AND (name ILIKE ? OR email ILIKE ?)";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, serie);
            pstmt.setString(2,nameOrEmail + "%");
            pstmt.setString(3,nameOrEmail + "%");
            ResultSet rs = pstmt.executeQuery();

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

    /**
     * Retrieves StudentDTO (including grades) for a specific enrollment and subject.
     */
    public List<StudentDTO> selectStudentDTOByEnrollment(int enrollment, String subject) {

        List<StudentDTO> students = new ArrayList<>();

        // Join query to fetch student info alongside subject-specific grades
        String sql = "SELECT s.enrollment, s.email, s.issue_date, s.password, s.name, s.serie, g.grade1, g.grade2, g.rec, g.subject_id, sjc.name AS subject FROM students s " +
                "LEFT JOIN ( " +
                "   SELECT g.*, rc.student_id " +
                "   FROM grades g " +
                "   JOIN grade_rep gr ON gr.grade_id = g.id " +
                "   JOIN report_card rc ON rc.id = gr.rep_id " +
                ") g ON g.student_id = s.enrollment AND g.subject_id = ( " +
                "       SELECT id FROM subjects WHERE name = ? " +
                ")  " +
                "LEFT JOIN subjects sjc ON sjc.id = g.subject_id " +
                "WHERE s.enrollment = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, subject);
            pstmt.setInt(2, enrollment);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                StudentDTO stud = new StudentDTO();
                stud.setEnrollment(rs.getInt("enrollment"));
                stud.setEmail(rs.getString("email"));
                stud.setIssueDate(rs.getDate("issue_date"));
                stud.setPassword(rs.getString("password"));
                stud.setName(rs.getString("name"));
                stud.setSerie(rs.getInt("serie"));

                // Manual null checks using wasNull() for Double values
                stud.setGrade1(rs.getDouble("grade1"));
                if (rs.wasNull()) { stud.setGrade1(-1); }
                stud.setGrade2(rs.getDouble("grade2"));
                if (rs.wasNull()) { stud.setGrade2(-1); }
                stud.setRec(rs.getDouble("rec"));
                if (rs.wasNull()) { stud.setRec(-1); }
                stud.setSubject(rs.getString("subject") == null ? "" : rs.getString("subject"));

                students.add(stud);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    /**
     * Search StudentDTO list by matching name or email prefix.
     */
    public List<StudentDTO> selectStudentDTOByNameOrEmail(String emailOrName, String subject) {

        List<StudentDTO> students = new ArrayList<>();

        // Join query searching across name and email fields
        String sql = "SELECT s.enrollment, s.email, s.issue_date, s.password, s.name, s.serie, g.grade1, g.grade2, g.rec, g.subject_id, sjc.name AS subject FROM students s " +
                "LEFT JOIN ( " +
                "   SELECT g.*, rc.student_id " +
                "   FROM grades g " +
                "   JOIN grade_rep gr ON gr.grade_id = g.id " +
                "   JOIN report_card rc ON rc.id = gr.rep_id " +
                ") g ON g.student_id = s.enrollment AND g.subject_id = ( " +
                "       SELECT id FROM subjects WHERE name = ? " +
                ")  " +
                "LEFT JOIN subjects sjc ON sjc.id = g.subject_id " +
                "WHERE s.email ILIKE ? AND s.name ILIKE ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, subject);
            pstmt.setString(2, emailOrName + "%");
            pstmt.setString(3, emailOrName + "%");
            ResultSet rs = pstmt.executeQuery();

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
                students.add(stud);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Finds a student by an exact email match.
     */
    public Student selectStudentForEmail(String email) {
        Student student = null;

        // SQL query targeting exact email
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

    /**
     * Deletes a student record by enrollment ID.
     * @return boolean True if deletion was successful.
     */
    public boolean deleteStudent(int enrollment) {

        // SQL delete command
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

    /**
     * Validates student login credentials.
     */
    public boolean loginValid(String email, String password) {
        // SQL query to verify credentials
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
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates a student's password.
     */
    public boolean newPasswordStudent(int enrollment, String newPassword) {
        // SQL update for the password field
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

    /**
     * Creates a new student account and an associated report card.
     * Uses a transaction (commit/rollback) to ensure both operations succeed or fail together.
     */
    public boolean createAccount(Student student) {
        String sql1 = "INSERT INTO students(email, issue_Date, password, name, serie) VALUES(?,?,?,?,?)";
        String sql2 = "INSERT INTO report_card(final_situation,student_id) VALUES(?,?)";

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); // Transaction start
            int enrollment = -1;

            // Insert student and retrieve the auto-generated enrollment key
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

            // Create the initial report card for the new student
            try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
                stmt2.setString(1, "Em processo");
                stmt2.setInt(2, enrollment);
                stmt2.execute();
            }
            conn.commit(); // Transaction success
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Transaction fail
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

    /**
     * Checks if a student account with the given email already exists.
     */
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

    /**
     * Calculates class average rankings for a specific subject across different grades.
     * Uses a complex SQL aggregation to compute weighted averages including recovery logic.
     */
    public ArrayList<RakingDTO> selectClassRanking(String subjectName) {
        ArrayList<RakingDTO> rakingDTOS = new ArrayList<>();

        // SQL query calculating averages per grade based on specific academic rules
        String sql = "SELECT \n" +
                "    s.serie,\n" +
                "    AVG(\n" +
                "        CASE\n" +
                "            WHEN g.grade1 IS NOT NULL AND g.grade2 IS NOT NULL THEN\n" +
                "                CASE\n" +
                "                    WHEN (g.grade1 + g.grade2) / 2 >= 7 THEN\n" +
                "                        (g.grade1 + g.grade2) / 2\n" +
                "                    WHEN g.rec IS NULL THEN\n" +
                "                        (g.grade1 + g.grade2) / 2\n" +
                "                    ELSE\n" +
                "                        ((g.grade1 + g.grade2) / 2 + g.rec) / 2\n" +
                "                END\n" +
                "            WHEN g.grade1 IS NOT NULL AND g.grade2 IS NULL THEN\n" +
                "                g.grade1\n" +
                "            WHEN g.grade2 IS NOT NULL AND g.grade1 IS NULL THEN\n" +
                "                g.grade2\n" +
                "            ELSE 0\n" +
                "        END\n" +
                "    ) AS media_turma\n" +
                "FROM students s\n" +
                "LEFT JOIN report_card rc ON rc.student_id = s.enrollment\n" +
                "LEFT JOIN grade_rep gr ON gr.rep_id = rc.id\n" +
                "LEFT JOIN grades g ON g.id = gr.grade_id\n" +
                "LEFT JOIN subjects sub \n" +
                "    ON sub.id = g.subject_id \n" +
                "    AND sub.name = ?\n" +
                "GROUP BY s.serie\n" +
                "ORDER BY media_turma ASC LIMIT 3;";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1,subjectName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RakingDTO rakingDTO = new RakingDTO();
                rakingDTO.setSerie(rs.getInt(1));
                rakingDTO.setAvarageClass(rs.getDouble(2));
                rakingDTOS.add(rakingDTO);
            }
            return rakingDTOS;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves students in a specific grade.
     */
    public ArrayList<Student> selectStudentForSerie(int serie) {
        ArrayList<Student> students = new ArrayList<>();

        String sql = "SELECT enrollment, email, issue_Date, password, name, serie FROM students WHERE serie = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);

        ) {
            stmt.setInt(1,serie);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                student.setEnrollment(rs.getInt("enrollment"));
                student.setEmail(rs.getString("email"));
                student.setIssueDate(rs.getDate("issue_Date"));
                student.setPassword(rs.getString("password"));
                student.setName(rs.getString("name"));
                student.setSerie(rs.getInt("serie"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    /**
     * Retrieves students matching a grade and a specific enrollment ID.
     */
    public ArrayList<Student> selectStudentForSerieAndEnrollment(int serie, int enrollment) {
        ArrayList<Student> students = new ArrayList<>();

        String sql = "SELECT enrollment, email, issue_Date, password, name, serie FROM students WHERE serie = ? AND enrollment = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);

        ) {
            stmt.setInt(1,serie);
            stmt.setInt(2,enrollment);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                student.setEnrollment(rs.getInt("enrollment"));
                student.setEmail(rs.getString("email"));
                student.setIssueDate(rs.getDate("issue_Date"));
                student.setPassword(rs.getString("password"));
                student.setName(rs.getString("name"));
                student.setSerie(rs.getInt("serie"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    /**
     * Search for students within a grade by partial email or name.
     */
    public ArrayList<Student> selectStudentForSerieAndEmailOrName(int serie, String emailOrName) {
        ArrayList<Student> students = new ArrayList<>();

        String sql = "SELECT enrollment, email, issue_Date, password, name, serie FROM students WHERE serie = ? AND (email ILIKE ? OR name ILIKE ?)";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);

        ) {
            stmt.setInt(1,serie);
            stmt.setString(2,emailOrName + "%");
            stmt.setString(3,emailOrName + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                student.setEnrollment(rs.getInt("enrollment"));
                student.setEmail(rs.getString("email"));
                student.setIssueDate(rs.getDate("issue_Date"));
                student.setPassword(rs.getString("password"));
                student.setName(rs.getString("name"));
                student.setSerie(rs.getInt("serie"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    /**
     * Updates an existing student's personal information.
     */
    public boolean updateStudent(Student student) {
        // SQL update command for multiple fields
        String sql = "UPDATE students set email = ?, issue_date = ?, password = ?, name = ?, serie = ? WHERE enrollment = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1,student.getEmail());
            stmt.setDate(2,student.getIssueDate());
            stmt.setString(3,student.getPassword());
            stmt.setString(4,student.getName());
            stmt.setInt(5,student.getSerie());
            stmt.setInt(6,student.getEnrollment());
            return stmt.executeUpdate() >= 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}