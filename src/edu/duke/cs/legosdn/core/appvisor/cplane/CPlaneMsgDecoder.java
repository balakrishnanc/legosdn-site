
package edu.duke.cs.legosdn.core.appvisor.cplane;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class CPlaneMsgDecoder extends FrameDecoder {

    @Override
    protected Object decode(ChannelHandlerContext channelHandlerContext, Channel channel, ChannelBuffer buffer)
            throws Exception {
        return CPlaneMsg.readFrom(buffer);
    }

}
