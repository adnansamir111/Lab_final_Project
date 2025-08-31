package com.example.javaproject.all_class;

import javafx.beans.property.*;

public class Course {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty code = new SimpleStringProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty instructor = new SimpleStringProperty();
    private final IntegerProperty credits = new SimpleIntegerProperty();

    // NEW: marks for this course (nullable; use Double to allow nulls if needed)
    private final DoubleProperty quiz1 = new SimpleDoubleProperty();
    private final DoubleProperty quiz2 = new SimpleDoubleProperty();
    private final DoubleProperty quiz3 = new SimpleDoubleProperty();
    private final DoubleProperty quiz4 = new SimpleDoubleProperty();
    private final DoubleProperty mid   = new SimpleDoubleProperty();
    private final DoubleProperty fin   = new SimpleDoubleProperty(); // "final" in DB

    // Existing constructor (kept for compatibility)
    public Course(int id, String code, String title, String instructor, int credits) {
        this.id.set(id);
        this.code.set(code);
        this.title.set(title);
        this.instructor.set(instructor);
        this.credits.set(credits);
        // marks default to 0.0 (can be updated later)
    }


    @Override
    public String toString() {
        return getTitle();
    }
    // Getters and property methods (needed for JavaFX binding)

    // Optional convenience constructor with marks (not required elsewhere)
    public Course(int id, String code, String title, String instructor, int credits,
                  Double quiz1, Double quiz2, Double quiz3, Double quiz4, Double mid, Double fin) {
        this(id, code, title, instructor, credits);
        if (quiz1 != null) this.quiz1.set(quiz1);
        if (quiz2 != null) this.quiz2.set(quiz2);
        if (quiz3 != null) this.quiz3.set(quiz3);
        if (quiz4 != null) this.quiz4.set(quiz4);
        if (mid   != null) this.mid.set(mid);
        if (fin   != null) this.fin.set(fin);
    }

    // ----- getters/properties (JavaFX binding friendly) -----

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
}
