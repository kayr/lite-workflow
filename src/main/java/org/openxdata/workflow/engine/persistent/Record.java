package org.openxdata.workflow.engine.persistent;

/**
 * This interface is for
 * 1. Those objects that you want to later on retrieve one by one
 * based on the underlying storage recordId (for speed),. If you just load all values
 * to do a lookup for a particular item, then just use the Persistent interface.
 * This means that the recordId has to have been stored somewhere else to be used
 * for this purpose.
 * <p>
 * 2. Those objects that can be saved one by one after editing to ensure that the
 * appropriate record is identified and overwritten instead of having to load
 * all values, replace one of them, and then save the whole list again.
 * <p>
 * 3. Those objects that require deletion from the data store.
 *
 * @author Daniel
 */
public interface Record extends Persistent {
    void setRecordId(int id);

    int getRecordId();

    /**
     * true if is new record, else false.
     */
    boolean isNew();
}
