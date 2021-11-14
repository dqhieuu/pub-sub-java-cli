package mqttbroker.models;

public class MQTTMessage {
    public TopicTree topicTree;
    public String message;

    public MQTTMessage(TopicTree topicTree, String message) {
        this.topicTree = topicTree;
        this.message = message;
    }
}
