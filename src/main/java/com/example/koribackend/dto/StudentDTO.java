package com.example.koribackend.dto;

import java.sql.Date;

public class StudentDTO {
    private int enrollment;
    private String email;
    private Date issueDate;
    private String password;
    private String name;
    private int serie;
    private double grade1;
    private double grade2;
    private String subject;
    private double rec;

    public StudentDTO(int enrollment, String email, Date issueDate, String password, String name, int serie, double grade1, double grade2, String subject, double rec) {
        this.enrollment = enrollment;
        this.email = email;
        this.issueDate = issueDate;
        this.password = password;
        this.name = name;
        this.serie = serie;
        this.grade1 = grade1;
        this.grade2 = grade2;
        this.subject = subject;
        this.rec = rec;
    }

    public StudentDTO(String email, Date issueDate, String password, String name, int serie, double grade1, double grade2, String subject, double rec) {
        this.email = email;
        this.issueDate = issueDate;
        this.password = password;
        this.name = name;
        this.serie = serie;
        this.grade1 = grade1;
        this.grade2 = grade2;
        this.subject = subject;
        this.rec = rec;
    }

    public StudentDTO() {
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
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

    public double getGrade1() {
        return grade1;
    }

    public void setGrade1(double grade1) {
        this.grade1 = grade1;
    }

    public double getGrade2() {
        return grade2;
    }

    public void setGrade2(double grade2) {
        this.grade2 = grade2;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getRec() {
        return rec;
    }

    public void setRec(double rec) {
        this.rec = rec;
    }

    public String toString() {
        return "Enrollment: " + this.enrollment + "\nEmail: " + this.email + "\nIssue date: " + this.issueDate + "\nPassword: " + this.password + "\nName: " + this.name + "\nSerie: " + this.serie + "\nGrade 1: " + this.grade1 + "\nGrade 2: " + this.grade2 + "\nRec grade: " + this.rec + "\nSubject: " + this.subject;
    }
}
