package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface SiteItf extends Remote {
	
	public void createEdge(SiteItf parent, ArrayList<SiteItf> children) throws RemoteException;

	public String getName() throws RemoteException;
	
	public String getString() throws RemoteException;
	
	public void broadcastFromRoot(byte[] datas) throws RemoteException;
	
	public void broadcastFromNode(int source, byte[] datas) throws RemoteException;
	
	public int getId() throws RemoteException;

}
