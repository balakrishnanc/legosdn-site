package edu.duke.cs.legosdn.core.appvisor.proxy;

import edu.duke.cs.legosdn.core.appvisor.cplane.ServiceCallMsg;
import edu.duke.cs.legosdn.core.appvisor.cplane.ServiceType;
import edu.duke.cs.legosdn.core.service.IServiceCallProxy;
import org.jboss.netty.channel.Channel;

import java.util.HashMap;

/**
 * Registry of services supported by the proxy and stub components of AppVisor.
 */
public class ServiceRegistry {

    // List of service call proxies
    protected final HashMap<ServiceType, IServiceCallProxy> serviceCallProxies;

    /**
     * Instantiate ServiceRegistry.
     */
    public ServiceRegistry() {
        this.serviceCallProxies = new HashMap<ServiceType, IServiceCallProxy>(8);
    }

    /**
     * Add service call proxy to process remote service calls on the control plane.
     *
     * @param proxy Service call proxy
     */
    public void addServiceCallProxy(IServiceCallProxy proxy) {
        // FIXME: Ensure that there is just one proxy provider for each service
        this.serviceCallProxies.put(proxy.getServiceType(), proxy);
    }

    /**
     * Handle remote service call.
     *
     * @param msg Service call request
     * @param appId Application identifier
     * @param ch Channel for sending service call response
     */
    public void handleServiceCall(ServiceCallMsg msg, String appId, Channel ch) {
        this.serviceCallProxies.get(msg.getServiceType()).handleMessage(msg, appId, ch);
    }

    /**
     * Register endpoint.
     *
     * @param appId Application identifier
     */
    public void registerEndpt(String appId) {
        for (ServiceType serviceType : this.serviceCallProxies.keySet())
            this.serviceCallProxies.get(serviceType).registerEndpt(appId);
    }

}
