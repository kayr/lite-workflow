package org.openxdata.workflow.engine;

import org.openxdata.workflow.engine.persistent.PersistentHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author kay
 */
public class Variable implements IdPersistent {

	public enum TYPE {
		INPUT, OUTPUT, IO
	}

	private String name;
	private String value;
	private TYPE flow;

	public Variable() {
	}

	public Variable(String name, String value, TYPE type) {
		this.name = name;
		this.value = value;
		this.flow = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TYPE getFlow() {
		return flow;
	}

	public void setFlow(TYPE type) {
		this.flow = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isInput() {
		return flow == TYPE.INPUT || isInputOutput();
	}

	public boolean isOutput() {
		return flow == TYPE.OUTPUT || isInputOutput();
	}

	public boolean isInputOutput() {
		return flow == TYPE.IO;
	}

	public void copyToNetVariable() {
		if (!isOutput()) {
			return;
		}
	}

	@Override
	public String toString() {
		return name + "=" + value;
	}

	public String getId() {
		return name;
	}

	public void write(DataOutputStream dos) throws IOException {
		dos.writeUTF(name);
		PersistentHelper.writeUTF(dos, value);
		dos.writeUTF(flow.name());
	}

	public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
		name = dis.readUTF();
		value = PersistentHelper.readUTF(dis);
		flow = TYPE.valueOf(dis.readUTF());
	}
}
