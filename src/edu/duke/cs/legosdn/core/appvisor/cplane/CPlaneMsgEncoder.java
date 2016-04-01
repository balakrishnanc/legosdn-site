
package edu.duke.cs.legosdn.core.appvisor.cplane;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class CPlaneMsgEncoder extends SimpleChannelHandler {

    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        CPlaneMsg msg = (CPlaneMsg) e.getMessage();

        ChannelBuffer buf = ChannelBuffers.buffer(msg.getLength());
        msg.writeTo(buf);
        Channels.write(ctx, e.getFuture(), buf);
    }

}
