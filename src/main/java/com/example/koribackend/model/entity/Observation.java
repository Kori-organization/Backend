package com.example.koribackend.model.entity;

public class Observation {
    private int id;
    private String studentName;
    private String observation;

    public Observation(int id, String studentName, String observation) {
        this.id = id;
        this.studentName = studentName;
        this.observation = observation;
    }
    public Observation() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String toString() {
        return "ID: " + this.id + "Student name: " + this.studentName + "\nObservation: " + this.observation;
    }
}
