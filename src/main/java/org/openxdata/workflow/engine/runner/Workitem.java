package org.openxdata.workflow.engine.runner;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kay
 */
public class Workitem {

	private Map<String, String> paramaters = new HashMap<String, String>(0);
	private String caseID;
	private String taskID;
	private boolean submitted;

	public Workitem(String caseID, String taskID) {
		this.caseID = caseID;
		this.taskID = taskID;
	}

	public String getCaseID() {
		return caseID;
	}

	public void setCaseID(String caseID) {
		this.caseID = caseID;
	}

	public Map<String, String> getParamaters() {
		return paramaters;
	}

	public void setParamaters(Map<String, String> paramaters) {
		this.paramaters = paramaters;
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
