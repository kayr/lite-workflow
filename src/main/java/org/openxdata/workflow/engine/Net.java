package org.openxdata.workflow.engine;

import org.openxdata.workflow.engine.persistent.PersistentHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author kay
 */
public class Net extends Element {

	private Map<String, Task> netTasks = new HashMap<>(0);
	private Map<String, String> extendAttributes = new HashMap<>();
	private Task.STATE status = Task.STATE.DISABLED;

	private final Task startTask = new Task("START", "__start__");
	private final Task endTask = new Task("START", "__end__");

	public Net() {
		startTask.setRootNet(this);
		endTask.setRootNet(this);
	}

	public List<Flow> getOutFlows() {
		return startTask.getOutFlows();
	}

	public Flow addFlow() {
		return startTask.addOutFlow();
	}

	public List<Task> getCurrentEnabledTasks() {
		List<Task> tasks = new ArrayList<>(0);
		for (Task task : netTasks.values()) {
			if (task.getStatus() == Task.STATE.ENABLED) {
				tasks.add(task);
			}
		}
		return tasks;
	}

	public List<Task> getTasks(Task.STATE status) {
		Iterator<Task> tasksEn = netTasks.values().iterator();
		List<Task> tasks = new ArrayList<>(0);
		while (tasksEn.hasNext()) {
			Task task = tasksEn.next();
			if (task.getStatus() == status) {
				tasks.add(task);
			}
		}
		return tasks;
	}

	public void start() {
		status = Task.STATE.ENABLED;
		for (Flow flow : startTask.getOutFlows()) {
			Task task = flow.getNextElement();
			enableTask(task);
		}
	}

	public Task complete(Task task) {
		Task netTask = netTasks.get(task.getId());
		//TODO  Do not complete already completed tasks

		Map<String, Variable> variables = task.getVariablesTable();


		for (String varId : variables.keySet()) {//TODO: Only Operate for output params
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
		List<Task> nextTasks = submitTask.getNextTasksInExec();
		if ((nextTasks.isEmpty() || isAllComplete(nextTasks)) && !hasEnabledTasks()) {
			status = Task.STATE.COMPLETE;
		}
		for (Task nextTask : nextTasks) {
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
		StringBuilder buff = new StringBuilder();
		buff.append("Net: ").append(getId()).append("\n");
		for (Task task : netTasks.values()) {
			buff.append(task.toString()).append("\n");

		}
		return buff.toString();
	}

	public Map<String, Task> getNetTasks() {
		return netTasks;
	}

	public boolean isStarted() {
		return status == Task.STATE.ENABLED;
	}

	public boolean isComplete() {
		return status == Task.STATE.COMPLETE;
	}

	public Map<String, String> getExtendAttributes() {
		return extendAttributes;
	}

	public void setExtendAttributes(Map<String, String> extendAttributes) {
		this.extendAttributes = extendAttributes;
	}

	@Override
	public void write(DataOutputStream dos) throws IOException {
		super.write(dos);
		startTask.write(dos);
		StreamUtil.writeToStream(dos, netTasks);
		dos.writeUTF(status.name());
		PersistentHelper.write(extendAttributes, dos);
	}

	@Override
	public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
		super.read(dis);
		startTask.read(dis);
		netTasks = StreamUtil.readFromStream(dis,  Task.class);
		status = Task.STATE.valueOf(dis.readUTF());
		extendAttributes = PersistentHelper.readMap(dis);

		for (Flow flow : startTask.getOutFlows()) {
			flow.setRootNet(this);
		}

		for (Task task : netTasks.values()) {
			task.setRootNet(this);
		}
	}

	private boolean hasEnabledTasks() {
		for (Task task : netTasks.values()) {
			if (task.getStatus() == Task.STATE.ENABLED) {
				return true;
			}
		}
		return false;
	}

	public boolean isAllComplete(List<Task> tasks) {
		for (Task task : tasks) {
			if (!task.isComplete()) {
				return false;
			}
		}
		return true;

	}
}
