package tests;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import client.SiteItf;

/**
 * 
 * Class contenant le main pour envoyer un message
 * 
 * @author Vincent Vidal - Benjamin Burnouf
 * 
 */
public class BroadcastMessage {

	/**
	 * 
	 * @param args
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public static void main(String[] args) throws MalformedURLException,
			RemoteException, NotBoundException {

		Registry registre = LocateRegistry.getRegistry(10000);

		int source;
		String message;

		try {
			source = Integer.parseInt(args[0]);
			message = args[1];
		} catch (Exception e1) {
			System.out
					.println("Erreur arguments: BroadcastMessage source message");
			return;
		}

		SiteItf site1;
		if (source == -1) {
			site1 = (SiteItf) registre.lookup("Node1");
		}
		else{
			site1 = (SiteItf) registre.lookup("Node"+source);
		}

		site1.broadcast(source, message.getBytes());
	}

}
