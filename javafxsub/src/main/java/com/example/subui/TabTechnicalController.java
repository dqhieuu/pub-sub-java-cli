package com.example.subui;

import com.example.subui.models.TopicMsg;
import com.example.subui.models.TopicMsgTable;
import com.example.subui.services.MQTTSubscriber;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;


public class TabTechnicalController {
    public TableColumn<TopicMsgTable, String> topic;
    public TableColumn<TopicMsgTable, String> lastUpdate;
    public TableColumn<TopicMsgTable, String> value;

    @FXML
    private TableView<TopicMsgTable> table;

    private MQTTSubscriber subscriber = null;

    public void setSubscriber(MQTTSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    @FXML
    public void initialize() {
        topic.setCellValueFactory(new PropertyValueFactory<>("sensorDetail"));
        lastUpdate.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        value.setCellValueFactory(new PropertyValueFactory<>("msg"));

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),
                ae -> {
                    if (subscriber == null) return;
                    ObservableList<TopicMsgTable> data = FXCollections.observableArrayList();
                    for (var item : subscriber.getMessageMap().values()) {
                        data.addAll(item.getTopicMsg().toTableType());
                    }
                    table.setItems(data);
                }
        ));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
