package com.example.subui;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ItemLabelController {
    @FXML
    private Text mqttTopic;
    @FXML
    private Text mqttSensor;
    @FXML
    private Text mqttLocation;

    public void setMqttTopic(String topic) {
        this.mqttTopic.setText(topic);
    }

    public void setMqttSensor(String sensor) {
        this.mqttSensor.setText(sensor);
    }

    public void setMqttLocation(String location) {
        this.mqttLocation.setText(location);
    }
}
