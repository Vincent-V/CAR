import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
	
	File currentDir;

	public FtpRequest(Socket client) throws IOException {
		this.client = client;

		logins = new ArrayList<>();
		mdps = new ArrayList<>();

		logins.add("toto");
		mdps.add("bob");
		
		currentDir = new File(".");

		cReader = new BufferedReader(new InputStreamReader(
				client.getInputStream()));
		cWriter = new BufferedWriter(new OutputStreamWriter(
				client.getOutputStream()));

	}

	/**
	 * 
	 * Fonction permettant d'envoyer une chaine à un writer spécifique
	 * 
	 * @param writer
	 *            le writer sur lequel ecrire
	 * @param la
	 *            chaine à ecrire
	 * @throws IOException
	 */
	private void sendMessageTo(BufferedWriter writer, String s)
			throws IOException {
		System.out.println("envoit :" + s);
		writer.write(s + "\n");
		writer.flush();
	}

	/**
	 * 
	 * Methode permettant de séparer
	 * 
	 * @param s
	 * @return
	 */
	private static String getValue(String s) {
		return s.split(" ")[1];
	}

	private static String getCommande(String s) {
		return s.split(" ")[0];
	}

	/**
	 * 
	 * Methode permettant de demander une fois le couple login/mot de passe au
	 * client et de vérifier si il existe dans la base.
	 * 
	 * @return vrai si le client ftp réussi à s'authentifier, faux sinon
	 * @throws IOException
	 */
	private boolean checkCredentials() throws IOException {
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

	private void processUSER(String user) throws IOException {
		if (login == null) {
			login = user;
			sendMessageTo(cWriter, "331");
		}
	}

	private void processPASS(String mdp) throws IOException {
		if (login != null) {
			this.mdp = mdp;
			if (checkCredentials()) {
				logged = true;
				sendMessageTo(cWriter, "230");
				return;
			}
		}
		login = null;
		mdp = null;
		sendMessageTo(cWriter, "530");
		sendMessageTo(cWriter, "220");
		return;
	}

	private void openConnexionForDatas() throws IOException {
		sendMessageTo(cWriter, "150");
		clientData = new Socket(adressForDatas, portForDatas);
	}

	private void processPORT(String data) {
		String[] datas = data.split(",");
		try {
			adressForDatas = datas[0] + "." + datas[1] + "." + datas[2] + "."
					+ datas[3];
			portForDatas = Integer.parseInt(datas[4]) * 256
					+ Integer.parseInt(datas[5]);
			sendMessageTo(cWriter, "200");
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();// TODO gestion erreur
		}
	}

	private void processRETR(String filepath) throws IOException {
		int c;
		try {
			openConnexionForDatas();
			dWriter = new BufferedOutputStream(clientData.getOutputStream());
			FileInputStream ips = new FileInputStream(filepath);
			while ((c = ips.read()) != -1) {
				dWriter.write(c);
			}
			dWriter.close();
			ips.close();
			sendMessageTo(cWriter, "226");
		} catch (FileNotFoundException e) {
			sendMessageTo(cWriter, "550");
		}
	}
	
	private void processSTOR(String filepath) throws IOException {
		int c;
		openConnexionForDatas();
		dReader = new BufferedInputStream(clientData.getInputStream());
		FileOutputStream fos = new FileOutputStream(filepath);
		while ((c = dReader.read()) != -1) {
			fos.write(c);
		}
		dReader.close();
		fos.close();
		sendMessageTo(cWriter, "226");
	}
	
	private void processPWD() throws IOException{
		sendMessageTo(cWriter, currentDir.getAbsolutePath());
	}
	
	private void processCWD(String filepath) throws IOException{
		currentDir = new File(filepath);
		sendMessageTo(cWriter, "250");
		System.out.println("cd : " + currentDir);
	}
	
	private void processRMD(String pathname) throws IOException{
		File path = new File(pathname);
		path.delete();
		sendMessageTo(cWriter, "250");
	}

	private void processQUIT() throws IOException {
		sendMessageTo(cWriter, "221");
	}

	public void run() {
		String commande;
		try {
			sendMessageTo(cWriter, "220");

			while (true) {
				commande = cReader.readLine();
				System.out.println("recoit : " + commande);// erreur 500
				switch (getCommande(commande)) {
				case "USER":
					processUSER(getValue(commande));
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
				case "RMD":
					processRMD(getValue(commande));
					break;
				case "QUIT":
					processQUIT();
					System.out.println("on quite");
					return;
				case "PORT":
					processPORT(getValue(commande));
					break;
				default:
					sendMessageTo(cWriter, "202");
				}
			}
		} catch (IOException e) {
			/*
			 * 426 connexion closed Plus de connexion avec client, on suppose
			 * conenxion terminée, on quit le thread => evite fuite de memoire
			 */
			return;
		}

	}

}
