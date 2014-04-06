package Server;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 * 
 * @author Vincent Vidal - Benjamin Burnouf
 *
 */
public class ServerMain {

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
        		
		for (int i = 1; i < 7; i++){
			sites.add(new SiteImplGraph(i));
		}

		System.out.println("Server : objets créés");
		
		
		for (SiteImplGraph site : sites){
			site.exportServer(registre);
		}
		
		System.out.println("Server : objets exportés");

	}

}
