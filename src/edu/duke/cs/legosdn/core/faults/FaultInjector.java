package edu.duke.cs.legosdn.core.faults;

import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsg;
import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IListener;
import net.floodlightcontroller.core.IOFSwitch;
import org.openflow.protocol.OFMessage;

import java.util.Properties;

/**
 * FaultInjector simulates faults in SDN-Apps by raising exceptions in the receive method call invocation of
 * SDN-Application loader (or AppVisor-Stub).
 */
public abstract class FaultInjector {

    // Number of messages received by the application
    protected long numReceived;

    /**
     * Configure the behavior of the fault injector based on user-defined configuration parameters.
     *
     * @param properties User-defined configuration properties
     */
    public abstract void configure(Properties properties);

    /**
     * Receives the inbound message from the switch to the application and decides whether or not to inject a fault.
     *
     * @param sw Switch
     * @param msg Message
     */
    public abstract void receive(IOFSwitch sw, DPlaneMsg msg);

    /**
     * Receives the inbound message from the switch to the application and decides whether or not to inject a fault.
     *
     * @param sw Switch
     * @param msg Message
     * @param cntx FloodLight context
     * @return CONTINUE/STOP
     */
    public abstract IListener.Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx);

}
