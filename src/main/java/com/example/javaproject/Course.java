package com.example.javaproject;

import javafx.beans.property.*;

public class Course {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty code = new SimpleStringProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty instructor = new SimpleStringProperty();
    private final IntegerProperty credits = new SimpleIntegerProperty();

    public Course(int id, String code, String title, String instructor, int credits) {
        this.id.set(id);
        this.code.set(code);
        this.title.set(title);
        this.instructor.set(instructor);
        this.credits.set(credits);
    }

    // Getters and property methods (needed for JavaFX binding)
    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }

    public String getCode() { return code.get(); }
    public StringProperty codeProperty() { return code; }

    public String getTitle() { return title.get(); }
    public StringProperty titleProperty() { return title; }

    public String getInstructor() { return instructor.get(); }
    public StringProperty instructorProperty() { return instructor; }

    public int getCredits() { return credits.get(); }
    public IntegerProperty creditsProperty() { return credits; }
}
