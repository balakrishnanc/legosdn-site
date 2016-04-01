package edu.duke.cs.legosdn.core.faults;

import edu.duke.cs.legosdn.core.appvisor.dplane.*;
import net.floodlightcontroller.linkdiscovery.ILinkDiscovery;
import net.floodlightcontroller.linkdiscovery.LinkInfo;
import net.floodlightcontroller.routing.Link;
import org.openflow.protocol.OFMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Basic implementation of an event transformer that supports both pre-fault and post-fault transforms.
 */
public class BasicTransformer extends BaseEventTransformer {

    /**
     * Transform, if necessary, an inbound port notification message to an equivalent message prior to a fault.
     *
     * @param portNotif Port notification
     * @param appId Application identification
     * @return List of equivalent data plane messages
     */
    protected List<DPlaneMsgExternalizable> preFaultTransform(PortNotification portNotif, String appId) {
        final List<DPlaneMsgExternalizable> msgs = new ArrayList<DPlaneMsgExternalizable>();
        final long switchId = portNotif.getSwitchId();
        switch (portNotif.getPortChangeType()) {
            case ADD:
            case UP: {
                activateSwToAllow(portNotif, appId, msgs);
                break;
            }
            case OTHER_UPDATE:
            case DELETE:
            case DOWN:
                break;
        }
        // Retain the original message intact.
        return retainMsg(portNotif, msgs);
    }

    @Override
    protected List<DPlaneMsgExternalizable> postFaultTransform(PortNotification portNotif,
                                                               String appId,
                                                               int attempt) {
        List<DPlaneMsgExternalizable> msgs = null;
        final long switchId = portNotif.getSwitchId();
        switch (portNotif.getPortChangeType()) {
            case ADD:
            case UP: {
                msgs = new ArrayList<DPlaneMsgExternalizable>();
                SwitchNotification swNotif;
                swNotif = new SwitchNotification(switchId, SwitchNotification.NotificationType.SWITCH_ADDED);
                msgs.add(new DPlaneMsgExternalizable(DPlaneMsgType.SWITCH_NOTIF, switchId, swNotif));
                swNotif = new SwitchNotification(switchId, SwitchNotification.NotificationType.SWITCH_ACTIVATED);
                msgs.add(new DPlaneMsgExternalizable(DPlaneMsgType.SWITCH_NOTIF, switchId, swNotif));
                this.linkDiscoveryService.setSwitchUp(switchId, appId);

                if (logger.isInfoEnabled()) {
                    logger.info(String.format("postFaultTransform> PORT_ADD/PORT_UP =>" +
                                              " SWITCH_ADDED & SWITCH_ACTIVATED"));
                }
                break;
            }
            case OTHER_UPDATE:
                if (logger.isInfoEnabled()) {
                    logger.info(String.format("postFaultTransform> No transformations exist for PORT_OTHER_UPDATE"));
                }
                break;
            case DELETE:
            case DOWN: {
                msgs = new ArrayList<DPlaneMsgExternalizable>();
                final SwitchNotification swNotif =
                        new SwitchNotification(switchId, SwitchNotification.NotificationType.SWITCH_REMOVED);
                msgs.add(new DPlaneMsgExternalizable(DPlaneMsgType.SWITCH_NOTIF, switchId, swNotif));
                this.linkDiscoveryService.setSwitchDown(switchId, appId);
                for (Map.Entry<Link, LinkInfo> netLink: this.linkDiscoveryService.getLinks(appId).entrySet()) {
                    if (netLink.getKey().getSrc() == switchId ||
                        netLink.getKey().getDst() == switchId) {
                        this.linkDiscoveryService.delLink(netLink.getKey(), netLink.getValue(), appId);
                    }
                }

                if (logger.isInfoEnabled()) {
                    logger.info(String.format("postFaultTransform> PORT_DELETE/PORT_DOWN => SWITCH_REMOVED"));
                }
                break;
            }
        }
        return msgs;
    }

