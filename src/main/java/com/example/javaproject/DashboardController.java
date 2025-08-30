package com.example.javaproject;

import com.example.javaproject.all_class.Task;
import com.example.javaproject.all_class.TaskDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class DashboardController {

    @FXML
    private TilePane routineContainer;  // Today's routine

    @FXML
    private VBox taskContainer;         // Next 3 Days' tasks

    @FXML
    private VBox notificationContainer; // Notifications

    // ==================== Initialize ====================
    @FXML
    public void initialize() {
        displayTodayRoutine();
        loadUpcomingTasks();
        loadNotifications();
    }

    // ==================== Today's Routine ====================
    public void displayTodayRoutine() {
        String today = LocalDate.now().getDayOfWeek().toString();  // e.g. MONDAY
        List<Routine> routines = RoutineDAO.getRoutineForToDay(today);

        for (Routine routine : routines) {
            VBox routineCard = createRoutineCard(routine);
            routineContainer.getChildren().add(routineCard);
        }
    }

    private VBox createRoutineCard(Routine routine) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-radius: 8; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.5, 0, 0);");

        Label courseNameLabel = new Label("Course: " + routine.getCourseName());
        Label timeLabel = new Label("Time: " + routine.getStartTime() + " - " + routine.getEndTime());
        Label roomLabel = new Label("Room: " + routine.getRoom());

        card.getChildren().addAll(courseNameLabel, timeLabel, roomLabel);
        return card;
    }

    // ==================== Next 3 Days Tasks ====================
    private void loadUpcomingTasks() {
        if (taskContainer == null) return;
        taskContainer.getChildren().clear();

        List<Task> tasks = TaskDAO.getUpcomingTasks(3);
        for (Task task : tasks) {
            HBox card = new HBox(15);
            card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 10; "
                    + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

            Label name = new Label(task.getTitle());
            name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            Label due = new Label("Due: " + task.getDueAt());
            Label status = new Label("Status: " + task.getStatus());

            card.getChildren().addAll(name, due, status);
            taskContainer.getChildren().add(card);
        }
    }

    // ==================== Notifications ====================
    private void loadNotifications() {
        if (notificationContainer == null) return;
        notificationContainer.getChildren().clear();

        List<Task> dueTasks = TaskDAO.getDueReminders();
        for (Task task : dueTasks) {
            String today = LocalDate.now().toString();
            String threeDaysLater = LocalDate.now().plusDays(3).toString();

            String notifText;
            boolean isThreeDays = false;

            if (task.getDueAt().equals(today)) {
                notifText = "⚠️ Today is the deadline for: " + task.getTitle();
                isThreeDays = false;
            } else if (task.getDueAt().equals(threeDaysLater)) {
                notifText = "⏰ Upcoming deadline in 3 days: " + task.getTitle();
                isThreeDays = true;
            } else {
                notifText = "🔔 Task Reminder: " + task.getTitle();
            }

            boolean unseen = (isThreeDays && task.getSeen3Days() == 0) ||
                    (!isThreeDays && task.getSeenDayOf() == 0);

            HBox notifBox = new HBox(15);
            notifBox.setStyle("-fx-background-color: " + (unseen ? "#e6f0ff;" : "#f5f5f5;") +
                    "-fx-padding: 8; -fx-background-radius: 8;");

            Label msg = new Label(notifText + " (Due: " + task.getDueAt() + ")");
            msg.setStyle("-fx-font-size: 14px;");

            Button dismissBtn = new Button("Dismiss");
            boolean finalIsThreeDays = isThreeDays;
            dismissBtn.setOnAction(e -> {
                TaskDAO.markAsSeen(task.getId(), finalIsThreeDays);
                loadNotifications();
            });

            notifBox.getChildren().addAll(msg, dismissBtn);
            notificationContainer.getChildren().add(notifBox);
        }
    }

    @FXML
    private void handleViewAllNotifications() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NotificationView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("All Notifications");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Notifications page.", ButtonType.OK).show();
        }
    }

    // ==================== Navigation Buttons ====================
    @FXML
    private void goToRoutinePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RoutinePage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) routineContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToCoursePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) routineContainer.getScene().getWindow();
            stage.setMaximized(true);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToSessionsPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SessionsPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) routineContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ New GradeSheet navigation
    @FXML
    private void goToGradeSheetPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GradeSheet.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) routineContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
