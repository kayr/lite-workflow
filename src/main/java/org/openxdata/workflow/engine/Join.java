package org.openxdata.workflow.engine;

import java.util.List;

/**
 *
 * @author kay
 */
public class Join extends Jucntion {

	boolean areTasksComplete() {
		//TODO:Support for different types of join ie. XOR, OR, AND
		if (isAND()) {
			List<Flow> inFlows = getFlows();
			for (int i = 0; i < inFlows.size(); i++) {
				Flow flow = inFlows.get(i);
				if (!flow.getPreviousElement().isComplete()) {
					return false;
				}
			}
		}
		return true;
	}
}
