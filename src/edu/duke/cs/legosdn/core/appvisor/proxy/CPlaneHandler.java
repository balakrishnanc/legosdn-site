package edu.duke.cs.legosdn.core.appvisor.proxy;

import edu.duke.cs.legosdn.core.appvisor.cplane.*;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Service side channel handler to process control plane messages to/from remote applications.
 */
public class CPlaneHandler extends SimpleChannelHandler {

    private static final Logger logger = LoggerFactory.getLogger(CPlaneHandler.class);

    private final CPlaneProxy     cplaneProxy;
    private final ServiceRegistry serviceRegistry;

    /**
     * Instantiate control plane handler with the registries associated with it.
     *
     * @param cplaneProxy Instance of a control plane proxy to which this handler is associated.
     * @param serviceRegistry Instance of the service registry to which this handler is associated.
     */
    public CPlaneHandler(CPlaneProxy cplaneProxy, ServiceRegistry serviceRegistry) {
        this.cplaneProxy = cplaneProxy;
        this.serviceRegistry = serviceRegistry;
    }

    /**
     * Handle registration of a remote endpoint.
     *
     * @param appId Remote application identifier
     * @param re Remote endpoint information
     * @param ch Channel for communicating with the remote socket
     */
    private void handleEndptRegistration(String appId, RemoteEndpt re, Channel ch) {
        this.cplaneProxy.registerEndpoint(appId, re, ch);
        this.serviceRegistry.registerEndpt(appId);
        if (logger.isInfoEnabled()) {
            logger.info("handleEndptRegistration> discovered new application {}", appId);
        }
    }

    /**
     * Handle service call invocations.
     *
     * @param msg Service call message
     * @param ch Channel for communicating with the remote socket
     */
    private void handleServiceCall(ServiceCallMsg msg, Channel ch) {
        if (logger.isTraceEnabled()) {
            logger.trace("handleServiceCall> service: {}", msg.getServiceType());
        }

        if (msg.getAppPort() <= 0) {
            final String err = "Service requests should have a positive value for application port!";
            throw new RuntimeException(err);
        }

        this.serviceRegistry.handleServiceCall(msg, this.cplaneProxy.getAppID(msg.getAppPort()), ch);
    }

    /**
     * Handle message subscription or cancellation requests.
     *
     * @param appId Remote application identifier
     * @param msgSub Message subscription or cancellation
     * @param ch Channel for communicating with the remote socket
     */
    @SuppressWarnings("unused")
    private void handleEndptMsgSubscription(String appId, OFMsgSubscription msgSub, Channel ch) {
        switch (msgSub.getStatus()) {
            case ADD_SUBSCRIPTION:
                this.cplaneProxy.addMsgSubscription(appId, msgSub);

                if (logger.isInfoEnabled()) {
                    logger.info("handleEndptMsgSubscription> {} subscribed to {}", appId, msgSub.getMsgType());
                }
                break;
            case DEL_SUBSCRIPTION:
                this.cplaneProxy.delMsgSubscription(appId, msgSub);

                if (logger.isInfoEnabled()) {
                    logger.info("handleEndptMsgSubscription> {} cancelled subscription of {}",
                                appId, msgSub.getMsgType());
                }
                break;
        }
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        CPlaneMsg cplaneMsg = (CPlaneMsg) e.getMessage();

        if (logger.isTraceEnabled()) {
            logger.trace("messageReceived> {}", cplaneMsg.type);
        }

        switch (cplaneMsg.type) {
            case EP_REMOTEINFO:
                final RemoteEndpt re = (RemoteEndpt) cplaneMsg.payload;
                this.handleEndptRegistration(re.appId, re, e.getChannel());
                break;
            case OF_ADD_MSGSUB:
            case OF_DEL_MSGSUB:
                final OFMsgSubscription msgSub = (OFMsgSubscription) cplaneMsg.payload;
                this.handleEndptMsgSubscription(new String(msgSub.appId), msgSub, e.getChannel());
                break;
            case SERVICE_CALL_MSG:
                final ServiceCallMsg scallMsg = (ServiceCallMsg) cplaneMsg.payload;
                this.handleServiceCall(scallMsg, e.getChannel());
                break;
        }
    }

    /**
     * Handle remote endpoint disconnects.
     *
     * @param ch Channel for communicating with the remote socket
     */
    @SuppressWarnings("unused")
    private void disconnectEndpoint(Channel ch) {
        InetSocketAddress remote = (InetSocketAddress) ch.getRemoteAddress();
        String endptInfo = RemoteRegistry.endptInfo(remote.getHostName(), remote.getPort());

        logger.info(String.format("Received disconnect from endpoint: control <%s>", endptInfo.toString()));

        this.cplaneProxy.unregisterEndpoint(endptInfo);
        // Close control channel
        ch.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();

        logger.error(e.getCause().getLocalizedMessage());
    }

}
