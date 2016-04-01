package edu.duke.cs.legosdn.tools.cr.proto;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * C/R service control message to check the status of the service.
 */
public class ServiceCheck extends CRCtrlMsg {

    /**
     * Initialize ServiceCheck.
     */
    public ServiceCheck() {
        super(CRCtrlMsgType.SERVICE_CHECK);
    }

    @Override
    public ServiceCheck readFrom(ChannelBuffer buf) {
        return ServiceCheck.parse(buf);
    }

    public static ServiceCheck parse(ChannelBuffer buf) {
        return new ServiceCheck();
    }

}
