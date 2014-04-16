package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 * Interface RMI coté serveur représentant un des noeuds de la structure
 * 
 * @author Vincent Vidal - Benjamin Burnouf
 * 
 */
public interface SiteItf extends Remote {

	/**
	 * 
	 * Crée un arc entre deux noeud de la structure
	 * 
	 * @param site1
	 *            Le premier site à lier
	 * @param site2
	 *            Le second
	 * @throws RemoteException
	 */
	public void createEdge(SiteItf site1, SiteItf site2) throws RemoteException;

	/**
	 * 
	 * Donne le nom de l'objet, c'est le nom utilisé pour le registre RMI
	 * 
	 * @return le nom de l'objet
	 * @throws RemoteException
	 */
	public String getName() throws RemoteException;

	/**
	 * 
	 * Donne une chaine représentant l'objet avec des informations sur le noeux
	 * et ses voisins/fils
	 * 
	 * @return La chaine contenant l'information
	 * @throws RemoteException
	 */
	public String getString() throws RemoteException;

	/**
	 * 
	 * Envoit un message à ce noeux qui sera diffusé à tous les autres noeuds.
	 * 
	 * @param source
	 *            La source du message, peut être -1 dans certains cas : -Dans
	 *            un arbre si le noeud est (ou doit etre) consideré comme la
	 *            source (le message ne remontera pas dans la hiérarchie) -Dans
	 *            un graphe, dans tous les cas (source pas necessaire
	 * @param datas
	 *            Le message à envoyer
	 * @throws RemoteException
	 */
	public void broadcast(int source, byte[] datas) throws RemoteException;

	/**
	 * 
	 * Retourne l'identifiant du noeud
	 * 
	 * @return L'identifiant
	 * @throws RemoteException
	 */
	public int getIdent() throws RemoteException;

	/**
	 * 
	 * Difinie le site spécifié comme étant un voisin
	 * 
	 * @param end
	 *            Le voisin
	 * @throws RemoteException
	 */
	public void setEnd(SiteItf end) throws RemoteException;

}
