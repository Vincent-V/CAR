package lille1.car.ftpServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private ServerSocket server;
	private Socket client;

	Server(int port) throws IOException {
		server = new ServerSocket(port);
	}

	private void accept() throws IOException {
		client = server.accept();
	}

	private void runThread() throws IOException {
		new FtpRequest(client).start();
	}

	public static void main(String[] args) throws IOException {

		Server server = new Server(2121);
		while (true) {
			server.accept();
			System.out.println("accepted");
			server.runThread();
			System.out.println("thread launched");

		}

	}

}
