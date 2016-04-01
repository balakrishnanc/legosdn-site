package edu.duke.cs.legosdn.tools.cr.proto;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * Constructs C/R service control message from the read transmitted over the wire.
 */
public class CRCtrlMsgDecoder extends FrameDecoder {

    @Override
    protected Object decode(ChannelHandlerContext channelHandlerContext, Channel channel, ChannelBuffer buf)
            throws Exception {
        return CRCtrlMsg.parse(buf);
    }

}
