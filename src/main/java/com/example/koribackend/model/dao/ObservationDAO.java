package com.example.koribackend.model.dao;

import com.example.koribackend.model.entity.Observation;
import com.example.koribackend.config.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student Observations.
 * Handles the storage and retrieval of behavioral and pedagogical notes.
 */
public class ObservationDAO {

    // Retrieve all observations from the database joined with the student's name
    public List<Observation> selectObsAll() {

        List<Observation> observations = new ArrayList<>();

        // SQL query to join observations with students to get the student's name
        String sql = "SELECT obs.id, obs.observation, s.name AS student_name FROM observations obs JOIN students s ON s.enrollment = obs.student_id";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Observation obs = new Observation();
                obs.setId(rs.getInt("id"));
                obs.setObservation(rs.getString("observation"));
                obs.setStudentName(rs.getString("student_name"));

                observations.add(obs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return observations;
    }

    // Search for observations using a student's name (case-insensitive partial match)
    public List<Observation> selectObservationsForStudent(String name) {

        List<Observation> observations = new ArrayList<>();

        // Use ILIKE for case-insensitive matching in PostgreSQL
        String sql = "SELECT obs.id, obs.observation, s.name AS student_name FROM observations obs JOIN students s ON s.enrollment = obs.student_id WHERE s.name ILIKE ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            // Apply wildcards for the ILIKE search pattern
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Observation obs = new Observation();
                obs.setId(rs.getInt("id"));
                obs.setObservation(rs.getString("observation"));
                obs.setStudentName(rs.getString("student_name"));

                observations.add(obs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return observations;
    }

    // Fetch the most recent 8 observations for a specific student using their ID
    public List<Observation> selectObservationsForStudent(int idStudent) {

        List<Observation> observations = new ArrayList<>();

        // Order by ID descending to get the newest notes first, limited to 8 for UI performance
        String sql = "SELECT obs.id, obs.observation, s.name AS student_name FROM observations obs JOIN students s ON s.enrollment = obs.student_id WHERE obs.student_id = ? ORDER BY obs.id DESC LIMIT 8";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, idStudent);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Observation obs = new Observation();
                obs.setId(rs.getInt("id"));
                obs.setObservation(rs.getString("observation"));
                obs.setStudentName(rs.getString("student_name"));

                observations.add(obs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return observations;
    }

    // Remove a specific observation record by its primary key
    public boolean deleteObservation(int id) {

        String sql = "DELETE FROM observations WHERE id = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, id);
            // Returns true if a record was actually removed
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Create a new behavioral observation for a student
    public boolean insertObservation(int studentId, String observation) {

        String sql = "INSERT INTO observations (observation, student_id) VALUES (?, ?)";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {

            pstmt.setString(1, observation);
            pstmt.setInt(2, studentId);
            // Returns true if the insertion was successful
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}