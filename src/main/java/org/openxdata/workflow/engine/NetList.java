package org.openxdata.workflow.engine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import org.openxdata.db.util.Persistent;
import org.openxdata.db.util.PersistentHelper;

/**
 *
 * @author kay
 */
public class NetList implements Persistent {

	private Vector<Net> nets;

	public NetList() {
	}

	public NetList(Vector<Net> nets) {
		this.nets = nets;
	}

	public void write(DataOutputStream dos) throws IOException {
		PersistentHelper.write(nets, dos);
	}

	public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
		nets = PersistentHelper.read(dis, new Net().getClass());
	}

	public void setNets(Vector<Net> nets) {
		this.nets = nets;
	}

	public Vector<Net> getNets() {
		return nets;
	}
}
