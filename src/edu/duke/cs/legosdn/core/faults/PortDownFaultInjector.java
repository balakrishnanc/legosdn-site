
package edu.duke.cs.legosdn.core.faults;

import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsg;
import edu.duke.cs.legosdn.core.appvisor.dplane.PortNotification;
import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IListener;
import net.floodlightcontroller.core.IOFSwitch;
import org.openflow.protocol.OFMessage;

import java.util.Properties;

/**
 * Crashes on PORT_DOWN notifications.
 */
public class PortDownFaultInjector extends DeterministicFaultInjector {

    @Override
    public void configure(Properties properties) {

    }

    @Override
    public void receive(IOFSwitch sw, DPlaneMsg msg) {
        switch (msg.getMsgType()) {
            case PORT_NOTIF:
                final PortNotification portNotif = (PortNotification) msg.getMsgPayload();
                switch (portNotif.getPortChangeType()) {
                    case DOWN:
                        throw new RuntimeException("Panicking on receiving a PORT_DOWN notification!");
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public IListener.Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
        return null;
    }

    @Override
    public String toString() {
        return PortDownFaultInjector.class.getCanonicalName();
    }

}
