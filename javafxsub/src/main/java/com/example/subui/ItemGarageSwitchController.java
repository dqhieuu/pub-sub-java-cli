package com.example.subui;

import javafx.fxml.FXML;

public class ItemGarageSwitchController implements ItemFunctionality {
    @FXML
    private ItemGeneralImageController contentController;

    public static final String stateOnUrl = "/images/garage_open.png";
    public static final String stateOffUrl = "/images/garage_closed.png";

    private boolean curState;

    private void updateImage() {
        contentController.setImage(curState ? stateOnUrl : stateOffUrl);
    }

    @FXML
    public void initialize() {
        setLocation("Garage");
        setTopic("Cửa garage");
        updateImage();
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
