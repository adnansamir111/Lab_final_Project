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

        // Get the controller for the Dashboard
        DashboardController dashboardController = fxmlLoader.getController();

        // Inject HostServices into the DashboardController
        dashboardController.setHostServices(getHostServices());

        stage.setTitle("Study_Buddy");
        stage.setScene(scene);
        //stage.centerOnScreen();
        stage.setMaximized(true);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}