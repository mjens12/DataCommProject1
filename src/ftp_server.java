import java.io.*;
import java.net.*;
import java.util.*;

public class ftp_server {

	public static void main(String[] args) throws IOException

	{
		int port = 1200;

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
	int port = 1200;

	// path for Max
	File directory = new File("/home/collirob/Documents/CIS457/Project1/TestServerDataLocation");

	List<String> listOfFiles = new ArrayList<>();
	String firstln;
	private Socket connectionSocket;
	String fileName;
	StringTokenizer tokens = new StringTokenizer("");

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
		if (directory.exists()) {
			for (File file : directory.listFiles()) {
				if (file.isFile()) {
					listOfFiles.add(file.getName());
				}

			}
		}
	}

	public void run() {

		try {

			fromClient = inFromClient.readLine();
			if (fromClient != null) {
				tokens = new StringTokenizer(fromClient);
				firstln = tokens.nextToken();
				port = Integer.parseInt(firstln);
				clientCommand = tokens.nextToken();
				// fileName = tokens.nextToken();
			}

			if (fileName == null) {
				fileName = "noFileFound.txt";
			}
			serverFiles(directory, listOfFiles);

			if (clientCommand.equals("list:")) {
				// Max
				Socket dataSocket = new Socket(connectionSocket.getInetAddress(), port);
				DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());

				if (listOfFiles.isEmpty()) {
					dataOutToClient.writeUTF("There are no files on the server");
					System.out.println("Telling the client there are no files on the server");
				} else {
					String outputList = new String("");
					int x = 0;
					outputList.concat("The files on the server are:\n");
					while (x < listOfFiles.size()) {
						outputList = outputList.concat(listOfFiles.get(x) + "\n");
						x++;
					}
					System.out.println("Sending the client the files on the server");
					dataOutToClient.writeUTF(outputList);

				}

				dataSocket.close();

				System.out.println("Closing data connection");
			}

			if (clientCommand.equals("retr:")) {
				Socket dataSocket = new Socket(connectionSocket.getInetAddress(), port);
				DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());
				// String fileName = "file.txt";
				String fileName = tokens.nextToken();
				String filePath = directory.getPath() + "/" + fileName;
				File myFile = new File(filePath);
				//System.out.println(filePath);
				if (myFile.exists()) {
					byte[] mybytearray = new byte[(int) myFile.length() + 1];
					FileInputStream fis = new FileInputStream(myFile);
					BufferedInputStream bis = new BufferedInputStream(fis);
					bis.read(mybytearray, 0, mybytearray.length);
					System.out.println("Sending...");
					dataOutToClient.write(mybytearray, 0, mybytearray.length);
					dataOutToClient.flush();
					bis.close();
				} else {
					System.out.println("File Not Found");
				}

				dataSocket.close();
				System.out.println("Data Socket closed");
			}
			if (clientCommand.equals("stor:")) {
				// Sean
				String fileName = tokens.nextToken();
				Socket dataSocket = new Socket(connectionSocket.getInetAddress(), port);
				DataInputStream dataFromClient = new DataInputStream(dataSocket.getInputStream());

				int filesize = 6022386;
				int bytesRead;
				int current = 0;
				byte[] mybytearray = new byte[filesize];

				FileOutputStream fos = new FileOutputStream(fileName);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				bytesRead = dataFromClient.read(mybytearray, 0, mybytearray.length);
				current = bytesRead;

				do {
					bytesRead = dataFromClient.read(mybytearray, current, (mybytearray.length - current));
					if (bytesRead >= 0)
						current += bytesRead;
				} while (bytesRead > -1);

				bos.write(mybytearray, 0, current);
				bos.flush();
				bos.close();
				dataSocket.close();
				System.out.println("\nFile Retrieved");
				System.out.println("Datasocket Closed");
			}
			if (clientCommand.equals("quit")) {
				// Sean
			}

		}

		catch (

		IOException e) {
			System.out.println("Error: Unable to disconnect");
			System.out.println(e);
		}
	}

}
