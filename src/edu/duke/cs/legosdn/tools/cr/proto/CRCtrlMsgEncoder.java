package edu.duke.cs.legosdn.tools.cr.proto;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * write C/R service control message over the wire.
 */
public class CRCtrlMsgEncoder extends SimpleChannelHandler {

    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        CRCtrlMsg m = (CRCtrlMsg) e.getMessage();

        ChannelBuffer buf = ChannelBuffers.buffer(m.getLength());
        m.writeTo(buf);

        Channels.write(ctx, e.getFuture(), buf);
    }

}
