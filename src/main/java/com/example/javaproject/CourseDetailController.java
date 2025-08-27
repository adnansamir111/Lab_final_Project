package com.example.javaproject;

import com.example.javaproject.all_class.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

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

        loadTasks(); // 🔹 load tasks for this course
        loadSessions();
    }

    @FXML
    private void onBack() {
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
                onBack();
            }
        });
    }

    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, String> colTitle;
    @FXML private TableColumn<Task, String> colDue;
    @FXML private TableColumn<Task, String> colStatus;
    @FXML private TableColumn<Task, String> colNotes;

    @FXML
    public void initialize() {
        if (taskTable != null) {
            colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            colDue.setCellValueFactory(new PropertyValueFactory<>("dueAt"));
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));

            // ✅ Wrap text in Title column
            colTitle.setCellFactory(tc -> new TableCell<>() {
                private final Text text = new Text();
                {
                    text.wrappingWidthProperty().bind(tc.widthProperty());
                    setGraphic(text);
                }
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    text.setText(empty || item == null ? "" : item);
                }
            });

            // ✅ Wrap text in Notes column
            colNotes.setCellFactory(tc -> new TableCell<>() {
                private final Text text = new Text();
                {
                    text.wrappingWidthProperty().bind(tc.widthProperty());
                    setGraphic(text);
                }
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    text.setText(empty || item == null ? "" : item);
                }
            });

            // Row styling using CSS classes instead of hardcoded colors
            taskTable.setRowFactory(tv -> new TableRow<>() {
                @Override
                protected void updateItem(Task item, boolean empty) {
                    super.updateItem(item, empty);

                    // clear old styles
                    getStyleClass().removeAll("task-done", "task-pending", "task-missed");

                    if (item != null && !empty) {
                        if ("done".equalsIgnoreCase(item.getStatus())) {
                            getStyleClass().add("task-done");
                        } else if ("todo".equalsIgnoreCase(item.getStatus()) ||
                                "in-progress".equalsIgnoreCase(item.getStatus())) {
                            if (item.getDueAt() != null && !item.getDueAt().isEmpty()
                                    && LocalDate.parse(item.getDueAt()).isBefore(LocalDate.now())) {
                                getStyleClass().add("task-missed"); // overdue
                            } else {
                                getStyleClass().add("task-pending"); // active
                            }
                        }
                    }
                }
            });
        }
        ///
        if (sessionTable != null) {
            colStart.setCellValueFactory(new PropertyValueFactory<>("startedAt"));
            colEnd.setCellValueFactory(new PropertyValueFactory<>("endedAt"));
            colDuration.setCellValueFactory(new PropertyValueFactory<>("durationMin"));
            colSessionNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
        }

    }

    private void loadTasks() {
        List<Task> tasks = TaskDAO.listByCourse(course.getId());
        taskTable.setItems(FXCollections.observableArrayList(tasks));
    }

    @FXML
    private void onAddTask() {
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Add Task");

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        TextField titleField = new TextField();
        titleField.setPromptText("Task Title");

        DatePicker duePicker = new DatePicker();
        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("todo", "in-progress", "done");
        statusBox.setValue("todo");

        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Notes...");
        notesArea.setWrapText(true); // ✅ allow wrapping inside dialog

        VBox vbox = new VBox(10, new Label("Title:"), titleField,
                new Label("Deadline:"), duePicker,
                new Label("Status:"), statusBox,
                new Label("Notes:"), notesArea);
        vbox.setStyle("-fx-padding: 10;");
        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(button -> {
            if (button == saveBtn) {
                return new Task(0, course.getId(),
                        titleField.getText(),
                        notesArea.getText(),
                        (duePicker.getValue() != null) ? duePicker.getValue().toString() : "",
                        statusBox.getValue());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(task -> {
            TaskDAO.insert(task);
            loadTasks();
        });
    }

    @FXML
    private void onEditTask() {
        Task selected = taskTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a task to edit").showAndWait();
            return;
        }

        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Edit Task");

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        TextField titleField = new TextField(selected.getTitle());
        DatePicker duePicker = new DatePicker(
                (selected.getDueAt() != null && !selected.getDueAt().isEmpty())
                        ? java.time.LocalDate.parse(selected.getDueAt())
                        : null
        );
        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("todo", "in-progress", "done");
        statusBox.setValue(selected.getStatus());

        TextArea notesArea = new TextArea(selected.getNotes());
        notesArea.setWrapText(true); // ✅ wrapping for edit dialog

        VBox vbox = new VBox(10, new Label("Title:"), titleField,
                new Label("Deadline:"), duePicker,
                new Label("Status:"), statusBox,
                new Label("Notes:"), notesArea);
        vbox.setStyle("-fx-padding: 10;");
        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(button -> {
            if (button == saveBtn) {
                return new Task(
                        selected.getId(),
                        course.getId(),
                        titleField.getText(),
                        notesArea.getText(),
                        (duePicker.getValue() != null) ? duePicker.getValue().toString() : "",
                        statusBox.getValue()
                );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updated -> {
            TaskDAO.delete(selected.getId());
            TaskDAO.insert(updated);
            loadTasks();
        });
    }

    @FXML
    private void onDeleteTask() {
        Task selected = taskTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a task to delete").showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete task: " + selected.getTitle() + "?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                TaskDAO.delete(selected.getId());
                loadTasks();
            }
        });
    }

   /// start session
   @FXML private Label lblStopwatch;
    private long sessionStartTime = 0;      // millis when started/resumed
    private int accumulatedMinutes = 0;     // time before pause (in minutes)
    private long accumulatedMillis = 0;     // more precise, for stopwatch
    private boolean running = false;
    @FXML private TableView<StudySession> sessionTable;
    @FXML private TableColumn<StudySession, String> colStart;
    @FXML private TableColumn<StudySession, String> colEnd;
    @FXML private TableColumn<StudySession, Integer> colDuration;
    @FXML private TableColumn<StudySession, String> colSessionNotes;

    private javafx.animation.Timeline stopwatchTimeline;
    @FXML
    private void onStartSession() {
        if (running) {
            new Alert(Alert.AlertType.WARNING, "Session already running!").show();
            return;
        }

        sessionStartTime = System.currentTimeMillis();
        running = true;

        // start ticking stopwatch
        stopwatchTimeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), e -> updateStopwatchLabel())
        );
        stopwatchTimeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        stopwatchTimeline.play();

        new Alert(Alert.AlertType.INFORMATION, "Session started/resumed!").show();
    }
    @FXML
    private void onPauseSession() {
        if (!running) {
            new Alert(Alert.AlertType.WARNING, "No session running!").show();
            return;
        }

        long now = System.currentTimeMillis();
        accumulatedMillis += (now - sessionStartTime);
        running = false;

        if (stopwatchTimeline != null) stopwatchTimeline.stop();
        updateStopwatchLabel();

        new Alert(Alert.AlertType.INFORMATION,
                "Session paused. Accumulated: " + (accumulatedMillis / 60000) + " min").show();
    }
    @FXML
    private void onFinishSession() {
        if (running) {
            long now = System.currentTimeMillis();
            accumulatedMillis += (now - sessionStartTime);
            running = false;
            if (stopwatchTimeline != null) stopwatchTimeline.stop();
        }

        int totalMinutes = (int)(accumulatedMillis / 60000);
        if (totalMinutes == 0) {
            new Alert(Alert.AlertType.WARNING, "No session time recorded!").show();
            return;
        }

        // Use ZonedDateTime for accurate time zone handling
        ZonedDateTime zonedStartTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(sessionStartTime), ZoneId.of("Asia/Dhaka"));  // Correct start time // Adjust to your local time zone
        ZonedDateTime zonedEndTime = ZonedDateTime.now(ZoneId.of("Asia/Dhaka"));  // Adjust to your local time zone

        // Format the start and end times in AM/PM format
        String startedAt = zonedStartTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));
        String endedAt = zonedEndTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));

        // Create StudySession object
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Add notes for this session:");
        dialog.setContentText("Notes:");
        String notes = dialog.showAndWait().orElse("");

        StudySession s = new StudySession(0, course.getId(), startedAt, endedAt, totalMinutes, notes);
        StudySessionDAO.insert(s);

        loadSessions();

        // Reset everything
        accumulatedMillis = 0;
        sessionStartTime = 0;
        lblStopwatch.setText("00:00:00");

        new Alert(Alert.AlertType.INFORMATION, "Session finished and saved!").show();
    }

    // Helper method to format date-time
    private String formatDate(String dateTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            Date date = inputFormat.parse(dateTime);
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return dateTime; // return original if parsing fails
        }
    }

    private void updateStopwatchLabel() {
        long elapsed = accumulatedMillis;
        if (running) {
            elapsed += (System.currentTimeMillis() - sessionStartTime);
        }

        long seconds = elapsed / 1000;
        long hrs = seconds / 3600;
        long mins = (seconds % 3600) / 60;
        long secs = seconds % 60;

        lblStopwatch.setText(String.format("%02d:%02d:%02d", hrs, mins, secs));
    }
    private void loadSessions() {
        if (course == null) return;

        List<StudySession> sessions = StudySessionDAO.listByCourse(course.getId());
        sessionTable.setItems(FXCollections.observableArrayList(sessions));
    }
    @FXML
    private void onDeleteSession() {
        StudySession selected = sessionTable.getSelectionModel().getSelectedItem();  // Get selected row
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a session to delete").showAndWait();
            return;
        }

        // Confirm deletion
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete session: " + selected.getStartedAt() + "?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                // Delete the session from the database
                StudySessionDAO.delete(selected.getId());
                loadSessions();  // Reload sessions to reflect deletion
            }
        });
    }






}
