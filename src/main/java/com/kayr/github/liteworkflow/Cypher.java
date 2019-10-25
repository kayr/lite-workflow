package com.kayr.github.liteworkflow;

import java.util.Collection;
import java.util.List;

public class Cypher {

    private Cypher() {
    }

    /**
     * (`0` :JPA ) ,
     * (`1` :CORE ) ,
     * (`2` :HIbernate ) ,
     * (`3` :App ) ,
     * (`4` :EclipseLink ) ,
     * (`5` :Node ) ,
     * (`1`)-[:`RELATED_TO` ]->(`0`),
     * (`2`)-[:`RELATED_TO` ]->(`1`),
     * (`3`)-[:`RELATED_TO` ]->(`2`),
     * (`3`)-[:`RELATED_TO` ]->(`1`),
     * (`4`)-[:`RELATED_TO` ]->(`1`),
     * (`3`)-[:`RELATED_TO` ]->(`4`)
     */
    public static String generate(Net net) {
        Collection<Task> values = net.getNetTasks().values();

        StringBuilder b = new StringBuilder();
        b.append("CREATE \n");
        for (Task t : values) {
            b.append("(`").append(t.getIdNo()).append("` :").append("TASK").append(" {name: '").append(t.getId()).append("'}").append(")").append(",\n");
        }

        for (Task t : values) {
            final List<Flow> outFlows = t.getOutFlows();
            for (Flow f : outFlows) {
                b.append("(`").append(f.getPreviousElement().getIdNo()).append("`)")
                 .append("-[:`").append(f.getParentJunction().getType().name()).append("`]")
                 .append("->(`").append(f.getNextElement().getIdNo()).append("`)").append(",\n");
            }
        }


        return b.toString();
    }
}
