package edu.duke.cs.legosdn.core.appvisor.cplane;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class ServiceCallMsgDecoder extends FrameDecoder {

    @Override
    protected Object decode(ChannelHandlerContext channelHandlerContext, Channel channel, ChannelBuffer buffer)
            throws Exception {
        if (buffer.readableBytes() < 4) {
            return null;
        }

        int msgTypeNum = buffer.readInt();
        Class msgType = ServiceType.values()[msgTypeNum].type;
        return ((ServiceCallMsg) msgType.newInstance()).readFrom(buffer);
    }

}
