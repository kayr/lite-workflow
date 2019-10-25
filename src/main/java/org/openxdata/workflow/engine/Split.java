package org.openxdata.workflow.engine;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author kay
 */
public class Split extends Junction {

	public List<Task> getTasksInExec() {
		switch (getType()) {
			case AND:
				return getFlows().stream()
								 .map(Flow::getNextElement)
								 .collect(Collectors.toList());
			case XOR:
				return getFlows().stream()
								 .filter(Flow::isFlowAllowed).findFirst()
								 .map(Flow::getNextElement)
								 .map(Collections::singletonList)
								 .orElse(Collections.emptyList());
			case OR:
				return getFlows().stream()
								 .filter(Flow::isFlowAllowed)
								 .map(Flow::getNextElement)
								 .collect(Collectors.toList());
			default:
				throw new IllegalStateException("Unsupported split type: " + getType().name());
		}
	}
}
