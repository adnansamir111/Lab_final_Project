package com.example.javaproject;

public class Resource {
    private int id;
    private int courseId;  // Use courseId as an integer (foreign key reference to course table)
    private String topic;  // The topic name
    private String videoLink;  // The link to the video (e.g., YouTube link)

    // Constructor
    public Resource(int id, int courseId, String topic, String videoLink) {
        this.id = id;
        this.courseId = courseId;
        this.topic = topic;
        this.videoLink = videoLink;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {  // Change to return the courseId as integer
        return courseId;
    }

    public void setCourseId(int courseId) {  // Set courseId as integer
        this.courseId = courseId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }
}
