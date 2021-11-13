import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.util.regex.*;

public class FTPServerConnection extends TCPServerConnection implements Runnable {
    public static final int FILE_CHUNK_MAX_SIZE = 2048;

    public enum State {
        HELO, READ_FILENAME, SEND_DATA, DONE, QUIT
    };
    
    FTPServerConnection(Socket client) {
        super(client);
    }

    private static void printError(String error) {
        System.out.printf("Error: %s\n", error);
    }

    private void printClientAction(String info) {
        System.out.printf("Client (%s:%d) %s\n", clientHostName, clientPort, info);
    }

    @Override
    public void run() {
        String response = "";
        byte[] fileBuffer = new byte[FILE_CHUNK_MAX_SIZE];
        Matcher match;

        FileInputStream file = null;

        State curState = State.HELO;
        while(curState != State.QUIT && response != "QUIT") {
            switch(curState) {
                case HELO:
                    response = receive();
                    if(response.equals("HELO")){
                        printClientAction("connected.");
                        send("200 HI");
                        curState = State.READ_FILENAME;
                    } else {
                        printError("Handshaking failed");
                        send("400 Bad Request");
                        curState = State.QUIT;
                    }
                    break;
                case READ_FILENAME:
                    response = receive();
                    // NEED ./dir/folder/file name_LIKE.this
                    match = Pattern.compile("NEED (.*)").matcher(response);
                    if(match.find()) {
                        String filePath = match.group(1);
                        // read file in binary mode, cursor at the end to calculate file size
                        try {
                            file = new FileInputStream(filePath);
                            printClientAction("downloads "+filePath);
                            long fileSize = Files.size(Path.of(filePath));
                            int totalOfFrags = (int)Math.ceil((double)fileSize / FILE_CHUNK_MAX_SIZE);

                            // METADATA file_size total_of_frags
                            send(String.format("201 METADATA %d %d",fileSize, totalOfFrags));

                            response = receive();
                            if(response.equals("ACK META")) {
                                curState = State.SEND_DATA;
                            }
                        } catch (IOException ex) {
                            send("404 File Not Found");
                        }
                    } else {
                        send("400 Bad Request");
                        curState = State.QUIT;
                    }
                    break;
                case SEND_DATA:
                    try {
                        while(file.read(fileBuffer) > 0) {
                            send(fileBuffer);
                        }
                        file.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    curState = State.DONE;
                    break;
                case DONE:
                    response = receive();
                    if(response.equals("CONTINUE")) {
                        send("202 OK");
                        curState = State.READ_FILENAME;
                    } else {
                        curState = State.QUIT;
                    }
                    break;
                default:
                    break;
            }
        }

        printClientAction("disconnected.");
        send("100 Bye Bye");
    }
}
