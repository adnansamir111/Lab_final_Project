package com.example.javaproject.CONTROLLER;

import com.example.javaproject.all_class.Course;
import com.example.javaproject.DAO.CourseDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import java.io.IOException;
import java.util.List;

public class GradeSheetController {
    Scene parent;

    @FXML private TableView<Course> tableGrade;

    @FXML private TableColumn<Course, String>  colCourseId;

    @FXML private TableColumn<Course, Double> colQuiz1;
    @FXML private TableColumn<Course, Double> colQuiz2;
    @FXML private TableColumn<Course, Double> colQuiz3;
    @FXML private TableColumn<Course, Double> colQuiz4;
    @FXML private TableColumn<Course, Double> colMid;
    @FXML private TableColumn<Course, Double> colFinal;

    @FXML private Button btnSave;
    @FXML private Button btnBack;

    private final ObservableList<Course> data = FXCollections.observableArrayList();

    public void setParent(Scene parent) {
        this.parent = parent;
    }

//    @FXML
//    private void initialize() {
//        // Show course CODE in the first column (acts as visual "Course ID")
//        colCourseId.setCellValueFactory(cd -> cd.getValue().codeProperty());
//
//        colQuiz1.setCellValueFactory(cd -> cd.getValue().quiz1Property().asObject());
//        colQuiz2.setCellValueFactory(cd -> cd.getValue().quiz2Property().asObject());
//        colQuiz3.setCellValueFactory(cd -> cd.getValue().quiz3Property().asObject());
//        colQuiz4.setCellValueFactory(cd -> cd.getValue().quiz4Property().asObject());
//        colMid.setCellValueFactory(  cd -> cd.getValue().midProperty().asObject());
//        colFinal.setCellValueFactory(cd -> cd.getValue().finProperty().asObject());
//
//        // Editable numeric cells
//        tableGrade.setEditable(true);
//        DoubleStringConverter dsc = new DoubleStringConverter();
//
//        colQuiz1.setCellFactory(TextFieldTableCell.forTableColumn(dsc));
//        colQuiz2.setCellFactory(TextFieldTableCell.forTableColumn(dsc));
//        colQuiz3.setCellFactory(TextFieldTableCell.forTableColumn(dsc));
//        colQuiz4.setCellFactory(TextFieldTableCell.forTableColumn(dsc));
//        colMid.setCellFactory(  TextFieldTableCell.forTableColumn(dsc));
//        colFinal.setCellFactory(TextFieldTableCell.forTableColumn(dsc));
//
//        // Persist immediately on edit
//        colQuiz1.setOnEditCommit(e -> commitAndSave(e.getRowValue(), () -> e.getRowValue().setQuiz1(safe(e.getNewValue()))));
//        colQuiz2.setOnEditCommit(e -> commitAndSave(e.getRowValue(), () -> e.getRowValue().setQuiz2(safe(e.getNewValue()))));
//        colQuiz3.setOnEditCommit(e -> commitAndSave(e.getRowValue(), () -> e.getRowValue().setQuiz3(safe(e.getNewValue()))));
//        colQuiz4.setOnEditCommit(e -> commitAndSave(e.getRowValue(), () -> e.getRowValue().setQuiz4(safe(e.getNewValue()))));
//        colMid.setOnEditCommit(  e -> commitAndSave(e.getRowValue(), () -> e.getRowValue().setMid(safe(e.getNewValue()))));
//        colFinal.setOnEditCommit(e -> commitAndSave(e.getRowValue(), () -> e.getRowValue().setFin(safe(e.getNewValue()))));
//
//        reload();
//    }
@FXML
private void initialize() {
    // Show course CODE in the first column (acts as visual "Course ID")
    colCourseId.setCellValueFactory(cd -> cd.getValue().codeProperty());

    colQuiz1.setCellValueFactory(cd -> cd.getValue().quiz1Property().asObject());
    colQuiz2.setCellValueFactory(cd -> cd.getValue().quiz2Property().asObject());
    colQuiz3.setCellValueFactory(cd -> cd.getValue().quiz3Property().asObject());
    colQuiz4.setCellValueFactory(cd -> cd.getValue().quiz4Property().asObject());
    colMid.setCellValueFactory(cd -> cd.getValue().midProperty().asObject());
    colFinal.setCellValueFactory(cd -> cd.getValue().finProperty().asObject());

    // Editable numeric cells
    tableGrade.setEditable(true);
    DoubleStringConverter dsc = new DoubleStringConverter();

    // Set cell factory with bold effect on number input
    colQuiz1.setCellFactory(param -> new TextFieldTableCell<Course, Double>(dsc) {
        @Override
        public void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (item != null && item != 0.0) {
                    setStyle("-fx-font-weight: bold;");
                } else {
                    setStyle("-fx-font-weight: normal;");
                }
            }
        }
    });
    colQuiz2.setCellFactory(param -> new TextFieldTableCell<Course, Double>(dsc) {
        @Override
        public void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (item != null && item != 0.0) {
                    setStyle("-fx-font-weight: bold;");
                } else {
                    setStyle("-fx-font-weight: normal;");
                }
            }
        }
    });
    colQuiz3.setCellFactory(param -> new TextFieldTableCell<Course, Double>(dsc) {
        @Override
        public void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (item != null && item != 0.0) {
                    setStyle("-fx-font-weight: bold;");
                } else {
                    setStyle("-fx-font-weight: normal;");
                }
            }
        }
    });
    colQuiz4.setCellFactory(param -> new TextFieldTableCell<Course, Double>(dsc) {
        @Override
        public void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (item != null && item != 0.0) {
                    setStyle("-fx-font-weight: bold;");
                } else {
                    setStyle("-fx-font-weight: normal;");
                }
            }
        }
    });
    colMid.setCellFactory(param -> new TextFieldTableCell<Course, Double>(dsc) {
        @Override
        public void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (item != null&& item!=0.0) {
                    setStyle("-fx-font-weight: bold;");
                } else {
                    setStyle("-fx-font-weight: normal;");
                }
            }
        }
    });
    colFinal.setCellFactory(param -> new TextFieldTableCell<Course, Double>(dsc) {
        @Override
        public void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (item != null && item != 0.0) {
                    setStyle("-fx-font-weight: bold;");
                } else {
                    setStyle("-fx-font-weight: normal;");
                }
            }
        }
    });

    // Persist immediately on edit
    colQuiz1.setOnEditCommit(e -> commitAndSave(e.getRowValue(), () -> e.getRowValue().setQuiz1(safe(e.getNewValue()))));
    colQuiz2.setOnEditCommit(e -> commitAndSave(e.getRowValue(), () -> e.getRowValue().setQuiz2(safe(e.getNewValue()))));
    colQuiz3.setOnEditCommit(e -> commitAndSave(e.getRowValue(), () -> e.getRowValue().setQuiz3(safe(e.getNewValue()))));
    colQuiz4.setOnEditCommit(e -> commitAndSave(e.getRowValue(), () -> e.getRowValue().setQuiz4(safe(e.getNewValue()))));
    colMid.setOnEditCommit(e -> commitAndSave(e.getRowValue(), () -> e.getRowValue().setMid(safe(e.getNewValue()))));
    colFinal.setOnEditCommit(e -> commitAndSave(e.getRowValue(), () -> e.getRowValue().setFin(safe(e.getNewValue()))));

    reload();
}


    private double safe(Double v) { return v == null ? 0.0 : v; }

    private void commitAndSave(Course c, Runnable applyChange) {
        applyChange.run();
        CourseDAO.updateMarks(
                c.getId(),
                c.getQuiz1(),
                c.getQuiz2(),
                c.getQuiz3(),
                c.getQuiz4(),
                c.getMid(),
                c.getFin()
        );
        tableGrade.refresh();
    }

    @FXML
    private void onSave() {
        for (Course c : data) {
            CourseDAO.updateMarks(
                    c.getId(),
                    c.getQuiz1(),
                    c.getQuiz2(),
                    c.getQuiz3(),
                    c.getQuiz4(),
                    c.getMid(),
                    c.getFin()
            );
        }
    }

    @FXML
    private void onBack() throws IOException {
        Stage stage = (Stage) tableGrade.getScene().getWindow();
        //Parent root = FXMLLoader.load(parent);
        double width = stage.getWidth();
        double height = stage.getHeight();
        stage.setScene(parent);
        stage.setWidth(width);
        stage.setHeight(height);
        //stage.show();
    }

    public void reload() {
        List<Course> all = CourseDAO.listAll();
        data.setAll(all);
        tableGrade.setItems(data);
    }
}
