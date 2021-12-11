package com.example.pubui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PublisherDetailController {
    Publisher connection;

    @FXML
    private CheckBox activeThread;

    @FXML
    private Label topicName;

    @FXML
    private Label locationName;

    @FXML
    private Label sensorName;

    @FXML
    private Label emitFuncName;

    @FXML
    private Label frequencyName;

    @FXML
    private Button discardButton;

    @FXML
    protected void flipThread() {
        if(!activeThread.isSelected()) {
            connection.pause();
        } else {
            connection.resume();
        }
    }

    @FXML
    protected void onDiscard() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PublisherApplication.class.getResource("confirm-delete-view.fxml"));
        Stage confirmDeleteStage = new Stage();
        confirmDeleteStage.setScene(new Scene(fxmlLoader.load(), 121, 82));
        confirmDeleteStage.initModality(Modality.APPLICATION_MODAL);
        confirmDeleteStage.setResizable(false);
        confirmDeleteStage.show();
        confirmDeleteStage.setOnHidden(event -> {
            ConfirmDeleteController controller = fxmlLoader.getController();
            if (controller.delete) {
                Pane parent = (Pane) discardButton.getParent().getParent().getParent().getParent();
                parent.getChildren().remove(discardButton.getParent().getParent().getParent());
                connection.stop();
            }
        });
    }

    public void init(PublisherParams params, Publisher conn) {
        activeThread.setSelected(true);
        topicName.setText(params.topic);
        locationName.setText(params.location);
        sensorName.setText(params.sensor);
        emitFuncName.setText(params.emitFunc.toString());
        frequencyName.setText(String.valueOf(params.frequency));
        connection = conn;
    }
}