    @Override
    protected List<DPlaneMsgExternalizable> postFaultTransform(DPlaneMsgExternalizable dPlaneMsg,
                                                               String appId,
                                                               int attempt) {
        final OFMessage ofMessage = (OFMessage) dPlaneMsg.getMsgPayload();
        switch (ofMessage.getType()) {
            case HELLO:
                break;
            case ERROR:
                break;
            case ECHO_REQUEST:
                break;
            case ECHO_REPLY:
                break;
            case VENDOR:
                break;
            case FEATURES_REQUEST:
                break;
            case FEATURES_REPLY:
                break;
            case GET_CONFIG_REQUEST:
                break;
            case GET_CONFIG_REPLY:
                break;
            case SET_CONFIG:
                break;
            case PACKET_IN:
                break;
            case FLOW_REMOVED:
                break;
            case PORT_STATUS:
                break;
            case PACKET_OUT:
                break;
            case FLOW_MOD:
                break;
            case PORT_MOD:
                break;
            case STATS_REQUEST:
                break;
            case STATS_REPLY:
                break;
            case BARRIER_REQUEST:
                break;
            case BARRIER_REPLY:
                break;
            case QUEUE_GET_CONFIG_REQUEST:
                break;
            case QUEUE_GET_CONFIG_REPLY:
                break;
            default:

        }

        return null;
    }

    @Override
    protected List<DPlaneMsgExternalizable> postFaultTransform(SwitchNotification swNotif,
                                                               String appId,
                                                               int attempt) {
        List<DPlaneMsgExternalizable> msgs = null;
        final long switchId = swNotif.getSwitchId();
        switch (swNotif.getNotifType()) {
            case SWITCH_ADDED:
                break;
            case SWITCH_ACTIVATED:
                msgs = new ArrayList<DPlaneMsgExternalizable>();
                setAllPortsUp(switchId, msgs);

                if (logger.isInfoEnabled()) {
                    logger.info(String.format("postFaultTransform> SWITCH_ACTIVATED => PORT_UP"));
                }
                break;
            case SWITCH_REMOVED:
                msgs = new ArrayList<DPlaneMsgExternalizable>();
                setAllPortsDown(switchId, msgs);

                if (logger.isInfoEnabled()) {
                    logger.info(String.format("postFaultTransform> SWITCH_REMOVED => PORT_DOWN"));
                }
                break;
            case SWITCH_CHANGED:
                break;
        }
        return msgs;
    }

