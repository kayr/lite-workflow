package org.openxdata.workflow.engine;

import org.openxdata.workflow.engine.persistent.Persistent;
import org.openxdata.workflow.engine.persistent.PersistentHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

/**
 *
 * @author kay
 */
public abstract class Jucntion implements Persistent {

	public enum TYPE {
		XOR, DIRECT, AND, OR
	}

	private Vector<Flow> flows = new Vector<Flow>(0);
	private Net rootNet;
	private TYPE type = TYPE.DIRECT;

	public Jucntion() {
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public boolean isDirect() {
		return TYPE.DIRECT == type;
	}

	public boolean isXOR() {
		return TYPE.XOR == type;
	}

	public boolean isAND() {
		return TYPE.AND == type;
	}

	public boolean isOR() {
		return TYPE.OR == type;
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
			if (i != (flows.size() - 1)) {
				buffer.append(',');
			}
		}
		return buffer.toString();
	}

	public void write(DataOutputStream dos) throws IOException {
		dos.writeUTF(type.name());
		PersistentHelper.write(flows, dos);
	}

	public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
		type = TYPE.valueOf(dis.readUTF());
		flows = PersistentHelper.read(dis, Flow.class);
	}
}
