package org.openxdata.workflow.engine;

import org.openxdata.workflow.engine.persistent.PersistentHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author kay
 */
public class Task extends Element {

	public enum STATE{
		COMPLETE,DISABLED,ENABLED
	}

	private Split split = new Split();
	private Join join = new Join();
	private String name;
	private STATE status = STATE.DISABLED;
	private Integer studyId;
	private Integer formId;

	public Task() {
	}

	public Task(String name, String taskID) {
		setId(taskID);
		setName(name);
	}

	public List<Flow> getInFlows() {
		return join.getFlows();
	}

	public List<Flow> getOutFlows() {
		return split.getFlows();
	}

	public boolean isXORSplit() {
		return split.isXOR();
	}

	public boolean isDirectSplit() {
		return split.isDirect();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setFormId(Integer formId) {
		this.formId = formId;
	}

	public Integer getFormId() {
		return formId;
	}

	public void setStudyId(Integer studyId) {
		this.studyId = studyId;
	}

	public Integer getStudyId() {
		return studyId;
	}

	public Flow addOutFlow() {
		Flow flow = new Flow();
		flow.setPreviousElement(this);
		flow.setRootNet(getRootNet());
		split.getFlows().add(flow);
		return flow;
	}

	public Flow addAndOutFlow() {
		Flow flow = addOutFlow();
		split.setType(Junction.TYPE.AND);
		return flow;
	}

	public Task havingJoinType(Junction.TYPE type) {
		join.setType(type);
		return this;
	}

	public Task havingSplitType(Junction.TYPE type) {
		split.setType(type);
		return this;
	}

	@Override
	public void setRootNet(Net rootNet) {
		super.setRootNet(rootNet);
		join.setRootNet(rootNet);
		getRootNet().addTask(this);
		split.setRootNet(rootNet);
	}

	@Override
	public String toString() {
		return split.toString();
	}

	public List<Task> getNextTasksInExec() {
		return split.getTasksInExec();
	}

	public STATE getStatus() {
		return status;
	}

	public void setStatus(STATE status) {
		this.status = status;
	}

	public boolean areInFlowsComplete() {
		return join.areTasksComplete();
	}

	@Override
	public void write(DataOutputStream dos) throws IOException {
		super.write(dos);
		join.write(dos);
		split.write(dos);
		dos.writeUTF(name);
		dos.writeUTF(status.name());
		PersistentHelper.writeInteger(dos, studyId);
		PersistentHelper.writeInteger(dos, formId);
	}

	@Override
	public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
		super.read(dis);
		join.read(dis);
		split.read(dis);

		name = dis.readUTF();
		status = STATE.valueOf(dis.readUTF());
		studyId = PersistentHelper.readInteger(dis);
		formId = PersistentHelper.readInteger(dis);
	}

	boolean isComplete() {
		return status == STATE.COMPLETE;
	}
}
