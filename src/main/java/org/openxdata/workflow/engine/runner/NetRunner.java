package org.openxdata.workflow.engine.runner;

import org.openxdata.workflow.engine.persistent.PersistentHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kay
 */
public class NetRunner extends org.openxdata.workflow.engine.persistent.AbstractRecord {

    private Map<String, String> netVariables = new HashMap<>();
    private List<WorkItem> runningWorkItems = new ArrayList<>();
    private String caseID;
    private String specID;

    public NetRunner() {
    }

    public NetRunner(String caseID, String specID) {
        this.caseID = caseID;
        this.specID = specID;
    }

    public String getCaseID() {
        return caseID;
    }

    public void setCaseID(String caseID) {
        this.caseID = caseID;
    }

    public Map<String, String> getNetVariables() {
        return netVariables;
    }

    public void setNetVariables(Map<String, String> netVariables) {
        this.netVariables = netVariables;
    }

    public String getSpecID() {
        return specID;
    }

    public void setSpecID(String specID) {
        this.specID = specID;
    }

    public List<WorkItem> getWorkItems() {
        return runningWorkItems;
    }

    public void setWorkItems(List<WorkItem> workItems) {
        this.runningWorkItems = workItems;
    }

    public void write(DataOutputStream dos) throws IOException {
        PersistentHelper.write(netVariables, dos);
        PersistentHelper.write(runningWorkItems, dos);
        dos.writeUTF(caseID);
        dos.writeUTF(specID);
    }

    public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
        netVariables = PersistentHelper.readMap(dis);
        runningWorkItems = PersistentHelper.read(dis, WorkItem.class);
        caseID = dis.readUTF();
        specID = dis.readUTF();
    }
}
