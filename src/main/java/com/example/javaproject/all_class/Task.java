package com.example.javaproject.all_class;

import com.example.javaproject.DAO.CourseDAO;
import javafx.beans.property.*;

public class Task {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty courseId = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty notes = new SimpleStringProperty();
    private final StringProperty dueAt = new SimpleStringProperty();  // yyyy-MM-dd
    private final StringProperty status = new SimpleStringProperty();

    // 🔹 New fields
    private final IntegerProperty seen3Days = new SimpleIntegerProperty(0);
    private final IntegerProperty seenDayOf = new SimpleIntegerProperty(0);
    private final StringProperty completedAt = new SimpleStringProperty();

    // Default constructor
    public Task(int id, int courseId, String title, String notes,
                String dueAt, String status,
                int seen3Days, int seenDayOf, String completedAt) {
        this.id.set(id);
        this.courseId.set(courseId);
        this.title.set(title);
        this.notes.set(notes);
        this.dueAt.set(dueAt);
        this.status.set(status);
        this.seen3Days.set(seen3Days);
        this.seenDayOf.set(seenDayOf);
        this.completedAt.set(completedAt);
    }

    // ========== Getters / Properties ==========
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

    // 🔹 Seen 3 days before
    public int getSeen3Days() { return seen3Days.get(); }
    public void setSeen3Days(int value) { seen3Days.set(value); }
    public IntegerProperty seen3DaysProperty() { return seen3Days; }

    // 🔹 Seen on due date
    public int getSeenDayOf() { return seenDayOf.get(); }
    public void setSeenDayOf(int value) { seenDayOf.set(value); }
    public IntegerProperty seenDayOfProperty() { return seenDayOf; }

    // 🔹 Completed At
    public String getCompletedAt() { return completedAt.get(); }
    public void setCompletedAt(String value) { completedAt.set(value); }
    public StringProperty completedAtProperty() { return completedAt; }
    public String getCourseName() {
        String courseName = "";
        try {
            // Fetch course name from CourseDAO using course_id
            courseName = CourseDAO.getCourseNameById(this.getCourseId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseName;
    }
}
