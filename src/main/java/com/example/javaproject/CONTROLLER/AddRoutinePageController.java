package com.example.javaproject.CONTROLLER;

import com.example.javaproject.DAO.RoutineDAO;
import com.example.javaproject.all_class.Routine;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AddRoutinePageController {

    @FXML private TextField courseNameField;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private TextField roomField;
    @FXML private ComboBox<String> dayOfWeekComboBox;

    @FXML
    public void initialize() {
        dayOfWeekComboBox.getItems().addAll(
                "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"
        );
    }

    @FXML
    private void addRoutineEvent() {
        if (courseNameField.getText().isEmpty() || startTimeField.getText().isEmpty()
                || endTimeField.getText().isEmpty() || roomField.getText().isEmpty()
                || dayOfWeekComboBox.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields.", ButtonType.OK).show();
            return;
        }

        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
            String startTimeText = startTimeField.getText().trim().toUpperCase();
            String endTimeText = endTimeField.getText().trim().toUpperCase();

            startTimeText = startTimeText.replaceAll("(?<=\\d)(?=[AP])", " ");
            endTimeText = endTimeText.replaceAll("(?<=\\d)(?=[AP])", " ");

            LocalTime startTime = LocalTime.parse(startTimeText, timeFormatter);
            LocalTime endTime = LocalTime.parse(endTimeText, timeFormatter);

            String dayOfWeek = dayOfWeekComboBox.getValue();

            Routine newEvent = new Routine(
                    courseNameField.getText(),
                    startTime,
                    endTime,
                    roomField.getText(),
                    dayOfWeek
            );

            RoutineDAO.insert(newEvent);
            new Alert(Alert.AlertType.INFORMATION, "Routine event added successfully!", ButtonType.OK).show();

            courseNameField.clear();
            startTimeField.clear();
            endTimeField.clear();
            roomField.clear();
            dayOfWeekComboBox.getSelectionModel().clearSelection();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Invalid time format. Please enter time in format: H:MM AM/PM\n"
                    + "Examples: 2:30 PM, 10:45 AM, 2:30PM, 10:45AM", ButtonType.OK).show();
        }
    }

    @FXML

    private void back() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RoutinePage.fxml"));
            Parent root = loader.load();

            // Get the current window and set the new scene
            Stage stage = (Stage) courseNameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
