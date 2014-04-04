package Client;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class CreateTree {

	public static void main(String[] args) throws MalformedURLException,
			RemoteException, NotBoundException {

		Registry registre = LocateRegistry.getRegistry(2020);

		SiteItf site1 = (SiteItf) registre.lookup("Node1");
		SiteItf site2 = (SiteItf) registre.lookup("Node2");
		SiteItf site3 = (SiteItf) registre.lookup("Node3");
		SiteItf site4 = (SiteItf) registre.lookup("Node4");
		SiteItf site5 = (SiteItf) registre.lookup("Node5");
		SiteItf site6 = (SiteItf) registre.lookup("Node6");

		System.out.println("client : on a récupéré les objets");

		ArrayList<SiteItf> children = new ArrayList<SiteItf>();

		children.add(site2);
		children.add(site5);
		site1.createEdge(null, children);
		children.clear();

		children.add(site3);
		children.add(site4);
		site2.createEdge(site1, children);
		children.clear();

		children.add(site6);
		site5.createEdge(site1, children);
		children.clear();

		site3.createEdge(site2, children);
		site4.createEdge(site2, children);

		site6.createEdge(site5, children);

	}

}
