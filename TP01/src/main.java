/* Dufeu Jean-Philippe
 * Vidal Vincent
 * 
 * TP1 Car
 * 30/01/2014
 */

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class main {

	static void sendTo(String s, OutputStreamWriter o) throws IOException {
		o.write(s + "\n", 0, s.length() + 1);
		o.flush();
		return;
	}

	static String getValue(String s) {
		return s.split(" ")[1];
	}

	/**
	 * @param args
	 */
	
	
	//TODO login ne marche pas => retourne 530 avec bon login, c'est caca
	public static void main(String[] args) {
		ArrayList<String> logins = new ArrayList<String>();
		ArrayList<String> mdps = new ArrayList<String>();
		logins.add("toto");
		mdps.add("bob");
		ServerSocket s;
		int c = 0;
		Socket client;
		InputStreamReader r;
		OutputStreamWriter o;
		char[] buf = new char[1024];
		try {
			s = new ServerSocket(2121);
			System.out.println("En attente d'un client");
			client = s.accept();
			r = new InputStreamReader(client.getInputStream());
			o = new OutputStreamWriter(client.getOutputStream());
			System.out.println("Client connecté");
			// 220 : code service ready new user
			// (http://www.w3.org/Protocols/rfc959/8_PortNumber.html 4.2.1)
			
			sendTo("220", o);
			c = r.read(buf, 0, 1024);
			System.out.println("c : "+c);
			buf[1] = '\0';
			String login = getValue(new String(buf));
			System.out.println(login.length());
			login = login.substring(0, 1);
			System.out.println(login);
			System.out.println(logins.get(0));
			
			buf = new char[1024];
			
			sendTo("331", o);
			c = r.read(buf, 0, 1024);
			String mdp = getValue(new String(buf));
			System.out.println(mdp);
			System.out.println(mdps.get(0));

			int indexoflogin = logins.indexOf(login);
			if (indexoflogin != -1) {
				if (mdps.get(indexoflogin).equals(mdp)) {
					sendTo("230", o);
				} else {
					System.out.println("mauvais mdp");
					sendTo("530", o);
				}
			} else {
				System.out.println("pas de login");
				sendTo("530", o);
			}

			System.out.println("Fin lecture sur client");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
