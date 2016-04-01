
package edu.duke.cs.legosdn.core.faults;

import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsg;
import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IListener;
import net.floodlightcontroller.core.IOFSwitch;
import org.openflow.protocol.OFMessage;

import java.util.Properties;

/**
 * NoFaultInjector injects no faults at all. This is only a dummy or harmless injector.
 */
public class NoFaultInjector extends FaultInjector {

    @Override
    public void configure(Properties properties) {
    }

    @Override
    public void receive(IOFSwitch sw, DPlaneMsg msg) {
        return;
    }

    @Override
    public IListener.Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
        return IListener.Command.CONTINUE;
    }

    @Override
    public String toString() {
        return NoFaultInjector.class.getCanonicalName();
    }

}
