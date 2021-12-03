package com.example.subui;

import javafx.fxml.FXML;

public class ItemLightRangeController implements ItemFunctionality {
    @FXML
    private ItemGeneralImageWithTextController contentController;

    public static final String stateOnUrl = "/images/light_on.png";
    public static final String stateOffUrl = "/images/light_off.png";

    private double curState;

    @FXML
    public void initialize() {
        contentController.setImage(stateOnUrl);
        contentController.setText2("%");
        setState(0.23);
        setLocation("Phòng ngủ");
        setTopic("Ánh sáng");
    }

    private void updateImage(double prevState) {
        if(prevState > 0 ^ curState > 0) { // XOR
            contentController.setImage(curState > 0 ? stateOnUrl : stateOffUrl);
        }
    }

    public <T> void setState(T state) {
        if(!(state instanceof Double)) return;
        double stateCasted = (double) state;
        double prevState = curState;
        contentController.setText1(String.valueOf(Math.round(stateCasted*100)));
        updateImage(prevState);
        curState = stateCasted;
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
