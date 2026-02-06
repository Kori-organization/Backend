package com.example.koribackend.model.entity;

import java.sql.Date;

public class Student {
    private int enrollment;
    private String email;
    private Date issueDate;
    private String password;
    private String name;
    private int serie;

    public Student(int enrollment, String email, Date issueDate, String password, String name, int serie) {
        this.enrollment = enrollment;
        this.email = email;
        this.issueDate = issueDate;
        this.password = password;
        this.name = name;
        this.serie = serie;
    }

    public Student() {}

    public int getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(int enrollment) {
        this.enrollment = enrollment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSerie() {
        return serie;
    }

    public void setSerie(int serie) {
        this.serie = serie;
    }

    public String toString() {
        return "Enrollment: " + this.enrollment + "\nEmail: " + this.email + "Issue date: " + this.issueDate + "Password: " + this.password + "Name: " + this.name + "Serie" + this.serie;
    }
}
