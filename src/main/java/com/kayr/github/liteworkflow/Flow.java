package com.kayr.github.liteworkflow;

import com.kayr.github.liteworkflow.persistent.Persistent;
import com.kayr.github.liteworkflow.persistent.PersistentHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author kay
 */
public class Flow implements Persistent {

	private Net rootNet;
	private Task nextElement;
	private Task previousElement;
	private String nextTaskName;
	private String previousTaskName;
	private ICondition condition;
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

	public Flow(String nextTaskName, String previousTaskName, SimpleEqualCondition condition) {
		this.nextTaskName = nextTaskName;
		this.previousTaskName = previousTaskName;
		this.condition = condition;
	}

	public ICondition getCondition() {
		return condition;
	}

	public Flow withCondition(ICondition condition) {
		this.condition = condition;
		return this;
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
		if (parentJunction instanceof Split && parentJunction.isAND()) {
			throw new IllegalStateException("cannot have a condition on an AND split");
		}

		condition = new SimpleEqualCondition(taskVariable, value, previousTaskName);


		return this;
	}

	void setRootNet(Net rootNet) {
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
			return String.format("(:%s)-[:%s]->(:[%s]%s)", previousTaskName, type.name(),rootNet.getTask(nextTaskName).getJoin().getType(), nextTaskName);
		else
			return String.format("(:%s)-[:%s {c: %s}]->(:[%s]%s)", previousTaskName, type.name(), condition.toString(),rootNet.getTask(nextTaskName).getJoin().getType(), nextTaskName);


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
		return condition.isAllowed(rootNet, rootNet.getTask(previousTaskName));
	}

	public void write(DataOutputStream dos) throws IOException {
		dos.writeUTF(nextTaskName);
		PersistentHelper.writeUTF(dos, previousTaskName);
		if (condition == null) {
			dos.writeByte(0);
		} else if (condition instanceof Persistent) {
			dos.writeByte(1);
			dos.writeUTF(condition.getClass().getName());
			((Persistent) condition).write(dos);
		} else {
			throw new UnsupportedEncodingException("cannot write condition " + condition);
		}

	}

	public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
		nextTaskName = dis.readUTF();
		previousTaskName = PersistentHelper.readUTF(dis);
		byte cndByte = dis.readByte();
		if (cndByte == 1) {
			String s = dis.readUTF();
			Persistent o = null;
			try {
				o = (Persistent) Class.forName(s).newInstance();
			} catch (ClassNotFoundException e) {
				throw new InstantiationException("failed to instantiate class: " + s);
			}
			o.read(dis);
			condition = (ICondition) o;
		}
	}

	public void setParentJunction(Junction parentJunction) {
		this.parentJunction = parentJunction;
	}

	public Junction getParentJunction() {
		return parentJunction;
	}
}
