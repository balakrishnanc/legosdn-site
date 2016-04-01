package edu.duke.cs.legosdn.core.faults;

import edu.duke.cs.legosdn.core.appvisor.dplane.*;
import edu.duke.cs.legosdn.core.appvisor.proxy.IAppAwareLinkDiscoveryService;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.ImmutablePort;
import net.floodlightcontroller.linkdiscovery.LinkInfo;
import net.floodlightcontroller.routing.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Base class for event transformers with utility methods and
 *  simple implementations of pre-fault transformations.
 * NOTE: It is recommended that concrete implementations be implemented as Singletons.
 */
public abstract class BaseEventTransformer implements IEventTransformer {

    protected static final Logger logger = LoggerFactory.getLogger(BaseEventTransformer.class);

    // Controller
    protected IFloodlightProviderService    flProvider;
    // Application-aware link discovery service
    protected IAppAwareLinkDiscoveryService linkDiscoveryService;

    @Override
    public void registerService(IAppAwareLinkDiscoveryService proxyService) {
        this.linkDiscoveryService = proxyService;
    }

    @Override
    public List<DPlaneMsgExternalizable> verify(DPlaneMsgExternalizable dPlaneMsg, String appId) {
        final String err;
        switch (dPlaneMsg.getMsgType()) {
            case OFPACKET_IN:
            case OFFLOW_REMOVED:
            case OFFLOW_MOD:
            case OFSTATS_REPLY:
                return this.transformOFMessage(dPlaneMsg, false, appId, 0);
            case OFPACKET_OUT:
            case OFSTATS_REQUEST:
            case LISTENER_CMD:
                /* NOTE: Transformations are not applicable to outbound messages! */
                err = String.format("Outbound message (%s) cannot be verified!", dPlaneMsg.getMsgType());
                break;
            case SWITCH_NOTIF:
            case PORT_NOTIF:
            case LINK_NOTIF:
                return this.transformNotification(dPlaneMsg, false, appId, 0);
            default:
                /* NOTE: Control should never reach this point. */
                err = String.format("Unknown message type '%s'!", dPlaneMsg.getMsgType());
        }
        throw new IllegalArgumentException(err);
    }

    @Override
    public List<DPlaneMsgExternalizable> transform(DPlaneMsgExternalizable dPlaneMsg, String appId,
                                                   int attempt) {
        final String err;
        switch (dPlaneMsg.getMsgType()) {
            case OFPACKET_IN:
            case OFFLOW_REMOVED:
            case OFFLOW_MOD:
            case OFSTATS_REPLY:
                return this.transformOFMessage(dPlaneMsg, true, appId, attempt);
            case OFPACKET_OUT:
            case OFSTATS_REQUEST:
            case LISTENER_CMD:
                /* NOTE: Transformations are not applicable to outbound messages! */
                err = String.format("Outbound message (%s) cannot be transformed!", dPlaneMsg.getMsgType());
                break;
            case SWITCH_NOTIF:
            case PORT_NOTIF:
            case LINK_NOTIF:
                return this.transformNotification(dPlaneMsg, true, appId, attempt);
            default:
                /* NOTE: Control should never reach this point. */
                err = String.format("Unknown message type '%s'!", dPlaneMsg.getMsgType());
        }
        throw new IllegalArgumentException(err);
    }

    /**
     * Transform an inbound OpenFlow message to an equivalent message.
     *
     * @param dPlaneMsg Data plane message carrying an OpenFlow message as payload
     * @param isPostFault True, if message induced a crash; false, otherwise
     * @param appId Application identifier
     * @param attempt Attempt number
     * @return List of equivalent data plane messages
     */
    protected List<DPlaneMsgExternalizable> transformOFMessage(DPlaneMsgExternalizable dPlaneMsg,
                                                               boolean isPostFault,
                                                               String appId,
                                                               int attempt) {
        if (isPostFault) {
            return this.postFaultTransform(dPlaneMsg, appId, attempt);
        }
        return this.preFaultTransform(dPlaneMsg, appId);
    }

    /**
     * Transform, if necessary, an inbound OpenFlow message to an equivalent message prior to a fault.
     *
     * @param dPlaneMsg Inbound OpenFlow message
     * @param appId Application identification
     * @return List of equivalent data plane messages
     */
    protected List<DPlaneMsgExternalizable> preFaultTransform(DPlaneMsgExternalizable dPlaneMsg,
                                                              String appId) {
        return retainMsg(dPlaneMsg, new ArrayList<DPlaneMsgExternalizable>(1));
    }

