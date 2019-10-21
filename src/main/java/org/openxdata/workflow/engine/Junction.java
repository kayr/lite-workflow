package org.openxdata.workflow.engine;

import org.openxdata.workflow.engine.persistent.Persistent;
import org.openxdata.workflow.engine.persistent.PersistentHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kay
 */
public abstract class Junction implements Persistent {

	public enum TYPE {
		XOR, DIRECT, AND, OR
	}

	private List<Flow> flows = new ArrayList<>(0);
	private Net rootNet;
	private TYPE type = TYPE.DIRECT;

	public Junction() {
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

	public List<Flow> getFlows() {
		return flows;
	}

	public void setFlows(List<Flow> outFlows) {
		this.flows = outFlows;
	}

	public void setRootNet(Net rootNet) {
		this.rootNet = rootNet;
		for (int i = 0; i < flows.size(); i++) {
			Flow flow = flows.get(i);
			flow.setRootNet(rootNet);
		}
	}

	public List<Task> getAllNextTasks() {
		List<Task> tasks = new ArrayList<>();
		for (int i = 0; i < flows.size(); i++) {
			Flow flow = flows.get(i);
			tasks.add(flow.getNextElement());
		}
		return tasks;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < flows.size(); i++) {
			Flow flow = flows.get(i);
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
