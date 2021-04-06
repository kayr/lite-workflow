package com.kayr.github.liteworkflow;

public interface ICondition {

    boolean isAllowed(Net root, Task task);

}
