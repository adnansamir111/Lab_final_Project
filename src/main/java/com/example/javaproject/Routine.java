package com.example.javaproject;

import java.time.LocalTime;

public class Routine {

    private String courseName;
    private LocalTime startTime;
    private LocalTime endTime;
    private String room;
    private String dayOfWeek;  // Store the day of the week (e.g., "MONDAY")
    private int userId; // Optional, if you're tracking user data

    // Constructor with dayOfWeek
    public Routine(String courseName, LocalTime startTime, LocalTime endTime, String room, String dayOfWeek) {
        this.courseName = courseName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.dayOfWeek = dayOfWeek;
    }

    // Getters and setters for all fields
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


}
