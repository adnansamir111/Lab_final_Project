package com.example.javaproject;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Dashboard.fxml"));
        ///FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("CourseView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Study_Buddy");
        stage.setScene(scene);
        //stage.centerOnScreen();g
        stage.setMaximized(true);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}