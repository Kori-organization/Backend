package com.example.koribackend.model.entity;

// Entity class for Observation
public class Observation {
    private int id;
    private String studentName;
    private String observation;

    // Constructor with all fields
    public Observation(int id, String studentName, String observation) {
        this.id = id;
        this.studentName = studentName;
        this.observation = observation;
    }

    // Empty constructor
    public Observation() {}

    // Get id
    public int getId() {
        return id;
    }

    // Set id
    public void setId(int id) {
        this.id = id;
    }

    // Get student name
    public String getStudentName() {
        return studentName;
    }

    // Set student name
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    // Get observation text
    public String getObservation() {
        return observation;
    }

    // Set observation text
    public void setObservation(String observation) {
        this.observation = observation;
    }

    // Method to display observation data
    public String toString() {
        return "ID: " + this.id + "Student name: " + this.studentName + "\nObservation: " + this.observation;
    }
}