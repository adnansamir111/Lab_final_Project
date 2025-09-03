package com.example.javaproject.all_class;

public class Chapter {

    private int id;
    private int courseId;            // Foreign key to the course
    private String chapterName;
    private boolean isCompleted;

    // Constructor
    public Chapter(int id, int courseId, String chapterName, boolean isCompleted) {
        this.id = id;
        this.courseId = courseId;
        this.chapterName = chapterName;
        this.isCompleted = isCompleted;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
