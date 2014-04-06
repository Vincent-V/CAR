package Server;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import Client.SiteItf;

public class SiteImplTree implements SiteItf {

	private ArrayList<SiteItf> children;
	private SiteItf parent;
	protected int id;
	protected String name;

	/**
	 * 
	 * @param id
	 * @throws RemoteException
	 */
	protected SiteImplTree(int id) throws RemoteException {
		super();
		children = new ArrayList<SiteItf>();
		parent = null;
		this.id = id;
		this.name = "Node" + id;
	}
	
	public void exportServer(Registry registre) throws RemoteException, AlreadyBoundException {
		Remote obj = UnicastRemoteObject.exportObject(this, 10000);
		
        System.out.println("Bind avec "+this.getName());
        registre.bind(this.getName(), obj);
    }


	@Override
	public void createEdge(SiteItf parent, SiteItf child)
			throws RemoteException {

		child.setEnd(this);
		
		if (!this.children.contains(child)){
			this.children.add(child);
		}

	}
	
	@Override
	public void setEnd(SiteItf end) throws RemoteException {
		this.parent = end;
		
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

	public String getName() throws RemoteException {
		return name;
	}

	protected void printTrace(byte[] datas) {
		System.out.println(name + " a reçu " + new String(datas));
	}

	public int getIdent() throws RemoteException {
		return id;
	}

	

}
