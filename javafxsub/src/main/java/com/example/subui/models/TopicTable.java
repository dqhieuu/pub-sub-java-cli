package com.example.subui.models;

import javafx.beans.property.SimpleStringProperty;

public class TopicTable {
    private final SimpleStringProperty topic;

    public TopicTable(String topic) {
        this.topic = new SimpleStringProperty(topic);
    }

    public String getTopic() {
        return topic.get();
    }

    public void setTopic(String topic) {
        this.topic.set(topic);
    }
}
