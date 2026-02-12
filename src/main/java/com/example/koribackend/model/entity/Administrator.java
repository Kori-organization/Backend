package com.example.koribackend.model.entity;

public class Administrator {
    private int id;
    private String username;
    private String password;

    public Administrator() {
    }

    public Administrator(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "ID: " + this.id + "\nName: " + this.username + "\nPassword: " + this.password;
    }
}
