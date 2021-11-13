// Dao Quang Hieu
// 19020289
// Client FTP da luong bai 7

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.nio.file.Paths;



public class FTPClient {
    public static final int FILE_CHUNK_MAX_SIZE = TCPClient.FILE_CHUNK_MAX_SIZE;

    public enum State {
        HELO, INPUT_FILENAME, READ_META, READ_DATA, DONE, QUIT
    };

    public static void printError(String error) {
        System.out.printf("Error: %s\n", error);
    }

    public static void main(String[] args) throws IOException {
        TCPClient client = null;
        if (args.length >= 2) {
            client = new TCPClient(args[0], Integer.parseInt(args[1]));
        } else {
            client = new TCPClient();
        }

        Scanner consoleReader = new Scanner(System.in);

        String response;
        byte[] fileBuffer;
        Matcher match;
    
        String filePath = "";
        String fileName = "";
        FileOutputStream file = null;
        long fileSize = 0;
        long fileRemainingSize = 0;
        int totalOfFrags = 0;
    
        State curState = State.HELO;
        while(curState != State.QUIT) {
            switch(curState) {
                case HELO:
                    client.send("HELO");
                    response = client.receive();
                    if(TCPClient.getResponseCode(response) == 200){
                        curState = State.INPUT_FILENAME;
                    } else {
                        printError("Handshaking failed");
                        curState = State.QUIT;
                    }
                    break;
                case INPUT_FILENAME:
                    System.out.print("Enter the file you want download: ");
                    filePath = consoleReader.nextLine().trim();
                    fileName = Paths.get(filePath).getFileName().toString();
                    if(filePath.length() <= 0) break;
    
                    client.send("NEED " + filePath);
                    curState = State.READ_META;
                    break;
                case READ_META:
                    response = client.receive();
                    if(TCPClient.getResponseCode(response) == 404) {
                        printError("File not found in the database.");
                        curState = State.INPUT_FILENAME;
                        break;
                    }
                    
                    match = Pattern.compile("201 METADATA (\\d+) (\\d+)").matcher(response);
                    if(match.find()) {
                        fileSize = fileRemainingSize = Integer.parseInt(match.group(1));
                        totalOfFrags = Integer.parseInt(match.group(2));
                        
                        try {
                            file = new FileOutputStream("./download_"+fileName);
                        } catch (FileNotFoundException ex) {
                            printError("Can't write data to file.");
                            curState = State.QUIT;
                        }

                        client.send("ACK META");
                        curState = State.READ_DATA;
                    } else {
                        printError("Receiving metadata failed");
                        curState = State.QUIT;
                    }
                    break;
                case READ_DATA: {
                    int curFrag = 0; 
                    while (fileRemainingSize > 0) {
                        int recvSize = (int)((FILE_CHUNK_MAX_SIZE <= fileRemainingSize) ? FILE_CHUNK_MAX_SIZE : fileRemainingSize);
                        fileBuffer = client.receiveExact(FILE_CHUNK_MAX_SIZE);
    
                        file.write(fileBuffer, 0, recvSize);
    
                        // status log
                        if(curFrag % 123 == 0 || curFrag +1 >= totalOfFrags) {
                            float ratio = ((float)curFrag+1)/totalOfFrags;
                            float sizeMB = ((float)fileSize)/1024/1024;
                            System.out.printf("Received %d/%d fragments. (%.2f/%.2fMiB, %.2f%%)\r", 
                            curFrag+1, totalOfFrags, sizeMB*ratio, sizeMB, ratio*100.0);
                        }
                        fileRemainingSize -= recvSize;
                        curFrag++;
                    }
                    System.out.println();
    
                    file.close();
                    curState = State.DONE;
                    break;
                }
                case DONE: {
                    System.out.printf("Download finished (%s -> ./download_%s).\nDo you want to download more? (y/N): ", fileName, filePath);
                    String choice = consoleReader.nextLine();

                    if(choice.length() > 0 && Character.toLowerCase(choice.charAt(0)) == 'y' ) {
                        client.send("CONTINUE");
                        response = client.receive();
                        if(TCPClient.getResponseCode(response) == 202) {
                            curState = State.INPUT_FILENAME;
                        } else {
                            curState = State.QUIT;
                        }
                    } else {
                        curState = State.QUIT;
                    }
                    break;
                }
                default:
                    break;
            }
        }
        client.send("QUIT");

        consoleReader.close();
    }
}
