package com.example.koribackend.model.entity;

import java.util.List;

// Entity class for Report Card
public class ReportCard {
    private int id;
    private List<Grade> grader;
    private String studentName;
    private String finalSituation;
    private int serie;

    // Constructor with all fields
    public ReportCard(int id, List<Grade> grader, String studentName, String finalSituation, int serie) {
        this.id = id;
        this.grader = grader;
        this.studentName = studentName;
        this.finalSituation = finalSituation;
        this.serie = serie;
    }

    // Empty constructor
    public ReportCard() {}

    // Get report card id
    public int getId() {
        return id;
    }

    // Set report card id
    public void setId(int id) {
        this.id = id;
    }

    // Get list of grades
    public List<Grade> getGrader() {
        return grader;
    }

    // Set list of grades
    public void setGrader(List<Grade> grader) {
        this.grader = grader;
    }

    // Get student name
    public String getStudentName() {
        return studentName;
    }

    // Set student name
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    // Get final academic situation
    public String getFinalSituation() {
        return finalSituation;
    }

    // Set final academic situation
    public void setFinalSituation(String finalSituation) {
        this.finalSituation = finalSituation;
    }

    // Get grade level (serie)
    public int getSerie() {
        return this.serie;
    }

    // Set grade level (serie)
    public void setSerie(int serie) {
        this.serie = serie;
    }

    // Method to display report card data and its grades
    public String toString() {
        String text = "";
        for (Grade grade : grader) {
            text += grade;
        }
        return "ID: " + this.id + " Grader: " + this.grader + " Student name: " + this.studentName + " Final situation: " + this.finalSituation + "Grader: \n" + text;
    }
}