package com.example.javaproject;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.time.LocalDate;
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
}
