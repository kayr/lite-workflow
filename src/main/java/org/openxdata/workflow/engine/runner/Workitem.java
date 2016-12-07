package org.openxdata.workflow.engine.runner;

import java.util.Hashtable;

/**
 *
 * @author kay
 */
public class Workitem {

	private Hashtable<String, String> paramaters = new Hashtable<String, String>(0);
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

	public Hashtable<String, String> getParamaters() {
		return paramaters;
	}

	public void setParamaters(Hashtable<String, String> paramaters) {
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
