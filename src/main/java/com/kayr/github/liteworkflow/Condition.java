package com.kayr.github.liteworkflow;

import com.kayr.github.liteworkflow.persistent.Persistent;
import com.kayr.github.liteworkflow.persistent.PersistentHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author kay
 */
public class Condition implements Persistent {

    //TODO: Add support for verifying for task Variable too
    private Net rootNet;
    private String expectedValue;
    private String variableId;
    private String previousTaskName;

    public Condition() {
    }

    public Condition(String variableId, String expectedValue, String previousTaskName) {
        this.expectedValue = expectedValue;
        this.variableId = variableId;
        this.previousTaskName = previousTaskName;
    }

    public void setRootNet(Net rootNet) {
        this.rootNet = rootNet;
    }

    public Net getRootNet() {
        return rootNet;
    }

    protected String getExpectedValue() {
        return expectedValue;
    }

    void setTaskName(String previousTaskName) {
        this.previousTaskName = previousTaskName;
    }

    public String getPreviousTaskName() {
        return previousTaskName;
    }

    public boolean isAllowed() {
        String calctdNetValue = rootNet.getValue(variableId);
        String expValue = getExpectedValue();
        if (calctdNetValue == null && expValue == null) {
            return true;
        }
        if (calctdNetValue == null || expValue == null) {
            return false;
        }

        return calctdNetValue.equalsIgnoreCase(expValue);
    }


    @Override
    public String toString() {
        return "$" + variableId + " = " + expectedValue;
    }

    public void write(DataOutputStream dos) throws IOException {
        PersistentHelper.writeUTF(dos, expectedValue);
        PersistentHelper.writeUTF(dos, previousTaskName);
        PersistentHelper.writeUTF(dos, variableId);
    }

    public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
        expectedValue = PersistentHelper.readUTF(dis);
        previousTaskName = PersistentHelper.readUTF(dis);
        variableId = PersistentHelper.readUTF(dis);
    }
}
