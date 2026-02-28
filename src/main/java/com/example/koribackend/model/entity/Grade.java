package com.example.koribackend.model.entity;

// Entity class for Grade
public class Grade {
    private int id;
    private double grade1;
    private double grade2;
    private String subject;
    private double rec;

    // Constructor with id
    public Grade(int id, double grade1, double grade2, String subject, double rec) {
        this.id = id;
        this.grade1 = grade1;
        this.grade2 = grade2;
        this.subject = subject;
        this.rec = rec;
    }

    // Constructor without id
    public Grade(double grade1, double grade2, String subject, double rec) {
        this.grade1 = grade1;
        this.grade2 = grade2;
        this.subject = subject;
        this.rec = rec;
    }

    // Empty constructor
    public Grade() { }

    // Get id
    public int getId() {
        return id;
    }

    // Set id
    public void setId(int id) {
        this.id = id;
    }

    // Get first grade
    public double getGrade1() {
        return grade1;
    }

    // Set first grade
    public void setGrade1(double grade1) {
        this.grade1 = grade1;
    }

    // Get second grade
    public double getGrade2() {
        return grade2;
    }

    // Set second grade
    public void setGrade2(double grade2) {
        this.grade2 = grade2;
    }

    // Get subject name
    public String getSubject() {
        return subject;
    }

    // Set subject name
    public void setSubject(String subject) {
        this.subject = subject;
    }

    // Get recovery grade
    public double getRec() {
        return rec;
    }

    // Set recovery grade
    public void setRec(double rec) {
        this.rec = rec;
    }

    // Method to display grade data
    public String toString() {
        return "ID: " + this.id + "\tGrade 1: " + this.grade1 + "\tGrade 2: " + this.grade2 + "\tSubject: " + this.subject + "\tRecuperation: " + this.rec;
    }
}