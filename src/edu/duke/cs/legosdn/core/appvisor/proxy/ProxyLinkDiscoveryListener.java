package edu.duke.cs.legosdn.core.appvisor.proxy;

import edu.duke.cs.legosdn.core.appvisor.dplane.LinkDiscoveryNotification;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.linkdiscovery.ILinkDiscoveryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * ProxyLinkDiscoveryListener generates notifications on receiving link discovery updates, and dispatches them to
 * applications running in the stub.
 */
public class ProxyLinkDiscoveryListener implements ILinkDiscoveryListener, IFloodlightService {

    private static final Logger logger = LoggerFactory.getLogger(ProxyLinkDiscoveryListener.class);

    private static final ProxyLinkDiscoveryListener INSTANCE = new ProxyLinkDiscoveryListener();

    // Registry of remote/isolated endpoints
    protected RemoteRegistry remRegistry;

    /**
     * Initialize ProxyLinkDiscoveryListener.
     */
    private ProxyLinkDiscoveryListener() {
        /* NOTE: *Private* constructor exists to ensure that no one can instantiate this class! */
    }

    /**
     * Get an instance of ProxyLinkDiscoveryListener.
     *
     * @param remRegistry Instance of RemoteRegistry
     * @return Instance of ProxyLinkDiscoveryListener
     */
    public static ProxyLinkDiscoveryListener getInstance(RemoteRegistry remRegistry) {
        if (ProxyLinkDiscoveryListener.INSTANCE.remRegistry == null) {
            ProxyLinkDiscoveryListener.INSTANCE.remRegistry = remRegistry;
        }
        return ProxyLinkDiscoveryListener.INSTANCE;
    }

    /**
     * Configure ProxyLinkDiscoveryListener.
     *
     * @param remRegistry Remote Registry
     * @return Instance of ProxyLinkDiscoveryListener
     */
    public synchronized void configure(RemoteRegistry remRegistry) {
        if (this.remRegistry == null) {
            this.remRegistry = remRegistry;
        }
    }

    @Override
    public void linkDiscoveryUpdate(LDUpdate update) {
        if (logger.isDebugEnabled()) {
            logger.debug("linkDiscoveryUpdate> {}", update.toString());
        }

        final LinkDiscoveryNotification linkDiscoveryNotif = new LinkDiscoveryNotification(update);
        try {
            this.remRegistry.broadcastNotif(null, linkDiscoveryNotif);
        } catch (IOException e) {
            logger.error("switchAdded> failed to send link discovery notification to stub; {}",
                         e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void linkDiscoveryUpdate(List<LDUpdate> updateList) {
        if (logger.isDebugEnabled()) {
            logger.debug("linkDiscoveryUpdate> received {} updates", updateList.size());
        }

        final LinkDiscoveryNotification linkDiscoveryNotif = new LinkDiscoveryNotification(updateList);
        try {
            this.remRegistry.broadcastNotif(null, linkDiscoveryNotif);
        } catch (IOException e) {
            logger.error("switchAdded> failed to send list of link discovery notifications to stub; {}",
                         e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

}
