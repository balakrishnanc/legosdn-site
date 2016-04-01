/** -*- mode: java; coding: utf-8; fill-column: 80; -*-
 * Created by Balakrishnan Chandrasekaran on 2014-09-22 01:26
 * Copyright (c) 2014 Balakrishnan Chandrasekaran <balakrishnan.c@gmail.com>.
 */
package edu.duke.cs.legosdn.tools.cr.cli;

import edu.duke.cs.legosdn.core.Defaults;
import edu.duke.cs.legosdn.tools.cr.CRClientProvider;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Channel handler to handle C/R control messages between controller and the C/R service.
 *
 * @param <T> Actual message type supported by the channel handler.
 */
public class CRCtrlChHandler<T> extends SimpleChannelHandler {

    private Logger logger = LoggerFactory.getLogger(CRCtrlChHandler.class);

    private final CRClientProvider crClientProvider;

    private final Queue<T> msgQueue;

    /**
     * Initialize C/R control channel handler with the C/R service client provider.
     *
     * @param crClientProvider C/R service client provider
     */
    CRCtrlChHandler(CRClientProvider crClientProvider) {
        this.crClientProvider = crClientProvider;
        this.msgQueue = new ArrayDeque<T>(2);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        // FIXME: Message Queue is not safe! Responses to one service call may be collected by another!!!
        synchronized (this.msgQueue) {
            this.msgQueue.add((T) e.getMessage());
            this.msgQueue.notify();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        logger.error(e.getCause().getLocalizedMessage(), e);

        ctx.getChannel().close();
    }

    public T waitForMessage() {
        synchronized (this.msgQueue) {
            while(this.msgQueue.isEmpty()) {
                try {
                    if (logger.isTraceEnabled()) {
                        logger.trace("waitForMessage> Waiting for response");
                    }

                    this.msgQueue.wait(Defaults.CR_RESP_WAIT_TIME);
                    break;
                } catch (InterruptedException e) {
                    logger.error("Failed while waiting for service response; {}", e.getLocalizedMessage(), e);
                    break;
                }
            }
            return this.msgQueue.poll();
        }
    }

}
