package edu.duke.cs.legosdn.core.appvisor.dplane;

import net.floodlightcontroller.core.IListener;
import org.jboss.netty.buffer.ChannelBuffer;
import org.openflow.protocol.*;
import org.openflow.protocol.factory.*;

/**
 * Describes the structure of data plane messages.
 *
 * @author bala
 */
public class DPlaneMsg {

    private static final int MSGTYPE_LEN  = Integer.SIZE / 8;
    private static final int APPMSGID_LEN = Long.SIZE / 8;
    private static final int SWITCHID_LEN = Long.SIZE / 8;
    private static final int ISREPLAY_LEN = Integer.SIZE / 8;
    private static final int PREAMBLE_LEN = MSGTYPE_LEN + APPMSGID_LEN + SWITCHID_LEN + ISREPLAY_LEN;

    // Message type
    protected DPlaneMsgType msgType;
    // Application specific Message ID
    protected Long          appMsgId;
    // Switch identifier
    protected Long          switchID;
    // Flag indicating if the message is part of a replay.
    protected boolean       isReplay;
    // Message payload
    protected Object        msgPayload;

    public static final Long INTERNAL_QUEUE = Long.MIN_VALUE;

    /**
     * Create an empty data plane message.
     */
    public DPlaneMsg() {
        this.reset();
    }

    /**
     * Create a new data plane message.
     *
     * @param type Message type
     * @param switchID Switch Identifier
     * @param payload Message payload
     */
    public DPlaneMsg(DPlaneMsgType type, Long switchID, Object payload) {
        this(type, switchID, payload, false);
    }

    /**
     * Create a new data plane message.
     *
     * @param type Message type
     * @param switchID Switch Identifier
     * @param payload Message payload
     * @param isReplay Flag indicating if message is part of a replay
     */
    public DPlaneMsg(DPlaneMsgType type, Long switchID, Object payload, boolean isReplay) {
        this(type, 0L, switchID, payload, isReplay);
    }

    /**
     * Create a new data plane message.
     *
     * @param type Message type
     * @param appMsgId Application specific message ID
     * @param switchID Switch Identifier
     * @param payload Message payload
     * @param isReplay Flag indicating if message is part of a replay
     */
    public DPlaneMsg(DPlaneMsgType type, Long appMsgId, Long switchID, Object payload, boolean isReplay) {
        this.msgType = type;
        this.appMsgId = appMsgId;
        this.switchID = switchID;
        this.msgPayload = payload;
        this.isReplay = isReplay;
    }

    /**
     * Create a new data plane message.
     *
     * @param payload Message payload
     * @throws UnknownMsgTypeException
     */
    public DPlaneMsg(Object payload) throws UnknownMsgTypeException {
        this(INTERNAL_QUEUE, payload);
    }

    /**
     * Create a new data plane message.
     *
     * @param switchID Switch Identifier
     * @param payload Message payload
     * @throws UnknownMsgTypeException
     */
    public DPlaneMsg(Long switchID, Object payload) throws UnknownMsgTypeException {
        this(DPlaneMsgType.typeOfObject(payload), switchID, payload);
    }

    /**
     * @return Data plane message type
     */
    public DPlaneMsgType getMsgType() {
        return msgType;
    }

    /**
     * @return Application specific message ID
     */
    public Long getAppMsgId() {
        return appMsgId;
    }

    /**
     * @param appMsgId Application specific message ID
     */
    public void setAppMsgId(Long appMsgId) {
        this.appMsgId = appMsgId;
    }

    /**
     * @return Switch ID associated with the data plane message
     */
    public Long getSwitchID() {
        return switchID;
    }

    /**
     * @return Message payload
     */
    public Object getMsgPayload() {
        return msgPayload;
    }

    /**
     * Write message out on the wire.
     *
     * @param buf Buffer to write out the message
     */
    public void writeTo(ChannelBuffer buf) {
        // Wire format:
        // <message type identifier><payload>
        buf.writeInt(this.msgType.ordinal());
        buf.writeLong(this.appMsgId);
        buf.writeLong(this.switchID);
        buf.writeInt(this.isReplay ? 1 : 0);
        switch (this.msgType) {
            case OFPACKET_IN:
                ((OFPacketIn) this.msgPayload).writeTo(buf);
                break;
            case OFFLOW_REMOVED:
                ((OFFlowRemoved) this.msgPayload).writeTo(buf);
                break;
            case OFPACKET_OUT:
                ((OFPacketOut) this.msgPayload).writeTo(buf);
                break;
            case OFFLOW_MOD:
                ((OFFlowMod) this.msgPayload).writeTo(buf);
                break;
            case OFSTATS_REQUEST:
            	((OFStatisticsRequest) this.msgPayload).writeTo(buf);
            	break;
            case OFSTATS_REPLY:
            	((OFStatisticsReply) this.msgPayload).writeTo(buf);
            	break;
            case SWITCH_NOTIF:
                ((SwitchNotification) this.msgPayload).writeTo(buf);
                break;
            case PORT_NOTIF:
                ((PortNotification) this.msgPayload).writeTo(buf);
                break;
            case LINK_NOTIF:
                ((LinkDiscoveryNotification) this.msgPayload).writeTo(buf);
                break;
            case LISTENER_CMD:
                buf.writeInt(((IListener.Command) this.msgPayload).ordinal());
                break;
        }
    }

