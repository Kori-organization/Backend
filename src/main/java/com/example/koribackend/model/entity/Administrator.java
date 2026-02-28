package com.example.koribackend.model.entity;

// Entity class for Administrator
public class Administrator {
    private int id;
    private String username;
    private String password;

    // Empty constructor
    public Administrator() {
    }

    // Constructor with fields
    public Administrator(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

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
    public void setUsername(String name) {
        this.username = name;
    }

    // Get password
    public String getPassword() {
        return password;
    }

    // Set password
    public void setPassword(String password) {
        this.password = password;
    }

    // Method to display object data
    public String toString() {
        return "ID: " + this.id + "\nName: " + this.username + "\nPassword: " + this.password;
    }
}