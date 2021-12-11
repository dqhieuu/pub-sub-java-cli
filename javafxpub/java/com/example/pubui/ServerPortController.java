package com.example.pubui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ServerPortController {
    final int DEFAULT_PORT = 9000;
    final String DEFAULT_ADDRESS = "localhost";
    public String server = "";
    public int port = -1;

    @FXML
    private TextField serverAddress;

    @FXML
    private TextField portNumber;

    @FXML
    private Button confirmAddress;

    @FXML
    private Button useDefault;

    @FXML
    protected void setBindAddress() {
        server = serverAddress.getText().trim();
        try{
            port = Integer.parseInt(portNumber.getText().trim());
        } catch (NumberFormatException e) {
            port = -1;
        }
        confirmAddress.getScene().getWindow().hide();
    }

    @FXML
    protected void setDefault() {
        server = DEFAULT_ADDRESS;
        port = DEFAULT_PORT;
        useDefault.getScene().getWindow().hide();
    }
}
