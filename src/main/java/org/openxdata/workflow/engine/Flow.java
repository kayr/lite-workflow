package org.openxdata.workflow.engine;

import org.openxdata.workflow.engine.persistent.Persistent;
import org.openxdata.workflow.engine.persistent.PersistentHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author kay
 */
public class Flow implements Persistent {

	private Net rootNet;
	private Task nextElement;
	private Task previousElement;
	private String nextTaskName;
	private String previousTaskName;
	private Condition condition;
	private Junction parentJunction;

	public Flow() {
	}

	public Flow(Task previousElement) {
		setPreviousElement(previousElement);
	}

	public Flow(Task previousElement, Task nextElement) {
		setNextElement(nextElement);
		setPreviousElement(previousElement);
	}

	public Flow(String nextTaskName, String previousTaskName, Condition condition) {
		this.nextTaskName = nextTaskName;
		this.previousTaskName = previousTaskName;
		this.condition = condition;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Task getNextElement() {
		if (nextElement == null) {
			nextElement = rootNet.getTask(nextTaskName);
		}
		return nextElement;
	}

	public void setNextElement(Task nextElement) {
		this.nextTaskName = nextElement.getId();
		this.nextElement = nextElement;
	}

	public Task getPreviousElement() {
		if (previousElement == null) {
			previousElement = rootNet.getTask(previousTaskName);
		}
		return previousElement;
	}

	public void setPreviousElement(Task previousElement) {
		this.previousTaskName = previousElement.getId();
		this.previousElement = previousElement;
	}

	public Task forFlowingIntoTask(Task task) {
		task.setRootNet(rootNet);
		setNextElement(task);
		task.getInFlows().add(this);
		return task;
	}

	public Task forFlowingIntoNewTask(String name, String variable) {
		Task task = new Task(name, variable);

		return forFlowingIntoTask(task);
	}

	public Flow withEqualCondition(String taskVariable, String value) {
		condition = new Condition(taskVariable, value, previousTaskName);
		condition.setRootNet(rootNet);
		condition.setTaskName(previousTaskName);
		return this;
	}

	void setRootNet(Net rootNet) {
		if (condition != null) {
			condition.setRootNet(rootNet);
		}
		this.rootNet = rootNet;
	}

	public String getNextTaskName() {
		return nextTaskName;
	}

	public String getPreviousTaskName() {
		return previousTaskName;
	}

	public String toString(Junction.TYPE type) {
		if (condition == null)
			return String.format("(:%s)-[:%s]->(:%s)", previousTaskName, type.name(), nextTaskName);
		else
			return String.format("(:%s)-[:%s {c: %s}]->(:%s)", previousTaskName, type.name(), condition.toString(), nextTaskName);


	}
	public String toString() {
		if (parentJunction != null)
			return toString(parentJunction.getType());
		else if (condition == null)
			return "(:" + previousTaskName + ")->(:" + nextTaskName + ")";
		else
			return "(:" + previousTaskName + ")-[" + condition.toString() + "]->(:" + nextTaskName + ")";


	}

	public boolean isFlowAllowed() {
		if (condition == null) {
			return true;
		}
		return condition.isAllowed();
	}

	public void write(DataOutputStream dos) throws IOException {
		dos.writeUTF(nextTaskName);
		PersistentHelper.writeUTF(dos, previousTaskName);
		if (condition == null) {
			dos.writeByte(0);
		} else {
			dos.writeByte(1);
			condition.write(dos);
		}

	}

	public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
		nextTaskName = dis.readUTF();
		previousTaskName = PersistentHelper.readUTF(dis);
		byte cndByte = dis.readByte();
		if (cndByte == 1) {
			condition = new Condition();
			condition.read(dis);
		}
	}

	public void setParentJunction(Junction parentJunction) {
		this.parentJunction = parentJunction;
	}
}
