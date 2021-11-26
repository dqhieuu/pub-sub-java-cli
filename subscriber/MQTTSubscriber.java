import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MQTTSubscriber {
    private final TCPClient client;
    private final ConcurrentHashMap<String, TopicMsg> messageQueue;
    private State curState;
    private final SubscriberController controller;

    public enum State {
        HELLO,
        OK,
        QUIT
    }

    MQTTSubscriber(String[] config) {
        if (config.length < 2) {
            client = new TCPClient();
        } else {
            client = new TCPClient(config[0], Integer.parseInt(config[1]));
        }
        messageQueue = new ConcurrentHashMap<>();
        controller = new SubscriberController(this.client, this.messageQueue);
    }

    private void addToMsgQueue(String response) {
        System.out.println(response);
        Matcher match = Pattern.compile("([\\w\\-+/]+) (.+)").matcher(response);
        if (!match.find()) {
            System.out.println("Invalid sensorDetail format");
            return;
        }
        String sensorDetail = match.group(1);
        String msg = match.group(2);
        messageQueue.put(sensorDetail, new TopicMsg(sensorDetail, msg));
    }

    private boolean isSubResp(String response) {
        Matcher match = Pattern.compile("((\\d){3})(.*)").matcher(response);
        if (match.matches()) {
            System.out.println(response);
            if (response.equals("100 Bye Bye")) {
                curState = State.QUIT;
            }
            return true;
        }
        return false;
    }

    public void connectBroker() throws IOException {
        curState = State.HELLO;
        String response;
        while (curState != State.QUIT) {
            switch (curState) {
                case HELLO -> {
                    client.send("HELO AS SUB");
                    response = client.receive();
                    if (TCPClient.getResponseCode(response) == 200) {
                        curState = State.OK;
                        new Thread(controller).start();
                    } else {
                        System.out.print("Handshaking failed");
                        curState = State.QUIT;
                    }
                }
                case OK -> {
                    response = client.receive();
                    if (!isSubResp(response)) {
                        addToMsgQueue(response);
                    }
                }
                default -> {
                }
            }
        }
        
    }

    public static void main(String[] args) {
        MQTTSubscriber sub = new MQTTSubscriber(args);
        try {
            sub.connectBroker();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
