package com.example.koribackend.model.entity;

import java.util.List;

public class ReportCard {
    private List<Grade> grader;
    private String studentName;
    private boolean finalSituation;

    public ReportCard(List<Grade> grader, String studentName, boolean finalSituation) {
        this.grader = grader;
        this.studentName = studentName;
        this.finalSituation = finalSituation;
    }

    public ReportCard() {}

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

    public boolean isFinalSituation() {
        return finalSituation;
    }

    public void setFinalSituation(boolean finalSituation) {
        this.finalSituation = finalSituation;
    }

    public String toString() {
        return "Grader: " + this.grader + "Student name: " + this.studentName + "Final situation: " + this.finalSituation;
    }
}