    /**
     * Read message from the data on the wire.
     *
     * @param buf Buffer containing data stream read from the wire.
     * @return Instance of {@link DPlaneMsg}, if buffer contained sufficient data; otherwise, null.
     * @throws DPlaneMsgException
     */
    public static DPlaneMsg readFrom(ChannelBuffer buf) throws DPlaneMsgException {
        if (buf.readableBytes() < PREAMBLE_LEN) {
            return null;
        }

        buf.markReaderIndex();

        // Identify message type
        DPlaneMsgType type;
        int typeOrdinal = buf.readInt();
        try {
            type = DPlaneMsgType.values()[typeOrdinal];
        } catch (ArrayIndexOutOfBoundsException e) {
            String errDesc = String.format("Unknown message type (ordinal: %d)", typeOrdinal);
            throw new UnknownMsgTypeException(errDesc, e);
        }

        // Application specific message ID
        long appMsgID = buf.readLong();
        // Switch to which message is written to
        long switchID = buf.readLong();
        // Flag indicating if message is part of a replay
        boolean isReplay = buf.readInt() == 1 ? true : false;

        // Parse message payload
        final Object payload;
        switch (type) {
            case OFPACKET_IN:
                if (buf.readableBytes() < OFMessage.MINIMUM_LENGTH) {
                    buf.resetReaderIndex();
                    return null;
                }

                final OFPacketIn ofPacketIn = new OFPacketIn();
                ofPacketIn.readFrom(buf);
                payload = ofPacketIn;
                break;
            case OFFLOW_REMOVED:
                if (buf.readableBytes() < OFMessage.MINIMUM_LENGTH) {
                    buf.resetReaderIndex();
                    return null;
                }

                final OFFlowRemoved ofFlowRemoved = new OFFlowRemoved();
                ofFlowRemoved.readFrom(buf);
                payload = ofFlowRemoved;
                break;
            case OFPACKET_OUT:
            case OFFLOW_MOD:
            case OFSTATS_REQUEST:
            case OFSTATS_REPLY:
                if (buf.readableBytes() < OFMessage.MINIMUM_LENGTH) {
                    buf.resetReaderIndex();
                    return null;
                }

                final OFMessage demux = new OFMessage();
                OFMessage ofm;

                demux.readFrom(buf);
                buf.resetReaderIndex();

                if (demux.getLengthU() > (buf.readableBytes() - PREAMBLE_LEN)) {
                    return null;
                }

                ofm = demux.getType().newInstance();
                if (ofm == null) {
                    return null;
                }

                if (ofm instanceof OFActionFactoryAware) {
                    ((OFActionFactoryAware) ofm).setActionFactory(BasicFactory.SINGLETON_INSTANCE);
                }
                if (ofm instanceof OFMessageFactoryAware) {
                    ((OFMessageFactoryAware) ofm).setMessageFactory(BasicFactory.SINGLETON_INSTANCE);
                }
                if (ofm instanceof OFStatisticsFactoryAware) {
                    ((OFStatisticsFactoryAware) ofm).setStatisticsFactory(BasicFactory.SINGLETON_INSTANCE);
                }
                if (ofm instanceof OFVendorDataFactoryAware) {
                    ((OFVendorDataFactoryAware) ofm).setVendorDataFactory(BasicFactory.SINGLETON_INSTANCE);
                }

                // Discard the already read bytes (bytes containing the preamble)
                buf.readBytes(PREAMBLE_LEN);

                ofm.readFrom(buf);
                if (OFMessage.class.equals(ofm.getClass())) {
                    // advance the position for un-implemented messages
                    buf.readerIndex(buf.readerIndex() + (ofm.getLengthU() - OFMessage.MINIMUM_LENGTH));
                }
                payload = ofm;
                break;
            case SWITCH_NOTIF:
                final SwitchNotification switchNotification = new SwitchNotification();
                switchNotification.readFrom(buf);
                payload = switchNotification;
                break;
            case PORT_NOTIF:
                final PortNotification portNotification = new PortNotification();
                portNotification.readFrom(buf);
                payload = portNotification;
                break;
            case LINK_NOTIF:
                final LinkDiscoveryNotification linkDiscNotification = new LinkDiscoveryNotification();
                linkDiscNotification.readFrom(buf);
                payload = linkDiscNotification;
                break;
            case LISTENER_CMD:
                if (buf.readableBytes() < 4) {
                    buf.resetReaderIndex();
                    return null;
                }

                final int cmdOrdinal = buf.readInt();
                try {
                    payload = IListener.Command.values()[cmdOrdinal];
                } catch (ArrayIndexOutOfBoundsException e) {
                    String errDesc = String.format("Invalid listener command (ordinal: %d)", cmdOrdinal);
                    throw new InvalidListenerCommandException(errDesc, e);
                }
                break;
            default:
                payload = null;
        }

        if (payload == null) {
            buf.resetReaderIndex();
            return null;
        }

        buf.markReaderIndex();

        return new DPlaneMsg(type, appMsgID, switchID, payload, isReplay);
    }

