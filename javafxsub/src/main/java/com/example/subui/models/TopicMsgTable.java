package com.example.subui.models;

import javafx.beans.property.SimpleStringProperty;

public class TopicMsgTable {
    private final SimpleStringProperty timestamp;
    private final SimpleStringProperty sensorDetail;
    private final SimpleStringProperty msg;

    public TopicMsgTable(String timestamp, String sensorDetail, String msg) {
        this.timestamp = new SimpleStringProperty(timestamp);
        this.sensorDetail = new SimpleStringProperty(sensorDetail);
        this.msg = new SimpleStringProperty(msg);
    }

    public String getTimestamp() {
        return timestamp.get();
    }

    public void setTimestamp(String timestamp) {
        this.timestamp.set(timestamp);
    }

    public String getSensorDetail() {
        return sensorDetail.get();
    }

    public void setSensorDetail(String sensorDetail) {
        this.sensorDetail.set(sensorDetail);
    }

    public String getMsg() {
        return msg.get();
    }

    public void setMsg(String msg) {
        this.msg.set(msg);
    }
}
