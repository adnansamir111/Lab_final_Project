package com.example.javaproject;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CourseDetailController {

    @FXML private Label lblCourseTitle;
    @FXML private Label lblInstructor;
    @FXML private Label lblCredits;

    private Course course;

    // Called by CoursesController when navigating
    public void setCourse(Course course) {
        this.course = course;
        lblCourseTitle.setText(course.getCode() + " – " + course.getTitle());
        lblInstructor.setText("👨‍🏫 " + course.getInstructor());
        lblCredits.setText("🎓 Credits: " + course.getCredits());
    }

    @FXML
    private void onBack() {
        // go back to CoursesView.fxml
        try {
            Stage stage = (Stage) lblCourseTitle.getScene().getWindow();
            stage.getScene().setRoot(
                    javafx.fxml.FXMLLoader.load(getClass().getResource("CourseView.fxml"))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onDelete() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete " + course.getTitle() + "?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                CourseDAO.delete(course.getId());
                onBack(); // go back after delete
            }
        });
    }
}
