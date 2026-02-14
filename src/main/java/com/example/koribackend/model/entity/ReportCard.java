package com.example.koribackend.model.entity;

import java.util.List;

public class ReportCard {
    private int id;
    private List<Grade> grader;
    private String studentName;
    private String finalSituation;
    private int serie;

    public ReportCard(int id, List<Grade> grader, String studentName, String finalSituation, int serie) {
        this.id = id;
        this.grader = grader;
        this.studentName = studentName;
        this.finalSituation = finalSituation;
        this.serie = serie;
    }

    public ReportCard() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Grade> getGrader() {
        return grader;
    }

    public void setGrader(List<Grade> grader) {
        this.grader = grader;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getFinalSituation() {
        return finalSituation;
    }

    public void setFinalSituation(String finalSituation) {
        this.finalSituation = finalSituation;
    }

    public int getSerie() {
        return this.serie;
    }

    public void setSerie(int serie) {
        this.serie = serie;
    }

    public String toString() {
        return "ID: " + this.id + " Grader: " + this.grader + " Student name: " + this.studentName + " Final situation: " + this.finalSituation;
    }
}
