package com.example.javaproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.IOException;
import java.util.List;

public class RoutinePageController {

    @FXML private HBox mondayHBox;
    @FXML private HBox tuesdayHBox;
    @FXML private HBox wednesdayHBox;
    @FXML private HBox thursdayHBox;
    @FXML private HBox fridayHBox;
    @FXML private HBox saturdayHBox;
    @FXML private HBox sundayHBox;

    @FXML private Button addRoutineButton;

    // Initialize the page with the routines for each day
    @FXML
    public void initialize() {
        displayWeeklyRoutine();
    }

    // Fetch and display routine for the entire week
    public void displayWeeklyRoutine() {
        // Clear all HBoxes first
        clearAllHBoxes();

        // Fetch all routines for the week
        List<Routine> weeklyRoutines = RoutineDAO.getRoutineForWeek();

        // Add the routines for each day to the corresponding HBox
        for (Routine routine : weeklyRoutines) {
            switch (routine.getDayOfWeek().toUpperCase()) {
                case "MONDAY":
                    addRoutineToDay(mondayHBox, routine);
                    break;
                case "TUESDAY":
                    addRoutineToDay(tuesdayHBox, routine);
                    break;
                case "WEDNESDAY":
                    addRoutineToDay(wednesdayHBox, routine);
                    break;
                case "THURSDAY":
                    addRoutineToDay(thursdayHBox, routine);
                    break;
                case "FRIDAY":
                    addRoutineToDay(fridayHBox, routine);
                    break;
                case "SATURDAY":
                    addRoutineToDay(saturdayHBox, routine);
                    break;
                case "SUNDAY":
                    addRoutineToDay(sundayHBox, routine);
                    break;
            }
        }
    }

    // Clear all HBox containers
    private void clearAllHBoxes() {
        mondayHBox.getChildren().clear();
        tuesdayHBox.getChildren().clear();
        wednesdayHBox.getChildren().clear();
        thursdayHBox.getChildren().clear();
        fridayHBox.getChildren().clear();
        saturdayHBox.getChildren().clear();
        sundayHBox.getChildren().clear();
    }

    // Add a routine to a specific day's HBox (side by side)
    private void addRoutineToDay(HBox dayHBox, Routine routine) {
        // Create a VBox for the routine to display course name above time
        VBox routineVBox = new VBox();
        routineVBox.setSpacing(5);
        routineVBox.setPadding(new Insets(10));
        routineVBox.setAlignment(Pos.CENTER);
        routineVBox.setStyle("-fx-background-color: #E3F2FD; -fx-border-color: #BBDEFB; " +
                "-fx-border-radius: 5; -fx-background-radius: 5; " +
                "-fx-min-width: 150px;");

        // Create label for course name
        Label courseLabel = new Label(routine.getCourseName());
        courseLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        courseLabel.setWrapText(true);

        // Create label for time
        Label timeLabel = new Label(routine.getStartTime() + " - " + routine.getEndTime());
        timeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555555;");

        // Add labels to VBox
        routineVBox.getChildren().addAll(courseLabel, timeLabel);

        // Add the VBox to the HBox for that day (side by side)
        dayHBox.getChildren().add(routineVBox);
    }

    // Method to handle adding a new routine event
    @FXML
    private void addRoutineEvent() {
        try {
            // Navigate to AddRoutinePage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddRoutinePage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addRoutineButton.getScene().getWindow();
            ///stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Add Routine Event page.", ButtonType.OK).show();
        }
    }

    // Method to refresh the routine display (can be called after adding a new routine)
    public void refreshRoutineDisplay() {
        displayWeeklyRoutine();
    }
    @FXML
    void backtodashboard(ActionEvent event) {
        try {
            // Navigate to AddRoutinePage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addRoutineButton.getScene().getWindow();
            ///stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load dashboard page.", ButtonType.OK).show();
        }
    }



}