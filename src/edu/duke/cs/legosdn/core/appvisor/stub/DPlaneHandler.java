/** -*- mode: java; coding: utf-8; fill-column: 80; -*-
 * Created by Balakrishnan Chandrasekaran on 2014-03-21 01:27
 * Copyright (c) 2014 Balakrishnan Chandrasekaran <balakrishnan.c@gmail.com>.
 */
package edu.duke.cs.legosdn.core.appvisor.stub;

import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsg;
import edu.duke.cs.legosdn.core.appvisor.dplane.UnknownMsgTypeException;
import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IListener;
import net.floodlightcontroller.core.IOFMessageListener;
import org.jboss.netty.channel.*;
import org.openflow.protocol.OFMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Data plane channel handler for the AppVisor-Stub.
 */
public class DPlaneHandler extends SimpleChannelHandler {

    private Logger logger = LoggerFactory.getLogger(DPlaneHandler.class);

    private final IOFMessageListener                      app;
    private final FloodlightContext                       flContext;
    private final ConcurrentHashMap<Long, OFSwitchStub> switches;

    /**
     * Create a new data plane handler.
     *
     * @param app Application
     * @param flContext Floodlight context
     * @param switches Map of switches
     */
    public DPlaneHandler(IOFMessageListener app,
                         FloodlightContext flContext,
                         ConcurrentHashMap<Long, OFSwitchStub> switches) {
        this.app = app;
        this.flContext = flContext;
        this.switches = switches;
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        if (logger.isTraceEnabled()) {
            logger.trace("channelConnected> from {}", e.getChannel().getRemoteAddress().toString());
        }
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        final DPlaneMsg m = (DPlaneMsg) e.getMessage();

        if (logger.isTraceEnabled()) {
            logger.trace("messageReceived> {}", m.getMsgType());
        }

        if (!this.switches.containsKey(m.getSwitchID())) {
            // Encountered a new switch in the network!
            OFSwitchStub sw = new OFSwitchStub(m.getSwitchID(), null, null);
            this.switches.put(m.getSwitchID(), sw);
        }

        final OFSwitchStub sw = this.switches.get(m.getSwitchID());
        /* Channel used by the stub to relay the messages to the proxy will change for every message! */
        sw.setChannel(e.getChannel());

        final IListener.Command cmd;
        switch (m.getMsgType()) {
            case OFPACKET_IN:
                cmd = this.app.receive(sw, (OFMessage) m.getMsgPayload(), flContext);

                // Send the command output by the 'receive' method also as a data plane message to the proxy.
                e.getChannel().write(new DPlaneMsg(cmd)).addListener(new ChannelFutureListener() {

                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            if (logger.isTraceEnabled()) {
                                logger.trace("messageReceived> processed {} and responded with {}",
                                             m.getMsgType(), cmd);
                            }
                        }
                    }

                });
                break;
            default:
                throw new UnknownMsgTypeException(String.format("Received unknown message type '%s'", m.getMsgType()));
        }
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        e.getChannel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        logger.error(e.getCause().getLocalizedMessage());

        e.getChannel().close();
    }

}
