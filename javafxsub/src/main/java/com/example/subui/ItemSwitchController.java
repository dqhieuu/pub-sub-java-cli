package com.example.subui;

import javafx.fxml.FXML;

public class ItemSwitchController implements ItemFunctionality {
    @FXML
    private ItemGeneralImageController contentController;

    public static final String stateOnUrl = "/images/switch_on.png";
    public static final String stateOffUrl = "/images/switch_off.png";

    private boolean curState;

    private void updateImage() {
        contentController.setImage(curState ? stateOnUrl : stateOffUrl);
    }

    @FXML
    public void initialize() {
        updateImage();
        updateImage();
        setLocation("Phòng ngủ");
        setTopic("Công tắc");
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
