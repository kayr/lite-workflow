package com.kayr.github.liteworkflow;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.kayr.github.liteworkflow.persistent.Persistent;

/**
 *
 * @author kay
 */
public class Mapping implements Persistent {

	private Net rootNet;
	private String netVariable;
	private String taskVarId;
	private String taskID;
	private Variable.FLOW flow;

	public Mapping(String netVariable, String taskVariable, String taskID, Variable.FLOW input) {
		this.netVariable = netVariable;
		this.taskVarId = taskVariable;
		this.taskID = taskID;
		this.flow = input;
	}

	public Mapping() {
	}

	public Task getTask() {
		return rootNet.getTask(taskID);
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
		Variable variable = rootNet.getVariable(this.netVariable);
		variable.setValue(taskValue);
	}

	public void setRootNet(Net rootNet) {
		this.rootNet = rootNet;
	}

	public Net getRootNet() {
		return rootNet;
	}

	public Variable.FLOW getFlow() {
		return flow;
	}

	public String getTaskVarId() {
		return taskVarId;
	}

	public void write(DataOutputStream dos) throws IOException {
		dos.writeUTF(netVariable);
		dos.writeUTF(taskID);
		dos.writeUTF(taskVarId);
		dos.writeUTF(flow.name());
	}

	public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
		netVariable = dis.readUTF();
		taskID = dis.readUTF();
		taskVarId = dis.readUTF();
		flow = Variable.FLOW.valueOf(dis.readUTF());
	}
}
