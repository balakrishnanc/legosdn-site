package edu.duke.cs.legosdn.core.service;

import edu.duke.cs.legosdn.core.appvisor.cplane.ServiceCallMsg;
import edu.duke.cs.legosdn.core.appvisor.cplane.ServiceType;
import org.jboss.netty.channel.Channel;

/**
 * Service call proxy interface.
 */
public interface IServiceCallProxy {

    /**
     * @return Service type
     */
    public ServiceType getServiceType();

    /**
     * Register endpoint.
     *
     * @param appId Application identifier
     */
    public void registerEndpt(String appId);

    /**
     * Handle remote service call.
     *
     * @param msg Service call request
     * @param appId Application identifier
     * @param ch Channel for sending service call response
     */
    public void handleMessage(ServiceCallMsg msg, String appId, Channel ch);

}
