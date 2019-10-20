package org.openxdata.workflow.engine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.openxdata.workflow.engine.persistent.PersistentHelper;

/**
 *
 * @author kay
 */
public class Net extends Element {

	private Vector<Flow> outFlows = new Vector<Flow>(0);
	private Hashtable<String, Task> netTasks = new Hashtable<String, Task>(0);
	private Hashtable<String, String> extendAttributes = new Hashtable<String, String>();
	private Task.STATE status = Task.STATE.DISABLED;

	public void addFlow(Flow e) {
		outFlows.addElement(e);
	}

	public Vector<Flow> getOutFlows() {
		return outFlows;
	}

	public void setOutFlows(Vector<Flow> outFlows) {
		this.outFlows = outFlows;
	}

	public Flow addFlow() {
		Flow flow = new Flow();
		flow.setRootNet(this);
		outFlows.addElement(flow);
		return flow;
	}

	public Vector<Task> getCurrentEnabledTasks() {
		Vector<Task> tasks = new Vector<Task>(0);
		Enumeration<Task> tasksEn = netTasks.elements();
		while (tasksEn.hasMoreElements()) {
			Task task = tasksEn.nextElement();
			if (task.getStatus() == Task.STATE.ENABLED) {
				tasks.addElement(task);
			}
		}
		return tasks;
	}

	public Vector<Task> getTasks(Task.STATE status) {
		Enumeration<Task> tasksEn = netTasks.elements();
		Vector<Task> tasks = new Vector<Task>(0);
		while (tasksEn.hasMoreElements()) {
			Task task = tasksEn.nextElement();
			if (task.getStatus() == status) {
				tasks.addElement(task);
			}
		}
		return tasks;
	}

	public void start() {
		status = Task.STATE.ENABLED;
		for (int i = 0; i < outFlows.size(); i++) {
			Flow flow = outFlows.elementAt(i);
			Task task = flow.getNextElement();
			enableTask(task);

		}
	}

	public Task complete(Task task) {
		Task netTask = netTasks.get(task.getId());
		//TODO  Do not complete already completed tasks

		Hashtable<String, Variable> variables = task.getVariablesTable();
		Enumeration<String> taskIdEnum = variables.keys();

		while (taskIdEnum.hasMoreElements()) {//TODO: Only Operate for output params
			String varId = taskIdEnum.nextElement();
			String varValue = task.getValue(varId);
			netTask.getVariable(varId).setValue(varValue);
		}
		netTask.processOutputMappings();
		netTask.setStatus(Task.STATE.COMPLETE);
		enableNextTasks(netTask);
		return netTask;
	}

	protected void enableTask(Task nextTask) {
		if (nextTask.getStatus() == Task.STATE.DISABLED && nextTask.areInFlowsComplete()) {
			//TODO: Somehow add support for backward flows
			nextTask.setStatus(Task.STATE.ENABLED);
			nextTask.processInputMappings();
		}

	}

	protected void enableNextTasks(Task submitTask) {
		Vector<Task> nextTasks = submitTask.getNextTasksInExec();
		if ((nextTasks.isEmpty() || isAllComplete(nextTasks)) && !hasEnabledTasks()) {
			status = Task.STATE.COMPLETE;
		}
		for (int j = 0; j < nextTasks.size(); j++) {
			Task nextTask = nextTasks.elementAt(j);
			enableTask(nextTask);
		}
	}

	public void addTask(Task task) {
		netTasks.put(task.getId(), task);
	}

	public String getNetID() {
		return getId();
	}

	public void setNetID(String netID) {
		setId(netID);
	}

	public Task getTask(String taskId) {
		return netTasks.get(taskId);
	}

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("Net: ").append(getId()).append("\n");
		Enumeration<Task> netTasksEnm = netTasks.elements();
		while (netTasksEnm.hasMoreElements()) {
			Task task = netTasksEnm.nextElement();
			buff.append(task.toString()).append("\n");

		}
		return buff.toString();
	}

	public Hashtable<String, Task> getNetTasks() {
		return netTasks;
	}

	public boolean isStarted() {
		return status == Task.STATE.ENABLED;
	}

	public boolean isComplete() {
		return status == Task.STATE.COMPLETE;
	}

	public Hashtable<String, String> getExtendAttributes() {
		return extendAttributes;
	}

	public void setExtendAttributes(Hashtable<String, String> extendAttributes) {
		this.extendAttributes = extendAttributes;
	}

	@Override
	public void write(DataOutputStream dos) throws IOException {
		super.write(dos);
		PersistentHelper.write(outFlows, dos);
		Util.writeToStream(dos, netTasks);
		dos.writeUTF(status.name());
		PersistentHelper.write(extendAttributes, dos);
	}

	@Override
	public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
		super.read(dis);
		outFlows = PersistentHelper.read(dis, Flow.class);
		netTasks = Util.readFromStrem(dis,  Task.class);
		status = Task.STATE.valueOf(dis.readUTF());
		extendAttributes = PersistentHelper.read(dis);

		for (int i = 0; i < outFlows.size(); i++) {
			Flow flow = outFlows.elementAt(i);
			flow.setRootNet(this);
		}

		Enumeration<Task> tasksEn = netTasks.elements();
		while (tasksEn.hasMoreElements()) {
			Task task = tasksEn.nextElement();
			task.setRootNet(this);
		}
	}

	private boolean hasEnabledTasks() {
		Enumeration<Task> tasks = netTasks.elements();
		while (tasks.hasMoreElements()) {
			Task task = tasks.nextElement();
			if (task.getStatus() == Task.STATE.ENABLED) {
				return true;
			}
		}
		return false;
	}

	public boolean isAllComplete(Vector<Task> tasks) {
		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.elementAt(i);
			if (!task.isComplete()) {
				return false;
			}
		}
		return true;

	}
}
