package com.example.javaproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class DashboardController {

    @FXML
    private TilePane routineContainer;  // This will display today's routine

    // Called when the page is loaded
    @FXML
    public void initialize() {
        displayTodayRoutine();
    }

    // Fetch and display today's routine
    public void displayTodayRoutine() {
        String today = LocalDate.now().getDayOfWeek().toString();  // Get the current day of the week (e.g., "MONDAY")
        List<Routine> routines = RoutineDAO.getRoutineForToDay(today);  // Fetch routines for today

        // Add each routine to the TilePane (display each routine as a VBox with details)
        for (Routine routine : routines) {
            VBox routineCard = createRoutineCard(routine);
            routineContainer.getChildren().add(routineCard);
        }
    }

    // Create a routine card (display for each routine)
    private VBox createRoutineCard(Routine routine) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.5, 0, 0);");

        // Create Labels for course name, time, and room
        Label courseNameLabel = new Label("Course: " + routine.getCourseName());
        Label timeLabel = new Label("Time: " + routine.getStartTime() + " - " + routine.getEndTime());
        Label roomLabel = new Label("Room: " + routine.getRoom());

        card.getChildren().addAll(courseNameLabel, timeLabel, roomLabel);
        return card;
    }

    // Navigate to the Routine Page when the button is clicked
    @FXML
    private void goToRoutinePage() {
        try {
            // Load the RoutinePage.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RoutinePage.fxml"));
            Parent root = loader.load();

            // Create a new scene and set it to the current stage
            Stage stage = (Stage) routineContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Routine page.", ButtonType.OK).show();
        }
    }

    // Navigate to the Tasks Page
    @FXML
    private void goToCoursePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseView.fxml"));  // Path to your Tasks FXML
            Parent root = loader.load();

            // Create a new scene and set it to the current stage
            Stage stage = (Stage) routineContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Tasks page.", ButtonType.OK).show();
        }
    }

    // Navigate to the Sessions Page
    @FXML
    private void goToSessionsPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SessionsPage.fxml"));  // Path to your Sessions FXML
            Parent root = loader.load();

            // Create a new scene and set it to the current stage
            Stage stage = (Stage) routineContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Sessions page.", ButtonType.OK).show();
        }
    }
}
