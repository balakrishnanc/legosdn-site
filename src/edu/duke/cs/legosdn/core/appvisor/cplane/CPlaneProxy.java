
package edu.duke.cs.legosdn.core.appvisor.cplane;

import org.jboss.netty.channel.Channel;

/**
 * Required methods for a control plane proxy implementation.
 */
public interface CPlaneProxy {

    /**
     * Handle registration of a remote endpoint.
     *
     * @param appId Remote application identifier
     * @param re Remote endpoint information
     * @param ch Channel for communicating with the remote socket
     */
    public void registerEndpoint(String appId, RemoteEndpt re, Channel ch);

    /**
     * De-register an existing active remote application.
     *
     * @param appId Remote application identifier
     */
    public void unregisterEndpoint(String appId);

    /**
     * Add new message subscription.
     *
     * @param appId Remote application identifier
     * @param msgSub Subscription addition/cancellation message
     */
    public void addMsgSubscription(String appId, OFMsgSubscription msgSub);

    /**
     * Cancel message subscription.
     *
     * @param appId Remote application identifier
     * @param msgSub Subscription addition/cancellation message
     */
    public void delMsgSubscription(String appId, OFMsgSubscription msgSub);

    /**
     * Return the application identifier corresponding to the data plane listen port number.
     *
     * @param appPort Application data plane port number
     * @return Application identifier
     */
    public String getAppID(short appPort);

}
