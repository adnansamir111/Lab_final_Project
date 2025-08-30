package com.example.javaproject;

import com.example.javaproject.all_class.Course;
import com.example.javaproject.all_class.CourseDAO;
import com.example.javaproject.all_class.StudySessionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller for the analytics view. This controller is responsible for
 * generating a weekly bar chart that shows how many minutes were spent
 * studying each course in the selected week. It provides convenience
 * buttons to quickly jump to the current or previous week and exposes
 * a {@link DatePicker} so users can choose any Monday as the starting
 * point for their report. The bar chart will automatically resize its
 * y‑axis based on the largest value in the dataset to ensure that bars
 * never truncate when study sessions exceed the default range.
 */
public class AnalyticsController {

    @FXML
    private BarChart<String, Number> studyBarChart;
    @FXML
    private DatePicker weekStartPicker;
    @FXML
    private ComboBox<Course> courseComboBox;

    /**
     * Initialise the controller by selecting the current week, loading
     * available courses and registering listeners. This method is called
     * automatically after the FXML file is loaded.
     */
    @FXML
    public void initialize() {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        weekStartPicker.setValue(monday);

        // Populate the course list with a dummy "ALL" entry and real courses.
        loadCourses();

        // Remove automatic refresh when a course is selected
        // courseComboBox.getSelectionModel().selectedItemProperty().addListener((obs,
        // oldVal, newVal) -> handleRefresh());

        // Load the chart for this week by default.
        loadChartForWeek(monday, monday.plusDays(6));
    }

    @FXML
    private void handleThisWeek() {
        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
        weekStartPicker.setValue(monday);
        loadChartForWeek(monday, monday.plusDays(6));
    }

    @FXML
    private void handleLastWeek() {
        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY).minusWeeks(1);
        weekStartPicker.setValue(monday);
        loadChartForWeek(monday, monday.plusDays(6));
    }

    // @FXML
    // private void handleRefresh() {
    // LocalDate start = weekStartPicker.getValue();
    // if (start == null) {
    // start = LocalDate.now().with(DayOfWeek.MONDAY);
    // weekStartPicker.setValue(start);
    // }
    // loadChartForWeek(start, start.plusDays(6));
    // }

    /**
     * Query the database and build a chart for the selected week.
     * If a specific course is selected, filter the results; otherwise,
     * display totals for all courses.
     *
     * @param start start of the week (inclusive)
     * @param end   end of the week (inclusive)
     */
    private void loadChartForWeek(LocalDate start, LocalDate end) {
        Map<String, Integer> data;
        Course selectedCourse = courseComboBox.getValue();

        if (selectedCourse != null && selectedCourse.getId() > 0) {
            // Filter by a specific course
            data = StudySessionDAO.getWeeklyStudyTimeForCourse(selectedCourse.getId(), start, end);
        } else {
            // Aggregate all courses
            data = StudySessionDAO.getWeeklyStudyTime(start, end);
        }

        // Ensure all days in the week are represented
        Map<String, Integer> completeData = new LinkedHashMap<>();
        LocalDate current = start;
        while (!current.isAfter(end)) {
            String dateStr = current.toString();
            completeData.put(dateStr, data.getOrDefault(dateStr, 0));
            current = current.plusDays(1);
        }

        // Clear and populate the bar chart
        studyBarChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        double maxMinutes = 0;

        // Convert minutes to fractional hours (use decimal points)
        for (Map.Entry<String, Integer> entry : completeData.entrySet()) {
            double hours = entry.getValue() / 60.0; // Fractional hours to handle small sessions
            series.getData().add(new XYChart.Data<>(entry.getKey(), hours));
            if (hours > maxMinutes) {
                maxMinutes = hours;
            }
        }

        studyBarChart.getData().add(series);

        // Adjust y‑axis range dynamically based on the maximum value
        NumberAxis yAxis = (NumberAxis) studyBarChart.getYAxis();
        double upperBound = maxMinutes * 1.2; // Add 20% padding to the max value
        if (upperBound < 1) {
            upperBound = 1; // Minimum upper bound
        }
        yAxis.setUpperBound(upperBound);
        yAxis.setTickUnit(upperBound / 5); // Adjust tick unit dynamically

        // Set axis labels and chart title
        CategoryAxis xAxis = (CategoryAxis) studyBarChart.getXAxis();
        xAxis.setLabel("Date");
        yAxis.setLabel("Hours");
        studyBarChart.setTitle(String.format("Weekly Study Time (Hours) — %s to %s", start, end));
    }

    /**
     * Populate the course dropdown with "ALL" and the actual courses from the
     * database.
     */
    private void loadCourses() {
        ObservableList<Course> items = FXCollections.observableArrayList();
        // Dummy course with id -1 indicates all courses
        items.add(new Course(-1, "ALL", "All Courses", "", 0));
        items.addAll(CourseDAO.listAll());
        courseComboBox.setItems(items);
        courseComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Navigate back to the dashboard.
     */
    @FXML
    private void handleBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
            Stage stage = (Stage) studyBarChart.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
