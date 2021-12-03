package com.example.subui;

import javafx.fxml.FXML;

public class ItemTemperatureController implements ItemFunctionality {
    @FXML
    private ItemGeneralImageWithTextController contentController;

    @FXML
    public void initialize() {
        contentController.setImage("/images/temperature.png");
        contentController.setText2("°C");
        setState(293);
        setLocation("Phòng ngủ");
        setTopic("Nhiệt độ");
    }

    public double getCelsius(double kelvin) {
        return kelvin - 273.15;
    }


    public <T> void setState(T state) {
        if(!(state instanceof Double)) return;
        double kelvin = (double) state;
        contentController.setText1(String.valueOf(Math.round(getCelsius(kelvin))));
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
