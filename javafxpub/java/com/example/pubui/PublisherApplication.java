package com.example.pubui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class PublisherApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PublisherApplication.class.getResource("publisher-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 407);
        PublisherController controller = fxmlLoader.getController();
        controller.init();
        scene.getStylesheets().add("style.css");
        stage.setTitle("MQTT Publisher");
        stage.setScene(scene);
        stage.show();
        stage.setOnHiding(event -> {
            System.exit(0);
        });

        FXMLLoader serverPortLoader =
                new FXMLLoader(PublisherApplication.class.getResource("server-port-view.fxml"));
        Stage enterBindAddress = new Stage();
        enterBindAddress.setScene(new Scene(serverPortLoader.load(), 460, 225));
        enterBindAddress.setTitle("Bind address");
        enterBindAddress.initModality(Modality.APPLICATION_MODAL);
        enterBindAddress.setResizable(false);
        enterBindAddress.show();
        enterBindAddress.setOnHiding(
                event -> {
                    ServerPortController serverPortController = serverPortLoader.getController();
                    controller.setPortAndAddress(serverPortController.server, serverPortController.port);

                });
    }

    public static void main(String[] args) {
        launch();
    }
}