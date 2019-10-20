package org.openxdata.workflow.engine;

import java.util.Vector;

/**
 *
 * @author kay
 */
public class Split extends Jucntion {

	public Vector<Task> getTasksInExec() {
		if (isAND()) {
			return getAllNextTasks();
		}
		Vector<Flow> outFlows = getFlows();
		Vector<Task> tasks = new Vector<Task>(0);
		for (int i = 0; i < outFlows.size(); i++) {
			Flow flow = outFlows.elementAt(i);
			if (flow.isFlowAllowed()) {
				tasks.addElement(flow.getNextElement());
				if (isXOR()) {
					//TODO Add support for for XOR splits.
					//The tasks in the XOR split are also supposed to be diasabled
					break;
				}
			} else {
				flow.getNextElement().setStatus(Task.STATE.COMPLETE);
			}
		}

		if (isXOR()) {//TODO Make tests for this
			for (int i = 0; i < outFlows.size(); i++) {
				Flow flow = outFlows.elementAt(i);
				if (!tasks.contains(flow.getNextElement())) {
					flow.getNextElement().setStatus(Task.STATE.COMPLETE);
				}
			}
		}
		return tasks;
	}
}
