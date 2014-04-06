package Server;

import java.rmi.RemoteException;
import java.util.ArrayList;

import Client.SiteItf;

public class SiteImplGraph extends SiteImplTree {

	private ArrayList<SiteItf> ends;
	private ArrayList<String> logs;

	protected SiteImplGraph(int id) throws RemoteException {
		super(id);
		ends = new ArrayList<SiteItf>();
		logs = new ArrayList<String>();
	}

	@Override
	public String getString() throws RemoteException {
		return this.name;
	}

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
