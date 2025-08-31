package com.example.javaproject;

import com.example.javaproject.all_class.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class CourseDetailController {

    // ====== Top labels ======
    @FXML private Label lblCourseTitle;
    @FXML private Label lblInstructor;
    @FXML private Label lblCredits;

    // ====== Google Classroom button ======
    @FXML private Button btnClassroom;

    // ====== Tasks tab ======
    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, String> colTitle;
    @FXML private TableColumn<Task, String> colDue;
    @FXML private TableColumn<Task, String> colStatus;
    @FXML private TableColumn<Task, String> colNotes;

    // ====== Study sessions tab ======
    @FXML private Label lblStopwatch;
    @FXML private TableView<StudySession> sessionTable;
    @FXML private TableColumn<StudySession, String> colStart;
    @FXML private TableColumn<StudySession, String> colEnd;
    @FXML private TableColumn<StudySession, String> colDuration;
    @FXML private TableColumn<StudySession, String> colTopic;

    // ====== State ======
    private Course course;

    // ====== Initialize (keep your existing setup here) ======
    @FXML
    public void initialize() {
        // If your original file sets up columns/listeners, keep that here.
        // Examples (uncomment/adjust to match your original):
        // colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        // colDue.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        // colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        // colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
        //
        // colStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        // colEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        // colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        // colTopic.setCellValueFactory(new PropertyValueFactory<>("topic"));
    }

    // ====== Called by CoursesController when opening ======
    public void setCourse(Course course) {
        this.course = course;
        lblCourseTitle.setText(course.getCode() + " – " + course.getTitle());
        lblInstructor.setText("👨‍🏫 " + course.getInstructor());
        lblCredits.setText("🎓 Credits: " + course.getCredits());

        loadTasks();
        loadSessions();
    }

    // ====== Navigation ======
    @FXML
    private void onBack() {
        try {
            Stage stage = (Stage) lblCourseTitle.getScene().getWindow();
            stage.getScene().setRoot(
                    FXMLLoader.load(getClass().getResource("CourseView.fxml"))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ====== Delete Course (existing) ======
    @FXML
    private void onDelete() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete this course?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Confirm Delete");
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES && course != null) {
                CourseDAO.delete(course.getId());
                try {
                    Stage stage = (Stage) lblCourseTitle.getScene().getWindow();
                    stage.getScene().setRoot(
                            FXMLLoader.load(getClass().getResource("CourseView.fxml"))
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // ====== Google Classroom handler (uses HostServices) ======
    @FXML
    private void onClassroomClick() {
        if (course == null) return;
        String current = course.getClassroomUrl();

        try {
            if (current != null && !current.isBlank()) {
                // Open existing link via JavaFX HostServices
                MainApplication.getAppHostServices().showDocument(current.trim());
                return;
            }

            // No link set -> open dialog
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("ClassroomLinkDialog.fxml"));
            Parent root = loader.load();
            ClassroomLinkDialogController ctrl = loader.getController();
            ctrl.setInitialUrl(current);

            Stage dialog = new Stage();
            dialog.setTitle("Set Google Classroom Link");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(((Node) btnClassroom).getScene().getWindow());
            dialog.setScene(new Scene(root));
            dialog.setResizable(false);
            dialog.showAndWait();

            ctrl.getResult().ifPresent(url -> {
                // Persist in DB and update model
                CourseDAO.updateClassroomUrl(course.getId(), url);
                course.setClassroomUrl(url);
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR,
                    "Unexpected error: " + ex.getMessage(),
                    ButtonType.OK);
            a.showAndWait();
        }
    }

    // ====== Tasks logic (replace with your original implementations) ======
    @FXML private void onAddTask()    { /* TODO: your existing add-task logic */ }
    @FXML private void onEditTask()   { /* TODO: your existing edit-task logic */ }
    @FXML private void onDeleteTask() { /* TODO: your existing delete-task logic */ }

    // ActionEvent overloads so FXML handler resolution always works
    @FXML private void onAddTask(ActionEvent e)    { onAddTask(); }
    @FXML private void onEditTask(ActionEvent e)   { onEditTask(); }
    @FXML private void onDeleteTask(ActionEvent e) { onDeleteTask(); }

    // ====== Study sessions logic (replace with your original implementations) ======
    @FXML private void onStart()       { /* TODO: start stopwatch/session */ }
    @FXML private void onStop()        { /* TODO: stop stopwatch/session */ }
    @FXML private void onDeleteSession(){ /* TODO: delete selected session */ }

    // ActionEvent overloads for Study Sessions
    @FXML private void onStart(ActionEvent e)        { onStart(); }
    @FXML private void onStop(ActionEvent e)         { onStop(); }
    @FXML private void onDeleteSession(ActionEvent e){ onDeleteSession(); }

    // ====== Data loaders (keep originals) ======
    private void loadTasks() {
        // your existing task loading logic...
    }

    private void loadSessions() {
        // your existing session loading logic...
    }
}
