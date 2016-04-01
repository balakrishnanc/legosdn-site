package edu.duke.cs.legosdn.core.appvisor.proxy;

import edu.duke.cs.legosdn.core.appvisor.dplane.PortNotification;
import edu.duke.cs.legosdn.core.appvisor.dplane.SwitchNotification;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.IOFSwitchListener;
import net.floodlightcontroller.core.ImmutablePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * ProxySwitchListener listens for switch-related events and sends appropriate notifications to applications running in
 * the stub.
 */
public class ProxySwitchListener implements IOFSwitchListener {

    private static final Logger logger = LoggerFactory.getLogger(ProxySwitchListener.class);

    private static final ProxySwitchListener INSTANCE = new ProxySwitchListener();

    // Registry of remote/isolated endpoints
    protected RemoteRegistry remRegistry;

    /**
     * Initialize ProxySwitchListener.
     */
    private ProxySwitchListener() {
        /* NOTE: *Private* constructor exists to ensure that no one can instantiate this class! */
    }

    /**
     * Get an instance of ProxySwitchListener.
     *
     * @param remRegistry Remote Registry (instance of RemoteRegistry)
     * @return Instance of ProxySwitchListener
     */
    public static ProxySwitchListener getInstance(RemoteRegistry remRegistry) {
        if (ProxySwitchListener.INSTANCE.remRegistry == null) {
            ProxySwitchListener.INSTANCE.remRegistry = remRegistry;
        }
        return ProxySwitchListener.INSTANCE;
    }

    @Override
    public void switchAdded(long switchId) {
        if (logger.isDebugEnabled()) {
            logger.debug("switchAdded> switch: {}", switchId);
        }

        final SwitchNotification swNotif =
                new SwitchNotification(switchId, SwitchNotification.NotificationType.SWITCH_ADDED);
        try {
            this.remRegistry.broadcastNotif(null, swNotif);
        } catch (IOException e) {
            logger.error("switchAdded> failed to send switch notification to stub; {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void switchRemoved(long switchId) {
        if (logger.isDebugEnabled()) {
            logger.debug("switchRemoved> switch: {}", switchId);
        }

        final SwitchNotification swNotif =
                new SwitchNotification(switchId, SwitchNotification.NotificationType.SWITCH_REMOVED);
        try {
            this.remRegistry.broadcastNotif(null, swNotif);
        } catch (IOException e) {
            logger.error("switchRemoved> failed to send switch notification to stub; {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void switchActivated(long switchId) {
        if (logger.isDebugEnabled()) {
            logger.debug("switchActivated> switch: {}", switchId);
        }

        final SwitchNotification swNotif =
                new SwitchNotification(switchId, SwitchNotification.NotificationType.SWITCH_ACTIVATED);

        try {
            this.remRegistry.broadcastNotif(null, swNotif);
        } catch (IOException e) {
            logger.error("switchActivated> failed to send switch notification to stub; {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void switchPortChanged(long switchId, ImmutablePort port, IOFSwitch.PortChangeType type) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("switchPortChanged> status of port %d (%s) on switch %d: %s",
                                       port.getPortNumber(), port.getName(), switchId, type.toString()));
        }

        final PortNotification portNotif = new PortNotification(switchId, port, type);
        try {
            this.remRegistry.broadcastNotif(null, portNotif);
        } catch (IOException e) {
            logger.error("switchPortChanged> failed to send port notification to stub; {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void switchChanged(long switchId) {
        if (logger.isDebugEnabled()) {
            logger.debug("switchChanged> switch: {}", switchId);
        }

        final SwitchNotification swNotif =
                new SwitchNotification(switchId, SwitchNotification.NotificationType.SWITCH_CHANGED);
        try {
            this.remRegistry.broadcastNotif(null, swNotif);
        } catch (IOException e) {
            logger.error("switchChanged> failed to send switch notification to stub; {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

}
