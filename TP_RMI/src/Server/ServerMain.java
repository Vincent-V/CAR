package Server;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.*;

public class ServerMain {

	public static void main(String[] args) throws RemoteException, MalformedURLException, AlreadyBoundException {

			SiteImpl site1 = new SiteImpl(1);
			SiteImpl site2 = new SiteImpl(2);
			SiteImpl site3 = new SiteImpl(3);
			SiteImpl site4 = new SiteImpl(4);
			SiteImpl site5 = new SiteImpl(5);
			SiteImpl site6 = new SiteImpl(6);
			
			System.out.println("Server : objet créé");
			Registry registre = LocateRegistry.createRegistry(2020);
			registre.bind(site1.getName(), site1);
			registre.bind(site2.getName(), site2);
			registre.bind(site3.getName(), site3);
			registre.bind(site4.getName(), site4);
			registre.bind(site5.getName(), site5);
			registre.bind(site6.getName(), site6);
			System.out.println("Server : objets ajoutés au registre");
		
	}

}
