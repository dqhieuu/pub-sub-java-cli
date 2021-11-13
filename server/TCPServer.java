// Dao Quang Hieu
// 19020289
// Server TCP bai 7

import java.net.*;
import java.io.*;


public class TCPServer {
    private ServerSocket server = null;
    public static final int DEFAULT_PORT = 9000;
    
    public TCPServer(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TCPServer() {
        this(DEFAULT_PORT);
    }

    public Socket acceptClient() throws IOException {
        return server.accept();
    }
}
