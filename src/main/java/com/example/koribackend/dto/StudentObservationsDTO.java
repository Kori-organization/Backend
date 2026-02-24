package com.example.koribackend.dto;

import com.example.koribackend.model.dao.ObservationDAO;
import com.example.koribackend.model.entity.Observation;

import java.util.ArrayList;
import java.util.List;

public class StudentObservationsDTO {

    private int enrollment;
    private String name;
    private String email;
    private int serie;
    private List<Observation> observations;

    public StudentObservationsDTO(int enrollment, String name, String email, int serie) {
        this.enrollment = enrollment;
        this.name = name;
        this.email = email;
        this.serie = serie;
    }

    public StudentObservationsDTO() { }

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

    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(ArrayList<Observation> observations) {
        this.observations = observations;
    }

    public void getAllObservations() {
        this.observations = new ObservationDAO().selectObservationsForStudent(this.enrollment);
    }
}
