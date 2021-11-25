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
        String[] ret = new String[2];
        if (in.equals("QUIT")) {
            ret[0] = ret[1] = "QUIT";
        } else if (in.substring(0, 3).equals("SUB")) {
            ret = in.split("!");
            Matcher matcher = Pattern.compile("([a-zA-Z]+)\\/([a-zA-Z]+)\\/(.+)").matcher(ret[1]);
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

        ArrayList<TopicMsg> temp = new ArrayList<>();
        while (messageQueue.size() > 0) {
            TopicMsg pubMessage = messageQueue.poll();
            long msgTime = pubMessage.time();
            if (now.getTime() < msgTime + 60000) {
                temp.add(pubMessage);
            }
        }
        
        for (TopicMsg pubMessage : temp) {
            this.messageQueue.add(pubMessage);
        }

        return temp;
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
