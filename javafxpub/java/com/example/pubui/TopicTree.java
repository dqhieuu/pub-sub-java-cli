package com.example.pubui;

import java.util.regex.Pattern;

public class TopicTree {
    public String topic;
    public String location;
    public String sensor;

    public TopicTree(String _topic, String _location, String _sensor) {
        topic = _topic;
        location = _location;
        sensor = _sensor;
    }

    public boolean contains(TopicTree compared) {
        if (!this.topic.equals(compared.topic) && !this.topic.equals("+") && !compared.topic.equals("+")) {
            return false;
        }

        if (!this.location.equals(compared.location) && !this.location.equals("+") && !compared.location.equals("+")) {
            return false;
        }

        if (!this.sensor.equals(compared.sensor) && !this.sensor.equals("+") && !compared.sensor.equals("+")) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return topic + "/" + location + "/" + sensor;
    }

    public static TopicTree fromString(String str) {
        var match = Pattern.compile("(.+)\\/(.+)\\/(.+)").matcher(str);
        if(match.find()) {
            return new TopicTree(match.group(1), match.group(2), match.group(3));
        }

        return null;
    }
}
