package edu.duke.cs.legosdn.tools.cr.srv;

import edu.duke.cs.legosdn.tools.cr.CRServiceProvider;
import edu.duke.cs.legosdn.tools.cr.proto.CRCtrlMsg;
import edu.duke.cs.legosdn.tools.cr.proto.CRCtrlMsgType;
import edu.duke.cs.legosdn.tools.cr.proto.ServiceReply;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Channel handler to handle C/R control messages between controller and the C/R service.
 */
public class CRCtrlChHandler extends SimpleChannelHandler {

    private Logger logger = LoggerFactory.getLogger(CRCtrlChHandler.class);

    private final CRServiceProvider<CRCtrlMsg> crServiceProvider;

    private final ChannelGroup channelGroup;

    // Service-time statistics
    private Map<CRCtrlMsgType, AtomicLong> aggServiceTimes;
    private Map<CRCtrlMsgType, AtomicLong> numServiceCalls;

    /**
     * Initialize C/R control channel handler with the C/R service provider.
     *
     * @param crServiceProvider C/R service provider
     */
    CRCtrlChHandler(CRServiceProvider<CRCtrlMsg> crServiceProvider) {
        this.crServiceProvider = crServiceProvider;
        this.channelGroup = new DefaultChannelGroup();

        this.aggServiceTimes = new HashMap<CRCtrlMsgType, AtomicLong>(CRCtrlMsgType.values().length);
        this.numServiceCalls = new HashMap<CRCtrlMsgType, AtomicLong>(CRCtrlMsgType.values().length);
        for (CRCtrlMsgType crCtrlMsgType : CRCtrlMsgType.values()) {
            this.aggServiceTimes.put(crCtrlMsgType, new AtomicLong(0));
            this.numServiceCalls.put(crCtrlMsgType, new AtomicLong(0));
        }
    }

    /**
     * Update service-time trackers.
     *
     * @param crCtrlMsgType C/R service control message type
     * @param tElapsed Time elapsed
     */
    private void updateServiceTimes(CRCtrlMsgType crCtrlMsgType, long tElapsed) {
        this.numServiceCalls.get(crCtrlMsgType).incrementAndGet();
        this.aggServiceTimes.get(crCtrlMsgType).addAndGet(tElapsed);
    }

    /**
     * Dump service-time trackers.
     */
    public void dumpServiceTimes() {
        System.out.println("\n");
        System.out.println("---[ Average Service Times ]---");
        for (CRCtrlMsgType crCtrlMsgType : CRCtrlMsgType.values()) {
            double elapsedTime = this.aggServiceTimes.get(crCtrlMsgType).longValue() / 1000000.0;
            long numCalls = this.numServiceCalls.get(crCtrlMsgType).longValue();
            System.out.printf("  %16s: %.2fms\n", crCtrlMsgType, elapsedTime / numCalls);
        }
        System.out.println("\n");
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("channelConnected> AppVisor-Proxy @ %s",
                                      e.getChannel().getRemoteAddress().toString()));
        }

        this.channelGroup.add(e.getChannel());
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        final long tBeg, tEnd;
        final CRCtrlMsg req = (CRCtrlMsg) e.getMessage();

        CRCtrlMsg res;
        ChannelFuture f;
        switch (req.msgType) {
            case SERVICE_CHECK:
                tBeg = System.nanoTime();
                res = this.crServiceProvider.checkServiceStatus(req);
                tEnd = System.nanoTime();
                f = e.getChannel().write(res);
                if (!((ServiceReply) res).isOK()) {
                    // Service is not READY. There's no point in keeping the channel connected.
                    f.addListener(ChannelFutureListener.CLOSE);

                    if (logger.isWarnEnabled()) {
                        logger.warn("messageReceived> C/R service not ready. Channel closed.");
                    }
                }
                break;
            case REGISTER_PROC:
                tBeg = System.nanoTime();
                res = this.crServiceProvider.registerProcess(req);
                tEnd = System.nanoTime();
                f = e.getChannel().write(res);
                if (!((ServiceReply) res).isOK()) {
                    // Failed to register the process. There's no point in keeping the channel connected.
                    f.addListener(ChannelFutureListener.CLOSE);

                    if (logger.isWarnEnabled()) {
                        logger.warn("messageReceived> Failed to register process. Channel closed.");
                    }
                }
                break;
            case CHECKPOINT_PROC:
                tBeg = System.nanoTime();
                res = this.crServiceProvider.checkpointProcess(req);
                tEnd = System.nanoTime();
                e.getChannel().write(res);
                break;
            case RESTORE_PROC:
                tBeg = System.nanoTime();
                res = this.crServiceProvider.restoreProcess(req);
                tEnd = System.nanoTime();
                e.getChannel().write(res);
                break;
            default:
                tBeg = 0;
                tEnd = 0;
        }

        this.updateServiceTimes(req.msgType, tEnd - tBeg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        logger.error(e.getCause().getLocalizedMessage());

        ctx.getChannel().close();
    }

    /**
     * Terminate all open connections to the C/R service listener.
     */
    void terminateConnections() {
        this.channelGroup.close();
        this.channelGroup.clear();
    }

}
