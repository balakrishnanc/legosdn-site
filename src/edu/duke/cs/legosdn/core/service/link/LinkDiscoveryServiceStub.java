package edu.duke.cs.legosdn.core.service.link;

import edu.duke.cs.legosdn.core.SynchronousMessaging;
import edu.duke.cs.legosdn.core.appvisor.cplane.CPlaneMsg;
import edu.duke.cs.legosdn.core.appvisor.cplane.CPlaneMsgType;
import edu.duke.cs.legosdn.core.appvisor.cplane.CPlaneStub;
import net.floodlightcontroller.linkdiscovery.ILinkDiscovery;
import net.floodlightcontroller.linkdiscovery.ILinkDiscoveryListener;
import net.floodlightcontroller.linkdiscovery.ILinkDiscoveryService;
import net.floodlightcontroller.linkdiscovery.LinkInfo;
import net.floodlightcontroller.routing.Link;
import net.floodlightcontroller.topology.NodePortTuple;
import org.jboss.netty.channel.Channel;
import org.openflow.protocol.OFPacketOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * Stub for ILinkDiscoveryService.
 */
public class LinkDiscoveryServiceStub implements ILinkDiscoveryService {

    private static final Logger logger = LoggerFactory.getLogger(LinkDiscoveryServiceStub.class);

    private final CPlaneStub                      cPlaneStub;
    private final SynchronousMessaging<CPlaneMsg> cPlaneHandler;

    public LinkDiscoveryServiceStub(CPlaneStub cplaneStub, SynchronousMessaging<CPlaneMsg> cPlaneHandler) {
        this.cPlaneStub = cplaneStub;
        this.cPlaneHandler = cPlaneHandler;
    }

    @Override
    public boolean isTunnelPort(long sw, short port) {
        final TunnelPortReqMsg req = new TunnelPortReqMsg(sw, port, this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final TunnelPortResMsg res = (TunnelPortResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.isTunnelPort;
    }

    @Override
    public Map<Link, LinkInfo> getLinks() {
        final LinksReqMsg req = new LinksReqMsg(this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final LinksResMsg res = (LinksResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.links;
    }

    @Override
    public LinkInfo getLinkInfo(Link link) {
        return null;
    }

    @Override
    public ILinkDiscovery.LinkType getLinkType(Link lt, LinkInfo info) {
        return null;
    }

    @Override
    public OFPacketOut generateLLDPMessage(long sw, short port, boolean isStandard, boolean isReverse) {
        return null;
    }

    @Override
    public Map<Long, Set<Link>> getSwitchLinks() {
        return null;
    }

    @Override
    public void addListener(ILinkDiscoveryListener listener) {
        logger.warn("LinkDiscoveryServiceStub::addListener method has no body!");
    }

    @Override
    public Set<NodePortTuple> getSuppressLLDPsInfo() {
        return null;
    }

    @Override
    public void AddToSuppressLLDPs(long sw, short port) {

    }

    @Override
    public void RemoveFromSuppressLLDPs(long sw, short port) {

    }

    @Override
    public Set<Short> getQuarantinedPorts(long sw) {
        return null;
    }

    @Override
    public boolean isAutoPortFastFeature() {
        return false;
    }

    @Override
    public void setAutoPortFastFeature(boolean autoPortFastFeature) {

    }

    @Override
    public Map<NodePortTuple, Set<Link>> getPortLinks() {
        return null;
    }

    @Override
    public void addMACToIgnoreList(long mac, int ignoreBits) {

    }

}
