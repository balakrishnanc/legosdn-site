package edu.duke.cs.legosdn.core.service.link;

import edu.duke.cs.legosdn.core.appvisor.cplane.CPlaneMsg;
import edu.duke.cs.legosdn.core.appvisor.cplane.CPlaneMsgType;
import edu.duke.cs.legosdn.core.appvisor.cplane.ServiceCallMsg;
import edu.duke.cs.legosdn.core.appvisor.cplane.ServiceType;
import edu.duke.cs.legosdn.core.appvisor.proxy.IAppAwareLinkDiscoveryService;
import edu.duke.cs.legosdn.core.service.IServiceCallProxy;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.linkdiscovery.ILinkDiscoveryService;
import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ILinkDiscoveryService proxy.
 */
public class LinkDiscoveryServiceProxy implements IServiceCallProxy {

    private static final Logger logger = LoggerFactory.getLogger(LinkDiscoveryServiceProxy.class);

    // ILinkDiscoveryService provider
    private final ILinkDiscoveryService         service;
    private final IAppAwareLinkDiscoveryService proxyService;

    public LinkDiscoveryServiceProxy(FloodlightModuleContext moduleContext) {
        this.service = moduleContext.getServiceImpl(ILinkDiscoveryService.class);
        this.proxyService = (IAppAwareLinkDiscoveryService) this.service;
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.LINKDISCOVERY;
    }

    @Override
    public void handleMessage(ServiceCallMsg serviceCallMsg, String appId, Channel ch) {
        final LinkDiscoveryServiceMsg msg = (LinkDiscoveryServiceMsg) serviceCallMsg;

        if (msg instanceof TunnelPortReqMsg) {
            if (logger.isDebugEnabled()) {
                logger.debug("handleMessage> service({}) received 'TunnelPortReqMsg' message",
                             msg.getServiceType());
            }

            final TunnelPortReqMsg req = (TunnelPortReqMsg) msg;
            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG,
                                   new TunnelPortResMsg(this.service.isTunnelPort(req.sw, req.port))));
            return;
        }

        if (msg instanceof LinksReqMsg) {
            if (logger.isDebugEnabled()) {
                logger.debug("handleMessage> service({}) received 'LinksReqMsg' message",
                             msg.getServiceType());
            }

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG,
                                   new LinksResMsg(this.proxyService.getLinks(appId))));
            return;
        }

        logger.error("handleMessage> received unknown message for service '{}'", msg.getServiceType());
    }

    @Override
    public void registerEndpt(String appId) {
        this.proxyService.registerEndpt(appId);
    }

}
