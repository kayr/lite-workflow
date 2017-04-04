package org.openxdata.workflow.engine;

import org.openxdata.workflow.engine.persistent.PersistentHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author kay
 */
public class Element extends org.openxdata.workflow.engine.persistent.AbstractRecord implements IdPersistent {

    private Hashtable<String, Variable> variablesTable = new Hashtable<String, Variable>(0);
    private Vector<Mapping> mappings = new Vector<Mapping>(0);
    private Net rootNet;
    private String id;

    public Variable setValue(String varName, String value) {
        Variable variable = getVariable(varName);
        variable.setValue(value);
        return variable;

    }

    public String getValue(String variableId) {
        Variable variable = variablesTable.get(variableId);
        return variable.getValue();
    }

    public Element withInputMapping(String netVariableName, String taskVarName) {
        validateNetAndTaskNames(taskVarName, netVariableName);
        if (containInputMappingForVariable(taskVarName)) {
            throw new WfException("Duplicate Mapping for variable: " + getId() + "." + taskVarName);
        }
        Mapping mapping = new Mapping(netVariableName, taskVarName, id, Variable.TYPE_INPUT);
        mapping.setRootNet(rootNet);
        mappings.addElement(mapping);
        return this;

    }

    public boolean containInputMappingForVariable(String varName) {
        for (int i = 0; i < mappings.size(); i++) {
            Mapping mapping = mappings.elementAt(i);
            if (mapping.getFlow() == Variable.TYPE_INPUT && mapping.getTaskVarId().equals(varName)) {
                return true;
            }
        }
        return false;
    }

    public void validateNetAndTaskNames(String taskVarName, String netVariableName) throws NullPointerException {
        Variable taskVariable = variablesTable.get(taskVarName);
        Variable netVariable = rootNet.getVariable(netVariableName);
        if (taskVariable == null) {
            throw new NullPointerException("The TaskVariable: " + taskVarName + " Has No Matching variable");
        }

        if (netVariable == null) {
            throw new NullPointerException("The NetVariable: " + netVariableName + " Has No Matching variable");
        }
    }

    public Element withOutputMapping(String taskVarName, String netVarName) {
        validateNetAndTaskNames(taskVarName, netVarName);
        Mapping mapping = new Mapping(netVarName, taskVarName, id, Variable.TYPE_OUTPUT);
        mapping.setRootNet(rootNet);
        mappings.addElement(mapping);
        return this;
    }

    public void processInputMappings() {
        for (int i = 0; i < mappings.size(); i++) {
            Mapping mapping = mappings.elementAt(i);
            if (mapping.getFlow() == Variable.TYPE_INPUT) {
                mapping.performInputCopy();
            }
        }
    }

    public void processOutputMappings() {
        for (int i = 0; i < mappings.size(); i++) {
            Mapping mapping = mappings.elementAt(i);
            if (mapping.getFlow() == Variable.TYPE_OUTPUT) {
                mapping.performOutputCopy();
            }
        }
    }

    public Element withParemeter(String name, byte flow) {
        withParemeter(name, flow, true);
        return this;
    }

    public Element withParemeter(String name, byte flow, boolean directTransfer) {
        Variable variable = new Variable(name, null, flow);
        variablesTable.put(name, variable);
        if (!directTransfer) {
            return this;
        }

        //TODO: handleFor Direct Data Transfer
        forDirectTransfer(variable);
        return this;

    }

    public Variable getVariable(String variableName) {
        return variablesTable.get(variableName);
    }

    private Element forDirectTransfer(Variable variable) {
        byte direction = variable.getFlow();
        String variableName = variable.getName();

        switch (direction) {
            case Variable.TYPE_INPUT:
                addInputMapping(variableName);
                break;
            case Variable.TYPE_OUTPUT:
                addOutputMapping(variableName);
                break;
            case Variable.TYPE_IO:
                addInputMapping(variableName);
                addOutputMapping(variableName);
                break;
        }
        return this;
    }

    protected void addOutputMapping(String variableName) {
        rootNet.withParemeter(variableName, Variable.TYPE_OUTPUT, false);
        withOutputMapping(variableName, variableName);
    }

    protected void addInputMapping(String variableName) {
        rootNet.withParemeter(variableName, Variable.TYPE_INPUT, false);
        withInputMapping(variableName, variableName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Net getRootNet() {
        return rootNet;
    }

    public void setRootNet(Net rootNet) {
        this.rootNet = rootNet;
        for (int i = 0; i < mappings.size(); i++) {
            Mapping mapping = mappings.elementAt(i);
            mapping.setRootNet(rootNet);
        }
    }

    public Hashtable<String, Variable> getVariablesTable() {
        return variablesTable;
    }

    public void write(DataOutputStream dos) throws IOException {
        Util.writeToStream(dos, variablesTable);
        PersistentHelper.write(mappings, dos);
        dos.writeUTF(id);
    }

    public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
        variablesTable = Util.readFromStrem(dis, Variable.class);
        mappings = PersistentHelper.read(dis, Mapping.class);
        id = dis.readUTF();
    }
}
