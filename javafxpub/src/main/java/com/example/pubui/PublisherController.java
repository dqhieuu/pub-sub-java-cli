package com.example.pubui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PublisherController {
  public PublisherParams params = new PublisherParams();
  public ArrayList<String> paramList = new ArrayList<>();

  public enum Topics {
    TV_SWITCH,
    LIGHT_SWITCH,
    GARAGE_SWITCH,
    VOLUME_RANGE,
    HUMIDITY_RANGE,
    AC_TEMP,
    ROOM_TEMP
  }

  public enum Locations {
    BEDROOM,
    LIVING_ROOM,
    BATHROOM,
    BALCONY,
    HALL,
    CORRIDOR,
    KITCHEN,
    YARD,
    DINING_ROOM,
    BASEMENT
  }

  public enum EmitFunctions {
    SIN,
    RANDOM
  }

  public final int[] Frequencies = {16, 32, 64, 100, 500};

  @FXML private VBox publishers;

  @FXML private Label notification;

  @FXML private ComboBox<Topics> topicParam;

  @FXML private ComboBox<Locations> locationParam;

  @FXML private TextField sensorParam;

  @FXML private ComboBox<EmitFunctions> emitFuncParam;

  @FXML private ComboBox<Integer> frequencyParam;

  @FXML private Button confirmButton;

  @FXML
  protected void onAddPublisher() throws IOException {
    params.sensor = sensorParam.getText();
    if (!params.isValidLength()) notification.setText("Please enter topic, location and sensor field");
    else if (!params.checkWildcard()) notification.setText("Publisher can't use wild cards");
    else if (!params.isAllPha()) notification.setText("Topic only contains alphabet char");

    else {
      notification.setText("");
      PublisherParams newParams = new PublisherParams();

      params.copy(newParams);

      // create a new thread
      Publisher newPublisherConnection = new Publisher(newParams);
      new Thread(newPublisherConnection).start();

      // update the UI
      try {
        FXMLLoader pubDetailLoader =
                new FXMLLoader(
                        PublisherApplication.class.getResource("publisher-detail-view.fxml"));
        Pane pubInfo = pubDetailLoader.load();
        PublisherDetailController pubDetailController = pubDetailLoader.getController();
        pubDetailController.init(params, newPublisherConnection);
        publishers.getChildren().add(pubInfo);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @FXML
  protected void setTopic() {
    params.topic = topicParam.getValue().toString();
  }

  @FXML
  protected void setLocation() {
    params.location = locationParam.getValue().toString();
  }

  @FXML
  protected void setEmitFunction() {
    params.emitFunc = emitFuncParam.getValue();
  }

  @FXML
  protected void setFrequencies() {
    params.frequency = frequencyParam.getValue();
  }

  public void init() {
    for (Locations location : Locations.values()) {
      locationParam.getItems().add(location);
    }

    for (Topics topic : Topics.values()) {
      topicParam.getItems().add(topic);
    }

    for (EmitFunctions function : EmitFunctions.values()) {
      emitFuncParam.getItems().add(function);
    }

    for (int frequency : Frequencies) {
      frequencyParam.getItems().add(frequency);
    }
  }

  public void setPortAndAddress(String address, int port) {
    params.server = address;
    params.port = port;
  }
}
