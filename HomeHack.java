import java.io.*;
import java.net.*;


public class HomeHack {

	public static void main(String[] args) {
		int last = 999; // Impossible value.

		// Infinite loop.
		while (true) {
			// Is the laptop plugged in?
			int status = checkAC();

			// Skip if the status hasn't changed.
			if (status == last) { continue; }

			if (status == 1) {
				System.out.println("Got power!");
				sendRequest("hamster dance on");


			} else {
				System.out.println("Unplugged.");
				sendRequest("hamster dance off");

			}

			last = status;

			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				System.out.println("Error sleeping: " + e);
			}

		}
	}

	public static int checkAC() {
		int status = 0;
		String s = "";

		try {
			Process process = Runtime.getRuntime().exec(new String[]{"bash", "-c", "acpi -a"});

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));                                          
			s = reader.readLine();                      

		} catch (Exception e) {
			System.out.println("Error getting AC status: " + e);

		}  

		//System.out.println(s);
		if (s.equals("Adapter 0: on-line")) {
			status = 1;
		}

		return status;

	}

	public static int sendRequest(String req) {
		// AWS VM.
		String host = "18.218.241.166";
	        int port = 8080;

	        try ( // Try to open socket connection.
			// Establish a socket connection with the server.
			Socket serverConn = new Socket(host, port);

			// Open readers and writers on the socket.
			PrintWriter out = new PrintWriter(serverConn.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(serverConn.getInputStream()));

	        ) { // Socket opened successfully.
			// Send data to server.
			out.println(req);

			// Get result from server.
                	System.out.println(in.readLine());

			// Clean up.
			serverConn.close();

	        } catch (UnknownHostException e) { // Host connection error.
			System.err.println("Failed to connect to host: " + host);
			System.exit(1);

		} catch (IOException e) { // Socket I/O error.
			System.err.println("Error getting I/O from: " + host);
			System.exit(1);
		}

		return 0;

	}
}

