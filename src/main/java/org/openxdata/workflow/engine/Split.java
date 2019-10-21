package org.openxdata.workflow.engine;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kay
 */
public class Split extends Junction {

	public List<Task> getTasksInExec() {
		if (isAND()) {
			return getAllNextTasks();
		}
		List<Flow> outFlows = getFlows();
		List<Task> tasks = new ArrayList<Task>(0);
		for (Flow flow : outFlows) {
			if (flow.isFlowAllowed()) {
				tasks.add(flow.getNextElement());
				if (isXOR()) {
					//TODO Add support for for XOR splits.
					//The tasks in the XOR split are also supposed to be disabled
					break;
				}
			} else {
				flow.getNextElement().setStatus(Task.STATE.COMPLETE);
			}
		}

		if (isXOR()) {//TODO Make tests for this
			for (Flow flow : outFlows) {
				if (!tasks.contains(flow.getNextElement())) {
					flow.getNextElement().setStatus(Task.STATE.COMPLETE);
				}
			}
		}
		return tasks;
	}
}
