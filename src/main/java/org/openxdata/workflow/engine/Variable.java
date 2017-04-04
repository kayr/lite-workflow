package org.openxdata.workflow.engine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.openxdata.workflow.engine.persistent.PersistentHelper;

/**
 *
 * @author kay
 */
public class Variable implements IdPersistent {

	public final static byte TYPE_INPUT = 1;
	public final static byte TYPE_OUTPUT = 2;
	public final static byte TYPE_IO = 3;
	private String name;
	private String value;
	private byte flow;

	public Variable() {
	}

	public Variable(String name, String value, byte type) {
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

	public byte getFlow() {
		return flow;
	}

	public void setFlow(byte type) {
		this.flow = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isInput() {
		return flow == TYPE_INPUT || isInputOutput();
	}

	public boolean isOutput() {
		return flow == TYPE_OUTPUT || isInputOutput();
	}

	public boolean isInputOutput() {
		return flow == TYPE_IO;
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
		dos.writeByte(flow);
	}

	public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
		name = dis.readUTF();
		value = PersistentHelper.readUTF(dis);
		flow = dis.readByte();
	}
}
