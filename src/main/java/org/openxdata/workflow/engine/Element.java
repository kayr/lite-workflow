package org.openxdata.workflow.engine;

import org.openxdata.workflow.engine.persistent.PersistentHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author kay
 */
public class Element extends org.openxdata.workflow.engine.persistent.AbstractRecord implements IdPersistent {

    private Map<String, Variable> variablesTable = new HashMap<>(0);
    private List<Mapping> mappings = new ArrayList<>(0);
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
        Mapping mapping = new Mapping(netVariableName, taskVarName, id, Variable.FLOW.INPUT);
        mapping.setRootNet(rootNet);
        mappings.add(mapping);
        return this;

    }

    public boolean containInputMappingForVariable(String varName) {
        for (Mapping mapping : mappings) {
            if (mapping.getFlow() == Variable.FLOW.INPUT && mapping.getTaskVarId().equals(varName)) {
                return true;
            }
        }
        return false;
    }

    public void validateNetAndTaskNames(String taskVarName, String netVariableName)  {
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
        Mapping mapping = new Mapping(netVarName, taskVarName, id, Variable.FLOW.OUTPUT);
        mapping.setRootNet(rootNet);
        mappings.add(mapping);
        return this;
    }

    public void processInputMappings() {
        for (Mapping mapping : mappings) {
            if (mapping.getFlow() == Variable.FLOW.INPUT) {
                mapping.performInputCopy();
            }
        }
    }

    public void processOutputMappings() {
        for (Mapping mapping : mappings) {
            if (mapping.getFlow() == Variable.FLOW.OUTPUT) {
                mapping.performOutputCopy();
            }
        }
    }

    public Element withParameter(String name, Variable.FLOW flow) {
        withParameter(name, flow, true);
        return this;
    }

    public Element withParameter(String name, Variable.FLOW flow, boolean directTransfer) {
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
        Variable.FLOW direction = variable.getFlow();
        String variableName = variable.getName();

        switch (direction) {
            case INPUT:
                addInputMapping(variableName);
                break;
            case OUTPUT:
                addOutputMapping(variableName);
                break;
            case IO:
                addInputMapping(variableName);
                addOutputMapping(variableName);
                break;
        }
        return this;
    }

    protected void addOutputMapping(String variableName) {
        rootNet.withParameter(variableName, Variable.FLOW.OUTPUT, false);
        withOutputMapping(variableName, variableName);
    }

    protected void addInputMapping(String variableName) {
        rootNet.withParameter(variableName, Variable.FLOW.INPUT, false);
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
        for (Mapping mapping : mappings) {
            mapping.setRootNet(rootNet);
        }
    }

    public Map<String, Variable> getVariablesTable() {
        return variablesTable;
    }

    public void write(DataOutputStream dos) throws IOException {
        StreamUtil.writeToStream(dos, variablesTable);
        PersistentHelper.write(mappings, dos);
        dos.writeUTF(id);
    }

    public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
        variablesTable = StreamUtil.readFromStream(dis, Variable.class);
        mappings = PersistentHelper.read(dis, Mapping.class);
        id = dis.readUTF();
    }
}
