package com.example.subui;

import com.example.subui.services.MQTTSubscriber;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

public class TabVisualizedController {
    @FXML
    private FlowPane container;

    private MQTTSubscriber subscriber = null;

    public void setSubscriber(MQTTSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    @FXML
    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),
                ae -> {
                    if (subscriber == null) return;
                    subscriber.update();
                    container.getChildren().clear();
                    for (var item : subscriber.getMessageMap().values()) {
                        container.getChildren().add(item.getNode());
                    }
                }
        ));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
