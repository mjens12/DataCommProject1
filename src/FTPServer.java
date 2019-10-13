import java.io.*;
import java.net.*;
import java.util.*;

class FTPServer {

    public static void main(String[] args) throws IOException

    {
        int port = 12000;

        // Create socket server and wait for client to connect
        ServerSocket welcomeSocket = new ServerSocket(port);
        do {
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("\n\nUser Connected!\n\n");

            // Creating threads
            ClientHandler handler = new ClientHandler(connectionSocket);
            handler.start();
        } while (true);
    }
}

class ClientHandler extends Thread {

    private DataOutputStream outToClient;
    private BufferedReader inFromClient;

    String fromClient;
    String clientCommand;
    byte[] data;
    int port = 12000;
    File directory = new File("/Users/isfar/Desktop/CIS457/Project_1");
    List<String> listOfFiles = new ArrayList<>();
    String firstln;
    private Socket connectionSocket;

    public ClientHandler(Socket socket) {
        connectionSocket = socket;

        try {
            outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error in connection");
            System.out.println(e);
        }
    }

    public static void serverFiles(File directory, List<String> listOfFiles) {

        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                listOfFiles.add(file.getName());
            }
        }
    }

    public void run() {

        try {

            fromClient = inFromClient.readLine();
            StringTokenizer tokens = new StringTokenizer(fromClient);
            firstln = tokens.nextToken();
            port = Integer.parseInt(firstln);
            clientCommand = tokens.nextToken();

            serverFiles(directory, listOfFiles);

            if (clientCommand.equals("list:")) {
            	//Max
            }
            
            if (clientCommand.equals("retr")) {

                Socket dataSocket = new Socket(connectionSocket.getInetAddress(), port);
                System.out.println(connectionSocket.getInetAddress());
                DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());

                String fileName = tokens.nextToken();

                if (!listOfFiles.contains(fileName)) {
                    System.out.println("Error: File Not Found");
                }

                else {

                    File sendFile = new File(directory, fileName);
                    byte[] bytes = new byte[(int) sendFile.length()];
                    FileInputStream inputStream = new FileInputStream(sendFile);
                    inputStream.read(bytes);
                    inputStream.close();

                    dataOutToClient.write(bytes, 0, bytes.length);

                }

                dataSocket.close();
                System.out.println("Data Socket closed");
            }
            if (clientCommand.equals("stor:")) {
            	//Sean
            }
            if (clientCommand.equals("quit")) {
            	//Sean
            }

        }

        catch (IOException e) {
            System.out.println("Error: Unable to disconnect");
            System.out.println(e);
        }
    }

}