import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Timestamp;

public class SubscriberController implements Runnable {
    private TCPClient client;
    BlockingQueue<TopicMsg> messageQueue;
    boolean stop;

    public SubscriberController(TCPClient client, BlockingQueue<TopicMsg> messageQueue) {
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
        String[] ret = null;
        if (in.substring(0, 3).equals("SUB")) {
            ret = in.split("!");
        } else if (in.substring(0, 5).equals("QUERY")) {
            ret = in.split("\\?");
        } else if (in.equals("QUIT")) {
            setIsStop(true);
            ret = new String[2];
            ret[0] = ret[1] = "QUIT";
        } else {
            System.out.println("Invalid input format!");
        }
        ret[1] = ret[1].toUpperCase();

        return ret;
    }

    public void subscribe(String sensorDetail) throws IOException {
        client.send("SUB" + " " + sensorDetail);
    }

    public ArrayList<TopicMsg> query(String sensorDetail) {
        ArrayList<TopicMsg> ret = new ArrayList<>();

        Matcher match = Pattern.compile("(.+)\\/(.+)\\/(.+)").matcher(sensorDetail);
        if(!match.find()) {
            System.out.println("Invalid format");
            return ret;
        }
        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        ArrayList<TopicMsg> temp = new ArrayList<>();

        System.out.println("Now: " + now);

        while (messageQueue.size() > 0) {
            TopicMsg pubMessage = messageQueue.poll();
            long msgTime = pubMessage.time();
            if (now.getTime() < msgTime + 60000) {
                temp.add(pubMessage);
            }
        }
        
        for (TopicMsg pubMessage : temp) {
            this.messageQueue.add(pubMessage);
            if (sensorDetail.equals(pubMessage.getSensorDetail())) {
                ret.add(pubMessage);
            }
        }

        return ret;
    }

    public void handle(String type, String data) throws IOException {
        if (type.equals("SUB")) {
            subscribe(data);
        } else {
            System.out.println("Data in sub:");
            ArrayList<TopicMsg> ret = query(data);
            if (ret.size() == 0) {
                System.out.println("Subscriber has no data");
                return;
            }
            for (TopicMsg msg : ret) {
                System.out.println(msg);
            }
        }
    }

    @Override
    public void run() {
        System.out.println("Options (SUB!TOPIC/LOCATION/SENSOR | QUERY?TOPIC/LOCATION/SENSOR | QUIT): \n");
        while (true) {
            try {
                String[] input = cli();
                if (!input[0].equals("QUIT")) {
                    handle(input[0], input[1]);
                }
            } catch (Exception e) {
                System.out.println("Invalid input format! Try again");
            }
        }
    }
}
