package com.example.javaproject;

import java.time.LocalTime;

public class Routine {

    // DB primary key (auto-increment in routine_events)
    private int id;

    private String courseName;
    private LocalTime startTime;
    private LocalTime endTime;
    private String room;
    // Store the day of the week (whatever string you save: MONDAY, Tuesday, etc.)
    private String dayOfWeek;

    // Optional (present in your original)
    private int userId;

    // Constructor with dayOfWeek
    public Routine(String courseName, LocalTime startTime, LocalTime endTime, String room, String dayOfWeek) {
        this.courseName = courseName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.dayOfWeek = dayOfWeek;
    }

    // ---- id getter/setter ----
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // ---- existing getters/setters ----
    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public LocalTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getRoom() {
        return room;
    }
    public void setRoom(String room) {
        this.room = room;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
