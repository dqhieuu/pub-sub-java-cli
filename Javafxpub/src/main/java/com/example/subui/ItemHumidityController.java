package com.example.subui;

import javafx.fxml.FXML;

public class ItemHumidityController implements ItemFunctionality {
    @FXML
    private ItemGeneralImageWithTextController contentController;

    @FXML
    public void initialize() {
        contentController.setImage("/images/humidity.png");
        contentController.setText2("%");
        setLocation("Phòng ngủ");
        setTopic("Độ ẩm");
        setState(0.23);
    }

    public <T> void setState(T state) {
        if(!(state instanceof Double)) return;
        double stateCasted = (double) state;
        contentController.setText1(String.valueOf(Math.round(stateCasted*100)));
    }

    public void setTopic(String topic) {
        contentController.setTopic(topic);
    }

    public void setLocation(String sensor) {
        contentController.setLocation(sensor);
    }

    public void setSensor(String location) {
        contentController.setSensor(location);
    }
}
