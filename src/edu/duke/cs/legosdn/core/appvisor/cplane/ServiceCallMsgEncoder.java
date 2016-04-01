package edu.duke.cs.legosdn.core.appvisor.cplane;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class ServiceCallMsgEncoder extends SimpleChannelHandler {

    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        ServiceCallMsg sCallMsg = (ServiceCallMsg) e.getMessage();

        ChannelBuffer buf = ChannelBuffers.buffer(sCallMsg.getLength());
        sCallMsg.writeTo(buf);
        Channels.write(ctx, e.getFuture(), buf);
    }

}
