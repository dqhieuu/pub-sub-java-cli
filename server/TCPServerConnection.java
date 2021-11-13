import java.net.*;
import java.io.*;

public class TCPServerConnection {
    private Socket socket = null;
    private PrintWriter sender = null;
    private OutputStream binarySender = null;
    private BufferedReader receiver = null;

    public String clientHostName = null;
    public int clientPort;

    TCPServerConnection(Socket client) {
        socket = client;
        clientHostName = client.getInetAddress().getHostName();
        clientPort = client.getPort();
        System.out.printf("Accepted new client %s:%d\n", clientHostName, clientPort);
        try {
            binarySender = socket.getOutputStream();
            sender = new PrintWriter(binarySender);
            receiver = new BufferedReader(new InputStreamReader(new DataInputStream(socket.getInputStream())));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String receive() {
        String lineRead = "";
        try {
            lineRead = receiver.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineRead;
    }

    public void send(String message) {
        sender.println(message);
        sender.flush();
    }

    public void send(byte[] data) {
        try {
            binarySender.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
