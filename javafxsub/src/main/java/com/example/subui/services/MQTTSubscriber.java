package com.example.subui.services;

import com.example.subui.models.ItemNode;
import com.example.subui.models.TopicMsg;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MQTTSubscriber implements Runnable {
    private final TCPClient client;
    private final Map<String, ItemNode> messageMap;
    private State curState;

    private final String[] subscribedTopics;

    public enum State {
        HELLO,
        OK,
        QUIT
    }

    public MQTTSubscriber(String address, String port, String[] topics) {
        client = new TCPClient(address, Integer.parseInt(port));
        subscribedTopics = topics;
        messageMap = new ConcurrentHashMap<>();
    }

    public Map<String, ItemNode> getMessageMap() {
        return messageMap;
    }

    public void update() {
        for (String key : messageMap.keySet()) {
            var item = messageMap.get(key);
            if (!item.isValid()) {
                item.remove();
                messageMap.remove(key);
            }
        }
    }


    public void subscribe(String sensorDetail) {
        client.send("SUB" + " " + sensorDetail);
    }

    public void unsubscribe(String sensorDetail) {
        client.send("UNSUB" + " " + sensorDetail);
    }

    public void quit() {
        client.send("QUIT");
        curState = State.QUIT;
    }

    private void addToMsgQueue(String response) throws IOException {
        System.out.println(response);
        Matcher match = Pattern.compile("([\\w\\-+/]+) (.+)").matcher(response);
        if (!match.find()) {
            System.out.println("Invalid sensorDetail format");
            return;
        }
        String sensorDetail = match.group(1);
        String msg = match.group(2);

        ItemNode itemFromSamePublisher = messageMap.get(sensorDetail);

        if(itemFromSamePublisher == null) {
            ItemNode item = ItemNode.fromTopicMsg(new TopicMsg(sensorDetail, msg));
            if(item != null) {
                messageMap.put(sensorDetail, item);
            }
        } else {
            itemFromSamePublisher.setTopicMsg(new TopicMsg(sensorDetail, msg));
        }
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
                        for (var topic: subscribedTopics) {
                            subscribe(topic);
                        }
                        System.out.println("Topics subscribed: " + Arrays.toString(subscribedTopics));
                        curState = State.OK;
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

    @Override
    public void run() {
        try {
            connectBroker();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
