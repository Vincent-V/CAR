package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 * @author Vincent Vidal - Benjamin Burnouf
 * 
 */
public interface SiteItf extends Remote {

	/**
	 * 
	 * @param parent
	 * @param children
	 * @throws RemoteException
	 */
	public void createEdge(SiteItf site1, SiteItf site2)
			throws RemoteException;

	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public String getName() throws RemoteException;

	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public String getString() throws RemoteException;

	/**
	 * 
	 * @param source
	 * @param datas
	 * @throws RemoteException
	 */
	public void broadcast(int source, byte[] datas) throws RemoteException;

	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public int getIdent() throws RemoteException;
	
	
	/**
	 * 
	 * @param end
	 * @throws RemoteException
	 */
	public void setEnd(SiteItf end) throws RemoteException;

}
