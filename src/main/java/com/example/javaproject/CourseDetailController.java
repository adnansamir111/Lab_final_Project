package com.example.javaproject;

import com.example.javaproject.all_class.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.application.HostServices;

import javafx.application.HostServices;
import java.net.URI;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class CourseDetailController {

    @FXML private Label lblCourseTitle;
    @FXML private Label lblInstructor;
    @FXML private Label lblCredits;

    private Course course;
    private HostServices hostServices;  // Store the HostServices instance

    // Setter method to inject HostServices
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    // Called by CoursesController when navigating
    public void setCourse(Course course) {
        this.course = course;
        lblCourseTitle.setText(course.getCode() + " – " + course.getTitle());
        lblInstructor.setText("👨‍🏫 " + course.getInstructor());
        lblCredits.setText("🎓 Credits: " + course.getCredits());

        loadTasks();
        loadSessions();
        loadResources();
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

    // ==================== Task Table ====================
    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, String> colTitle;
    @FXML private TableColumn<Task, String> colDue;
    @FXML private TableColumn<Task, String> colStatus;
    @FXML private TableColumn<Task, String> colNotes;
    // resource table
    @FXML private TableView<Resource> resourceTable;
    @FXML private TableColumn<Resource, String> colTopic;
    @FXML private TableColumn<Resource, String> colVideoLink;

//    @FXML
//    public void initialize() {
//        if (taskTable != null) {
//            colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
//            colDue.setCellValueFactory(new PropertyValueFactory<>("dueAt"));
//            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
//            colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
//
//            taskTable.setRowFactory(tv -> new TableRow<>() {
//                @Override
//                protected void updateItem(Task item, boolean empty) {
//                    super.updateItem(item, empty);
//                    getStyleClass().removeAll("task-done", "task-pending", "task-missed");
//
//                    if (item != null && !empty) {
//                        if ("done".equalsIgnoreCase(item.getStatus())) {
//                            getStyleClass().add("task-done");
//                        } else if ("todo".equalsIgnoreCase(item.getStatus()) ||
//                                "in-progress".equalsIgnoreCase(item.getStatus())) {
//                            if (item.getDueAt() != null && !item.getDueAt().isEmpty()
//                                    && LocalDate.parse(item.getDueAt()).isBefore(LocalDate.now())) {
//                                getStyleClass().add("task-missed"); // overdue
//                            } else {
//                                getStyleClass().add("task-pending"); // active
//                            }
//                        }
//                    }
//                }
//            });
//        }
//
//        if (sessionTable != null) {
//            colStart.setCellValueFactory(new PropertyValueFactory<>("startedAt"));
//            colEnd.setCellValueFactory(new PropertyValueFactory<>("endedAt"));
//            colDuration.setCellValueFactory(new PropertyValueFactory<>("durationMin"));
//            colSessionNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
//        }
//
//        if (resourceTable != null) {
//            colTopic.setCellValueFactory(new PropertyValueFactory<>("topic"));
//            colVideoLink.setCellValueFactory(new PropertyValueFactory<>("videoLink"));
//            loadResources();  // Load resources for the current course
//        }
//    }

    @FXML
    public void initialize() {
        if (taskTable != null) {
            colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            colDue.setCellValueFactory(new PropertyValueFactory<>("dueAt"));
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));

            taskTable.setRowFactory(tv -> new TableRow<>() {
                @Override
                protected void updateItem(Task item, boolean empty) {
                    super.updateItem(item, empty);
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

        if (sessionTable != null) {
            colStart.setCellValueFactory(new PropertyValueFactory<>("startedAt"));
            colEnd.setCellValueFactory(new PropertyValueFactory<>("endedAt"));
            colDuration.setCellValueFactory(new PropertyValueFactory<>("durationMin"));
            colSessionNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
        }

        if (resourceTable != null) {
            colTopic.setCellValueFactory(new PropertyValueFactory<>("topic"));

            // ✅ This line was missing; without it the cell value is null so nothing shows.
            colVideoLink.setCellValueFactory(new PropertyValueFactory<>("videoLink"));

            // Render each cell as a clickable Hyperlink
            colVideoLink.setCellFactory(column -> new TableCell<Resource, String>() {
                private final Hyperlink hyperlink = new Hyperlink();
                {
                    hyperlink.setWrapText(true);
                    hyperlink.setMaxWidth(Double.MAX_VALUE);
                }
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null || item.isBlank()) {
                        setGraphic(null);
                    } else {
                        hyperlink.setText(item);
                        hyperlink.setOnAction(e -> openLink(item));
                        setGraphic(hyperlink);
                    }
                }
            });

            loadResources();  // Load resources for the current course
        }
    }



    //    private void openLink(String url) {
//        try {
//            // Open the URL in the default web browser
//            HostServices hostServices = getHostServices();
//            hostServices.showDocument(url);
//        } catch (Exception e) {
//            new Alert(Alert.AlertType.ERROR, "Invalid URL").show();
//        }
//    }
    private void openLink(String url) {
        try {
            String target = (url == null) ? "" : url.trim();
            if (target.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Empty URL").show();
                return;
            }
            // add https:// if user pasted without scheme
            if (!target.matches("(?i)^https?://.*")) {
                target = "https://" + target;
            }

            if (hostServices != null) {
                hostServices.showDocument(target);  // default browser
            } else if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().browse(new java.net.URI(target));
            } else {
                new Alert(Alert.AlertType.ERROR, "No supported way to open links on this platform.").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Invalid URL").show();
        }
    }


    private void loadTasks() {
        List<Task> tasks = TaskDAO.listByCourse(course.getId());
        taskTable.setItems(FXCollections.observableArrayList(tasks));
    }

    // Load resources for the current course
    private void loadResources() {
        if (course == null) return;  // Ensure the course is not null
        List<Resource> resources = ResourceDAO.listByCourse(course.getId());  // Fetch resources using course code
        resourceTable.setItems(FXCollections.observableArrayList(resources));  // Set the items in the table
    }

    @FXML
    private void onAddResource() {
        Dialog<Resource> dialog = new Dialog<>();
        dialog.setTitle("Add Resource");

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        // Create input fields for topic and video link
        TextField topicField = new TextField();
        TextField linkField = new TextField();

        VBox vbox = new VBox(10, new Label("Topic:"), topicField,
                new Label("Video Link:"), linkField);
        vbox.setStyle("-fx-padding: 10;");
        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(button -> {
            if (button == saveBtn) {
                // Create a new Resource object with the provided details
                return new Resource(0, course.getId(), topicField.getText(), linkField.getText());
            }
            return null;
        });

        // Show the dialog and handle the result
        dialog.showAndWait().ifPresent(resource -> {
            ResourceDAO.insert(resource);  // Insert the new resource into the database
            loadResources();  // Reload the resources table to display the new resource
        });
    }



    @FXML
    private void onAddTask() {
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Add Task");

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        TextField titleField = new TextField();
        DatePicker duePicker = new DatePicker();
        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("todo", "in-progress", "done");
        statusBox.setValue("todo");
        TextArea notesArea = new TextArea();

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
                        statusBox.getValue(),
                        0, 0, null);
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
                        ? LocalDate.parse(selected.getDueAt())
                        : null
        );
        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("todo", "in-progress", "done");
        statusBox.setValue(selected.getStatus());
        TextArea notesArea = new TextArea(selected.getNotes());

        VBox vbox = new VBox(10, new Label("Title:"), titleField,
                new Label("Deadline:"), duePicker,
                new Label("Status:"), statusBox,
                new Label("Notes:"), notesArea);
        vbox.setStyle("-fx-padding: 10;");
        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(button -> {
            if (button == saveBtn) {
                return new Task(selected.getId(), course.getId(),
                        titleField.getText(),
                        notesArea.getText(),
                        (duePicker.getValue() != null) ? duePicker.getValue().toString() : "",
                        statusBox.getValue(),
                        selected.getSeen3Days(),
                        selected.getSeenDayOf(),
                        selected.getCompletedAt());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updated -> {
            TaskDAO.update(updated);   // ✅ update instead of delete/insert
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

    // ==================== Study Sessions ====================
    @FXML private Label lblStopwatch;
    private long sessionStartTime = 0;
    private long accumulatedMillis = 0;
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
        ZonedDateTime zonedStartTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(sessionStartTime), ZoneId.of("Asia/Dhaka"));
        ZonedDateTime zonedEndTime = ZonedDateTime.now(ZoneId.of("Asia/Dhaka"));
        String startedAt = zonedStartTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));
        String endedAt = zonedEndTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));

        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Add notes for this session:");
        dialog.setContentText("Notes:");
        String notes = dialog.showAndWait().orElse("");

        StudySession s = new StudySession(0, course.getId(), startedAt, endedAt, totalMinutes, notes);
        StudySessionDAO.insert(s);
        loadSessions();

        accumulatedMillis = 0;
        sessionStartTime = 0;
        lblStopwatch.setText("00:00:00");
        new Alert(Alert.AlertType.INFORMATION, "Session finished and saved!").show();
    }

    private void updateStopwatchLabel() {
        long elapsed = accumulatedMillis;
        if (running) elapsed += (System.currentTimeMillis() - sessionStartTime);
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
        StudySession selected = sessionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a session to delete").showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete session: " + selected.getStartedAt() + "?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                StudySessionDAO.delete(selected.getId());
                loadSessions();
            }
        });
    }

    // Manually Add Study Session
    @FXML
    private void onAddManualRecord() {
        Dialog<StudySession> dialog = new Dialog<>();
        dialog.setTitle("Add Study Session");

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        // Create DatePicker for date selection
        DatePicker datePicker = new DatePicker();
        TextField startTimeField = new TextField();
        TextField endTimeField = new TextField();
        TextArea notesArea = new TextArea();

        // Add format hint
        Label formatHint = new Label("Enter time in format: hh:mm AM/PM");

        VBox vbox = new VBox(10, new Label("Select Date:"), datePicker,
                new Label("Start Time:"), startTimeField,
                new Label("End Time:"), endTimeField,
                formatHint,
                new Label("Notes:"), notesArea);
        vbox.setStyle("-fx-padding: 10;");
        dialog.getDialogPane().setContent(vbox);

        // Enable/disable save button based on valid input
        Node saveButton = dialog.getDialogPane().lookupButton(saveBtn);
        saveButton.setDisable(true); // Disable initially

        // Validation listener
        ChangeListener<String> validationListener = (obs, oldV, newV) -> {
            boolean isValid = !datePicker.getValue().toString().isEmpty() &&
                    !startTimeField.getText().trim().isEmpty() &&
                    !endTimeField.getText().trim().isEmpty();
            saveButton.setDisable(!isValid);
        };

        startTimeField.textProperty().addListener(validationListener);
        endTimeField.textProperty().addListener(validationListener);
        datePicker.valueProperty().addListener((obs, oldV, newV) -> {
            boolean isValid = newV != null &&
                    !startTimeField.getText().trim().isEmpty() &&
                    !endTimeField.getText().trim().isEmpty();
            saveButton.setDisable(!isValid);
        });

        dialog.setResultConverter(button -> {
            if (button == saveBtn) {
                // Ensure the date, start time, and end time are valid
                if (datePicker.getValue() == null || startTimeField.getText().isEmpty() || endTimeField.getText().isEmpty()) {
                    new Alert(Alert.AlertType.ERROR, "Date, Start Time, and End Time must be provided").show();
                    return null;
                }

                long duration;
                try {
                    duration = calculateDuration(startTimeField.getText(), endTimeField.getText());
                } catch (IllegalArgumentException e) {
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                    return null; // Stop the process if there's an error
                }

                if (duration <= 0) {
                    new Alert(Alert.AlertType.ERROR, "End time must be later than start time.").show();
                    return null; // Stop the process if duration is invalid
                }

                // Combine the selected date with the start and end times
                String startDateTime = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + startTimeField.getText();
                String endDateTime = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + endTimeField.getText();

                // Return the manual study session with calculated duration
                return new StudySession(0, course.getId(),
                        startDateTime, endDateTime,
                        (int) duration,  // Get the calculated duration
                        notesArea.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(session -> {
            StudySessionDAO.insert(session); // Insert into the database
            loadSessions();  // Reload the sessions table
        });
    }

    private long calculateDuration(String start, String end) {
        try {
            // Use DateTimeFormatter to support AM/PM format (hh:mm a)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");

            // Parse the start and end times into LocalTime objects
            LocalTime startTime = LocalTime.parse(start.trim(), formatter);
            LocalTime endTime = LocalTime.parse(end.trim(), formatter);

            // Calculate the duration in minutes
            long duration = Duration.between(startTime, endTime).toMinutes();

            return duration;

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format. Please use 'hh:mm AM/PM'.");
        }
    }
}
