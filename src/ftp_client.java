import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;

public class ftp_client {

	public static void main(String argv[]) throws IOException
    {
    	String sentence;
        String modifiedSentence;
        boolean isOpen = true;
        int number = 1;
        boolean notEnd = true;
        String statusCode;
        boolean clientGo = true;
        int port = 1200;
        int port1 = port + 2;


        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket controlSocket = new Socket("127.0.0.1", port);

        System.out.println("You are connected to the server");
        System.out.println("\nWhat would you like to do : \n retr: file.txt ||stor: file.txt  || quit\n");
    



while( isOpen && clientGo){

    DataOutputStream outToServer = new DataOutputStream(controlSocket.getOutputStream());
    DataInputStream inFromServer = new DataInputStream(new BufferedInputStream (controlSocket.getInputStream()));
    sentence = inFromUser.readLine();
    StringTokenizer tokens = new StringTokenizer(sentence);

    if (sentence.equals("list:")) {
                    // Max

                    port = port + 2;
                    outToServer.writeBytes(port + " " + sentence + " " + '\n');

                    ServerSocket welcomeData = new ServerSocket(port);
                    Socket dataSocket = welcomeData.accept();

                    DataInputStream inData = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));
                    while (notEnd) {
                        modifiedSentence = inData.readUTF();
                        System.out.println(modifiedSentence);
                    }

                    welcomeData.close();
                    dataSocket.close();
                    System.out.println("\nWhat would you like to do next: \n retr: file.txt ||stor: file.txt  || quit");
                }
        else if (sentence.equals("retr:")) {

            int bytesRead;
                    int current = 0;
                    int filesize = 10;
                    byte[] byteArray = new byte[10];

                    outToServer.writeBytes(port + " " + sentence + " " + '\n');
                    ServerSocket welcomeData = new ServerSocket(port);
                    Socket dataSocket = welcomeData.accept();

                    
                    String filename = "file.txt";
                    File file = new File(filename);
                    FileOutputStream fos = new FileOutputStream(file);

                    DataInputStream inData = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    ByteArrayOutputStream byteos = new ByteArrayOutputStream();
                    bytesRead = inData.read(byteArray, 0, byteArray.length);
                    
                    current = bytesRead;

                    do {
                        byteos.write(byteArray);
                        current = inData.read(byteArray);
                    } while (current > -1);

                    bos.write(byteos.toByteArray());
                    bos.flush();
                    bos.close();

                    System.out.println("\nFile Retrieved");

                    bos.flush();
                    bos.close();
                    welcomeData.close();
                    dataSocket.close();
                    System.out.println("\nWhat would you like to do next: \n retr: file.txt ||stor: file.txt  || quit");


                }

                else if (sentence.startsWith("stor: ")) {
                    // Rob
                    port = port + 2;

                    Socket dataSocket = new Socket(controlSocket.getInetAddress(), port);
                    System.out.println(controlSocket.getInetAddress());
                    DataOutputStream dataToServer = new DataOutputStream(dataSocket.getOutputStream());

                    String fileName = tokens.nextToken();
                    File clientFilesDirectory = new File(
            ".");
                    String filePath = clientFilesDirectory + "/" + fileName;
                    File file = new File(filePath);

                    if(file.exists()){
                        byte[] bytes = new byte[(int) file.length()];
                        FileInputStream inputStream = new FileInputStream(
                                file);
                        inputStream.read(bytes);
                        inputStream.close();
    
                        dataToServer.write(bytes, 0, bytes.length);
                    }
    
                    else {
                        System.out.println("File does not exist");
                    }
    
                    dataSocket.close();
                    System.out.println("Data Socket closed");

                }

                else if (sentence.startsWith("quit")) {
                    // Rob
                    System.out.println("\nGoodbye");
                    System.exit(0);
                }



}
}


}


