package com.example.subui;

import com.example.subui.models.TopicTable;
import com.example.subui.services.MQTTSubscriber;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class TabTopicsController {
    @FXML
    private TextField topicInput;
    @FXML
    private Button addNewTopic;
    @FXML
    private TableView<TopicTable> topicTable;
    @FXML
    private TableColumn<TopicTable, String> topic;

    private final ObservableList<TopicTable> topics = FXCollections.observableArrayList();

    private MQTTSubscriber subscriber = null;

    public void setSubscriber(MQTTSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    public void setTopics(String[] topicsArr) {
        for (var topic : topicsArr) {
            topics.addAll(new TopicTable(topic));
        }
    }

    @FXML
    public void changeTopicInput() {
        String topicsStr = topicInput.getText();
        boolean validTopics = topicsStr.matches("^([\\w\\-]+|\\+)/([\\w\\-]+|\\+)/([\\w\\-]+|\\+)$");
        addNewTopic.setDisable(!validTopics);
    }

    public void deleteTopic() {
        TopicTable selectedItem = topicTable.getSelectionModel().getSelectedItem();
        if(selectedItem == null) {
            return;
        }
        subscriber.unsubscribe(selectedItem.getTopic());
        topicTable.getItems().remove(selectedItem);
    }

    public void addNewTopic() {
        String newTopic = topicInput.getText();
        subscriber.subscribe(newTopic);
        topicTable.getItems().addAll(new TopicTable(newTopic));
    }

    @FXML
    public void initialize() {
        topic.setCellValueFactory(new PropertyValueFactory<>("topic"));
        topicTable.setItems(topics);
    }
}
