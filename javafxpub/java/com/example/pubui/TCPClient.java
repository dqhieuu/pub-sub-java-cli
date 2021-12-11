package com.example.pubui;// Dao Quang Hieu
// 19020289
// Client TCP bai 7

import java.io.*;
import java.net.Socket;

public class TCPClient {
    public static final String DEFAULT_ADDRESS = "localhost";
    public static final int DEFAULT_PORT = 9000;

    public static final int FILE_CHUNK_MAX_SIZE = 2048;

    private Socket socket = null;
    private PrintWriter sender = null;
    private BufferedReader receiver = null;
    private OutputStream binarySender = null;
    private DataInputStream binaryReceiver = null;
    private byte[] fileBuffer = new byte[FILE_CHUNK_MAX_SIZE];

    public TCPClient(String hostName, int port) {
        try {
            socket = new Socket(hostName, port);
            System.out.println("Connected to server.");

            binarySender = socket.getOutputStream();
            sender = new PrintWriter(binarySender);
            binaryReceiver = new DataInputStream(socket.getInputStream());
            receiver = new BufferedReader(new InputStreamReader(binaryReceiver));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TCPClient() {
        this(DEFAULT_ADDRESS,DEFAULT_PORT);
    }

    public String receive() throws IOException {
        return receiver.readLine().trim();
    }

    public byte[] receiveExact(int numOfBytes) throws IOException {
        byte[] tempBuffer = (numOfBytes == FILE_CHUNK_MAX_SIZE) ? fileBuffer : new byte[numOfBytes];
        binaryReceiver.readFully(tempBuffer);
        return tempBuffer;
    }

    public void send(String message) throws IOException {
        sender.println(message);
        sender.flush();
    }

    public void send(byte[] data) throws IOException {
        binarySender.write(data);
    }

    public static int getResponseCode(String message) {
        return Integer.parseInt(message.substring(0, 3));
    }
}
