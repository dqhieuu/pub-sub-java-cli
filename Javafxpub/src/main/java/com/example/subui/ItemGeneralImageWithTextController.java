package com.example.subui;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class ItemGeneralImageWithTextController {
    @FXML
    private Text text1;
    @FXML
    private Text text2;
    @FXML
    private ImageView image;
    @FXML
    private ItemLabelController itemLabelController;


    public void setTopic(String topic) {
        itemLabelController.setMqttTopic(topic);
    }

    public void setSensor(String sensor) {
        itemLabelController.setMqttSensor(sensor);
    }

    public void setLocation(String location) {
        itemLabelController.setMqttLocation(location);
    }

    public void setImage(String url) {
        image.setImage(new Image(url,160,160,true,true));
    }

    public void setText1(String text) {
        text1.setText(text);
    }

    public void setText2(String text) {
        text2.setText(text);
    }
}
