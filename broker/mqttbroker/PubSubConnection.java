package mqttbroker;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mqttbroker.models.MQTTMessage;
import mqttbroker.models.SubscriberSocket;
import mqttbroker.models.TopicTree;

public class PubSubConnection extends TCPServerConnection implements Runnable {
    private BlockingQueue<MQTTMessage> messageQueue;
    private List<SubscriberSocket> subscriberList;
    
    PubSubConnection(Socket client, BlockingQueue<MQTTMessage> messageQueue, List<SubscriberSocket> subscriberList) {
        super(client);

        this.messageQueue = messageQueue;
        this.subscriberList = subscriberList;
    }

    public enum State {
        HELO, PUB_LISTEN, SUB_READ, QUIT
    };

    private static void printError(String error) {
        System.out.printf("Error: %s\n", error);
    }

    private void printClientAction(String info) {
        System.out.printf("Client (%s:%d) %s\n", clientHostName, clientPort, info);
    }

    @Override
    public void run() {
        String response = "";
        Matcher match;

        State curState = State.HELO;
        
        SubscriberSocket subscriber = null;

        while(curState != State.QUIT) {
            switch(curState) {
                case HELO:
                    response = receive();
                    if(response.equals("HELO AS PUB")){
                        printClientAction("connected as publisher.");
                        send("200 HI");
                        curState = State.SUB_READ;
                        
                    } else if (response.equals("HELO AS SUB")) {
                        printClientAction("connected as subscriber.");
                        send("200 HI");
                        subscriber = new SubscriberSocket(this);
                        subscriberList.add(subscriber);

                        curState = State.PUB_LISTEN;
                    } else {
                        printError("Handshaking failed");
                        send("400 Bad Request");
                        curState = State.QUIT;
                    }
                    break;
                case PUB_LISTEN: {
                    response = receive();
                    match = Pattern.compile("PUB ([a-zA-Z0-9_-/]+) (.+)").matcher(response);

                    if (match.find()) {
                        TopicTree topicTree = TopicTree.fromString(match.group(1));
                        String message = match.group(2);

                        if(topicTree != null) {
                            // We don't need to put this inside a synchronize block because the queue is thread-safe
                            // See: https://stackoverflow.com/questions/3661282/do-i-need-extra-synchronization-when-using-a-blockingqueue
                            messageQueue.add(new MQTTMessage(topicTree, message));
                        }
                    }
                    break;
                }
                case SUB_READ:
                    response = receive();
                    if(response.equals("QUIT")) {
                        curState = State.QUIT;
                        printClientAction("as subscriber has disconnected.");
                        subscriberList.remove(subscriber);
                    } else {
                        match = Pattern.compile("SUB (.*)").matcher(response);
                        if(match.find()) {
                            String topicTreeStr = match.group(1);
                            boolean success = subscriber.addTopicTree(topicTreeStr);
                            if(success) {
                                send("201 OK");
                            } else {
                                send("401 Invalid Format");
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }


        send("100 Bye Bye");
    }
}
