package edu.duke.cs.legosdn.core.appvisor.cplane;

import edu.duke.cs.legosdn.core.service.link.LinkDiscoveryServiceMsg;
import edu.duke.cs.legosdn.core.service.topology.TopologyServiceMsg;

/**
 * Service types.
 */
public enum ServiceType {

    TOPOLOGY(TopologyServiceMsg.class),
    LINKDISCOVERY(LinkDiscoveryServiceMsg.class),
    ;

    // Actual message type
    public final Class type;

    /**
     * Register service type with implementation.
     *
     * @param msgType Message type (Class)
     */
    private ServiceType(Class msgType) {
        this.type = msgType;
    }

}
