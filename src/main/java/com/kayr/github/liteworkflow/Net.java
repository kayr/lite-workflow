package com.kayr.github.liteworkflow;

import com.kayr.github.liteworkflow.persistent.PersistentHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author kay
 */
public class Net extends Element {

	public static final String START_TASK_ID = "__start__";
	public static final String END_TASK_ID = "__end__";
	private Map<String, Task> netTasks = new HashMap<>(0);
	private Map<String, String> extendAttributes = new HashMap<>();
	private Task.STATE status = Task.STATE.DISABLED;

	private Task startTask;
	private Task endTask;

	private AtomicInteger igGen = new AtomicInteger();

	public int nextId() {
		return igGen.incrementAndGet();
	}


	public List<Flow> getOutFlows() {
		return getStartTask().getOutFlows();
	}

	public Flow addFlow() {
		return getStartTask().addOutFlow();
	}

	public List<Task> getCurrentEnabledTasks() {
		if (status == Task.STATE.COMPLETE) return Collections.emptyList();

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
			mayBeEnableTask(task);
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

		mayBeEnableNextTasks(netTask);

		return netTask;
	}

	private boolean isEndTask(Task task) {
		return task == endTask;
	}

	protected void mayBeEnableTask(Task nextTask) {
		if (nextTask.getStatus() == Task.STATE.DISABLED && nextTask.areInFlowsComplete()) {
			//TODO: Somehow add support for backward flows
			nextTask.setStatus(Task.STATE.ENABLED);
			nextTask.processInputMappings();
		}

	}

	/**
	 * Will enable next tasks if the next taks join condition/or type allows it.
	 * if a join if of type and and all the previouse tasks of that specific task are not
	 * complete, then the task will not enabled
	 */
	protected void mayBeEnableNextTasks(Task submitTask) {
		List<Task> nextTasks = submitTask.getNextTasksInExec();
		if (nextTasks.isEmpty()) {
			status = Task.STATE.COMPLETE;
		}

		for (Task nextTask : nextTasks) {
			if (isEndTask(nextTask)) {
				System.out.println("Reached end task");
				status = Task.STATE.COMPLETE;
				break;
			} else {
				mayBeEnableTask(nextTask);
			}
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
		buff.append("   Net: ").append(getId()).append("\n");

		List<Task> visited = new ArrayList<>();

		Deque<Flow> dq = new ArrayDeque<>(startTask.getOutFlows());

		while (!dq.isEmpty()) {
			Flow curr = dq.poll();
			buff.append(curr.getPreviousElement().getIdNo()).append(": ").append(curr.toString()).append("\n");

			Task nextElement = curr.getNextElement();
			List<Flow> outFlows = nextElement.getOutFlows();

			if(visited.contains(nextElement)) continue;

			if (outFlows.isEmpty() ) {
				buff.append(curr.getNextElement().getIdNo()).append(": ").append(nextElement.toString()).append("\n");
			} else {
				dq.addAll(outFlows);
			}
			visited.add(nextElement);
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
		StreamUtil.writeMap(dos, netTasks);
		dos.writeUTF(status.name());
		PersistentHelper.write(extendAttributes, dos);
	}

	@Override
	public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
		super.read(dis);
		netTasks = StreamUtil.readMap(dis,  Task.class);
		status = Task.STATE.valueOf(dis.readUTF());
		extendAttributes = PersistentHelper.readMap(dis);

		startTask = getTask(START_TASK_ID);
		endTask = getTask(END_TASK_ID);


		for (Task task : netTasks.values()) {
			task.setRootNet(this);
		}


	}

	public Task getStartTask() {

		if (startTask == null) {
			startTask = new Task("START", START_TASK_ID);
			startTask.setRootNet(this);
		}
		return startTask;

	}

	public Task getEndTask() {
		if (endTask == null) {
			endTask = new Task("END", END_TASK_ID);
			endTask.setRootNet(this);
		}
		return endTask;
	}
}
