package com.example.javaproject;



import com.example.javaproject.CONTROLLER.DashboardController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Dashboard.fxml"));

        Scene scene = new Scene(fxmlLoader.load());


        DashboardController dashboardController = fxmlLoader.getController();

        /// Inject HostServices into the DashboardController for using links
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