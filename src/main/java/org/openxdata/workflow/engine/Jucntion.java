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
public abstract class Jucntion implements Persistent {

	public static byte TYPE_XOR = 1;
	public static byte TYPE_DIRECT = 2;
	public static byte TYPE_AND = 3;
	public static byte TYPE_OR = 4;
	private Vector<Flow> flows = new Vector<Flow>(0);
	private Net rootNet;
	private byte type;

	public Jucntion() {
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public boolean isDirect() {
		return TYPE_DIRECT == type;
	}

	public boolean isXOR() {
		return TYPE_XOR == type;
	}

	public boolean isAND() {
		return TYPE_AND == type;
	}

	public boolean isOR() {
		return TYPE_OR == type;
	}

	public Net getRootNet() {
		return rootNet;
	}

	public Vector<Flow> getFlows() {
		return flows;
	}

	public void setFlows(Vector<Flow> outFlows) {
		this.flows = outFlows;
	}

	public void setRootNet(Net rootNet) {
		this.rootNet = rootNet;
		for (int i = 0; i < flows.size(); i++) {
			Flow flow = flows.elementAt(i);
			flow.setRootNet(rootNet);
		}
	}

	public Vector<Task> getAllNextTasks() {
		Vector<Task> tasks = new Vector<Task>();
		for (int i = 0; i < flows.size(); i++) {
			Flow flow = flows.elementAt(i);
			tasks.addElement(flow.getNextElement());
		}
		return tasks;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < flows.size(); i++) {
			Flow flow = flows.elementAt(i);
			buffer.append(flow.toString());
			if (!(i == flows.size() - 1)) {
				buffer.append(',');
			}
		}
		return buffer.toString();
	}

	public void write(DataOutputStream dos) throws IOException {
		dos.writeByte(type);
		PersistentHelper.write(flows, dos);
	}

	public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
		type = dis.readByte();
		flows = PersistentHelper.read(dis, new Flow().getClass());
	}
}
