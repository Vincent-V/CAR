package Client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import Server.SiteImpl;

public class BroadcastMessage {

	
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		
		Registry registre = LocateRegistry.getRegistry(2020);
		
		SiteItf site1 = (SiteItf) registre.lookup("Node1");
		
		site1.broadcastFromNode(1, "totoro".getBytes());
	}

}
