package com.example.subui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MQTTSubApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        FXMLLoader fxmlLoader = new FXMLLoader(MQTTSubApplication.class.getResource("/com/example/subui/startup-prompt.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        stage.setTitle("Ứng dụng theo dõi Smart Home");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}