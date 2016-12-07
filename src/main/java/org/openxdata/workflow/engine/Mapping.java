package org.openxdata.workflow.engine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.openxdata.db.util.Persistent;

/**
 *
 * @author kay
 */
public class Mapping implements Persistent {

	private Net rootNet;
	private String netVariable;
	private String taskVarId;
	private String taskID;
	private byte flow;

	public Mapping(String netVariable, String taskVariable, String taskID, byte input) {
		this.netVariable = netVariable;
		this.taskVarId = taskVariable;
		this.taskID = taskID;
		this.flow = input;
	}

	public Mapping() {
	}

	public Task getTask() {
		Task task = rootNet.getTask(taskID);
		return task;
	}

	public void performInputCopy() {
		Task task = getTask();
		Variable taskVar = task.getVariable(taskVarId);
		String netValue = rootNet.getValue(netVariable);
		taskVar.setValue(netValue);
	}

	public void performOutputCopy() {
		Task task1 = getTask();
		String taskValue = task1.getValue(taskVarId);
		Variable netVarible = rootNet.getVariable(netVariable);
		netVarible.setValue(taskValue);
	}

	public void setRootNet(Net rootNet) {
		this.rootNet = rootNet;
	}

	public Net getRootNet() {
		return rootNet;
	}

	public byte getFlow() {
		return flow;
	}

	public String getTaskVarId() {
		return taskVarId;
	}

	public void write(DataOutputStream dos) throws IOException {
		dos.writeUTF(netVariable);
		dos.writeUTF(taskID);
		dos.writeUTF(taskVarId);
		dos.writeByte(flow);
	}

	public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
		netVariable = dis.readUTF();
		taskID = dis.readUTF();
		taskVarId = dis.readUTF();
		flow = dis.readByte();
	}
}
