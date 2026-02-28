package com.example.koribackend.model.entity;

// Entity class for Professor
public class Professor {
    private int id;
    private String username;
    private String password;
    private String name;
    private String subjectName;

    // Constructor with id
    public Professor(int id, String username, String password, String name, String subjectName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.subjectName = subjectName;
    }

    // Constructor without id
    public Professor(String username, String password, String name, String subjectName) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.subjectName = subjectName;
    }

    // Empty constructor
    public Professor() {}

    // Get id
    public int getId() {
        return id;
    }

    // Set id
    public void setId(int id) {
        this.id = id;
    }

    // Get username
    public String getUsername() {
        return username;
    }

    // Set username
    public void setUsername(String username) {
        this.username = username;
    }

    // Get password
    public String getPassword() {
        return password;
    }

    // Set password
    public void setPassword(String password) {
        this.password = password;
    }

    // Get name
    public String getName() {
        return name;
    }

    // Set name
    public void setName(String name) {
        this.name = name;
    }

    // Get subject name
    public String getSubjectName() {
        return subjectName;
    }

    // Set subject name
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    // Method to display professor data
    @Override
    public String toString() {
        return "ID: " + this.id + " Username: " + this.username + " Password: [PROTECTED]" +  this.name + "Subject name: "  + this.subjectName;
    }
}