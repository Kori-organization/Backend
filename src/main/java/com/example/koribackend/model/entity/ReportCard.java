package com.example.koribackend.model.entity;

import java.util.List;

public class ReportCard {
    private int id;
    private List<Grade> grader;
    private String studentName;
    private boolean finalSituation;
    private String subjectName;
    private String professorName;

    public ReportCard(int id, List<Grade> grader, String studentName, boolean finalSituation, String subjectName, String professorName) {
        this.id = id;
        this.grader = grader;
        this.studentName = studentName;
        this.finalSituation = finalSituation;
        this.subjectName = subjectName;
        this.professorName = professorName;
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

    public boolean isFinalSituation() {
        return finalSituation;
    }

    public void setFinalSituation(boolean finalSituation) {
        this.finalSituation = finalSituation;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String toString() {
        return "ID: " + this.id + " Grader: " + this.grader + " Student name: " + this.studentName + " Final situation: " + this.finalSituation + " Subject name: " + this.subjectName + " Professor name: " + this.professorName;
    }
}