    @Override
    protected List<DPlaneMsgExternalizable> postFaultTransform(LinkDiscoveryNotification linkNotif,
                                                               String appId,
                                                               int attempt) {
        List<DPlaneMsgExternalizable> msgs = new ArrayList<DPlaneMsgExternalizable>();
        final long switchId = linkNotif.getSwitchId();
        for (ILinkDiscovery.LDUpdate ldUpdate : linkNotif.getLinkDiscoveryUpdates()) {
            switch (ldUpdate.getOperation()) {
                case LINK_UPDATED: {
                    // Issue two port notifications, one for each endpoint of the link

//                    final OFPhysicalPort srcPort = new OFPhysicalPort();
//                    srcPort.setPortNumber(ldUpdate.getSrcPort());
//                    final PortNotification srcPortNotif =
//                            new PortNotification(ldUpdate.getSrc(), ImmutablePort.fromOFPhysicalPort(srcPort),
//                                                 IOFSwitch.PortChangeType.UP);
//                    msgs.add(new DPlaneMsgExternalizable(DPlaneMsgType.PORT_NOTIF, ldUpdate.getSrc(), srcPortNotif));
//
//                    if (ldUpdate.getDstPort() == 0) break;
//
//                    final OFPhysicalPort dstPort = new OFPhysicalPort();
//                    srcPort.setPortNumber(ldUpdate.getDstPort());
//                    final PortNotification dstPortNotif =
//                            new PortNotification(ldUpdate.getDst(), ImmutablePort.fromOFPhysicalPort(dstPort),
//                                                 IOFSwitch.PortChangeType.UP);
//                    msgs.add(new DPlaneMsgExternalizable(DPlaneMsgType.PORT_NOTIF, ldUpdate.getDst(), dstPortNotif));
                    break;
                }
                case LINK_REMOVED: {
                    // Issue two port notifications, one for each endpoint of the link

//                    final OFPhysicalPort srcPort = new OFPhysicalPort();
//                    srcPort.setPortNumber(ldUpdate.getSrcPort());
//                    final PortNotification srcPortNotif =
//                            new PortNotification(ldUpdate.getSrc(), ImmutablePort.fromOFPhysicalPort(srcPort),
//                                                 IOFSwitch.PortChangeType.DOWN);
//                    msgs.add(new DPlaneMsgExternalizable(DPlaneMsgType.PORT_NOTIF, ldUpdate.getSrc(), srcPortNotif));
//
//                    if (ldUpdate.getDstPort() == 0) break;
//
//                    final OFPhysicalPort dstPort = new OFPhysicalPort();
//                    srcPort.setPortNumber(ldUpdate.getDstPort());
//                    final PortNotification dstPortNotif =
//                            new PortNotification(ldUpdate.getDst(), ImmutablePort.fromOFPhysicalPort(dstPort),
//                                                 IOFSwitch.PortChangeType.DOWN);
//                    msgs.add(new DPlaneMsgExternalizable(DPlaneMsgType.PORT_NOTIF, ldUpdate.getDst(), dstPortNotif));
                    break;
                }
                case SWITCH_UPDATED: {
                    final SwitchNotification swNotif =
                            new SwitchNotification(ldUpdate.getSrc(),
                                                   SwitchNotification.NotificationType.SWITCH_ACTIVATED);
                    msgs.add(new DPlaneMsgExternalizable(DPlaneMsgType.SWITCH_NOTIF, ldUpdate.getSrc(), swNotif));
                    break;
                }
                case SWITCH_REMOVED: {
//                    final SwitchNotification swNotif =
//                            new SwitchNotification(ldUpdate.getSrc(),
//                                                   SwitchNotification.NotificationType.SWITCH_REMOVED);
//                    msgs.add(new DPlaneMsgExternalizable(DPlaneMsgType.SWITCH_NOTIF, ldUpdate.getSrc(), swNotif));
                    break;
                }
                case PORT_UP: {
//                    final OFPhysicalPort srcPort = new OFPhysicalPort();
//                    srcPort.setPortNumber(ldUpdate.getSrcPort());
//                    final PortNotification srcPortNotif =
//                            new PortNotification(ldUpdate.getSrc(), ImmutablePort.fromOFPhysicalPort(srcPort),
//                                                 IOFSwitch.PortChangeType.UP);
//                    msgs.add(new DPlaneMsgExternalizable(DPlaneMsgType.PORT_NOTIF, ldUpdate.getSrc(), srcPortNotif));
                    break;
                }
                case PORT_DOWN: {
//                    final OFPhysicalPort srcPort = new OFPhysicalPort();
//                    srcPort.setPortNumber(ldUpdate.getSrcPort());
//                    final PortNotification srcPortNotif =
//                            new PortNotification(ldUpdate.getSrc(), ImmutablePort.fromOFPhysicalPort(srcPort),
//                                                 IOFSwitch.PortChangeType.DOWN);
//                    msgs.add(new DPlaneMsgExternalizable(DPlaneMsgType.PORT_NOTIF, ldUpdate.getSrc(), srcPortNotif));
                    break;
                }
                case TUNNEL_PORT_ADDED: {
                    break;
                }
                case TUNNEL_PORT_REMOVED: {
                    break;
                }
            }
        }

        if (msgs.isEmpty()) return null;
        return msgs;
    }

}
