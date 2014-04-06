package tests;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import server.SiteImplGraph;

/**
 * 
 * Crée les serveurs (noeuds) et les publie sur le registre
 * 
 * @author Vincent Vidal - Benjamin Burnouf
 * 
 */
public class ServerMainGraph {

	/**
	 * 
	 * @param args
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws AlreadyBoundException
	 */
	public static void main(String[] args) throws RemoteException,
			MalformedURLException, AlreadyBoundException {

		Registry registre = LocateRegistry.createRegistry(10000);
		ArrayList<SiteImplGraph> sites = new ArrayList<SiteImplGraph>();

		for (int i = 1; i < 7; i++) {
			sites.add(new SiteImplGraph(i));
		}


		for (SiteImplGraph site : sites) {
			site.exportServer(registre);
		}


	}

}
