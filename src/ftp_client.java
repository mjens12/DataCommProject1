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

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket controlSocket = new Socket("127.0.0.1", port);

		System.out.println("You are connected to the server");
		System.out.println("\nWhat would you like to do : \nlist: || retr: file.txt || stor: file.txt  || quit\n");

		while (isOpen && clientGo) {

			DataOutputStream outToServer = new DataOutputStream(controlSocket.getOutputStream());
			DataInputStream inFromServer = new DataInputStream(new BufferedInputStream(controlSocket.getInputStream()));
			sentence = inFromUser.readLine();
			StringTokenizer tokens = new StringTokenizer(sentence);

			if (sentence.equals("list:")) {
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
			} else if (sentence.equals("retr:")) {
				port = port + 2;
				String fileName = tokens.nextToken();
				outToServer.writeBytes(port + " " + sentence + " " + fileName + '\n');

				ServerSocket welcomeData = new ServerSocket(port);
				Socket dataSocket = welcomeData.accept();

				DataInputStream inData = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));

				int filesize = 6022386;
				int bytesRead;
				int current = 0;
				byte[] mybytearray = new byte[filesize];

				FileOutputStream fos = new FileOutputStream("def");
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

			else if (sentence.startsWith("stor: ")) {
				// Rob
				port = port + 2;
				outToServer.writeBytes(port + " " + sentence + " " + '\n');

				ServerSocket welcomeData = new ServerSocket(port);
				Socket dataSocket = welcomeData.accept();

				DataOutputStream outData = new DataOutputStream(new BufferedOutputStream(dataSocket.getOutputStream()));

				String fileName = tokens.nextToken();
				File clientFilesDirectory = new File(".");
				String filePath = clientFilesDirectory + "/" + fileName;
				File file = new File(filePath);

				if (file.exists()) {
					byte[] bytes = new byte[(int) file.length()];
					FileOutputStream outputStream = new FileOutputStream(file);
					outputStream.write(bytes);
					outputStream.close();

					outData.write(bytes, 0, bytes.length);
				}

				else {
					System.out.println("File does not exist");
				}

				dataSocket.close();
				welcomeData.close();
				System.out.println("Data Socket closed");

			}

			else if (sentence.startsWith("quit")) {
				// Rob
				port = port + 2;
				outToServer.writeBytes(port + " " + sentence + " " + "\n");
				System.out.println("\nGoodbye");
				System.exit(0);
			}

		}
	}

}
