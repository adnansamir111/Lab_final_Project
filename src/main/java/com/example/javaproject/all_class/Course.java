package com.example.javaproject.all_class;

import javafx.beans.property.*;

public class Course {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty code = new SimpleStringProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty instructor = new SimpleStringProperty();
    private final DoubleProperty credits = new SimpleDoubleProperty();

    // Google Classroom link (optional)
    private final StringProperty classroomUrl = new SimpleStringProperty();

    // NEW: marks for this course (nullable; use Double to allow nulls if needed)
    private final DoubleProperty quiz1 = new SimpleDoubleProperty();
    private final DoubleProperty quiz2 = new SimpleDoubleProperty();
    private final DoubleProperty quiz3 = new SimpleDoubleProperty();
    private final DoubleProperty quiz4 = new SimpleDoubleProperty();
    private final DoubleProperty mid   = new SimpleDoubleProperty();
    private final DoubleProperty fin   = new SimpleDoubleProperty(); // "final" in DB

    // Existing constructor (kept for compatibility)
    public Course(int id, String code, String title, String instructor, double credits) {
        this.id.set(id);
        this.code.set(code);
        this.title.set(title);
        this.instructor.set(instructor);
        this.credits.set(credits);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public void setId(int id) { this.id.set(id); }

    public String getCode() { return code.get(); }
    public StringProperty codeProperty() { return code; }
    public void setCode(String code) { this.code.set(code); }

    public String getTitle() { return title.get(); }
    public StringProperty titleProperty() { return title; }
    public void setTitle(String title) { this.title.set(title); }

    public String getInstructor() { return instructor.get(); }
    public StringProperty instructorProperty() { return instructor; }
    public void setInstructor(String instructor) { this.instructor.set(instructor); }

    public double getCredits() { return credits.get(); }
    public DoubleProperty creditsProperty() { return credits; }
    public void setCredits(double credits) { this.credits.set(credits); }

    public double getQuiz1() { return quiz1.get(); }
    public DoubleProperty quiz1Property() { return quiz1; }
    public void setQuiz1(double v) { quiz1.set(v); }

    public double getQuiz2() { return quiz2.get(); }
    public DoubleProperty quiz2Property() { return quiz2; }
    public void setQuiz2(double v) { quiz2.set(v); }

    public double getQuiz3() { return quiz3.get(); }
    public DoubleProperty quiz3Property() { return quiz3; }
    public void setQuiz3(double v) { quiz3.set(v); }

    public double getQuiz4() { return quiz4.get(); }
    public DoubleProperty quiz4Property() { return quiz4; }
    public void setQuiz4(double v) { quiz4.set(v); }

    public double getMid() { return mid.get(); }
    public DoubleProperty midProperty() { return mid; }
    public void setMid(double v) { mid.set(v); }

    public double getFin() { return fin.get(); } // final mark
    public DoubleProperty finProperty() { return fin; }
    public void setFin(double v) { fin.set(v); }

    // --- Classroom URL ---
    public String getClassroomUrl() { return classroomUrl.get(); }
    public StringProperty classroomUrlProperty() { return classroomUrl; }
    public void setClassroomUrl(String url) { classroomUrl.set(url); }
}