    /**
     * Transform an inbound OpenFlow message to an equivalent message after a fault.
     *
     * @param dPlaneMsg Inbound OpenFlow message
     * @param appId Application identification
     * @return List of equivalent data plane messages
     */
    protected abstract List<DPlaneMsgExternalizable> postFaultTransform(DPlaneMsgExternalizable dPlaneMsg,
                                                               String appId,
                                                               int attempt);

    /**
     * Transform an inbound notification message to an equivalent message.
     *
     * @param dPlaneMsg Data plane message carrying a notification as payload
     * @param isPostFault True, if message induced a crash; false, otherwise
     * @param appId Application identifier
     * @param attempt Attempt number
     * @return List of equivalent data plane messages
     */
    protected List<DPlaneMsgExternalizable> transformNotification(DPlaneMsgExternalizable dPlaneMsg,
                                                                           boolean isPostFault,
                                                                           String appId,
                                                                           int attempt) {
        final DPlaneNotification dPlaneNotification = (DPlaneNotification) dPlaneMsg.getMsgPayload();
        switch (dPlaneNotification.getMsgType()) {
            case SWITCH_NOTIF:
                SwitchNotification swNotif = (SwitchNotification) dPlaneNotification;
                return this.transformSwitchNotif(swNotif, isPostFault, appId, attempt);
            case PORT_NOTIF:
                PortNotification portNotif = (PortNotification) dPlaneNotification;
                return this.transformPortNotif(portNotif, isPostFault, appId, attempt);
            case LINK_NOTIF:
                LinkDiscoveryNotification linkNotif = (LinkDiscoveryNotification) dPlaneNotification;
                return this.transformLinkNotif(linkNotif, isPostFault, appId, attempt);
            default:
                /* NOTE: Control should never reach this point. */
                final String err = String.format("Unknown notification type '%s'!", dPlaneMsg.getMsgType());
                throw new IllegalArgumentException(err);
        }
    }

