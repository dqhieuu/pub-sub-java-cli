package com.example.subui;

import javafx.fxml.FXML;

public class ItemLightSwitchController implements ItemFunctionality {
    @FXML
    private ItemGeneralImageController contentController;

    public static final String stateOnUrl = "/images/light_on.png";
    public static final String stateOffUrl = "/images/light_off.png";

    private boolean curState;

    private void updateImage() {
        contentController.setImage(curState ? stateOnUrl : stateOffUrl);
    }

    @FXML
    public void initialize() {
        updateImage();
        setLocation("Phòng ngủ");
        setTopic("Ánh sáng");
    }

    public <T> void setState(T state) {
        if(!(state instanceof Boolean)) return;
        boolean stateCasted = (boolean) state;
        if(curState != stateCasted) {
            curState = stateCasted;
            updateImage();
        }
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
