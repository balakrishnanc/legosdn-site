package edu.duke.cs.legosdn.core.service.topology;

import edu.duke.cs.legosdn.core.SynchronousMessaging;
import edu.duke.cs.legosdn.core.appvisor.cplane.CPlaneMsg;
import edu.duke.cs.legosdn.core.appvisor.cplane.CPlaneMsgType;
import edu.duke.cs.legosdn.core.appvisor.cplane.CPlaneStub;
import net.floodlightcontroller.topology.ITopologyListener;
import net.floodlightcontroller.topology.ITopologyService;
import net.floodlightcontroller.topology.NodePortTuple;
import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Set;

/**
 * Stub for ITopologyService.
 */
public class TopologyServiceStub implements ITopologyService {

    private static final Logger logger = LoggerFactory.getLogger(TopologyServiceStub.class);

    private final CPlaneStub                      cPlaneStub;
    private final SynchronousMessaging<CPlaneMsg> cPlaneHandler;

    public TopologyServiceStub(CPlaneStub cplaneStub, SynchronousMessaging<CPlaneMsg> cPlaneHandler) {
        this.cPlaneStub = cplaneStub;
        this.cPlaneHandler = cPlaneHandler;
    }

    @Override
    public void addListener(ITopologyListener listener) {
        logger.warn("TopologyServiceStub::addListener method has no body!");
    }

