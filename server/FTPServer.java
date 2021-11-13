// Dao Quang Hieu
// 19020289
// Server FTP bai 7

import java.net.*;
import java.io.*;

public class FTPServer {
    public static void main(String[] args) throws IOException {
        int port = TCPServer.DEFAULT_PORT;

        if(args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }
    
        var server = new TCPServer(port);
        
        while(true) {
            Socket client = server.acceptClient();
            new Thread(new FTPServerConnection(client)).start();
        }
    }
}
