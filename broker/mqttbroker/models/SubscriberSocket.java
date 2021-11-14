package mqttbroker.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mqttbroker.TCPServerConnection;

public class SubscriberSocket {
    public List<TopicTree> topicTrees = Collections.synchronizedList(new ArrayList<>());
    public TCPServerConnection connection;

    public SubscriberSocket(TCPServerConnection connection) {
        this.connection = connection;
    }

    public boolean addTopicTree(String str) {
        TopicTree topicTree = TopicTree.fromString(str);
        if(topicTree != null) {
            topicTrees.add(topicTree);
            return true;
        }
        return false;
    }
}
