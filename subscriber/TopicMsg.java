import java.sql.Timestamp;

public class TopicMsg {
    private Timestamp timestamp;
    private String sensorDetail;
    private String msg;

    public TopicMsg(String sensorDetail, String msg) {
        this.sensorDetail = sensorDetail;
        this.msg = msg;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public long time() {
        return this.timestamp.getTime();
    }

    public String getSensorDetail() {
        return this.sensorDetail;
    }

    @Override
    public String toString() {
        return "sensor: " + this.sensorDetail + "\nmsg: " + this.msg + "\ntimestamp: " + timestamp;
    }
}
