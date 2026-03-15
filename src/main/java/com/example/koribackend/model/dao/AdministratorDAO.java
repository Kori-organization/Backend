package com.example.koribackend.model.dao;

import com.example.koribackend.model.entity.Administrator;
import com.example.koribackend.config.ConnectionFactory;
import com.example.koribackend.model.entity.Event;
import com.example.koribackend.model.entity.Observation;
import com.example.koribackend.model.entity.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for Administrator entities.
 * Manages database operations including retrieval, deletion, and authentication.
 */
public class AdministratorDAO {

    // Retrieve a complete list of all administrators from the database
    public List<Administrator> selectAdministratorAll() {

        List<Administrator> admins = new ArrayList<>();
        String sql = "SELECT id, username, password_hash FROM administrators";

        // Use try-with-resources to ensure the connection and statement are closed automatically
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            // Iterate through the results and map each row to an Administrator object
            while (rs.next()) {
                Administrator admin = new Administrator();
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password_hash"));
                admins.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admins;
    }

    // Remove an administrator record based on its unique primary key
    public boolean deleteAdministrator(int id) {

        String sql = "DELETE FROM administrators WHERE id = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            // Return true if at least one row was affected by the delete operation
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Validate administrator credentials during the login process
    public boolean loginValid(String username, String passwordHash) {

        // Use SELECT 1 as an efficient way to check for existence without fetching data
        String sql = "SELECT 1 FROM administrators WHERE username = ? AND password_hash = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);

            ResultSet rs = stmt.executeQuery();
            // If rs.next() is true, a matching user was found
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Fetch a single administrator profile using their username
    public Administrator selectAdministratorForUsername(String username) {

        Administrator admin = null;
        String sql = "SELECT id, username, password_hash FROM administrators WHERE username LIKE ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                admin = new Administrator();
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password_hash"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }

    public boolean salveEventOnCalender(Event event) {
        String sql = "INSERT INTO calendar_events (admin_id, event_name, event_desc, event_date, event_start, event_end) VALUES (?, ?, ?, ?, ?, ?)";
        int rows = 0;
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, event.getAdminId());
            stmt.setString(2, event.getEventNome());
            stmt.setString(3, event.getEventText());
            stmt.setDate(4, event.getEventDate());
            stmt.setTime(5, event.getEventStart());
            stmt.setTime(6, event.getEventEnd());
            rows = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (rows > 0) {
            return true;
        }
        return false;
    }

    public boolean deleteEventOnCalendar( String dateStr) {
        java.sql.Date sqlDate = java.sql.Date.valueOf(dateStr);
        int rows = 0;

        String sql = "DELETE FROM calendar_events WHERE event_date = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setDate(1, sqlDate);
            rows = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (rows > 0) {
            return true;
        }
        return false;
    }

    public boolean updateEventOnCalenar(Event event) {
        String sql = "UPDATE calendar_events SET event_name=?, event_desc=?, event_date=?, event_start=?, event_end=?, admin_id=? WHERE event_date=?";
        int rows = 0;
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1, event.getEventNome());
            stmt.setString(2, event.getEventText());
            stmt.setDate(3, event.getEventDate());
            stmt.setTime(4, event.getEventStart());
            stmt.setTime(5, event.getEventEnd());
            stmt.setInt(6, event.getAdminId());
            stmt.setDate(7, event.getEventDate());
            rows = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (rows > 0) {
            return true;
        }
        return false;
    }

    public Map<String, Object> selectEventForDate(String dateStr) {
        Map<String, Object> eventResult = new HashMap<>();
        java.sql.Date sqlDate = java.sql.Date.valueOf(dateStr);
        Event event = null;

        String sql = "SELECT event_name, event_desc, event_date, event_start, event_end FROM calendar_events WHERE event_date = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setDate(1, sqlDate);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event();
                event.setEventNome(rs.getString("event_name"));
                event.setEventText(rs.getString("event_desc"));
                event.setEventDate(rs.getDate("event_date"));
                event.setEventStart(rs.getTime("event_start"));
                event.setEventEnd(rs.getTime("event_end"));
                eventResult = event.toMap();
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventResult;
    }

    public ArrayList<Map<String, Object>> selectAllEvents() {
        ArrayList<Map<String, Object>> eventResult = new ArrayList<>();

        String sql = "SELECT event_name, event_desc, event_date, event_start, event_end FROM calendar_events";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Event event = new Event();
                event.setEventNome(rs.getString("event_name"));
                event.setEventText(rs.getString("event_desc"));
                event.setEventDate(rs.getDate("event_date"));
                event.setEventStart(rs.getTime("event_start"));
                event.setEventEnd(rs.getTime("event_end"));
                eventResult.add(event.toMap());
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventResult;
    }

    public ArrayList<Student> StudentsFilter(String situation, int grade) {
        ArrayList<Student> listResult = new ArrayList<>();
        String sql = "SELECT DISTINCT ON(s.enrollment) s.enrollment, s.name, s.email, s.issue_date " +
                "FROM students s " +
                "JOIN report_card rc " +
                "ON s.enrollment = rc.student_id " +
                "JOIN grade_rep gr " +
                "ON rc.id = gr.rep_id " +
                "JOIN grades g " +
                "ON g.id = gr.grade_id " +
                "JOIN subjects sub " +
                "ON sub.id = g.subject_id " +
                "WHERE rc.final_situation = ? AND s.serie = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, situation);
            stmt.setInt(2, grade);
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