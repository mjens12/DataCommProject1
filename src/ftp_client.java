import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;

public class ftp_client {

	public static void main(String argv[]) throws IOException {
		String sentence;
		String modifiedSentence;
		boolean isOpen = true;
		int number = 1;
		boolean notEnd = true;
		String statusCode;
		boolean clientGo = true;
		int port = 1200;
		int port1 = port + 2;
		String command = "";
		String fileName = "";
		StringTokenizer tokens = new StringTokenizer("");

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket controlSocket = new Socket("127.0.0.1", port);

		System.out.println("You are connected to the server");
		System.out.println("\nWhat would you like to do : \nlist: || retr: file.txt || stor: file.txt  || quit\n");

		while (isOpen && clientGo) {

			DataOutputStream outToServer = new DataOutputStream(controlSocket.getOutputStream());
			DataInputStream inFromServer = new DataInputStream(new BufferedInputStream(controlSocket.getInputStream()));
			sentence = inFromUser.readLine();
			// System.out.println(sentence);
			if (sentence != null) {
				tokens = new StringTokenizer(sentence);
				command = tokens.nextToken();
			}

			if (command.equals("list:")) {
				// Max

				port = port + 2;
				outToServer.writeBytes(port + " " + sentence + " " + '\n');

				ServerSocket welcomeData = new ServerSocket(port);
				Socket dataSocket = welcomeData.accept();

				DataInputStream inData = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));
				try {
					modifiedSentence = inData.readUTF();
					System.out.print(modifiedSentence);
				} catch (EOFException e) {
					System.out.println("End of stream reached");
				}

				welcomeData.close();
				dataSocket.close();
				System.out.println(
						"\nWhat would you like to do next: \nlist: || retr: file.txt ||stor: file.txt || quit");
			} else if (command.equals("retr:")) {
				port = port + 2;
				fileName = tokens.nextToken();
				outToServer.writeBytes(port + " " + sentence + " " + fileName + '\n');

				ServerSocket welcomeData = new ServerSocket(port);
				Socket dataSocket = welcomeData.accept();

				DataInputStream inData = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));

				int filesize = 6022386;
				int bytesRead;
				int current = 0;
				byte[] mybytearray = new byte[filesize];

				FileOutputStream fos = new FileOutputStream(fileName);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				bytesRead = inData.read(mybytearray, 0, mybytearray.length);
				current = bytesRead;

				do {
					bytesRead = inData.read(mybytearray, current, (mybytearray.length - current));
					if (bytesRead >= 0)
						current += bytesRead;
				} while (bytesRead > -1);

				bos.write(mybytearray, 0, current);
				bos.flush();
				bos.close();

				System.out.println("\nFile Retrieved");

				welcomeData.close();
				dataSocket.close();
				System.out.println(
						"\nWhat would you like to do next: \nlist: || retr: file.txt || stor: file.txt || quit");

			}

			else if (command.equals("stor:")) {
				// Rob
				port = port + 2;
				fileName = tokens.nextToken();
				outToServer.writeBytes(port + " " + sentence + " " + fileName + '\n');

				ServerSocket welcomeData = new ServerSocket(port);
				Socket dataSocket = welcomeData.accept();

				DataOutputStream outData = new DataOutputStream(new BufferedOutputStream(dataSocket.getOutputStream()));

				String filePath = "/home/collirob/Documents/CIS457/Project1/" + fileName;
				File myFile = new File(filePath);
				// System.out.println(filePath);
				if (myFile.exists()) {
					byte[] mybytearray = new byte[(int) myFile.length() + 1];
					FileInputStream fis = new FileInputStream(myFile);
					BufferedInputStream bis = new BufferedInputStream(fis);
					bis.read(mybytearray, 0, mybytearray.length);
					System.out.println("Sending...");
					outData.write(mybytearray, 0, mybytearray.length);
					outData.flush();
					bis.close();
				} else {
					System.out.println("File Not Found");
				}

				welcomeData.close();
				dataSocket.close();
				System.out.println(
						"\nWhat would you like to do next: \nlist: || retr: file.txt || stor: file.txt || quit");

			}

			else if (command.equals("quit")) {
				// Rob
				port = port + 2;
				outToServer.writeBytes(port + " " + sentence + " " + "\n");
				System.out.println("\nGoodbye");
				System.exit(0);
			} else {
				System.out.println("Invalid Command");
			}

		}
	}

}
