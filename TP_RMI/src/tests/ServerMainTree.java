package tests;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import server.SiteImplTree;

public class ServerMainTree {

	/**
	 * 
	 * Crée les serveurs (noeuds) et les publie sur le registre
	 * 
	 * @param args
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws AlreadyBoundException
	 */
	public static void main(String[] args) throws RemoteException,
			MalformedURLException, AlreadyBoundException {

		Registry registre = LocateRegistry.createRegistry(10000);
		ArrayList<SiteImplTree> sites = new ArrayList<SiteImplTree>();

		for (int i = 1; i < 7; i++) {
			sites.add(new SiteImplTree(i));
		}


		for (SiteImplTree site : sites) {
			site.exportServer(registre);
		}

	}
}
