package com.example.pubui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class Publisher implements Runnable {
    public PublisherParams params;
    final int DEFAULT_PORT = 9000;
    final String DEFAULT_ADDRESS = "localhost";
    public static final String END_MESS = "QUIT";

    private final TCPClient client;
    private volatile boolean paused = false;
    private volatile boolean running = true;
    private final Object pauseLock = new Object();

    public enum State {
        HELO,
        PUBLISH,
        QUIT
    }

    public Publisher(PublisherParams params) {
        this.params = params;
        if (params.server.equals("")) params.server = DEFAULT_ADDRESS;
        if (params.port < 0) params.port = DEFAULT_PORT;
        this.client = new TCPClient(params.server, params.port);
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double emitSin(double max, double min, int frequency) {
        double border = (max - min) / 2;
        return round((border * Math.sin(frequency * System.currentTimeMillis()) + min + border), 2);
    }

    public double emitRandom(double max, double min) {
        return round(new Random().nextDouble(max - min) + min, 2);
    }

    public String flip(String value) {
        if (value.equals("1")) return "0";
        else return "1";
    }

    @Override
    public void run() {
        try {
//            System.out.println(params.server);
//            System.out.println(params.port);
            TopicTree topics = new TopicTree(params.topic, params.location, params.sensor);
            String response;
            String message;
            String tempMessage = "0";
            boolean switches = false;

            double max = 1;
            double min = -1;

            if (params.topic.contains("SWITCH")) {
                switches = true;
            } else if (params.topic.contains("RANGE")) {
                max = 1.0;
                min = 0.0;
            } else if (params.topic.contains("TEMP")) {
                max = 303.0;
                min = 283.0;
            }

            State curState = State.HELO;
            while (curState != State.QUIT) {
                if (!running) break;
                if (paused) {
                    try {
                        synchronized (pauseLock) {
                            pauseLock.wait();
                        }
                    } catch (InterruptedException ex) {
                        break;
                    }
                    if (!running) break;
                }
                switch (curState) {
                    case HELO -> {
                        client.send("HELO AS PUB");
                        response = client.receive();
                        System.out.println(response);
                        if (TCPClient.getResponseCode(response) == 200) {
                            curState = State.PUBLISH;
                        } else {
                            System.out.println("Handshaking failed");
                            curState = State.QUIT;
                        }
                    }
                    case PUBLISH -> {
                        switch (params.emitFunc) {
                            case SIN -> {
                                if (switches) {
                                    tempMessage = flip(tempMessage);
                                } else {
                                    tempMessage = String.valueOf(emitSin(max, min, 10));
                                }
                            }
                            case RANDOM -> {
                                if (switches) {
                                    tempMessage = flip(tempMessage);
                                } else {
                                    tempMessage = String.valueOf(emitRandom(max, min));
                                }
                            }
                        }
                        message = "PUB " + topics + " " + tempMessage;
                        client.send(message);
                        response = client.receive();
                        if (TCPClient.getResponseCode(response) == 201) {
                            System.out.println("Message sent");
                        } else {
                            System.out.println("Failed to send message");
                        }
                    }
                    default -> {
                    }
                }
                try {
                    Thread.sleep((long) (1 / (params.frequency * 1.0) * 100000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            client.send(END_MESS);
            System.out.println("Thread has ended");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        paused = true;
        System.out.println("Pausing thread");
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
        System.out.println("Resuming thread");
    }

    public void stop() {
        running = false;
        resume();
    }
}
