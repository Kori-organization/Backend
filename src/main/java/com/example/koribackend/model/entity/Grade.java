package com.example.koribackend.model.entity;

public class Grade {
    private int id;
    private double grade1;
    private double grade2;
    private String subject;
    private double rec;

    public Grade(int id, double grade1, double grade2, String subject, double rec) {
        this.id = id;
        this.grade1 = grade1;
        this.grade2 = grade2;
        this.subject = subject;
        this.rec = rec;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
