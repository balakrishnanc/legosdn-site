package edu.duke.cs.legosdn.core.appvisor.cplane;

import org.jboss.netty.channel.Channel;

/**
 * Methods required to be supported by any AppVisor-stub implementation.
 */
public interface CPlaneStub {

    /**
     * Create new control plane channel to interact with the proxy (in the controller).
     * @return control plane channel
     */
    public Channel openCPlaneChannel();

    /**
     * @return Port on which the application receives data plane messages.
     */
    public short getAppPort();

}
