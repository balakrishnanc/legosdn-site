package edu.duke.cs.legosdn.core.service.topology;

import edu.duke.cs.legosdn.core.appvisor.cplane.CPlaneMsg;
import edu.duke.cs.legosdn.core.appvisor.cplane.CPlaneMsgType;
import edu.duke.cs.legosdn.core.appvisor.cplane.ServiceCallMsg;
import edu.duke.cs.legosdn.core.appvisor.cplane.ServiceType;
import edu.duke.cs.legosdn.core.service.IServiceCallProxy;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.topology.ITopologyService;
import net.floodlightcontroller.topology.NodePortTuple;
import org.jboss.netty.channel.Channel;

import java.util.Set;

/**
 * ITopologyService proxy.
 */
public class TopologyServiceProxy implements IServiceCallProxy {

    // ITopologyService provider
    private final ITopologyService service;

    public TopologyServiceProxy(FloodlightModuleContext moduleContext) {
        this.service = moduleContext.getServiceImpl(ITopologyService.class);
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.TOPOLOGY;
    }

    @Override
    public void handleMessage(ServiceCallMsg serviceCallMsg, String appId, Channel ch) {
        TopologyServiceMsg msg = (TopologyServiceMsg) serviceCallMsg;

        if (msg instanceof LastUpdateTimeReqMsg) {
            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG,
                                   new LastUpdateTimeResMsg(this.service.getLastUpdateTime())));
            return;
        }

        if (msg instanceof AttachmentPointPortReqMsg) {
            AttachmentPointPortReqMsg req = (AttachmentPointPortReqMsg) msg;
            boolean res = this.service.isAttachmentPointPort(req.switchid, req.port, req.tunnelEnabled);

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new AttachmentPointPortResMsg(res)));
            return;
        }

        if (msg instanceof OpenflowDomainIdReqMsg) {
            OpenflowDomainIdReqMsg req = (OpenflowDomainIdReqMsg) msg;
            long res = this.service.getOpenflowDomainId(req.switchid, req.tunnelEnabled);

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new OpenflowDomainIdResMsg(res)));
            return;
        }

        if (msg instanceof L2DomainIdReqMsg) {
            L2DomainIdReqMsg req = (L2DomainIdReqMsg) msg;
            long res = this.service.getL2DomainId(req.switchid, req.tunnelEnabled);

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new L2DomainIdResMsg(res)));
            return;
        }

        if (msg instanceof SameOpenflowDomainReqMsg) {
            SameOpenflowDomainReqMsg req = (SameOpenflowDomainReqMsg) msg;
            boolean res = this.service.inSameOpenflowDomain(req.switch1, req.switch2, req.tunnelEnabled);

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new SameOpenflowDomainResMsg(res)));
            return;
        }

        if (msg instanceof SameL2DomainReqMsg) {
            SameL2DomainReqMsg req = (SameL2DomainReqMsg) msg;
            boolean res = this.service.inSameL2Domain(req.switch1, req.switch2, req.tunnelEnabled);

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new SameL2DomainResMsg(res)));
            return;
        }

        if (msg instanceof BroadcastDomainPortReqMsg) {
            BroadcastDomainPortReqMsg req = (BroadcastDomainPortReqMsg) msg;
            boolean res = this.service.isBroadcastDomainPort(req.switchid, req.port, req.tunnelEnabled);

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new BroadcastDomainPortResMsg(res)));
            return;
        }

        if (msg instanceof AllowedReqMsg) {
            AllowedReqMsg req = (AllowedReqMsg) msg;
            boolean res = this.service.isAllowed(req.switchid, req.port, req.tunnelEnabled);

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new AllowedResMsg(res)));
            return;
        }

        if (msg instanceof IncomingBroadcastAllowedReqMsg) {
            IncomingBroadcastAllowedReqMsg req = (IncomingBroadcastAllowedReqMsg) msg;
            boolean res = this.service.isIncomingBroadcastAllowed(req.switchid, req.port, req.tunnelEnabled);

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new IncomingBroadcastAllowedResMsg(res)));
            return;
        }

        if (msg instanceof ConsistentReqMsg) {
            ConsistentReqMsg req = (ConsistentReqMsg) msg;
            boolean res = this.service.isConsistent(req.oldSw, req.oldPort, req.newSw, req.newPort, req.tunnelEnabled);

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new ConsistentResMsg(res)));
            return;
        }

        if (msg instanceof SameBroadcastDomainReqMsg) {
            SameBroadcastDomainReqMsg req = (SameBroadcastDomainReqMsg) msg;
            boolean res = this.service.isInSameBroadcastDomain(req.s1, req.p1, req.s2, req.p2, req.tunnelEnabled);

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new SameBroadcastDomainResMsg(res)));
            return;
        }

        if (msg instanceof OutgoingSwitchPortReqMsg) {
            OutgoingSwitchPortReqMsg req = (OutgoingSwitchPortReqMsg) msg;
            NodePortTuple res = this.service.getOutgoingSwitchPort(req.src, req.srcPort,
                                                                   req.dst, req.dstPort, req.tunnelEnabled);

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new OutgoingSwitchPortResMsg(res)));
            return;
        }

        if (msg instanceof IncomingSwitchPortReqMsg) {
            IncomingSwitchPortReqMsg req = (IncomingSwitchPortReqMsg) msg;
            NodePortTuple res = this.service.getIncomingSwitchPort(req.src, req.srcPort,
                                                                   req.dst, req.dstPort, req.tunnelEnabled);

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new IncomingSwitchPortResMsg(res)));
            return;
        }

        if (msg instanceof AllowedOutgoingBroadcastPortReqMsg) {
            AllowedOutgoingBroadcastPortReqMsg req = (AllowedOutgoingBroadcastPortReqMsg) msg;
            NodePortTuple res = this.service.getAllowedOutgoingBroadcastPort(req.src, req.srcPort,
                                                                             req.dst, req.dstPort, req.tunnelEnabled);

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new AllowedOutgoingBroadcastPortResMsg(res)));
            return;
        }

        if (msg instanceof AllowedIncomingBroadcastPortReqMsg) {
            AllowedIncomingBroadcastPortReqMsg req = (AllowedIncomingBroadcastPortReqMsg) msg;
            NodePortTuple res = this.service.getAllowedIncomingBroadcastPort(req.src, req.srcPort, req.tunnelEnabled);

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new AllowedIncomingBroadcastPortResMsg(res)));
            return;
        }

        if (msg instanceof SwitchesInOpenflowDomainReqMsg) {
            SwitchesInOpenflowDomainReqMsg req = (SwitchesInOpenflowDomainReqMsg) msg;
            Set<Long> res = this.service.getSwitchesInOpenflowDomain(req.switchid, req.tunnelEnabled);

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new SwitchesInOpenflowDomainResMsg(res)));
            return;
        }

        if (msg instanceof PortsWithLinksReqMsg) {
            PortsWithLinksReqMsg req = (PortsWithLinksReqMsg) msg;
            Set<Short> res = this.service.getPortsWithLinks(req.switchid, req.tunnelEnabled);

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new PortsWithLinksResMsg(res)));
            return;
        }

        if (msg instanceof BroadcastPortsReqMsg) {
            BroadcastPortsReqMsg req = (BroadcastPortsReqMsg) msg;
            Set<Short> res = this.service.getBroadcastPorts(req.targetSw, req.src, req.srcPort, req.tunnelEnabled);

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new BroadcastPortsResMsg(res)));
            return;
        }

        if (msg instanceof PortsReqMsg) {
            PortsReqMsg req = (PortsReqMsg) msg;
            Set<Short> res = this.service.getPorts(req.sw);

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new PortsResMsg(res)));
            return;
        }

        if (msg instanceof BroadcastDomainPortsReqMsg) {
            BroadcastDomainPortsReqMsg req = (BroadcastDomainPortsReqMsg) msg;
            Set<NodePortTuple> res = this.service.getBroadcastDomainPorts();

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new BroadcastDomainPortsResMsg(res)));
            return;
        }

        if (msg instanceof TunnelPortsReqMsg) {
            TunnelPortsReqMsg req = (TunnelPortsReqMsg) msg;
            Set<NodePortTuple> res = this.service.getTunnelPorts();

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new TunnelPortsResMsg(res)));
            return;
        }

        if (msg instanceof BlockedPortsReqMsg) {
            BlockedPortsReqMsg req = (BlockedPortsReqMsg) msg;
            Set<NodePortTuple> res = this.service.getTunnelPorts();

            ch.write(new CPlaneMsg(CPlaneMsgType.SERVICE_CALL_MSG, new BlockedPortsResMsg(res)));
            return;
        }
    }

    @Override
    public void registerEndpt(String appId) {
    }

}
