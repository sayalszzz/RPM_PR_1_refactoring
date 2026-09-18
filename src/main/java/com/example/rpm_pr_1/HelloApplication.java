package com.example.rpm_pr_1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.rpm_pr_1.controller.GradeController;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 860, 740);
        GradeController controller = fxmlLoader.getController();
        stage.setTitle("Учёт студентов и успеваемости");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> controller.shutdown());
        stage.show();
    }
}
