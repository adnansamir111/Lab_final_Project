package com.example.javaproject;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    // NEW: expose HostServices for controllers
    private static HostServices hostServicesRef;

    public static HostServices getAppHostServices() {
        return hostServicesRef;
    }

    @Override
    public void start(Stage stage) throws IOException {
        // capture HostServices once app starts
        hostServicesRef = getHostServices();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Dashboard.fxml"));
        ///FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("CourseView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
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
