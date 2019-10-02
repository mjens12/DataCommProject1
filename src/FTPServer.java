import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

class FTPServer {

	public static void main(String argv[]) throws Exception {
		String fromClient;
		String clientCommand;
		byte[] data;
		int port = 0;

		ServerSocket welcomeSocket = new ServerSocket(12000);
		String frstln;

		while (true) {
			Socket connectionSocket = welcomeSocket.accept();

			DataOutputStream outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());
			BufferedReader inFromClient =
					new BufferedReader(new InputStreamReader(
							connectionSocket.getInputStream()));

			fromClient = inFromClient.readLine();

			StringTokenizer tokens = new StringTokenizer(fromClient);
			frstln = tokens.nextToken();
			port = Integer.parseInt(frstln);
			clientCommand = tokens.nextToken();

			if (clientCommand.equals("list:")) {

				Socket dataSocket = new Socket(
						connectionSocket.getInetAddress(), port);
				DataOutputStream dataOutToClient =
						new DataOutputStream(
								dataSocket.getOutputStream());

				// Max ..........................

				dataSocket.close();
				System.out.println("Data Socket closed");
			}

			if (clientCommand.equals("retr:")) {
				// Isfar..............................
				// ..............................
			}

			if (clientCommand.equals("stor:")) {
				// Sean ..............................
				// ..............................
			}

			if (clientCommand.equals("quit")) {
				// Sean ..............................
				// ..............................
			}
		}
	}
}