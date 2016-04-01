package edu.duke.cs.legosdn.core.faults;

/**
 * RandomFaultInjector simulates random faults in SDN-Apps by raising exceptions in the receive method call
 * invocation of SDN-Application loader (or AppVisor-Stub).
 */
public abstract class RandomFaultInjector extends FaultInjector {

    /**
     * Initialize AbstractRandomFaultInjector.
     */
    protected RandomFaultInjector() {
    }

}
