package com.kayr.github.liteworkflow;

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
				//i think this is where a token will come in handy...where tasks keep track of the tokens
				// they have already processed. which means if the incoming task hands them a token they
				// have have already processed they will not activate.

				/*
				  1. whenever a task is moving to the next taks it will hand over pairs of [received token and new token]
				  2. if next take processed received token
				  		do not activate next task
				  	else
				  		activate next task
				  		process task
				  		when complete generate new token and store received token
				  		hand the two tokens over to the next taks

				 */
				return flows.stream().anyMatch(f -> f.getPreviousElement().isComplete());
			default:
				throw new IllegalStateException(getType().name() + " Joins not supported");
		}
	}


}
