import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MQTTPublisher {
    final String WILD_CARDS = "+";
    final int DEFAULT_PORT = 9000;
    final String DEFAULT_ADDRESS = "localhost";
    public static final String END_MESS = "QUIT";

    private TCPClient client = null;

    public enum State {
        HELO, PUBLISH, QUIT
    }

    public MQTTPublisher(String address, int port) {
        client = new TCPClient(address, port);
    }

    public MQTTPublisher() {
        client = new TCPClient(DEFAULT_ADDRESS, DEFAULT_PORT);
    }

    static boolean isAllAlpha(String str) {
        Matcher match = Pattern.compile("([\\w\\-]+)").matcher(str);
        return match.matches();
    }

    public TopicTree fakeUI() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the topic: ");
        String topic = reader.readLine().toUpperCase();
        System.out.print("Enter the location: ");
        String location = reader.readLine().toUpperCase();
        System.out.print("Enter the sensor: ");
        String sensor = reader.readLine().toUpperCase();

        if (topic.length() == 0 || location.length() == 0 || sensor.length() == 0) {
            System.out.print("Invaid length of topic|location|sensor");
            return null;
        }

        if (topic.equals(WILD_CARDS) || location.equals(WILD_CARDS) || sensor.equals(WILD_CARDS)) {
            System.out.println("Publisher can't use wild cards");
            return null;
        }

        if (!isAllAlpha(topic)) {
            System.out.println("Topic only contains alphabet char");
            return null;
        }

        return new TopicTree(topic, location, sensor);
    }

    public long emitSin (int max, int min, int frequency) {
        return Math.round((max * Math.sin(frequency * System.currentTimeMillis()) + min) % 60000 * Math.PI);
    }

    public int emitRandom (int max, int min) {
        return 1;
    }

    public static void main(String[] args) {
        try {
            MQTTPublisher publisher = null;
            if (args.length >= 2) {
                publisher = new MQTTPublisher(args[0], Integer.parseInt(args[1]));
            } else {
                publisher = new MQTTPublisher();
            }
            TopicTree topics = publisher.fakeUI();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String response;
            String message;
            State curState = State.HELO;
            while (curState != State.QUIT) {
                switch (curState) {
                    case HELO:
                        publisher.client.send("HELO AS PUB");
                        response = publisher.client.receive();
                        if(TCPClient.getResponseCode(response) == 200){
                            curState = State.PUBLISH;
                        } else {
                            System.out.println("Handshaking failed");
                            curState = State.QUIT;
                        }
                        break;

                    case PUBLISH:
                        System.out.println("Enter your message: ");
                        String tempMessage = reader.readLine();
                        if (tempMessage.equals(END_MESS)) {
                            curState = State.QUIT;
                            break;
                        }
                        message = "PUB " + topics.toString() + " " + tempMessage;
                        publisher.client.send(message);
                        response = publisher.client.receive();
                        if(TCPClient.getResponseCode(response) == 201){
                            System.out.println("Message send");
                        } else {
                            System.out.println("Failed to send message");
                        }
                        break;
                    default:
                        break;
                }
            }
            publisher.client.send(END_MESS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
