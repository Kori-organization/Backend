package com.example.koribackend.model.entity;

public class Observations {
    private int id;
    private String password;
    private String studentName;

    public Observations(int id, String password, String studentName) {
        this.id = id;
        this.password = password;
        this.studentName = studentName;
    }

    public Observations() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String toString() {
        return "ID: " + this.id + "Password: " + this.password + "Student name: " + this.studentName;
    }
}
