package org.openxdata.workflow.engine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import org.openxdata.workflow.engine.persistent.Persistent;
import org.openxdata.workflow.engine.persistent.PersistentHelper;

/**
 *
 * @author kay
 */
public class NetList implements Persistent {

	private List<Net> nets;

	public NetList() {
	}

	public NetList(List<Net> nets) {
		this.nets = nets;
	}

	public void write(DataOutputStream dos) throws IOException {
		PersistentHelper.write(nets, dos);
	}

	public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
		nets = PersistentHelper.read(dis, Net.class);
	}

	public void setNets(List<Net> nets) {
		this.nets = nets;
	}

	public List<Net> getNets() {
		return nets;
	}
}