    /**
     * Transform an inbound switch notification message to an equivalent message.
     *
     * @param swNotif Switch notification
     * @param isPostFault True, if message induced a crash; false, otherwise
     * @param appId Application identification
     * @param attempt Attempt number
     * @return List of equivalent data plane messages
     */
    protected List<DPlaneMsgExternalizable> transformSwitchNotif(SwitchNotification swNotif,
                                                                 boolean isPostFault,
                                                                 String appId,
                                                                 int attempt) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("transformSwitchNotif> %s  Sw-%d %s",
                                       isPostFault ? "NO-FAULT" : "AT-FAULT",
                                       swNotif.getSwitchId(), swNotif.getNotifType().toString()));
        }

        if (isPostFault)
            return this.postFaultTransform(swNotif, appId, attempt);
        return this.preFaultTransform(swNotif, appId);
    }

    /**
     * Transform, if necessary, an inbound switch notification message to an equivalent message prior to a fault.
     *
     * @param swNotif Switch notification
     * @param appId Application identification
     * @return List of equivalent data plane messages
     */
    protected List<DPlaneMsgExternalizable> preFaultTransform(SwitchNotification swNotif, String appId) {
        return retainMsg(swNotif, new ArrayList<DPlaneMsgExternalizable>(1));
    }

    /**
     * Transform an inbound switch notification message to an equivalent message after a fault.
     *
     * @param swNotif Switch notification
     * @param appId Application identification
     * @param attempt Attempt number
     * @return List of equivalent data plane messages
     */
    protected abstract List<DPlaneMsgExternalizable> postFaultTransform(SwitchNotification swNotif,
                                                                        String appId,
                                                                        int attempt);

    /**
     * Transform an inbound port notification message to an equivalent message.
     *
     * @param portNotif Port notification
     * @param isPostFault True, if message induced a crash; false, otherwise
     * @param appId Application identification
     * @param attempt Attempt number
     * @return List of equivalent data plane messages
     */
    protected List<DPlaneMsgExternalizable> transformPortNotif(PortNotification portNotif,
                                                               boolean isPostFault,
                                                               String appId,
                                                               int attempt) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("transformPortNotif> %s  Sw-%d:%d %s",
                                       isPostFault ? "NO-FAULT" : "AT-FAULT",
                                       portNotif.getSwitchId(), portNotif.getPort().getPortNumber(),
                                       portNotif.getPortChangeType().toString()));
        }

        if (isPostFault)
            return this.postFaultTransform(portNotif, appId, attempt);
        return this.preFaultTransform(portNotif, appId);
    }

    /**
     * Transform, if necessary, an inbound port notification message to an equivalent message prior to a fault.
     *
     * @param portNotif Port notification
     * @param appId Application identification
     * @return List of equivalent data plane messages
     */
    protected List<DPlaneMsgExternalizable> preFaultTransform(PortNotification portNotif, String appId) {
        return retainMsg(portNotif, new ArrayList<DPlaneMsgExternalizable>(1));
    }

    /**
     * Transform an inbound port notification message to an equivalent message after a fault.
     *
     * @param portNotif Port notification
     * @param appId Application identification
     * @param attempt Attempt number
     * @return List of equivalent data plane messages
     */
    protected abstract List<DPlaneMsgExternalizable> postFaultTransform(PortNotification portNotif,
                                                                        String appId,
                                                                        int attempt);

    /**
     * Transform an inbound link notification message to an equivalent message.
     *
     * @param linkNotif Link discovery notification
     * @param isPostFault True, if message induced a crash; false, otherwise
     * @param appId Application identification
     * @param attempt Attempt number
     * @return List of equivalent data plane messages
     */
    protected List<DPlaneMsgExternalizable> transformLinkNotif(LinkDiscoveryNotification linkNotif,
                                                               boolean isPostFault,
                                                               String appId,
                                                               int attempt) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("transformLinkNotif> %s  Sw-%d",
                                       isPostFault ? "NO-FAULT" : "AT-FAULT",
                                       linkNotif.getSwitchId()));
        }

        if (isPostFault)
            return this.postFaultTransform(linkNotif, appId, attempt);
        return this.preFaultTransform(linkNotif, appId);
    }

    /**
     * Transform, if necessary, an inbound link notification message to an equivalent message prior to a fault.
     *
     * @param linkNotif Link discovery notification
     * @param appId Application identification
     * @return List of equivalent data plane messages
     */
    protected List<DPlaneMsgExternalizable> preFaultTransform(LinkDiscoveryNotification linkNotif, String appId) {
        return retainMsg(linkNotif, new ArrayList<DPlaneMsgExternalizable>(1));
    }

    /**
     * Transform an inbound link notification message to an equivalent message after a fault.
     *
     * @param linkNotif Link discovery notification
     * @param appId Application identification
     * @param attempt Attempt number
     * @return List of equivalent data plane messages
     */
    protected abstract List<DPlaneMsgExternalizable> postFaultTransform(LinkDiscoveryNotification linkNotif,
                                                                        String appId,
                                                                        int attempt);

    /**
     * Activate switches as necessary to allow delivery of port notification messages to an application.
     *
     * @param portNotif Inbound port notification message
     * @param appId Application identifier
     * @param msgs List to be updated with generated messages.
     */
    public void activateSwToAllow(PortNotification portNotif,
                                  String appId,
                                  final List<DPlaneMsgExternalizable> msgs) {
        final long switchId = portNotif.getSwitchId();
        final short portNumber = portNotif.getPort().getPortNumber();

        // Links that are currently unknown to the application.
        final Map<Link, LinkInfo> delLinks = this.linkDiscoveryService.getDelLinks(appId);
        // Switches that are currently unknown to the application.
        final Set<Long> delSwitches = this.linkDiscoveryService.getSwitchesDel(appId);

        // Set of switches to be activated for the port notification to be processed by the application.
        final Set<Long> activSws = new HashSet<Long>(8);
        for (Map.Entry<Link, LinkInfo> netLink: delLinks.entrySet()) {
            final Link link = netLink.getKey();
            if (link.getSrc() != switchId && link.getDst() != switchId)
                continue;
            if (link.getSrcPort() != portNumber && link.getDstPort() != portNumber)
                continue;
            if (!activSws.contains(link.getSrc()) &&
                delSwitches.contains(link.getSrc())) {
                final long swID = link.getSrc();
                activSws.add(swID);
                addSwActivations(swID, msgs);
                this.linkDiscoveryService.setSwitchUp(swID, appId);
            }
            if (!activSws.contains(link.getDst()) &&
                delSwitches.contains(link.getDst())) {
                final long swID = link.getDst();
                activSws.add(swID);
                addSwActivations(swID, msgs);
                this.linkDiscoveryService.setSwitchUp(swID, appId);
            }
        }

        for (Map.Entry<Link, LinkInfo> netLink: delLinks.entrySet()) {
            final Link link = netLink.getKey();

            if (!activSws.contains(link.getSrc()) &&
                !activSws.contains(link.getDst())) {
                continue;
            }

            this.linkDiscoveryService.addLink(link, netLink.getValue(), appId);

            if (logger.isDebugEnabled()) {
                logger.debug(String.format("preFaultTransform> adding ignored link Sw-%d:%d => Sw-%d:%d",
                                           link.getSrc(), link.getSrcPort(),
                                           link.getDst(), link.getDstPort()));
            }
        }
    }

    /**
     * Bring up the port on a given switch.
     *
     * @param switchId Switch
     * @param port Port
     * @param msgs List to be updated with generated messages.
     * @return Generated messages.
     */
    public List<DPlaneMsgExternalizable> setPortUp(long switchId,
                                                   ImmutablePort port,
                                                   final List<DPlaneMsgExternalizable> msgs) {
        final PortNotification portNotif =
                new PortNotification(switchId, port, IOFSwitch.PortChangeType.UP);
        msgs.add(new DPlaneMsgExternalizable(DPlaneMsgType.PORT_NOTIF, switchId, portNotif));
        return msgs;
    }

    /**
     * Bring up all ports on a given switch.
     *
     * @param switchId Switch
     * @param msgs List to be updated with generated messages.
     */
    public void setAllPortsUp(long switchId, final List<DPlaneMsgExternalizable> msgs) {
        for (ImmutablePort port : this.flProvider.getSwitch(switchId).getPorts()) {
            this.setPortUp(switchId, port, msgs);
        }
    }

    /**
     * Bring down the port on a given switch.
     *
     * @param switchId Switch
     * @param port Port
     * @param msgs List to be updated with generated messages.
     * @return Generated messages.
     */
    public List<DPlaneMsgExternalizable> setPortDown(long switchId,
                                                     ImmutablePort port,
                                                     final List<DPlaneMsgExternalizable> msgs) {
        final PortNotification portNotif =
                new PortNotification(switchId, port, IOFSwitch.PortChangeType.DOWN);
        msgs.add(new DPlaneMsgExternalizable(DPlaneMsgType.PORT_NOTIF, switchId, portNotif));
        return msgs;
    }

    /**
     * Bring down all ports on a given switch.
     *
     * @param switchId Switch
     * @param msgs List to be updated with generated messages.
     */
    public void setAllPortsDown(long switchId, final List<DPlaneMsgExternalizable> msgs) {
        for (ImmutablePort port : this.flProvider.getSwitch(switchId).getPorts()) {
            this.setPortDown(switchId, port, msgs);
        }
    }

    /**
     * Retain the message intact.
     *
     * @param m Message
     * @param msgs List to be updated with generated messages.
     * @return Generated messages.
     */
    public static List<DPlaneMsgExternalizable> retainMsg(DPlaneMsgExternalizable m,
                                                          final List<DPlaneMsgExternalizable> msgs) {
        msgs.add(m);
        return msgs;
    }

    /**
     * Retain the switch notification message intact.
     *
     * @param m Switch notification message.
     * @param msgs List to be updated with generated messages.
     * @return Generated messages.
     */
    public static List<DPlaneMsgExternalizable> retainMsg(SwitchNotification m,
                                                          final List<DPlaneMsgExternalizable> msgs) {
        final long switchId = m.getSwitchId();
        // Retain the original message intact.
        msgs.add(new DPlaneMsgExternalizable(DPlaneMsgType.SWITCH_NOTIF, switchId, m));
        return msgs;
    }

    /**
     * Retain the port notification message intact.
     *
     * @param m Port notification message.
     * @param msgs List to be updated with generated messages.
     * @return Generated messages.
     */
    public static List<DPlaneMsgExternalizable> retainMsg(PortNotification m,
                                                          final List<DPlaneMsgExternalizable> msgs) {
        final long switchId = m.getSwitchId();
        // Retain the original message intact.
        msgs.add(new DPlaneMsgExternalizable(DPlaneMsgType.PORT_NOTIF, switchId, m));
        return msgs;
    }

    /**
     * Retain the link discovery notification message intact.
     *
     * @param m Link discovery notification message.
     * @param msgs List to be updated with generated messages.
     * @return Generated messages.
     */
    public static List<DPlaneMsgExternalizable> retainMsg(LinkDiscoveryNotification m,
                                                          final List<DPlaneMsgExternalizable> msgs) {
        final long switchId = m.getSwitchId();
        // Retain the original message intact.
        msgs.add(new DPlaneMsgExternalizable(DPlaneMsgType.LINK_NOTIF, switchId, m));
        return msgs;
    }

    /**
     * Generate switch activation messages.
     *
     * @param swID Switch ID
     * @param msgs List to be updated with activation messages.
     */
    public static void addSwActivations(long swID, final List<DPlaneMsgExternalizable> msgs) {
        SwitchNotification swNotif;
        swNotif = new SwitchNotification(swID, SwitchNotification.NotificationType.SWITCH_ADDED);
        msgs.add(new DPlaneMsgExternalizable(DPlaneMsgType.SWITCH_NOTIF, swID, swNotif));

        swNotif = new SwitchNotification(swID, SwitchNotification.NotificationType.SWITCH_ACTIVATED);
        msgs.add(new DPlaneMsgExternalizable(DPlaneMsgType.SWITCH_NOTIF, swID, swNotif));

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("addSwActivations> activating Sw-%d", swID));
        }
    }

}
