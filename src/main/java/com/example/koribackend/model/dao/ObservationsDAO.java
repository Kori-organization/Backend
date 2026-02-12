package com.example.koribackend.model.dao;

import com.example.koribackend.model.entity.Observations;
import com.example.koribackend.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ObservationsDAO {

    // Gets all observations
    public List<Observations> selectObsAll() {

        List<Observations> observations = new ArrayList<>();

        // SQL query
        String sql = "SELECT obs.id, obs.observation, s.name AS student_name FROM observations obs JOIN students s ON s.enrollment = obs.student_id";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Observations obs = new Observations();
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

    // Gets observations by student name
    public List<Observations> selectObservationsForStudent(String name) {

        List<Observations> observations = new ArrayList<>();

        // SQL query
        String sql = "SELECT obs.id, obs.observation, s.name AS student_name FROM observations obs JOIN students s ON s.enrollment = obs.student_id WHERE s.name ILIKE ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, "%" + name + "%"); //Helps on ILIKE consult
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Observations obs = new Observations();
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

    // Gets observations by student ID
    public List<Observations> selectObservationsForStudent(int idStudent) {

        List<Observations> observations = new ArrayList<>();

        // SQL query
        String sql = "SELECT obs.id, obs.observation, s.name AS student_name FROM observations obs JOIN students s ON s.enrollment = obs.student_id WHERE obs.student_id = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, idStudent);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Observations obs = new Observations();
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

    // Deletes by ID
    public boolean deleteObservation(int id) {

        // SQL query
        String sql = "DELETE FROM observations WHERE id = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
