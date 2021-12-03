package com.example.subui;

import com.example.subui.services.MQTTSubscriber;
import javafx.fxml.FXML;

public class MainUiController {
    @FXML
    private TabVisualizedController tabVisualizedController;
    @FXML
    private TabTechnicalController tabTechnicalController;


    public void init(String address, String port, String[] topics) {
        MQTTSubscriber subscriber = new MQTTSubscriber(address, port, topics);
        tabVisualizedController.setSubscriber(subscriber);
        tabTechnicalController.setSubscriber(subscriber);
        new Thread(subscriber).start();
    }


}
