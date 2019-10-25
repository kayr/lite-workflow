package com.kayr.github.liteworkflow;

import com.kayr.github.liteworkflow.persistent.PersistentHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author kay
 */
public class Variable implements IdPersistent {

	public enum FLOW {
		INPUT, OUTPUT, IO
	}

	private String name;
	private String value;
	private FLOW flow;

	public Variable() {
	}

	public Variable(String name, String value, FLOW flow) {
		this.name = name;
		this.value = value;
		this.flow = flow;
	}

	public String getName() {
		return getId();
	}

	public void setName(String name) {
		this.name = name;
	}

	public FLOW getFlow() {
		return flow;
	}

	public void setFlow(FLOW flow) {
		this.flow = flow;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isInput() {
		return flow == FLOW.INPUT || isInputOutput();
	}

	public boolean isOutput() {
		return flow == FLOW.OUTPUT || isInputOutput();
	}

	public boolean isInputOutput() {
		return flow == FLOW.IO;
	}

	@Override
	public String toString() {
		return name + "=" + value;
	}

	@Override
	public String getId() {
		return name;
	}

	public void write(DataOutputStream dos) throws IOException {
		dos.writeUTF(name);
		PersistentHelper.writeUTF(dos, value);
		dos.writeUTF(flow.name());
	}

	public void read(DataInputStream dis) throws IOException {
		name = dis.readUTF();
		value = PersistentHelper.readUTF(dis);
		flow = FLOW.valueOf(dis.readUTF());
	}
}
