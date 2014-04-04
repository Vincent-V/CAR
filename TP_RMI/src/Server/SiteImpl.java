package Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import Client.SiteItf;

public class SiteImpl implements SiteItf {

	private static final long serialVersionUID = 1L;
	private ArrayList<SiteItf> children;
	private SiteItf parent;
	private int id;
	private String name;

	/**
	 * 
	 * @param id
	 * @throws RemoteException
	 */
	protected SiteImpl(int id) throws RemoteException {
		super();
		children = new ArrayList<SiteItf>();
		parent = null;
		this.id = id;
		this.name = "Node" + id;
	}

	@Override
	public void createEdge(SiteItf parent, ArrayList<SiteItf> children)
			throws RemoteException {

		this.setParent(parent);

		for (SiteItf child : children) {
			this.addChild(child);
		}

	}

	/* source == -1 pour broadcast from root */
	@Override
	public void broadcast(int source, final byte[] datas)
			throws RemoteException {

		this.printTrace(datas);

		if (source != -1 && parent != null && parent.getIdent() != source) {
			new Thread() {
				public void run() {
					try {
						parent.broadcast(getIdent(), datas);
					} catch (RemoteException e) {
					}
				}
			}.start();

		}
		for (final SiteItf child : children) {
			if (child.getIdent() != source) {
				new Thread() {
					public void run() {
						try {
							child.broadcast(getIdent(), datas);
						} catch (RemoteException e) {
						}
					}
				}.start();

			}
		}
	}

	@Override
	public String getString() throws RemoteException {
		String result = name + " fils de " + parent.getName() + " et pere de :";
		for (SiteItf child : children) {
			result += child.getName();
		}
		return result;
	}

	private void addChild(SiteItf child) throws RemoteException {
		if (!children.contains(child)) {
			children.add(child);
		}
	}

	private void setParent(SiteItf parent) throws RemoteException {
		this.parent = parent;
	}

	public String getName() throws RemoteException {
		return name;
	}

	private void printTrace(byte[] datas) {
		System.out.println(name + " a reçu " + new String(datas));
	}

	public int getIdent() throws RemoteException {
		return id;
	}

}
