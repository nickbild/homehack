import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.Runtime;

/*
* Nick Bild
* 2018-03-02
* Listen for remote commands from HomeHack client
* and take appropriate actions.
*/
public class HomeHackServer {

	public static void main(String[] args) throws IOException {
        	// Check input.
		if (args.length != 1) {
			System.err.println("Usage: java HomeHackServer <port>");
			System.exit(1);
		}
        
        	int port = Integer.parseInt(args[0]);

		// Set listening port.
		ServerSocket serverSocket = new ServerSocket(port);
		Socket clientSocket = null;

		System.out.println("Starting Server on port: " + port);

		// Infinite loop to accept new connections.
		while (true) {
	        	try {
				// Open a new client connection.
				clientSocket = serverSocket.accept();

				// Spawn new thread to handle client.
				Runnable clientHandler = new ClientHandler(clientSocket);
				Thread t = new ClientHandler(clientSocket);
				t.start();

			} catch (Exception e) { // Catch errors and clean up.
				clientSocket.close();
	                	e.printStackTrace();
			}
		}
	}
}

/*
* Nick Bild
* 2018-03-02
* ClientHandler is ran in a thread for each individual client connection.
* It handles all server interaction with the clients.
*/
class ClientHandler extends Thread {
	private final Socket clientSocket;

	public ClientHandler(Socket clientSocket) {
	        this.clientSocket = clientSocket;
	}

	public void run() {
		try {
			// Open readers and writers on socket.
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                        // Listen for 1 line of text.
                        String data = in.readLine();
                        
			if (data.equals("hamster dance on")) {
				// Link up the hamster as the current image shown.
				String[] cmd = {
					"/bin/sh",
					"-c",
					"sudo ln -f /var/www/html/hamster.gif /var/www/html/showme.gif"
				};
				Process pr = Runtime.getRuntime().exec(cmd);
				
				out.println("Response: Hamster dance on.");
			}

			if (data.equals("hamster dance off")) {
                                // Link up the white block as the current image shown.
                                String[] cmd = {
                                        "/bin/sh",
                                        "-c",
                                        "sudo ln -f /var/www/html/white.gif /var/www/html/showme.gif"
                                };
                                Process pr = Runtime.getRuntime().exec(cmd);

                                out.println("Response: Hamster dance off.");
                        }

			clientSocket.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());

		}

	}
}

