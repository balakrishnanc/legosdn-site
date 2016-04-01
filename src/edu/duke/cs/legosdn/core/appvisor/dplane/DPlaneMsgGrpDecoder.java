package edu.duke.cs.legosdn.core.appvisor.dplane;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * Decodes data plane messages once they are read from the wire.
 */
public class DPlaneMsgGrpDecoder extends FrameDecoder {

    @Override
    protected Object decode(ChannelHandlerContext channelHandlerContext, Channel channel, ChannelBuffer buf)
            throws Exception {
        return DPlaneMsgGrp.readFrom(buf);
    }

}
