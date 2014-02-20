package lille1.car.ftpServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * Classe representant le serveur
 * Contient la méthode main et lance un thread par client accepté
 *
 */
public class Server {

	private ServerSocket server;
	private Socket client;

	/**
	 * 
	 * Constructeur du serveur ecoutant sur le port spécifié
	 * 
	 * @param port le port sur lequel ecouter
	 * @throws IOException
	 */
	public Server(int port) throws IOException {
		server = new ServerSocket(port);
	}

	/**
	 * 
	 * Methode bloquante acceptant la connection d'un client
	 * 
	 * @throws IOException
	 */
	public void accept() throws IOException {
		client = server.accept();
	}

	/**
	 * 
	 * Lance un nouvel objet FtpRequest (threadé) 
	 * avec la socket recue du client
	 * 
	 * @throws IOException
	 */
	public void runThread() throws IOException {
		new FtpRequest(client).start();
	}
	
	/**
	 * 
	 * Methode main ne demandant pas d'argument
	 * Initialise le serveur et lance la boucle d'attente de connexion client
	 * 
	 * @param args 
	 * @throws IOException
	 */
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
