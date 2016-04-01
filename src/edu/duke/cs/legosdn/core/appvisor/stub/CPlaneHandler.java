package edu.duke.cs.legosdn.core.appvisor.stub;

import edu.duke.cs.legosdn.core.Defaults;
import edu.duke.cs.legosdn.core.SynchronousMessaging;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * CPlaneHandler is the control plane channel handler for the AppProxy-stub.
 *
 * @param <T> Actual message type supported by the channel handler.
 */
public class CPlaneHandler<T> extends SimpleChannelHandler implements SynchronousMessaging<T> {

    private static final Logger logger = LoggerFactory.getLogger(CPlaneHandler.class);

    private final Queue<T> msgQueue;

    /**
     * Initialize CPlaneHandler.
     */
    public CPlaneHandler() {
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
        e.getCause().printStackTrace();
        logger.error(e.getCause().getLocalizedMessage());

        ctx.getChannel().close();
    }

    @Override
    public T waitForMessage() {
        synchronized (this.msgQueue) {
            while(this.msgQueue.isEmpty()) {
                try {
                    if (logger.isTraceEnabled()) {
                        logger.trace("waitForMessage> Waiting for response");
                    }

                    this.msgQueue.wait(Defaults.APP_CP_RESP_WAIT_TIME);
                } catch (InterruptedException e) {
                    logger.error("Failed while waiting for service response; {}", e.getLocalizedMessage(), e);
                    break;
                }
            }
            return this.msgQueue.poll();
        }
    }

}
