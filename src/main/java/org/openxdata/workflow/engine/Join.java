package org.openxdata.workflow.engine;

import java.util.List;

/**
 *
 * @author kay
 */
public class Join extends Junction {

	boolean areTasksComplete() {
		//TODO:Support for different types of join ie. XOR, OR, AND
		if (isAND()) {
			List<Flow> inFlows = getFlows();
			for (Flow flow : inFlows) {
				if (!flow.getPreviousElement().isCompleteOrDisabled()) {
					return false;
				}
			}
		}
		return true;
	}
}