    /**
     * @return length of message (in bytes)
     */
    public int getLength() {
        switch (this.msgType) {
            case OFPACKET_IN:
                return PREAMBLE_LEN + ((OFPacketIn) this.msgPayload).getLengthU();
            case OFFLOW_REMOVED:
                return PREAMBLE_LEN + ((OFFlowRemoved) this.msgPayload).getLengthU();
            case OFPACKET_OUT:
                return PREAMBLE_LEN + ((OFPacketOut) this.msgPayload).getLengthU();
            case OFFLOW_MOD:
                return PREAMBLE_LEN + ((OFFlowMod) this.msgPayload).getLength();
            case OFSTATS_REQUEST:
            	return PREAMBLE_LEN + ((OFStatisticsRequest) this.msgPayload).getLengthU();
            case OFSTATS_REPLY:
            	return PREAMBLE_LEN + ((OFStatisticsReply) this.msgPayload).getLengthU();
            case SWITCH_NOTIF:
                return PREAMBLE_LEN + ((SwitchNotification) this.msgPayload).getLength();
            case PORT_NOTIF:
                return PREAMBLE_LEN + ((PortNotification) this.msgPayload).getLength();
            case LINK_NOTIF:
                return PREAMBLE_LEN + ((LinkDiscoveryNotification) this.msgPayload).getLength();
            case LISTENER_CMD:
                return PREAMBLE_LEN + 4;
            default:
                // NOTE: Control should never reach here.
                return -1;
        }
    }

    /**
     * Reset state associated with the dataplane message.
     */
    public void reset() {
        this.msgType = null;
        this.appMsgId = 0L;
        this.switchID = DPlaneMsg.INTERNAL_QUEUE;
        this.msgPayload = null;
    }

    /**
     * Update state of data plane message.
     *
     * @param type Message type
     * @param switchID Switch Identifier
     * @param payload Message payload
     */
    public void update(DPlaneMsgType type, Long switchID, Object payload) {
        this.msgType = type;
        this.switchID = switchID;
        this.msgPayload = payload;
    }

    /**
     * @param isReplay set to True for replayed messages/events; false, for original inbound messages/events
     */
    public void setIsReplay(boolean isReplay) {
        this.isReplay = isReplay;
    }

    /**
     * @return true, if this is a replay message/event; false, otherwise
     */
    public boolean isReplay() {
        return isReplay;
    }

    /**
     * @return String describing payload type
     */
    public String getPayloadType() {
        switch (this.getMsgType()) {
            case OFPACKET_IN:
            case OFFLOW_REMOVED:
            case OFPACKET_OUT:
            case OFFLOW_MOD:
            case OFSTATS_REQUEST:
            case OFSTATS_REPLY:
                return this.getMsgType().toString();
            case SWITCH_NOTIF:
                return String.format("%s.%s",
                                     this.getMsgType().toString(),
                                     ((SwitchNotification) this.getMsgPayload()).getNotifType().toString());
            case PORT_NOTIF:
                return String.format("%s.%s",
                                     this.getMsgType().toString(),
                                     ((PortNotification) this.getMsgPayload()).getPortChangeType().toString());
            case LINK_NOTIF:
            case LISTENER_CMD:
                return this.getMsgType().toString();
        }
        return "UNKNOWN";
    }

}
