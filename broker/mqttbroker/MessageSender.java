package mqttbroker;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import mqttbroker.models.MQTTMessage;
import mqttbroker.models.SubscriberSocket;
import mqttbroker.models.TopicTree;

public class MessageSender implements Runnable{
    private BlockingQueue<MQTTMessage> messageQueue;
    private List<SubscriberSocket> subscriberList;

    public MessageSender(BlockingQueue<MQTTMessage> _messageQueue, List<SubscriberSocket> _subscriberList) {
        this.messageQueue = _messageQueue;
        this.subscriberList = _subscriberList;
    }
    
    @Override
    public void run() {
        while (true) {

            while (messageQueue.size() > 0) {
                MQTTMessage pubMessage = messageQueue.poll();

                for (SubscriberSocket sub: subscriberList) {
                    for (TopicTree topic: sub.topicTrees) {
                        if (topic.contains(pubMessage.topicTree)) {
                            sub.connection.send(pubMessage.topicTree.toString() + " " + pubMessage.message);
                            break;
                        }
                    }
                }

            }
            
            // Sleep when list is empty
            try {
                Thread.sleep(16); // ms. Choosing 16 for 60hz update rate
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
        }
    }

}
