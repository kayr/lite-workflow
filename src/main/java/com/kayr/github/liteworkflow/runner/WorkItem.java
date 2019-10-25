package com.kayr.github.liteworkflow.runner;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kay
 */
public class WorkItem {

	private Map<String, String> parameters = new HashMap<>(0);
	private String caseID;
	private String taskID;
	private boolean submitted;

	public WorkItem(String caseID, String taskID) {
		this.caseID = caseID;
		this.taskID = taskID;
	}

	public String getCaseID() {
		return caseID;
	}

	public void setCaseID(String caseID) {
		this.caseID = caseID;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public boolean isSubmitted() {
		return submitted;
	}

	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}
}
