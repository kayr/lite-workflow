package org.openxdata.workflow.engine;

import java.util.List;

/**
 *
 * @author kay
 */
public class Join extends Junction {

	public Join() {
		setType(TYPE.OR);
	}

	boolean areTasksComplete() {
		final List<Flow> flows = getFlows();
		switch (getType()) {
			case AND:
				return flows.stream().allMatch(f -> f.getPreviousElement().isComplete());
			case OR:
				return flows.stream().allMatch(f -> f.getPreviousElement().isCompleteOrDisabled());
			case XOR:
				return flows.stream().anyMatch(f -> f.getPreviousElement().isComplete());
			default:
				throw new IllegalStateException(getType().name() + " Joins not supported");
		}
	}


}
