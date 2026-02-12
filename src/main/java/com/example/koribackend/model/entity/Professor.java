package com.example.koribackend.model.entity;

public class Professor {
    private int id;
    private String username;
    private String password;
    private String name;
    private String subjectName;

    public Professor(int id, String username, String password, String name, String subjectName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.subjectName = subjectName;
    }

    public Professor() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Override
    public String toString() {
        return "ID: " + this.id + " Username: " + this.username + " Password: [PROTECTED]" +  this.name + "Subject name: "  + this.subjectName;
    }
}