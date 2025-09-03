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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsController {

    @FXML private BarChart<String, Number> studyBarChart;
    @FXML private DatePicker weekStartPicker;
    @FXML private ComboBox<Course> courseComboBox;

    @FXML
    public void initialize() {
        // Set current Monday as default week start
        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
        weekStartPicker.setValue(monday);

        // Load courses (includes an "ALL" sentinel)
        loadCourses();

        // Refresh when week changes
        weekStartPicker.valueProperty().addListener((obs, oldVal, newVal) -> reloadCurrentWeek());

        // 🔧 Refresh when course selection changes (this was previously commented out)
        courseComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> reloadCurrentWeek());

        // Initial chart
        reloadCurrentWeek();
    }

    @FXML
    private void handleThisWeek() {
        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
        weekStartPicker.setValue(monday);
        reloadCurrentWeek();
    }

    @FXML
    private void handleLastWeek() {
        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY).minusWeeks(1);
        weekStartPicker.setValue(monday);
        reloadCurrentWeek();
    }

    private void reloadCurrentWeek() {
        LocalDate start = weekStartPicker.getValue();
        if (start == null) {
            start = LocalDate.now().with(DayOfWeek.MONDAY);
            weekStartPicker.setValue(start);
        }
        loadChartForWeek(start, start.plusDays(6));
    }

    /**
     * Build the weekly bar chart; hours on Y, ISO dates on X.
     */
    private void loadChartForWeek(LocalDate start, LocalDate end) {
        // Choose data source: all courses vs a specific course
        Map<String, Integer> raw; // minutes per date string "YYYY-MM-DD"
        Course selected = courseComboBox.getValue();

        if (selected != null && selected.getId() > 0) {
            raw = StudySessionDAO.getWeeklyStudyTimeForCourse(selected.getId(), start, end);
        } else {
            raw = StudySessionDAO.getWeeklyStudyTime(start, end);
        }
        if (raw == null) raw = new LinkedHashMap<>();

        // Ensure all seven days exist (fill missing with 0)
        Map<String, Integer> complete = new LinkedHashMap<>();
        List<String> categories = new ArrayList<>(7);
        LocalDate d = start;
        while (!d.isAfter(end)) {
            String key = d.toString(); // must match DAO keys
            complete.put(key, raw.getOrDefault(key, 0));
            categories.add(key);
            d = d.plusDays(1);
        }

        // Prepare series (convert minutes -> fractional hours)
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        double maxHours = 0.0;
        for (Map.Entry<String, Integer> e : complete.entrySet()) {
            double hours = e.getValue() / 60.0;
            series.getData().add(new XYChart.Data<>(e.getKey(), hours));
            if (hours > maxHours) maxHours = hours;
        }

        // Update chart
        studyBarChart.getData().clear();
        studyBarChart.getData().add(series);

        // Fix Y-axis bounds (disable auto-range so our bounds apply)
        NumberAxis yAxis = (NumberAxis) studyBarChart.getYAxis();
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        double upper = Math.max(1.0, maxHours * 1.2); // 20% headroom, at least 1
        yAxis.setUpperBound(upper);
        yAxis.setTickUnit(Math.max(0.2, upper / 5));

        // X-axis ordered categories for the week
        CategoryAxis xAxis = (CategoryAxis) studyBarChart.getXAxis();
        xAxis.setCategories(FXCollections.observableArrayList(categories));
        xAxis.setLabel("Date");
        yAxis.setLabel("Hours");

        // Dynamic title (shows course if filtered)
        String titleSuffix = (selected != null && selected.getId() > 0)
                ? " — " + selected.getCode() + " (" + selected.getTitle() + ")"
                : " — All Courses";
        studyBarChart.setTitle(String.format("Weekly Study Time (Hours) — %s to %s%s", start, end, titleSuffix));
    }

    /**
     * Populate the course dropdown with "ALL" and actual courses.
     */
    private void loadCourses() {
        ObservableList<Course> items = FXCollections.observableArrayList();
        // Sentinel: id = -1 means ALL
        items.add(new Course(-1, "ALL", "All Courses", "", 0));
        items.addAll(CourseDAO.listAll());
        courseComboBox.setItems(items);
        courseComboBox.getSelectionModel().selectFirst();
    }

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
