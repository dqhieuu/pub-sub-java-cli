import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Timestamp;

public class SubscriberController implements Runnable {
    private final TCPClient client;
    private final ConcurrentHashMap<String, TopicMsg> messageQueue;
    boolean stop;

    public SubscriberController(TCPClient client, ConcurrentHashMap<String, TopicMsg> messageQueue) {
        this.client = client;
        this.messageQueue = messageQueue;
        this.stop = false;
    }

    public boolean isStop() {
        return this.stop;
    }

    public void setIsStop(boolean flag) {
        this.stop = flag;
    }

    private String[] cli() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        String in = reader.readLine();
        String[] ret = new String[2];
        if (in.equals("QUIT")) {
            ret[0] = ret[1] = "QUIT";
        } else if (in.startsWith("SUB")) {
            ret = in.split("!");
            Matcher matcher = Pattern.compile("((?:[\\w\\-]+|\\+))/((?:[\\w\\-]+|\\+))/((?:[\\w\\-]+|\\+))").matcher(ret[1]);
            if (!matcher.matches()) {
                throw new IOException("Invalid subscribe format");
            }
        } else if (in.equals("QUERY?")) {
            ret = new String[2];
            ret[0] = ret[1] = "QUERY";
        } else {
            System.out.println("Invalid input format! Try again");
            return ret;
        }
        ret[1] = ret[1].toUpperCase();
        return ret;
    }

    public void subscribe(String sensorDetail) throws IOException {
        client.send("SUB" + " " + sensorDetail);
    }

    public ArrayList<TopicMsg> query() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        System.out.println("Now: " + now);

        ArrayList<String> outdatedKeys = new ArrayList<>();
        ArrayList<TopicMsg> ret = new ArrayList<>();

        for (String key : messageQueue.keySet()) {
            TopicMsg data = messageQueue.get(key);
            if (now.getTime() < data.getTime() + 60000) {
                ret.add(data);
            } else {
                outdatedKeys.add(key);
            }
        }

        for (String key : outdatedKeys) {
            messageQueue.remove(key);
        }

        return ret;
    }

    public void handle(String type, String data) throws IOException {
        if (type.equals("SUB")) {
            subscribe(data);
        } else {
            System.out.println("Data in sub:");
            ArrayList<TopicMsg> ret = query();
            if (ret.size() == 0) {
                System.out.println("Subscriber has no data");
                return;
            }
            for (TopicMsg msg : ret) {
                System.out.println(msg);
            }
        }
    }

    public void quit() throws IOException {
        client.send("QUIT");
        setIsStop(true);
    }

    @Override
    public void run() {
        System.out.println("Options (SUB!TOPIC/LOCATION/SENSOR | QUERY? | QUIT): \n");
        while (!isStop()) {
            try {
                String[] input = cli();
                if (!input[0].equals("QUIT")) {
                    handle(input[0], input[1]);
                } else {
                    quit();
                }
            } catch (Exception e) {
                System.out.println("Invalid input format! Try again");
                e.printStackTrace();
            }
        }
    }
}
