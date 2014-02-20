package lille1.car.ftpServer;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;


/**
 *
 * Classe gerant une connexion d'un client au serveur FTP
 * Implemente les principales commandes FTP
 *
 */
public class FtpRequest extends Thread {

	private Socket client;
	private Socket clientData;

	/* liste des utilisateurs et leurs mdp */
	private ArrayList<String> logins;
	private ArrayList<String> mdps;

	/* Le login et mdp du client courant */
	private String login = null;
	private String mdp = null;

	/* si le client courant a reussi a se logguer */
	private boolean logged = false;

	/* Client reader for commands */
	private BufferedReader cReader;

	/* Client writer for commands */
	private BufferedWriter cWriter;

	private String adressForDatas;
	private int portForDatas;

	/* Client writer for datas */
	private BufferedOutputStream dWriter;

	/* Client reader for datas */
	private BufferedInputStream dReader;

	private ClientDirectory currentDir;
	
	private boolean running;

	/**
	 * 
	 * Cree un nouveau FtpRequest initialise avec la socket client recue
	 * par le serveur
	 * 
	 * @param client La socket client
	 * @throws IOException
	 */
	public FtpRequest(Socket client) throws IOException {
		this.client = client;
		
		running = true;

		logins = new ArrayList<>();
		mdps = new ArrayList<>();
		
		currentDir = new ClientDirectory(".");

		logins.add("toto");
		mdps.add("bob");

		cReader = new BufferedReader(new InputStreamReader(
				client.getInputStream()));
		cWriter = new BufferedWriter(new OutputStreamWriter(
				client.getOutputStream()));

	}

	/**
	 * 
	 * Fonction permettant d'envoyer une chaine de caracteres a un writter
	 * en respectant la syntaxe du protocole FTP.
	 * Sert principalement a envoyer les codes de retour des commandes.
	 * 
	 * @param writer
	 *            le writer sur lequel ecrire
	 * @param s
	 *            la chaine a envoyer
	 * @throws IOException
	 */
	public void sendMessageTo(BufferedWriter writer, String message)
			throws IOException {
		System.out.println("envoit :" + message);
		writer.write(message + "\n");
		writer.flush();
	}

	/**
	 * 
	 * A partir d'une commande FTP recu, renvoit la valeur
	 * ex : "RETR foo.txt" => "foo.txt"
	 * 
	 * @param command La commande recue
	 * @return la valeur de cette commande
	 */
	public static String getValue(String command) {
		return command.split(" ")[1];
	}

	/**
	 * 
	 * A partir d'une commande FTP recu, renvoit le nom de la commande
	 * ex : "RETR foo.txt" => "RETR"
	 * 
	 * @param command La commande recue
	 * @return Le nom de la commande
	 */
	public static String getCommande(String command) {
		return command.split(" ")[0];
	}

