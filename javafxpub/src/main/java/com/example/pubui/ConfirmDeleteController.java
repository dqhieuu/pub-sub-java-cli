package com.example.pubui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ConfirmDeleteController {
    public boolean delete = false;

    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;

    @FXML
    protected void onYes() {
        delete = true;
        yesButton.getScene().getWindow().hide();
    }

    @FXML
    protected void onNo() {
        delete = false;
        noButton.getScene().getWindow().hide();
    }
}
