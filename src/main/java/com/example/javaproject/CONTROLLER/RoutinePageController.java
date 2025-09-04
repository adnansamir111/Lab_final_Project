package com.example.javaproject.CONTROLLER;

import com.example.javaproject.DAO.RoutineDAO;
import com.example.javaproject.all_class.Routine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class RoutinePageController {

    @FXML private HBox mondayHBox;
    @FXML private HBox tuesdayHBox;
    @FXML private HBox wednesdayHBox;
    @FXML private HBox thursdayHBox;
    @FXML private HBox fridayHBox;
    @FXML private HBox saturdayHBox;
    @FXML private HBox sundayHBox;

    @FXML private Button addRoutineButton; // used to get the Stage for navigation

    @FXML
    public void initialize() {
        displayWeeklyRoutine();
    }

    /** Load and render all routines grouped by day. */
    public void displayWeeklyRoutine() {
        clearAllHBoxes();

        List<Routine> weeklyRoutines = RoutineDAO.getRoutineForWeek();
        for (Routine routine : weeklyRoutines) {
            HBox dayHBox = getDayHBox(routine.getDayOfWeek());
            if (dayHBox != null) {
                addRoutineToDay(dayHBox, routine);
            }
        }
    }

    private void clearAllHBoxes() {
        mondayHBox.getChildren().clear();
        tuesdayHBox.getChildren().clear();
        wednesdayHBox.getChildren().clear();
        thursdayHBox.getChildren().clear();
        fridayHBox.getChildren().clear();
        saturdayHBox.getChildren().clear();
        sundayHBox.getChildren().clear();
    }

    private HBox getDayHBox(String day) {
        if (day == null) return null;
        String d = day.trim().toLowerCase();
        switch (d) {
            case "monday":    return mondayHBox;
            case "tuesday":   return tuesdayHBox;
            case "wednesday": return wednesdayHBox;
            case "thursday":  return thursdayHBox;
            case "friday":    return fridayHBox;
            case "saturday":  return saturdayHBox;
            case "sunday":    return sundayHBox;
            default:          return null;
        }
    }

    ///Clicking it confirms & deletes just that entry.
    @FXML
    private void addRoutineToDay(HBox dayHBox, Routine routine) {
        // Base content (existing card style)
        VBox content = new VBox(4);
        content.setPadding(new Insets(8));
        content.setAlignment(Pos.CENTER_LEFT);
        content.setStyle("-fx-background-color: #bfffc2; -fx-border-color: black; -fx-border-radius: 6; -fx-background-radius: 6;");

        Label courseLabel = new Label(safe(routine.getCourseName())
                + ((routine.getRoom() != null && !routine.getRoom().isEmpty()) ? " (" + routine.getRoom() + ")" : ""));
        courseLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label timeLabel = new Label(routine.getStartTime() + " - " + routine.getEndTime());
        timeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555555;");

        content.getChildren().addAll(courseLabel, timeLabel);

        // Overlay delete button (hidden by default, shown on hover)
        Button deleteBtn = new Button("×");
        deleteBtn.setTooltip(new Tooltip("Delete"));
        deleteBtn.setFocusTraversable(false);
        deleteBtn.setVisible(false);
        deleteBtn.setMinSize(24, 24);
        deleteBtn.setPrefSize(24, 24);
        deleteBtn.setStyle(
                "-fx-background-color: #E53935; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 0;"
        );

        StackPane card = new StackPane(content, deleteBtn);
        StackPane.setAlignment(deleteBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(deleteBtn, new Insets(4));

        /// Hover behaviour
        card.setOnMouseEntered(e -> deleteBtn.setVisible(true));
        card.setOnMouseExited(e -> deleteBtn.setVisible(false));


        ///fixed width
        card.setPrefWidth(200);

        ///Adjust the margin for each event card to ensure uniform spacing
        HBox.setMargin(card, new Insets(3));  // Add consistent margin around each card

        // Delete handler (confirm → delete one matching row → refresh)
        deleteBtn.setOnAction(e -> {
            e.consume();
            String summary = (safe(routine.getDayOfWeek()) + "  " + routine.getStartTime() + "-" + routine.getEndTime()
                    + "  " + safe(routine.getCourseName())
                    + ((routine.getRoom() != null && !routine.getRoom().isEmpty()) ? " (" + routine.getRoom() + ")" : "")).trim();

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Delete this routine entry?\n\n" + summary,
                    ButtonType.YES, ButtonType.NO);
            confirm.setHeaderText("Confirm Delete");
            Optional<ButtonType> res = confirm.showAndWait();
            if (!res.isPresent() || res.get() != ButtonType.YES) return;

            int rows = RoutineDAO.deleteOneMatching(routine); // deletes exactly one row
            if (rows > 0) {
                new Alert(Alert.AlertType.INFORMATION, "Deleted successfully.", ButtonType.OK).show();
                refreshRoutineDisplay();
            } else {
                new Alert(Alert.AlertType.ERROR, "Delete failed.", ButtonType.OK).show();
            }
        });

        dayHBox.getChildren().add(card);
    }



    // Navigate to Add Routine page
    @FXML
    private void addRoutineEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaproject/AddRoutinePage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addRoutineButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Add Routine Event page.", ButtonType.OK).show();
        }
    }

    /** Re-render after any changes. */
    public void refreshRoutineDisplay() {
        displayWeeklyRoutine();
    }

    // Back to dashboard
    @FXML
    void backtodashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaproject/Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addRoutineButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load dashboard page.", ButtonType.OK).show();
        }
    }

    private static String safe(String s) { return (s == null) ? "" : s; }
}