	/**
	 * 
	 * Methode permettant de demander une fois le couple login/mot de passe au
	 * client et de verifier si il existe dans la base.
	 * 
	 * @return vrai si le client ftp reussi a s'authentifier, faux sinon
	 * @throws IOException
	 */
	public boolean checkCredentials() throws IOException {
		int indexoflogin = logins.indexOf(this.login);
		if (indexoflogin != -1) {
			// Login existe dans la base
			if (mdps.get(indexoflogin).equals(mdp)) {
				// Le mot de passe correspond au login
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * Methode traitant la commande FTP USER
	 * Le login est mis a jour seulement si il est null, cf : si il n'a jamais ete saisi
	 * ou si un couple login/mdp faux a ete insere.
	 * 
	 * @param user Le nom utilisateur
	 * @throws IOException
	 */
	public void processUSER(String user) throws IOException {
		//On ne met a jour le login que si il est null, soit jamais saisi, soit saisi login/mdp erronee soit logout
		//Cela permet de gerer les saisi multiples de USER sans causer de problemes
		if (login == null) {
			login = user;
			sendMessageTo(cWriter, "331 user ok");
		}
	}

	/**
	 * 
	 * Methode traitant la commande FTP PASS
	 * Met a jour le mot de passe et test authentification
	 * 
	 * @param mdp
	 * @throws IOException
	 */
	public void processPASS(String mdp) throws IOException {
		if (login != null) {
			this.mdp = mdp;
			if (checkCredentials()) {
				// si couple login/mdp ok l'utilisateur est authentifie
				logged = true;
				sendMessageTo(cWriter, "230 pass ok");
				return;
			}
		}
		//Sinon on remet login/mdp a zero pour relancer le process d'authentification
		login = null;
		mdp = null;
		sendMessageTo(cWriter, "530 pass ko");
		// sendMessageTo(cWriter, "220 pass ko");
		return;
	}

	/**
	 * 
	 * Methode permettant d'ouvrir une connexion sur la socket de donnees fournie par la client
	 * Doit avoir ete precedee par la commande PORT en mode actif
	 * Generalement utilisee avant d'envoyer des donnees au client
	 * 
	 * La socket doit etre fermee apres l'envoi de donnees pour que le mecanisme fonctionne correctement.
	 * 
	 * @throws IOException
	 */
	public void openConnexionForDatas() throws IOException {
		sendMessageTo(cWriter, "150 ok");
		clientData = new Socket(adressForDatas, portForDatas);
	}

	/**
	 * 
	 * Methode traitant la commande FTP PASS
	 * Met a jour les informations (adresse ip et port pour la socket de donnees)
	 * Cette methode n'ouvre pas de connexion, permettant ainsi de modifier facilement ces informations
	 * entre chaque envoit de donnees
	 * 
	 * @param data Chaine contenant adresse ip et port au format definie pour le protocole FTP
	 */
	public void processPORT(String data) {
		String[] datas = data.split(",");
		try {
			adressForDatas = datas[0] + "." + datas[1] + "." + datas[2] + "."
					+ datas[3];
			portForDatas = Integer.parseInt(datas[4]) * 256
					+ Integer.parseInt(datas[5]);
			sendMessageTo(cWriter, "200 ok");
		} catch (NumberFormatException | IOException e) {
			//On arrete la thread car la client à envoyer une mauvaise adresse ou mauvais port pour la socket
			//Peut causer des problemes si le clietn considere que la socket est correcte
			running = false;
		}
	}

	/**
	 * 
	 * Methode traitant la commande FTP RETR
	 * Permet de recuperer un fichier sur le serveur
	 * 
	 * @param filepath le chemin absolue ou relatif du fichier
	 * @throws IOException
	 */
	public void processRETR(String filepath) throws IOException {
		int c;
		try {
			//permet d'obtenir le chemin reel du fichier, si le chemin est relatif
			filepath = currentDir.getRealPath(filepath);
			
			//ouvre la connexion avec les informations obtenues par la commande PORT
			openConnexionForDatas();
			
			dWriter = new BufferedOutputStream(clientData.getOutputStream());
			FileInputStream ips = new FileInputStream(filepath);
			while ((c = ips.read()) != -1) {
				dWriter.write(c);
			}
			//fermeture de la connexion apres chaque envoit de donnees
			dWriter.close();
			ips.close();
			sendMessageTo(cWriter, "226 ok");
		} catch (FileNotFoundException e) {
			sendMessageTo(cWriter, "550 ok");
		}
	}

	/**
	 * 
	 * Methode traitant la commande FTP STOR
	 * Permet d'envoyer un fichier sur le serveur
	 * 
	 * @param filepath le chemin absolue ou relatif du fichier
	 * @throws IOException
	 */
	public void processSTOR(String filepath) throws IOException {
		if (!logged) {
			sendMessageTo(cWriter, "532 not logged");
			return;
		}
		int c;
		//permet d'obtenir le chemin reel du fichier, si le chemin est relatif
		filepath = currentDir.getRealPath(filepath);
		
		//ouvre la connexion avec les informations obtenues par la commande PORT
		openConnexionForDatas();
		
		dReader = new BufferedInputStream(clientData.getInputStream());
		FileOutputStream fos = new FileOutputStream(filepath);
		while ((c = dReader.read()) != -1) {
			fos.write(c);
		}
		//ferme la connexion apres chaque envoit de donnees
		dReader.close();
		fos.close();
		sendMessageTo(cWriter, "226 ok");
	}

	/**
	 * 
	 * Methode traitant la commande FTP PWD
	 * Envoit le repertoir courant du serveur
	 * 
	 * @throws IOException
	 */
	public void processPWD() throws IOException {
		sendMessageTo(cWriter, "257 " + currentDir.getAbsolutePath());
	}

	
	/**
	 * 
	 * Methode traitant la commande FTP CWD
	 * Met a jour le ClientDirectory avec le nouveau chemin
	 * 
	 * @param filepath
	 * @throws IOException
	 */
	public void processCWD(String filepath) throws IOException {
		currentDir.setNewPath(filepath);
		sendMessageTo(cWriter, "250 ok");
	}


	/**
	 * 
	 * Methode traitant la commande FTP QUIT
	 * 
	 * 
	 * @throws IOException
	 */
	public void processQUIT() throws IOException {
		sendMessageTo(cWriter, "221 ok");
	}
	
	/**
	 * 
	 * Methode traitant la commande FTP CDUP
	 * Equivalent a CWD mais pour le repertoir parent
	 * 
	 * @throws IOException
	 */
	public void processCDUP() throws IOException {
		currentDir.setToParent();
		sendMessageTo(cWriter, "200 ok");
		
	}

	/**
	 * 
	 * Methode traitant la commande FTP LIST
	 * Envoit sur la socket de data, une chaine contenant tous les fichiers du repertoir courant au format EPLF
	 * 
	 * @throws IOException
	 */
	public void processLIST() throws IOException {
		String s = currentDir.listDirectory();
		openConnexionForDatas();
		dWriter = new BufferedOutputStream(clientData.getOutputStream());
		dWriter.write(s.getBytes());

		dWriter.close();
		sendMessageTo(cWriter, "226 ok");

	}

	/**
	 * 
	 * Methode principale du thread, permet de traiter toutes les commandes arrivant
	 * 
	 */
	public void run() {
		String commande;
		try {
			sendMessageTo(cWriter, "220 coucou");

			while (running == true) {
				commande = cReader.readLine();
				System.out.println("recoit : " + commande);// erreur 500
				switch (getCommande(commande)) {
				case "USER":
					processUSER(getValue(commande));
					break;
				case "LIST":
					processLIST();
					break;
				case "PASS":
					processPASS(getValue(commande));
					break;
				case "PWD":
					processPWD();
					break;
				case "CWD":
					processCWD(getValue(commande));
					break;
				case "RETR":
					processRETR(getValue(commande));
					break;
				case "STOR":
					processSTOR(getValue(commande));
					break;
				case "QUIT":
					processQUIT();
					//Permet de quitter le thread
					return;
				case "CDUP":
					processCDUP();
					break;
				case "PORT":
					processPORT(getValue(commande));
					break;
				default:
					sendMessageTo(cWriter, "202 command not implemented");
				}
			}
		} catch (IOException e) {
			/**
			 * 
			 * Gestion des erreurs :
			 * 
			 * Toutes les exceptions de types IOExcpetion sont transmises jusqu'a cette methode
			 * En cas de IOExpetion, on considere que la connexion avec le client est perdue et on quitte le thread
			 * Ce comportement permet de de gerer les client quittant sans envoyer le message QUIT et evite ainsi les fuites de m�moire
			 * 
			 * 
			 */
			return;

		}
		catch (NullPointerException e){
			/**
			 * 
			 * Si le client envoit  une commande vide, 
			 * on ne fait rien, on continue de recevoir des commandes.
			 * 
			 * 
			 */
		}

	}

}
