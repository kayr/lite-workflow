package org.openxdata.workflow.engine.runner;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import org.openxdata.db.util.AbstractRecord;
import org.openxdata.db.util.PersistentHelper;
import org.openxdata.workflow.engine.Net;

/**
 *
 * @author kay
 */
public class NetRunner extends AbstractRecord {

	private Net net;
	private Hashtable<String, String> netVariables = new Hashtable<String, String>();
	private Vector<Workitem> runningWorkitems = new Vector<Workitem>();
	private String caseID;
	private String specID;

	public NetRunner() {
	}

	public NetRunner(String caseID, String specID) {
		this.caseID = caseID;
		this.specID = specID;
	}

	public String getCaseID() {
		return caseID;
	}

	public void setCaseID(String caseID) {
		this.caseID = caseID;
	}

	public Hashtable<String, String> getNetVariables() {
		return netVariables;
	}

	public void setNetVariables(Hashtable<String, String> netVariables) {
		this.netVariables = netVariables;
	}

	public String getSpecID() {
		return specID;
	}

	public void setSpecID(String specID) {
		this.specID = specID;
	}

	public Vector<Workitem> getWorkitems() {
		return runningWorkitems;
	}

	public void setWorkitems(Vector<Workitem> workitems) {
		this.runningWorkitems = workitems;
	}

	public void write(DataOutputStream dos) throws IOException {
		PersistentHelper.write(netVariables, dos);
		PersistentHelper.write(runningWorkitems, dos);
		dos.writeUTF(caseID);
		dos.writeUTF(specID);
	}

	public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
		netVariables = PersistentHelper.read(dis);
		//runningWorkitems = PersistentHelper.read(dis, Workitem.class);
		caseID = dis.readUTF();
		specID = dis.readUTF();
	}
}
