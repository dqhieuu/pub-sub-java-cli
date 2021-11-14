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
        
        BlockingQueue<MQTTMessage> messageQueue = new ArrayBlockingQueue<>(1000);
        List<SubscriberSocket> subscriberList = Collections.synchronizedList(new ArrayList<>());
        
        while(true) {
            Socket client = server.acceptClient();
            new Thread(new PubSubConnection(client, messageQueue, subscriberList)).start();
        }

    }
}
