package edu.duke.cs.legosdn.core.faults;

/**
 * DeterministicFaultInjector simulates deterministic faults in SDN-Apps by raising exceptions based on certain
 * user-defined criteria in the receive method call invocation of SDN-Application loader (or AppVisor-Stub).
 */
public abstract class DeterministicFaultInjector extends FaultInjector {
}
