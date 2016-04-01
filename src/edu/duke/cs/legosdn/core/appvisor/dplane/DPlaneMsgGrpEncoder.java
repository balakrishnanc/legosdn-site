package edu.duke.cs.legosdn.core.appvisor.dplane;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encodes data plane messages before they go out on the wire.
 *
 * @author bala
 */
public class DPlaneMsgGrpEncoder extends SimpleChannelHandler {

    private static final Logger logger = LoggerFactory.getLogger(DPlaneMsgGrpEncoder.class);

    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        DPlaneMsgGrp m = (DPlaneMsgGrp) e.getMessage();
        ChannelBuffer buf = ChannelBuffers.buffer(m.length);
        m.writeTo(buf);
        Channels.write(ctx, e.getFuture(), buf);
    }

}
