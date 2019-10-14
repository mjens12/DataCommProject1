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

		BufferedReader inFromUser = new BufferedReader(
				new InputStreamReader(System.in));
		Socket controlSocket = new Socket("127.0.0.1", port);

		System.out.println("You are connected to the server");
		System.out.println(
				"\nWhat would you like to do : \n retr: file.txt ||stor: file.txt  || quit\n");

		while (isOpen && clientGo) {

			DataOutputStream outToServer = new DataOutputStream(
					controlSocket.getOutputStream());
			DataInputStream inFromServer = new DataInputStream(
					new BufferedInputStream(
							controlSocket.getInputStream()));
			sentence = inFromUser.readLine();
			StringTokenizer tokens = new StringTokenizer(sentence);

			if (sentence.equals("list:")) {
				// Max

				port = port + 2;
				outToServer.writeBytes(
						port + " " + sentence + " " + '\n');

				ServerSocket welcomeData = new ServerSocket(port);
				Socket dataSocket = welcomeData.accept();

				DataInputStream inData = new DataInputStream(
						new BufferedInputStream(
								dataSocket.getInputStream()));
				try {
					modifiedSentence = inData.readUTF();
					System.out.print(modifiedSentence);
				} catch (EOFException e) {
					System.out.println("End of stream reached");
				}

				welcomeData.close();
				dataSocket.close();
				System.out.println(
						"\nWhat would you like to do next: \n retr: file.txt ||stor: file.txt  || quit");
			} else if (sentence.equals("retr:")) {

				int bytesRead;
				int current = 0;
				int filesize = 10;
				byte[] byteArray = new byte[10];
				port = port + 2;

				outToServer.writeBytes(
						port + " " + sentence + " " + '\n');
				ServerSocket welcomeData = new ServerSocket(port);
				Socket dataSocket = welcomeData.accept();

				//Might not need to supply a filename, the incoming file should already have a name
				String filename = "file.txt";
				//We probably need to create a new file here to hold the incoming file
				//Maybe pull the name from the file request message to the server?
				File file = new File(filename);
				FileInputStream fis = new FileInputStream(file);

				BufferedInputStream bis = new BufferedInputStream(
						fis);
				ByteArrayOutputStream byteos = new ByteArrayOutputStream();

				// Need to figure out how things work below
				// I just changed the relevant output stuff to input so nothing will work yet

				bytesRead = fis.read(byteArray, 0, byteArray.length);

				current = bytesRead;

				do {
					byteos.write(byteArray);
					current = fis.read(byteArray);
				} while (current > -1);

				bis.write(byteos.toByteArray());
				bis.flush();
				bis.close();

				System.out.println("\nFile Retrieved");

				bis.flush();
				bis.close();
				welcomeData.close();
				dataSocket.close();
				System.out.println(
						"\nWhat would you like to do next: \n retr: file.txt ||stor: file.txt  || quit");

			}

			else if (sentence.startsWith("stor: ")) {
				// Rob
				port = port + 2;
				outToServer.writeBytes(
						port + " " + sentence + " " + '\n');

				ServerSocket welcomeData = new ServerSocket(port);
				Socket dataSocket = welcomeData.accept();

				DataInputStream inData = new DataInputStream(
						new BufferedInputStream(
								dataSocket.getInputStream()));

				String fileName = tokens.nextToken();
				File clientFilesDirectory = new File(".");
				String filePath = clientFilesDirectory + "/"
						+ fileName;
				File file = new File(filePath);

				if (file.exists()) {
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
				welcomeData.close();
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
