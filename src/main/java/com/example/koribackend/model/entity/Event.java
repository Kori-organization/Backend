package com.example.koribackend.model.entity;

import com.example.koribackend.model.dao.AdministratorDAO;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Event {
    private int id ;
    private String eventName;
    private Date eventDate;
    private Time eventStart;
    private Time eventEnd;
    private String eventText;
    private int adminId;

    public Event(int id, String eventNome, Date eventDate, Time eventStart, Time eventEnd, String text, int adminId) {
        this.id = id;
        this.eventName = eventNome;
        this.eventDate = eventDate;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.eventText = text;
        this.adminId = adminId;
    }

    public Event(String eventNome, String eventDate, String eventStart, String eventEnd, String text, String adminName) {
        this.eventName = eventNome;
        this.eventDate = Date.valueOf(LocalDate.parse(eventDate));
        this.eventStart = eventStart != null && !eventStart.isEmpty() ? Time.valueOf(eventStart.length() == 5 ? eventStart + ":00" : eventStart) : null;
        this.eventEnd = eventEnd != null && !eventEnd.isEmpty() ? Time.valueOf(eventEnd.length() == 5 ? eventEnd + ":00" : eventEnd) : null;
        this.eventText = text;
        this.adminId = new AdministratorDAO().selectAdministratorForUsername(adminName).getId();
    }



    public Event() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventNome() {
        return eventName;
    }

    public void setEventNome(String eventNome) {
        this.eventName = eventNome;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Time getEventStart() {
        return eventStart;
    }

    public void setEventStart(Time eventStart) {
        this.eventStart = eventStart;
    }

    public Time getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(Time eventEnd) {
        this.eventEnd = eventEnd;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getEventText() {
        return eventText;
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
    }

    public String toString() {
        return "Event ID: " + this.id + ", Name: " + this.eventName + ", Date: " + this.eventDate + ", Start: " + this.eventStart + ", End: " + this.eventEnd + "]";
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("eventName", this.eventName);
        map.put("eventDate", this.eventDate != null ? this.eventDate.toString() : null);
        map.put("eventStart", this.eventStart != null ? this.eventStart.toString() : null);
        map.put("eventEnd", this.eventEnd != null ? this.eventEnd.toString() : null);
        map.put("eventText", this.eventText);

        return map;
    }
}
