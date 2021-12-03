package com.example.subui.models;

import java.sql.Timestamp;

public class TopicMsg {

    private final Timestamp timestamp;
    private final String sensorDetail;
    private final String msg;

    public TopicMsg(String sensorDetail, String msg) {
        this.sensorDetail = sensorDetail;
        this.msg = msg;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public long getTime() {
        return this.timestamp.getTime();
    }

    public String getSensorDetail() {
        return this.sensorDetail;
    }

    public String getMessage() {
        return msg;
    }


    @Override
    public String toString() {
        return "sensor: " + this.sensorDetail + "\nmsg: " + this.msg + "\ntimestamp: " + timestamp;
    }

    public TopicMsgTable toTableType() {
        return new TopicMsgTable(String.valueOf(timestamp.getTime()), sensorDetail, msg);
    }
}
