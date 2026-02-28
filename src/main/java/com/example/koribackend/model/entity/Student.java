package com.example.koribackend.model.entity;

import java.sql.Date;
import java.time.format.DateTimeFormatter;

// Entity class for Student
public class Student {
    private int enrollment;
    private String email;
    private Date issueDate;
    private String password;
    private String name;
    private int serie;

    // Constructor with all fields including enrollment
    public Student(int enrollment, String email, Date issueDate, String password, String name, int serie) {
        this.enrollment = enrollment;
        this.email = email;
        this.issueDate = issueDate;
        this.password = password;
        this.name = name;
        this.serie = serie;
    }

    // Constructor without enrollment (useful for new registrations)
    public Student(String email, Date issueDate, String password, String name, int serie) {
        this.email = email;
        this.issueDate = issueDate;
        this.password = password;
        this.name = name;
        this.serie = serie;
    }

    // Empty constructor
    public Student() {}

    // Get enrollment ID
    public int getEnrollment() {
        return enrollment;
    }

    // Set enrollment ID
    public void setEnrollment(int enrollment) {
        this.enrollment = enrollment;
    }

    // Get student email
    public String getEmail() {
        return email;
    }

    // Set student email
    public void setEmail(String email) {
        this.email = email;
    }

    // Get account issue date
    public Date getIssueDate() {
        return issueDate;
    }

    // Set account issue date
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    // Get student password
    public String getPassword() {
        return password;
    }

    // Set student password
    public void setPassword(String password) {
        this.password = password;
    }

    // Get student full name
    public String getName() {
        return name;
    }

    // Set student full name
    public void setName(String name) {
        this.name = name;
    }

    // Get grade level (serie)
    public int getSerie() {
        return serie;
    }

    // Set grade level (serie)
    public void setSerie(int serie) {
        this.serie = serie;
    }

    // Method to display student data
    public String toString() {
        return "Enrollment: " + this.enrollment + "\nEmail: " + this.email + "Issue date: " + this.issueDate + "Password: " + this.password + "Name: " + this.name + "Serie" + this.serie;
    }

    // Helper to get only the first name
    public String getFirstName() {
        return this.name.split(" ")[0];
    }

    // Helper to format the grade level as a string
    public String getCompleteSerie() {
        return String.format("%dº Ano",this.serie);
    }

    // Helper to format the issue date (dd/MM/yyyy)
    public String getFormatDate() {
        return this.issueDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

}