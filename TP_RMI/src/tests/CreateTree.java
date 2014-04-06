package tests;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import client.SiteItf;

public class CreateTree {

	/**
	 * 
	 * Contient le main pour récuperer les noeuds publié et ajouter les branches de l'arbre
	 * 
	 * @param args
	 * @throws RemoteException 
	 * @throws NotBoundException 
	 */
	public static void main(String[] args) throws RemoteException, NotBoundException {
		Registry registre = LocateRegistry.getRegistry(10000);

		SiteItf site1 = (SiteItf) registre.lookup("Node1");
		SiteItf site2 = (SiteItf) registre.lookup("Node2");
		SiteItf site3 = (SiteItf) registre.lookup("Node3");
		SiteItf site4 = (SiteItf) registre.lookup("Node4");
		SiteItf site5 = (SiteItf) registre.lookup("Node5");
		SiteItf site6 = (SiteItf) registre.lookup("Node6");


		site1.createEdge(site1, site2);
		site2.createEdge(site2, site3);
		site2.createEdge(site2, site4);
		site1.createEdge(site1, site5);
		site5.createEdge(site5, site6);

	}

}
