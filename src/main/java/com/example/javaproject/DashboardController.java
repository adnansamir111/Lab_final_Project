package com.example.javaproject;

import com.example.javaproject.all_class.Task;
import com.example.javaproject.all_class.TaskDAO;
import javafx.application.HostServices;
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

    private HostServices hostServices;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    private TilePane routineContainer; // Today's routine

    @FXML
    private VBox taskContainer; // Next 3 Days' tasks

    @FXML
    private VBox notificationContainer; // Notifications

    @FXML
    private Label routineHeader;

    // ==================== Initialize ====================
    @FXML
    public void initialize() {
        displayTodayRoutine();
        loadUpcomingTasks();
        loadNotifications();
    }

    // ==================== Today's Routine ====================
    public void displayTodayRoutine() {

        // Get today's day name (e.g., Monday)
        String todayName = LocalDate.now().getDayOfWeek()
                .getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH);


        // Update the header label dynamically (you should have fx:id for routine header in FXML)
        routineHeader.setText(" "+todayName);
        routineHeader.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-padding: 5;");

        // Clear previous routines
        routineContainer.getChildren().clear();

        // Fetch routines for today (database stores MONDAY, TUESDAY etc.)
        List<Routine> routines = RoutineDAO.getRoutineForToDay(todayName.toUpperCase());

        // Add each routine card
        for (Routine routine : routines) {
            VBox routineCard = createRoutineCard(routine);
            routineContainer.getChildren().add(routineCard);
        }

        // If no classes today
        if (routines.isEmpty()) {
            Label emptyMsg = new Label("✅ No classes today!");
            emptyMsg.setStyle("-fx-font-weight:bold; -fx-text-fill: #888; -fx-font-size: 14px; -fx-font-style: italic;");
            routineContainer.getChildren().add(emptyMsg);
        }
    }

    private VBox createRoutineCard(Routine routine) {
        VBox card = new VBox(6);
        card.setPrefWidth(200);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 12; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0, 0, 2);");

        // Course Name
        Label courseName = new Label(routine.getCourseName());
        courseName.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #2F4F4F;");

        // Time Range
        Label time = new Label("🕒 " + routine.getStartTime() + " - " + routine.getEndTime());
        time.setStyle("--fx-font-weight: bold;fx-text-fill: black; -fx-font-size: 15px;");

        // Room
        Label room = new Label("Room- " + routine.getRoom());
        room.setStyle("-fx-font-weight: bold;-fx-text-fill: black; -fx-font-size: 13px;");

        card.getChildren().addAll(courseName, time, room);
        return card;
    }

    // ==================== Next 3 Days Tasks ====================
    private void loadUpcomingTasks() {
        if (taskContainer == null)
            return;

        taskContainer.getChildren().clear();

        LocalDate currentDate = LocalDate.now(); // Get today's date
        LocalDate endDate = currentDate.plusDays(3); // Calculate 3 days from today

        List<Task> tasks = TaskDAO.getUpcomingTasks(currentDate, endDate); // Pass the date range to fetch the tasks
        for (Task task : tasks) {
            HBox card = new HBox(30); // spacing between fields
            card.setStyle("-fx-background-radius: 8; -fx-padding: 10; "
                    + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0, 0, 2);");

            // ✅ Background color by status
            String bgColor;
            if ("done".equalsIgnoreCase(task.getStatus())) {
                bgColor = "#d4edda"; // soft green
            } else if ("todo".equalsIgnoreCase(task.getStatus())) {
                bgColor = "#fff3cd"; // soft yellow
            } else if ("in-progress".equalsIgnoreCase(task.getStatus())) {
                bgColor = "#cce5ff"; // soft blue
            } else {

                    bgColor = "#e2e3e5"; // fallback

            }
            card.setStyle(card.getStyle() + "-fx-background-color: " + bgColor + ";");

            // ✅ Title (bold)
            Label name = new Label(task.getTitle());
            name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            // ✅ Due Date + Day (bold)
            String dueLabelText = "Due: " + task.getDueAt();
            try {
                LocalDate dueDate = LocalDate.parse(task.getDueAt());
                String dayName = dueDate.getDayOfWeek()
                        .getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH);
                dueLabelText += " (" + dayName + ")";
            } catch (Exception e) { /* ignore */ }
            Label due = new Label(dueLabelText);
            due.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

            // ✅ Status
            Label status = new Label("Status: " + task.getStatus());
            status.setStyle("-fx-font-weight: bold;-fx-text-fill: #444; -fx-font-size: 13px;");

            // Add all in one line
            card.getChildren().addAll(name, due, status);
            taskContainer.getChildren().add(card);
        }
    }



    // ==================== Notifications ====================
    private void loadNotifications() {
        if (notificationContainer == null)
            return;
        notificationContainer.getChildren().clear();

        List<Task> dueTasks = TaskDAO.getDueReminders();
        for (Task task : dueTasks) {
            String today = LocalDate.now().toString();
            String threeDaysLater = LocalDate.now().plusDays(3).toString();

            String notifText;
            boolean isThreeDays = false;

            if (task.getDueAt().equals(today)) {
                notifText = "⚠️Today is the deadline for : " + task.getTitle();
                isThreeDays = false;
            } else if (task.getDueAt().equals(threeDaysLater)) {
                notifText = "⏰Upcoming deadline in 3 days : " + task.getTitle();
                isThreeDays = true;
            } else {
                notifText = "🔔Task Reminder : " + task.getTitle();
            }

            boolean unseen = (isThreeDays && task.getSeen3Days() == 0) ||
                    (!isThreeDays && task.getSeenDayOf() == 0);

            HBox notifBox = new HBox(15);
            notifBox.setStyle("-fx-font-weight: bold; -fx-background-color: " + (unseen ? "#dec3c3;" : "#f5f5f5;") +
                    "-fx-padding: 8; -fx-background-radius: 8;");

            Label msg = new Label(notifText + " (Due: " + task.getDueAt() + ")");
            msg.setStyle("-fx-font-size: 14px;");

            Button dismissBtn = new Button("❌ Dismiss");
            dismissBtn.getStyleClass().add("dismiss-button");
            boolean finalIsThreeDays = isThreeDays;
            dismissBtn.setOnAction(e -> {
                TaskDAO.markAsSeen(task.getId(), finalIsThreeDays);
                loadNotifications();
            });

            notifBox.getChildren().addAll(msg, dismissBtn);
            notificationContainer.getChildren().add(notifBox);
        }
    }



    // ================= Analytics Page =================================
    @FXML
    private void goToAnalyticsPage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("AnalyticsView.fxml"));
            Stage stage = (Stage) routineContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Analytics page.", ButtonType.OK).show();
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

            // Get the controller for the CoursesView
            CoursesController coursesController = loader.getController();

            // Pass the HostServices to the CoursesController
            coursesController.setHostServices(this.hostServices);
            Stage stage = (Stage) routineContainer.getScene().getWindow();

            stage.setScene(new Scene(root,stage.getWidth(),stage.getHeight()));


            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToTaskExplorerPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskExplorer.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) routineContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // GradeSheet navigation
    @FXML
    private void goToGradeSheetPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GradeSheet.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) routineContainer.getScene().getWindow();
            GradeSheetController controller = loader.getController();
            controller.setParent(stage.getScene());
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
