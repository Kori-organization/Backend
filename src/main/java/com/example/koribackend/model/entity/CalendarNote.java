package com.example.koribackend.model.entity;

import java.time.LocalDate;

public class CalendarNote {
    private int id;
    private LocalDate date;
    private String note;
    private int adminId;

    public CalendarNote() {}

    public CalendarNote(LocalDate date, String note, int adminId) {
        this.date = date;
        this.note = note;
        this.adminId = adminId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
}