    @Override
    public Date getLastUpdateTime() {
        final LastUpdateTimeReqMsg req = new LastUpdateTimeReqMsg(this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final LastUpdateTimeResMsg res = (LastUpdateTimeResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.date;
    }

    @Override
    public boolean isAttachmentPointPort(long switchid, short port) {
        return this.isAttachmentPointPort(switchid, port, false);
    }

    @Override
    public boolean isAttachmentPointPort(long switchid, short port, boolean tunnelEnabled) {
        final AttachmentPointPortReqMsg req = new AttachmentPointPortReqMsg(switchid, port, tunnelEnabled,
                                                                            this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final AttachmentPointPortResMsg res = (AttachmentPointPortResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.isAttachmentPointPort;
    }

    @Override
    public long getOpenflowDomainId(long switchId) {
        return this.getOpenflowDomainId(switchId, false);
    }

    @Override
    public long getOpenflowDomainId(long switchId, boolean tunnelEnabled) {
        final OpenflowDomainIdReqMsg req = new OpenflowDomainIdReqMsg(switchId, tunnelEnabled,
                                                                      this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final OpenflowDomainIdResMsg res = (OpenflowDomainIdResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.ofDomainId;
    }

    @Override
    public long getL2DomainId(long switchId) {
        return this.getL2DomainId(switchId, false);
    }

    @Override
    public long getL2DomainId(long switchId, boolean tunnelEnabled) {
        final L2DomainIdReqMsg req = new L2DomainIdReqMsg(switchId, tunnelEnabled, this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final L2DomainIdResMsg res = (L2DomainIdResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.l2DomainId;
    }

    @Override
    public boolean inSameOpenflowDomain(long switch1, long switch2) {
        return this.inSameOpenflowDomain(switch1, switch2, false);
    }

    @Override
    public boolean inSameOpenflowDomain(long switch1, long switch2, boolean tunnelEnabled) {
        final SameOpenflowDomainReqMsg req = new SameOpenflowDomainReqMsg(switch1, switch2, tunnelEnabled,
                                                                          this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final SameOpenflowDomainResMsg res = (SameOpenflowDomainResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.inSameOpenflowDomain;
    }

    @Override
    public Set<Long> getSwitchesInOpenflowDomain(long switchDPID) {
        return this.getSwitchesInOpenflowDomain(switchDPID, false);
    }

    @Override
    public Set<Long> getSwitchesInOpenflowDomain(long switchDPID, boolean tunnelEnabled) {
        final SwitchesInOpenflowDomainReqMsg req = new SwitchesInOpenflowDomainReqMsg(switchDPID, tunnelEnabled,
                                                                                      this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final SwitchesInOpenflowDomainResMsg res =
                (SwitchesInOpenflowDomainResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.switchesInOpenflowDomain;
    }

    @Override
    public boolean inSameL2Domain(long switch1, long switch2) {
        return this.inSameL2Domain(switch1, switch2, false);
    }

    @Override
    public boolean inSameL2Domain(long switch1, long switch2, boolean tunnelEnabled) {
        final SameL2DomainReqMsg req = new SameL2DomainReqMsg(switch1, switch2, tunnelEnabled,
                                                              this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final SameL2DomainResMsg res = (SameL2DomainResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.inSameL2Domain;
    }

    @Override
    public boolean isBroadcastDomainPort(long sw, short port) {
        return this.isBroadcastDomainPort(sw, port, false);
    }

    @Override
    public boolean isBroadcastDomainPort(long sw, short port, boolean tunnelEnabled) {
        final BroadcastDomainPortReqMsg req = new BroadcastDomainPortReqMsg(sw, port, tunnelEnabled,
                                                                            this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final BroadcastDomainPortResMsg res = (BroadcastDomainPortResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.isBroadcastDomainPort;
    }

    @Override
    public boolean isAllowed(long sw, short portId) {
        return this.isAllowed(sw, portId, false);
    }

    @Override
    public boolean isAllowed(long sw, short portId, boolean tunnelEnabled) {
        final AllowedReqMsg req = new AllowedReqMsg(sw, portId, tunnelEnabled,
                                                    this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final AllowedResMsg res = (AllowedResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.isAllowed;
    }

    @Override
    public boolean isConsistent(long oldSw, short oldPort, long newSw, short newPort) {
        return this.isConsistent(oldSw, oldPort, newSw, newPort, false);
    }

    @Override
    public boolean isConsistent(long oldSw, short oldPort, long newSw, short newPort, boolean tunnelEnabled) {
        final ConsistentReqMsg req = new ConsistentReqMsg(oldSw, oldPort, newSw, newPort, tunnelEnabled,
                                                          this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final ConsistentResMsg res = (ConsistentResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.isConsistent;
    }

    @Override
    public boolean isInSameBroadcastDomain(long s1, short p1, long s2, short p2) {
        return this.isInSameBroadcastDomain(s1, p1, s2, p2, false);
    }

    @Override
    public boolean isInSameBroadcastDomain(long s1, short p1, long s2, short p2, boolean tunnelEnabled) {
        final SameBroadcastDomainReqMsg req = new SameBroadcastDomainReqMsg(s1, p1, s2, p2, tunnelEnabled,
                                                                            this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final SameBroadcastDomainResMsg res = (SameBroadcastDomainResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.isInSameBroadcastDomain;
    }

    @Override
    public Set<Short> getPortsWithLinks(long sw) {
        return this.getPortsWithLinks(sw, false);
    }

    @Override
    public Set<Short> getPortsWithLinks(long sw, boolean tunnelEnabled) {
        final PortsWithLinksReqMsg req = new PortsWithLinksReqMsg(sw, tunnelEnabled, this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final PortsWithLinksResMsg res = (PortsWithLinksResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.portsWithLinks;
    }

    @Override
    public Set<Short> getBroadcastPorts(long targetSw, long src, short srcPort) {
        return this.getBroadcastPorts(targetSw, src, srcPort, false);
    }

    @Override
    public Set<Short> getBroadcastPorts(long targetSw, long src, short srcPort, boolean tunnelEnabled) {
        final BroadcastPortsReqMsg req = new BroadcastPortsReqMsg(targetSw, src, srcPort, tunnelEnabled,
                                                                  this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final BroadcastPortsResMsg res = (BroadcastPortsResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.broadcastPorts;
    }

    @Override
    public boolean isIncomingBroadcastAllowed(long sw, short portId) {
        return this.isIncomingBroadcastAllowed(sw, portId, false);
    }

    @Override
    public boolean isIncomingBroadcastAllowed(long sw, short portId, boolean tunnelEnabled) {
        final IncomingBroadcastAllowedReqMsg req = new IncomingBroadcastAllowedReqMsg(sw, portId, tunnelEnabled,
                                                                                      this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final IncomingBroadcastAllowedResMsg res =
                (IncomingBroadcastAllowedResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.isIncomingBroadcastAllowed;
    }

    @Override
    public NodePortTuple getOutgoingSwitchPort(long src, short srcPort, long dst, short dstPort) {
        return this.getOutgoingSwitchPort(src, srcPort, dst, dstPort, false);
    }

    @Override
    public NodePortTuple getOutgoingSwitchPort(long src, short srcPort, long dst, short dstPort,
                                               boolean tunnelEnabled) {
        final OutgoingSwitchPortReqMsg req = new OutgoingSwitchPortReqMsg(src, srcPort, dst, dstPort, tunnelEnabled,
                                                                          this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final OutgoingSwitchPortResMsg res = (OutgoingSwitchPortResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.outgoingSwitchPort;
    }

    @Override
    public NodePortTuple getIncomingSwitchPort(long src, short srcPort, long dst, short dstPort) {
        return this.getIncomingSwitchPort(src, srcPort, dst, dstPort, false);
    }

    @Override
    public NodePortTuple getIncomingSwitchPort(long src, short srcPort, long dst, short dstPort,
                                               boolean tunnelEnabled) {
        final IncomingSwitchPortReqMsg req = new IncomingSwitchPortReqMsg(src, srcPort, dst, dstPort, tunnelEnabled,
                                                                          this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final IncomingSwitchPortResMsg res = (IncomingSwitchPortResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.incomingSwitchPort;
    }

    @Override
    public NodePortTuple getAllowedOutgoingBroadcastPort(long src, short srcPort, long dst, short dstPort) {
        return this.getAllowedOutgoingBroadcastPort(src, srcPort, dst, dstPort, false);
    }

    @Override
    public NodePortTuple getAllowedOutgoingBroadcastPort(long src, short srcPort, long dst, short dstPort,
                                                         boolean tunnelEnabled) {
        final AllowedOutgoingBroadcastPortReqMsg req =
                new AllowedOutgoingBroadcastPortReqMsg(src, srcPort, dst, dstPort, tunnelEnabled,
                                                       this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final AllowedOutgoingBroadcastPortResMsg res =
                (AllowedOutgoingBroadcastPortResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.allowedOutgoingBroadcastPort;
    }

    @Override
    public NodePortTuple getAllowedIncomingBroadcastPort(long src, short srcPort) {
        return this.getAllowedIncomingBroadcastPort(src, srcPort, false);
    }

    @Override
    public NodePortTuple getAllowedIncomingBroadcastPort(long src, short srcPort, boolean tunnelEnabled) {
        final AllowedIncomingBroadcastPortReqMsg req =
                new AllowedIncomingBroadcastPortReqMsg(src, srcPort, tunnelEnabled, this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final AllowedIncomingBroadcastPortResMsg res =
                (AllowedIncomingBroadcastPortResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.allowedIncomingBroadcastPort;
    }

    @Override
    public Set<NodePortTuple> getBroadcastDomainPorts() {
        final BroadcastDomainPortsReqMsg req = new BroadcastDomainPortsReqMsg(this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final BroadcastDomainPortsResMsg res = (BroadcastDomainPortsResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.broadcastDomainPorts;
    }

    @Override
    public Set<NodePortTuple> getTunnelPorts() {
        final TunnelPortsReqMsg req = new TunnelPortsReqMsg(this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final TunnelPortsResMsg res = (TunnelPortsResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.tunnelPorts;
    }

    @Override
    public Set<NodePortTuple> getBlockedPorts() {
        final BlockedPortsReqMsg req = new BlockedPortsReqMsg(this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final BlockedPortsResMsg res = (BlockedPortsResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.blockedPorts;
    }

    @Override
    public Set<Short> getPorts(long sw) {
        final PortsReqMsg req = new PortsReqMsg(sw, this.cPlaneStub.getAppPort());
        final Channel ch = cPlaneStub.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, req));
        final PortsResMsg res = (PortsResMsg) this.cPlaneHandler.waitForMessage().payload;
        ch.close();
        return res.ports;
    }

}
