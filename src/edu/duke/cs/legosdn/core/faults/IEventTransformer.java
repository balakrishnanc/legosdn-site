
package edu.duke.cs.legosdn.core.faults;

import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsgExternalizable;
import edu.duke.cs.legosdn.core.appvisor.proxy.IAppAwareLinkDiscoveryService;

import java.util.List;

/**
 * Methods to be supported by any event transformer module.
 */
public interface IEventTransformer {

    /**
     * Register proxy link discovery service.
     *
     * @param proxyService Instance of IProxyLinkDiscoveryService
     */
    void registerService(IAppAwareLinkDiscoveryService proxyService);

    /**
     * Verify if an inbound message can be safely delivered to an application, and transform if is not the case.
     *
     * @param dPlaneMsg Data plane message
     * @param appId Application identifier
     * @return List of equivalent data plane messages
     */
    List<DPlaneMsgExternalizable> verify(DPlaneMsgExternalizable dPlaneMsg, String appId);

    /**
     * Transform an inbound data plane message to an equivalent message.
     *
     * @param dPlaneMsg Data plane message
     * @param appId Application identifier
     * @param attempt Attempt number
     * @return List of equivalent data plane messages
     */
    List<DPlaneMsgExternalizable> transform(DPlaneMsgExternalizable dPlaneMsg, String appId, int attempt);

}
