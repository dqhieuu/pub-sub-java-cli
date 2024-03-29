package com.example.subui;

import com.example.subui.services.MQTTSubscriber;
import javafx.fxml.FXML;

public class MainUiController {
    @FXML
    private TabVisualizedController tabVisualizedController;
    @FXML
    private TabTechnicalController tabTechnicalController;
    @FXML
    private TabTopicsController tabTopicsController;

    public void init(String address, String port, String[] topics) {
        MQTTSubscriber subscriber = new MQTTSubscriber(address, port, topics);
        tabVisualizedController.setSubscriber(subscriber);
        tabTechnicalController.setSubscriber(subscriber);
        tabTopicsController.setSubscriber(subscriber);
        tabTopicsController.setTopics(topics);
        new Thread(subscriber).start();
    }
}
