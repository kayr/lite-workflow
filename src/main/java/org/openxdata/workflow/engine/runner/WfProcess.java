package org.openxdata.workflow.engine.runner;

import org.openxdata.workflow.engine.Net;
import org.openxdata.workflow.engine.persistent.AbstractRecord;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

/**
 * @author kay
 */
public class WfProcess extends AbstractRecord {

    private Net net;

    public WfProcess() {
    }

    public void startProcess(Net net) {
        this.net = net;
    }

    public Vector<Workitem> getCurrentWorkitems() {

        return null;

    }

    public void write(DataOutputStream dos) throws IOException {
    }

    public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
    }
}
