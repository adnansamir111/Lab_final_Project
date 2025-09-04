package com.example.javaproject.CONTROLLER;

import com.example.javaproject.DAO.CourseDAO;
import com.example.javaproject.all_class.Task;
import com.example.javaproject.DAO.TaskDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class TaskExplorerController {

    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, String> colTitle;
    @FXML private TableColumn<Task, String> colDueDate;
    @FXML private TableColumn<Task, String> colStatus;
    @FXML private TableColumn<Task, String> colCourse;
    @FXML private TableColumn<Task, String> colNotes;

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<String> courseComboBox;
    @FXML private ComboBox<String> dateRangeComboBox;

    @FXML private VBox customRangeBox; // Custom date range (from and to) VBox

    // Called when page loads
    @FXML
    public void initialize() {
        // Set up columns
        colTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        colDueDate.setCellValueFactory(cellData -> cellData.getValue().dueAtProperty());
        colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        colCourse.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseName()));
        colNotes.setCellValueFactory(cellData -> cellData.getValue().notesProperty());

        // Load courses into ComboBox (add "All Courses" option)
        loadCourses();

        // Default view: show all tasks
        loadAllTasks();

        // Initialize custom range box as hidden initially
        customRangeBox.setVisible(false);
        startDatePicker.setDisable(true);
        endDatePicker.setDisable(true);

        // Set up listener for ComboBox
        dateRangeComboBox.setOnAction(e -> handleDateRangeSelection());

        // Row Styling for Status Colors
        taskTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);

                // Reset the style of the row
                getStyleClass().removeAll("task-done", "task-pending", "task-missed");

                if (item != null && !empty) {
                    // Apply color based on status
                    if ("done".equalsIgnoreCase(item.getStatus())) {
                        getStyleClass().add("task-done");
                    } else if ("todo".equalsIgnoreCase(item.getStatus())) {
                        getStyleClass().add("task-pending");
                    } else if ("missed".equalsIgnoreCase(item.getStatus())) {
                        getStyleClass().add("task-missed");
                    }else if ("in-progress".equalsIgnoreCase(item.getStatus())) {
                        getStyleClass().add("task-pending");
                    }
                }
            }
        });
    }

    private void loadCourses() {
        List<String> courses = CourseDAO.getAllCourseNames();
        courseComboBox.setItems(FXCollections.observableArrayList(courses));
        courseComboBox.getItems().add(0, "All Courses");  // Add default option "All Courses"
        courseComboBox.setValue("All Courses");  // Default selection
    }

    private void loadAllTasks() {
        List<Task> tasks = TaskDAO.listAll();
        taskTable.setItems(FXCollections.observableArrayList(tasks));
    }

    @FXML
    private void handleThisWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        // If course is selected, filter tasks by course and date range
        String selectedCourse = courseComboBox.getValue();
        if (!"All Courses".equals(selectedCourse)) {
            int courseId = CourseDAO.getCourseIdByCode(selectedCourse);
            List<Task> tasks = TaskDAO.listByCourseAndDateRange(courseId, startOfWeek.toString(), endOfWeek.toString());
            taskTable.setItems(FXCollections.observableArrayList(tasks));
        } else {
            List<Task> tasks = TaskDAO.listByDateRange(startOfWeek.toString(), endOfWeek.toString());
            taskTable.setItems(FXCollections.observableArrayList(tasks));
        }
    }

    @FXML
    private void handleNextWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfNextWeek = today.plusWeeks(1).with(DayOfWeek.MONDAY);
        LocalDate endOfNextWeek = startOfNextWeek.with(DayOfWeek.SUNDAY);

        // If course is selected, filter tasks by course and date range
        String selectedCourse = courseComboBox.getValue();
        if (!"All Courses".equals(selectedCourse)) {
            int courseId = CourseDAO.getCourseIdByCode(selectedCourse);
            List<Task> tasks = TaskDAO.listByCourseAndDateRange(courseId, startOfNextWeek.toString(), endOfNextWeek.toString());
            taskTable.setItems(FXCollections.observableArrayList(tasks));
        } else {
            List<Task> tasks = TaskDAO.listByDateRange(startOfNextWeek.toString(), endOfNextWeek.toString());
            taskTable.setItems(FXCollections.observableArrayList(tasks));
        }
    }

    @FXML
    private void handleClearFilters() {
        courseComboBox.setValue("All Courses");
        loadAllTasks();  // Clear filters and load all tasks
        dateRangeComboBox.setValue(null);
        customRangeBox.setVisible(false);  // Hide custom range date pickers
        startDatePicker.setDisable(true);  // Disable custom range pickers
        endDatePicker.setDisable(true);
    }

    @FXML
    private void handleDateRangeSelection() {
        String selectedRange = dateRangeComboBox.getValue();

        if ("Custom Range".equals(selectedRange)) {
            customRangeBox.setVisible(true);
            startDatePicker.setDisable(false);  // Enable custom date range pickers
            endDatePicker.setDisable(false);
        } else {
            customRangeBox.setVisible(false);
            startDatePicker.setDisable(true);  // Disable custom range pickers
            endDatePicker.setDisable(true);
        }
    }
    @FXML
    private void handleSearchByDateRange() {
        String selectedRange = dateRangeComboBox.getValue();
        LocalDate start = null;
        LocalDate end = null;

        // Get the selected course for filtering by course
        String selectedCourse = courseComboBox.getValue();
        int courseId = -1;
        if (!"All Courses".equals(selectedCourse)) {
            courseId = CourseDAO.getCourseIdByCode(selectedCourse);  // Get course ID if a specific course is selected
        }

        if ("Custom Range".equals(selectedRange)) {
            start = startDatePicker.getValue();
            end = endDatePicker.getValue();
            if (start != null && end != null && !start.isAfter(end)) {
                // Filter tasks by both course and date range
                if (courseId != -1) {
                    List<Task> tasks = TaskDAO.listByCourseAndDateRange(courseId, start.toString(), end.toString());
                    taskTable.setItems(FXCollections.observableArrayList(tasks));
                } else {
                    List<Task> tasks = TaskDAO.listByDateRange(start.toString(), end.toString());
                    taskTable.setItems(FXCollections.observableArrayList(tasks));
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a valid date range").showAndWait();
            }
        } else if ("This Week".equals(selectedRange)) {
            handleThisWeek();
        } else if ("Next Week".equals(selectedRange)) {
            handleNextWeek();
        }
    }

    @FXML
    private void handleSearchByCourse() {
        String selectedCourse = courseComboBox.getValue();
        int courseId = -1;
        if (!"All Courses".equals(selectedCourse)) {
            courseId = CourseDAO.getCourseIdByCode(selectedCourse);  // Get course ID if a specific course is selected
        }

        String selectedRange = dateRangeComboBox.getValue();
        LocalDate start = null;
        LocalDate end = null;

        // Handle course and date range filtering
        if ("Custom Range".equals(selectedRange)) {
            start = startDatePicker.getValue();
            end = endDatePicker.getValue();
            if (start != null && end != null && !start.isAfter(end)) {
                // Filter tasks by both course and date range
                if (courseId != -1) {
                    List<Task> tasks = TaskDAO.listByCourseAndDateRange(courseId, start.toString(), end.toString());
                    taskTable.setItems(FXCollections.observableArrayList(tasks));
                } else {
                    List<Task> tasks = TaskDAO.listByDateRange(start.toString(), end.toString());
                    taskTable.setItems(FXCollections.observableArrayList(tasks));
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a valid date range").showAndWait();
            }
        } else {
            if (courseId != -1) {
                List<Task> tasks = TaskDAO.listByCourse(courseId);  // Filter by course only
                taskTable.setItems(FXCollections.observableArrayList(tasks));
            } else {
                loadAllTasks();  // Load all tasks if no course is selected
            }
        }
    }


    @FXML
    void handleback(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaproject/Dashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) customRangeBox.getScene().getWindow();
            stage.setMaximized(true);
            stage.setScene(new Scene(root));
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
