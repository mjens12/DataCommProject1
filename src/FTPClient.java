import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;

class FTPClient {

	public static void main(String argv[]) throws Exception {
		String sentence;
		String modifiedSentence;
		boolean isOpen = true;
		int number = 1;
		boolean notEnd = true;
		String statusCode;
		boolean clientgo = true;

		int port = 12000;

		BufferedReader inFromUser = new BufferedReader(
				new InputStreamReader(System.in));
		sentence = inFromUser.readLine();
		StringTokenizer tokens = new StringTokenizer(sentence);

		if (sentence.startsWith("connect")) {
			//Max
			String serverName = tokens.nextToken(); // pass the connect command
			serverName = tokens.nextToken();
			int port1 = Integer.parseInt(tokens.nextToken());
			System.out.println("You are connected to " + serverName);

			Socket ControlSocket = new Socket(serverName, port1);

			System.out.println("You are connected");

			while (isOpen && clientgo) {

				DataOutputStream outToServer = new DataOutputStream(
						ControlSocket.getOutputStream());
				DataInputStream inFromServer = new DataInputStream(
						new BufferedInputStream(
								ControlSocket.getInputStream()));

				sentence = inFromUser.readLine();

				if (sentence.equals("list:")) {
					
					port = port + 2;
					outToServer.writeBytes(
							port + " " + sentence + " " + '\n');

					ServerSocket welcomeData = new ServerSocket(port);
					Socket dataSocket = welcomeData.accept();

					DataInputStream inData = new DataInputStream(
							new BufferedInputStream(
									dataSocket.getInputStream()));
					while (notEnd) {
						modifiedSentence = inData.readUTF();
						// Max ......................
					}

					welcomeData.close();
					dataSocket.close();
					System.out.println(
							"\nWhat would you like to do next: \n retr: file.txt ||stor: file.txt  || close");

				} else if (sentence.startsWith("retr: ")) {
					// Isfar
					int bytesRead;
					int current = 0;
					byte[] byteArray = new byte[10];

					outToServer.writeBytes(
							port + " " + sentence + " " + '\n');

					ServerSocket welcomeData = new ServerSocket(port);
					Socket dataSocket = welcomeData.accept();

					DataInputStream inData = new DataInputStream(
							new BufferedInputStream(
									dataSocket.getInputStream()));

					FileOutputStream fos = new FileOutputStream(
							"def");
					BufferedOutputStream bos = new BufferedOutputStream(
							fos);
					bytesRead = inData.read(byteArray, 0,
							byteArray.length);
					current = bytesRead;

					do {
						bytesRead = inData.read(byteArray, current,
								(byteArray.length - current));
						if (bytesRead >= 0)
							current += bytesRead;
					} while (bytesRead > -1);

					bos.write(byteArray, 0, current);
					bos.flush();
					bos.close();

					System.out.println("\nFile Retrieved");
					welcomeData.close();
					dataSocket.close();
					System.out.println(
							"\nWhat would you like to do next: \n retr: file.txt ||stor: file.txt  || close");

				}
				else if (sentence.startsWith("stor: ")) {
					//Rob
				}
				else if (sentence.startsWith("quit")) {
					//Rob
				}

			}
		}
		
	}
}
