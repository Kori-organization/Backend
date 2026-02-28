package com.example.koribackend.dto;

// Import the DAO to allow the DTO to self-populate observations if needed
import com.example.koribackend.model.dao.ObservationDAO;
import com.example.koribackend.model.entity.Observation;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object that combines student identification with their behavioral history.
 * This is useful for views that display a student's full "pedagogical file."
 */
public class StudentObservationsDTO {

    private int enrollment;
    private String name;
    private String email;
    private int serie;
    private List<Observation> observations;

    // Constructor to initialize basic student details before loading observations
    public StudentObservationsDTO(int enrollment, String name, String email, int serie) {
        this.enrollment = enrollment;
        this.name = name;
        this.email = email;
        this.serie = serie;
    }

    // Default constructor for framework compatibility (e.g., JSON mappers)
    public StudentObservationsDTO() { }

    // --- Identification Getters and Setters ---

    public int getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(int enrollment) {
        this.enrollment = enrollment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSerie() {
        return serie;
    }

    public void setSerie(int serie) {
        this.serie = serie;
    }

    // --- Observation List Management ---

    public List<Observation> getObservations() {
        return observations;
    }

    // Setter for the list of behavioral notes
    public void setObservations(ArrayList<Observation> observations) {
        this.observations = observations;
    }

    /**
     * Active loading method that triggers a database query via the ObservationDAO.
     * It uses the current enrollment ID to fetch all related pedagogical notes.
     */
    public void getAllObservations() {
        this.observations = new ObservationDAO().selectObservationsForStudent(this.enrollment);
    }
}