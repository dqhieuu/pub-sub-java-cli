package com.example.subui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StartupPromptController {
    @FXML
    public Button setDefault;
    @FXML
    public TextField port;
    @FXML
    public TextArea topics;
    @FXML
    public Button start;
    @FXML
    public TextField address;

    boolean validAddress = true;
    boolean validPort = true;
    boolean validTopics = true;


    public static final String DEFAULT_ADDRESS = "localhost";
    public static final String DEFAULT_PORT = "9000";
    public static final String DEFAULT_TOPICS = "+/+/+";

    private void updateButtons() {
        String addressStr = address.getText();
        String portStr = port.getText();
        String topicsStr = topics.getText();

        setDefault.setDisable(DEFAULT_ADDRESS.equals(addressStr)
                && DEFAULT_PORT.equals(portStr)
                && DEFAULT_TOPICS.equals(topicsStr));
        start.setDisable(!(validAddress && validPort && validTopics));
    }

    public void restoreDefault() {
        address.setText(DEFAULT_ADDRESS);
        port.setText(DEFAULT_PORT);
        topics.setText(DEFAULT_TOPICS);
        updateButtons();
    }


    public void startApp() throws IOException {
        String addressStr = address.getText();
        String portStr = port.getText();
        String topicsStr = topics.getText();
        List<String> topicsArr = Arrays.stream(topicsStr.split("[\\r\\n]+")).map(String::trim).collect(Collectors.toList());
        topicsArr.removeIf(e -> e.length() <= 0);

        Stage stage = (Stage) start.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MQTTSubApplication.class.getResource("/com/example/subui/main-ui.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 650);
        MainUiController controller = fxmlLoader.getController();
        controller.init(addressStr, portStr, topicsArr.toArray(String[]::new));
        stage.setScene(scene);
    }

    @FXML
    public void changeTopics() {
        String topicsStr = topics.getText();
        List<String> topicsArr = Arrays.stream(topicsStr.split("[\\r\\n]+")).map(String::trim).collect(Collectors.toList());
        topicsArr.removeIf(e -> e.length() <= 0);
        validTopics = topicsArr.stream().allMatch(e -> Pattern.matches("([\\w\\-]+|\\+)/([\\w\\-]+|\\+)/([\\w\\-]+|\\+)", e));

        updateButtons();
    }

    @FXML
    public void changeAddress() {
        String addressStr = address.getText();
        validAddress = addressStr.length() > 0;
        updateButtons();
    }

    @FXML
    public void changePort() {
        String portStr = port.getText();
        validPort = Pattern.matches("\\d{1,5}", portStr);
        updateButtons();
    }
}
