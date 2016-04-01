package edu.duke.cs.legosdn.core.appvisor.dplane;

import net.floodlightcontroller.core.IListener;
import org.openflow.protocol.*;

/**
 * Data plane message types.
 */
public enum DPlaneMsgType {

    OFPACKET_IN(OFPacketIn.class),              // OpenFlow inbound (from switch to controller) message
    OFFLOW_REMOVED(OFFlowRemoved.class),        // OpenFlow inbound flow removed message
    OFPACKET_OUT(OFPacketOut.class),            // OpenFlow outbound (from controller to switch) message
    OFFLOW_MOD(OFFlowMod.class),                // OpenFlow outbound flow modifier message
    OFSTATS_REQUEST(OFStatisticsRequest.class), // OpenFlow stats request message
    OFSTATS_REPLY(OFStatisticsReply.class),     // OpenFlow stats reply message
    SWITCH_NOTIF(SwitchNotification.class),     // Switch-related notification
    PORT_NOTIF(PortNotification.class),         // Port-related notification
    LINK_NOTIF(LinkDiscoveryNotification.class),// Link discovery notification(s)
    LISTENER_CMD(IListener.Command.class);      // Command returned by 'IListener.receive'

    // Actual message type
    public final Class type;

    /**
     * Instantiate data plane message type constants.
     *
     * @param msgType Message type (Class)
     */
    private DPlaneMsgType(Class msgType) {
        this.type = msgType;
    }

    /**
     * @return Class backing the type
     */
    public Class getBackingClass() {
        return type;
    }

    /**
     * Check if a data plane message type is associated with notifications.
     *
     * @param type Data plane message type
     * @return True, if type belongs to a notification message; false, otherwise
     */
    public static boolean isNotification(DPlaneMsgType type) {
        switch (type) {
            case OFPACKET_IN:
            case OFFLOW_REMOVED:
            case OFPACKET_OUT:
            case OFFLOW_MOD:
            case OFSTATS_REQUEST:
            case OFSTATS_REPLY:
                return false;
            case SWITCH_NOTIF:
            case PORT_NOTIF:
            case LINK_NOTIF:
                return true;
            case LISTENER_CMD:
                return false;
        }
        return false;
    }

    /**
     * Maps the actual data plane payload to DPlaneMsgType.
     *
     * @param payload Actual data plane message payload
     * @return DPlaneMsgType corresponding to the payload type
     * @throws UnknownMsgTypeException
     */
    public static DPlaneMsgType typeOfObject(Object payload) throws UnknownMsgTypeException {
        if (payload instanceof OFPacketIn) {
            return DPlaneMsgType.OFPACKET_IN;
        } else if (payload instanceof OFFlowRemoved) {
            return DPlaneMsgType.OFFLOW_REMOVED;
        } else if (payload instanceof OFFlowMod) {
            return DPlaneMsgType.OFFLOW_MOD;
        } else if (payload instanceof OFStatisticsRequest) {
        	return DPlaneMsgType.OFSTATS_REQUEST;
        } else if (payload instanceof OFStatisticsReply) {
        	return DPlaneMsgType.OFSTATS_REPLY;
        } else if (payload instanceof SwitchNotification) {
            return DPlaneMsgType.SWITCH_NOTIF;
        } else if (payload instanceof PortNotification) {
            return DPlaneMsgType.PORT_NOTIF;
        } else if (payload instanceof LinkDiscoveryNotification) {
            return DPlaneMsgType.LINK_NOTIF;
        } else if (payload instanceof IListener.Command) {
            return DPlaneMsgType.LISTENER_CMD;
        } else {
            throw new UnknownMsgTypeException(String.format("Unsupported payload (type: %s)!",
                                                            payload.getClass().getCanonicalName()));
        }
    }

    /**
     * Maps an instance of OFType to DPlaneMsgType.
     *
     * @param ofType Instance of OFType
     * @return DPlaneMsgType corresponding to the OFType instance
     * @throws UnknownMsgTypeException
     */
    public static DPlaneMsgType mapTypeTo(OFType ofType) throws UnknownMsgTypeException {
        switch (ofType) {
            case HELLO:
            case ERROR:
            case ECHO_REQUEST:
            case ECHO_REPLY:
            case VENDOR:
            case FEATURES_REQUEST:
            case FEATURES_REPLY:
            case GET_CONFIG_REQUEST:
            case GET_CONFIG_REPLY:
            case SET_CONFIG:
                throw new UnknownMsgTypeException(String.format("Unsupported OFType '%s'!", ofType.toString()));
            case PACKET_IN:
                return OFPACKET_IN;
            case FLOW_REMOVED:
                return OFFLOW_REMOVED;
            case PORT_STATUS:
                throw new UnknownMsgTypeException(String.format("Unsupported OFType '%s'!", ofType.toString()));
            case PACKET_OUT:
                return OFPACKET_OUT;
            case FLOW_MOD:
                return OFFLOW_MOD;
            case STATS_REQUEST:
            	return OFSTATS_REQUEST;
            case STATS_REPLY:
            	return OFSTATS_REPLY;
            case PORT_MOD:
            case BARRIER_REQUEST:
            case BARRIER_REPLY:
            case QUEUE_GET_CONFIG_REQUEST:
            case QUEUE_GET_CONFIG_REPLY:
            default:
                throw new UnknownMsgTypeException(String.format("Unsupported OFType '%s'!", ofType.toString()));
        }
    }

}
