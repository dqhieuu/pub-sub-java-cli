package mqttbroker;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


import mqttbroker.models.MQTTMessage;
import mqttbroker.models.SubscriberSocket;

public class MQTTBroker {
    public static void main(String[] args) throws IOException {
        int port = TCPServer.DEFAULT_PORT;

        if(args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }

        var server = new TCPServer(port);
        
        // A queue for sending message sequentially
        BlockingQueue<MQTTMessage> messageQueue = new ArrayBlockingQueue<>(1000);

        // A list of subscribers (note that we don't use a dictionary type 
        // because to send a message we have to go through all subscribers,
        // the reason is that a subscriber may listen with a wildcard expression,
        // so dictionary exact matching won't work)
        List<SubscriberSocket> subscriberList = Collections.synchronizedList(new ArrayList<>());
        
        while(true) {
            Socket client = server.acceptClient();
            new Thread(new PubSubConnection(client, messageQueue, subscriberList)).start();
        }

    }
}
