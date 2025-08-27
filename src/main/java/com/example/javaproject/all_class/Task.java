package com.example.javaproject.all_class;

import javafx.beans.property.*;

public class Task {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty courseId = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty notes = new SimpleStringProperty();
    private final StringProperty dueAt = new SimpleStringProperty();  // yyyy-MM-dd
    private final StringProperty status = new SimpleStringProperty();

    public Task(int id, int courseId, String title, String notes, String dueAt, String status) {
        this.id.set(id);
        this.courseId.set(courseId);
        this.title.set(title);
        this.notes.set(notes);
        this.dueAt.set(dueAt);
        this.status.set(status);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }

    public int getCourseId() { return courseId.get(); }
    public IntegerProperty courseIdProperty() { return courseId; }

    public String getTitle() { return title.get(); }
    public StringProperty titleProperty() { return title; }

    public String getNotes() { return notes.get(); }
    public StringProperty notesProperty() { return notes; }

    public String getDueAt() { return dueAt.get(); }
    public StringProperty dueAtProperty() { return dueAt; }

    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }
}
