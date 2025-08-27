package com.example.javaproject.all_class;

import javafx.beans.property.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StudySession {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty courseId = new SimpleIntegerProperty();
    private final StringProperty startedAt = new SimpleStringProperty();
    private final StringProperty endedAt = new SimpleStringProperty();
    private final IntegerProperty durationMin = new SimpleIntegerProperty();
    private final StringProperty notes = new SimpleStringProperty();

    public StudySession(int id, int courseId, String startedAt, String endedAt, int durationMin, String notes) {
        this.id.set(id);
        this.courseId.set(courseId);
        this.startedAt.set(startedAt);
        this.endedAt.set(endedAt);
        this.durationMin.set(durationMin);
        this.notes.set(notes);
    }
    /// formating the date
    private String formatDate(String dateTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a"); // AM/PM format
            Date date = inputFormat.parse(dateTime);
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return dateTime; // return original if parsing fails
        }
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }

    public int getCourseId() { return courseId.get(); }
    public IntegerProperty courseIdProperty() { return courseId; }

    public String getStartedAt() { return startedAt.get(); }
    public StringProperty startedAtProperty() { return startedAt; }

    public String getEndedAt() { return endedAt.get(); }
    public StringProperty endedAtProperty() { return endedAt; }

    public int getDurationMin() { return durationMin.get(); }
    public IntegerProperty durationMinProperty() { return durationMin; }

    public String getNotes() { return notes.get(); }
    public StringProperty notesProperty() { return notes; }
}
