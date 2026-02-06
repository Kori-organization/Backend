package com.example.koribackend.model.entity;

import java.sql.Date;

public class Student {
    private int enrollment;
    private String email;
    private Date issue_date;
    private String password;
    private String name;
    private int serie;

    public Student(int enrollment, String email, Date issue_date, String password, String name, int serie) {
        this.enrollment = enrollment;
        this.email = email;
        this.issue_date = issue_date;
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

    public Date getIssue_date() {
        return issue_date;
    }

    public void setIssue_date(Date issue_date) {
        this.issue_date = issue_date;
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
}
