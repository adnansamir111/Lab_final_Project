package com.example.javaproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class CoursesController {

    @FXML
    private TilePane courseContainer;

    @FXML
    public void initialize() {
        loadCourses();
    }

    private void loadCourses() {
        // clear old cards
        courseContainer.getChildren().clear();

        // fetch from DB
        List<Course> courses = CourseDAO.listAll();

        for (Course c : courses) {
            VBox card = createCourseCard(c);
            courseContainer.getChildren().add(card);
        }
    }


    private VBox createCourseCard(Course course) {
        VBox card = new VBox();
        card.setStyle("""
        -fx-background-color: white;
        -fx-background-radius: 10;
        -fx-border-radius: 10;
        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5,0,0,3);
    """);
        card.setPrefSize(220, 180); // slightly bigger card

        // 🔹 Color palette
        String[] colors = {
                "#E91E63", "#3F51B5", "#009688", "#FF5722",
                "#4CAF50", "#9C27B0", "#2196F3"
        };
        int index = Math.abs(course.getCode().hashCode()) % colors.length;
        String headerColor = colors[index];

        // 🔹 Top colored header (Course Code only)
        Label header = new Label(course.getCode());
        header.setStyle("-fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-font-size: 25; -fx-padding: 10; "
                + "-fx-background-radius: 10 10 0 0; "
                + "-fx-background-color: " + headerColor + ";");
        header.setMaxWidth(Double.MAX_VALUE);

        // 🔹 Content area (everything bold)
        Label title = new Label(course.getTitle());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        Label instructor = new Label("👨‍🏫 " + course.getInstructor());
        instructor.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");

        Label credits = new Label("🎓 Credits: " + course.getCredits());
        credits.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");

        VBox content = new VBox(8, title, instructor, credits);
        content.setStyle("-fx-padding: 12;");

        // 🔹 Assemble card
        card.getChildren().addAll(header, content);

        // Make whole card clickable
        card.setOnMouseClicked(e -> openCourseDetail(course));

        return card;
    }




    @FXML
    private void onAdd() {
        Dialog<Course> dialog = new Dialog<>();
        dialog.setTitle("Add Course");
        dialog.setHeaderText("Enter course details");

        // Dialog buttons
        ButtonType addBtnType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtnType, ButtonType.CANCEL);

        // Input fields
        TextField codeField = new TextField();
        codeField.setPromptText("Course Code (e.g., CSE220)");

        TextField titleField = new TextField();
        titleField.setPromptText("Course Title");

        TextField instructorField = new TextField();
        instructorField.setPromptText("Instructor Name");

        TextField creditsField = new TextField();
        creditsField.setPromptText("Credits (number)");

        VBox vbox = new VBox(10, codeField, titleField, instructorField, creditsField);
        dialog.getDialogPane().setContent(vbox);

        // Convert result when "Add" is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addBtnType) {
                try {
                    int credits = Integer.parseInt(creditsField.getText().trim());
                    return new Course(0,
                            codeField.getText().trim(),
                            titleField.getText().trim(),
                            instructorField.getText().trim(),
                            credits);
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Credits must be a number").showAndWait();
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(course -> {
            CourseDAO.insert(course);
            loadCourses(); // refresh grid
        });
    }
    private void openCourseDetail(Course course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseDetailView.fxml"));
            Parent root = loader.load();

            CourseDetailController controller = loader.getController();
            controller.setCourse(course);

            Stage stage = (Stage) courseContainer.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
