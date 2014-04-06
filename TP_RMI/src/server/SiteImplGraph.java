package server;

import java.rmi.RemoteException;
import java.util.ArrayList;

import client.SiteItf;

/**
 * 
 * Implémentation de l'interface SiteItf sous forme de graphe non orienté
 * 
 * @author Vincent Vidal - Benjamin Burnouf
 *
 */
public class SiteImplGraph extends SiteImplTree {

	private ArrayList<SiteItf> ends;
	private ArrayList<String> logs;

	/**
	 * 
	 * Crée un noeud vide (sans voisins)
	 * 
	 * @param id L'identifiant du noeud
	 * @throws RemoteException
	 */
	public SiteImplGraph(int id) throws RemoteException {
		super(id);
		ends = new ArrayList<SiteItf>();
		logs = new ArrayList<String>();
	}

	@Override
	public String getString() throws RemoteException {
		String result = name + " avec pour voisins :";
		for (SiteItf end : ends) {
			result += end.getName();
		}
		return result;
	}

	/**
	 * Envoit un message à ce noeux qui sera diffusé à tous les autres noeuds.
	 * Dans cette implémentation, un message broadcasté peut être reçu plusieurs fois par un même noeud
	 * 
	 * @param source La source (peut etre egale à -1 dans le cas d'un graph)
	 * @param datas Le message à envoyer
	 * 
	 */
	@Override
	public void broadcast(int source, final byte[] datas)
			throws RemoteException {

		String message = new String(datas);

		if (!logs.contains(message)) {
			this.printTrace(datas);
			for (final SiteItf end : ends) {
				new Thread() {
					public void run() {
						try {
							end.broadcast(0, datas);
						} catch (RemoteException e) {
						}
					}
				}.start();
			}

			logs.add(0, message);
		}
	}

	@Override
	public void createEdge(SiteItf site1, SiteItf site2) throws RemoteException {

		if (!this.ends.contains(site2)) {
			this.ends.add(site2);
		}

		site2.setEnd(site1);

	}

	@Override
	public void setEnd(SiteItf end) throws RemoteException {
		if (!this.ends.contains(end)) {
			this.ends.add(end);
		}

	}

}
