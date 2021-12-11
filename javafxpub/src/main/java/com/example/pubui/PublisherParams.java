package com.example.pubui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PublisherParams implements Cloneable{
    final String WILD_CARDS = "+";

    public String topic = "";
    public String location = "";
    public String sensor = "";
    public PublisherController.EmitFunctions emitFunc;
    public int frequency;
    public String server;
    public int port;

    public PublisherParams() {
        emitFunc = PublisherController.EmitFunctions.SIN;
        frequency = 16;
        server = "";
        port = -1;
    }

    public PublisherParams(String topic, String location, String sensor, PublisherController.EmitFunctions emitFunc, int frequency) {
        this.topic = topic;
        this.location = location;
        this.sensor = sensor;
        this.emitFunc = emitFunc;
        this.frequency = frequency;
    }

    public boolean isValidLength(){
        return !topic.equals("") && !location.equals("") && !sensor.equals("");
    }

    public boolean checkWildcard() {
        return !topic.equals(WILD_CARDS) && !location.equals(WILD_CARDS) && !sensor.equals(WILD_CARDS);
    }

    public boolean isAllPha() {
        Matcher match = Pattern.compile("([\\w\\-]+)").matcher(topic);
        return match.matches();
    }

    public void copy(PublisherParams params) {
        params.topic = this.topic;
        params.location = this.location;
        params.sensor = this.sensor;
        params.emitFunc = this.emitFunc;
        params.frequency = this.frequency;
        params.server = this.server;
        params.port = this.port;
    }
}
