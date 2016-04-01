package edu.duke.cs.legosdn.core.service.topology;

import edu.duke.cs.legosdn.core.appvisor.cplane.ServiceCallMsg;
import edu.duke.cs.legosdn.core.appvisor.cplane.ServiceType;
import net.floodlightcontroller.topology.NodePortTuple;
import org.jboss.netty.buffer.ChannelBuffer;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public abstract class TopologyServiceMsg implements ServiceCallMsg {

    public static enum CallType {

        GET_LAST_UPDATE_TIME_REQ(LastUpdateTimeReqMsg.class),
        GET_LAST_UPDATE_TIME_RES(LastUpdateTimeResMsg.class),
        ATTCH_PT_PORT_REQ(AttachmentPointPortReqMsg.class),
        ATTCH_PT_PORT_RES(AttachmentPointPortResMsg.class),
        OF_DOM_ID_REQ(OpenflowDomainIdReqMsg.class),
        OF_DOM_ID_RES(OpenflowDomainIdResMsg.class),
        L2_DOM_ID_REQ(L2DomainIdReqMsg.class),
        L2_DOM_ID_RES(L2DomainIdResMsg.class),
        SAME_OF_DOM_REQ(SameOpenflowDomainReqMsg.class),
        SAME_OF_DOM_RES(SameOpenflowDomainResMsg.class),
        SAME_L2_DOM_REQ(SameL2DomainReqMsg.class),
        SAME_L2_DOM_RES(SameL2DomainResMsg.class),
        BCAST_DOM_PORT_REQ(BroadcastDomainPortReqMsg.class),
        BCAST_DOM_PORT_RES(BroadcastDomainPortResMsg.class),
        ALLOWED_REQ(AllowedReqMsg.class),
        ALLOWED_RES(AllowedResMsg.class),
        IN_BCAST_ALLOWED_REQ(IncomingBroadcastAllowedReqMsg.class),
        IN_BCAST_ALLOWED_RES(IncomingBroadcastAllowedResMsg.class),
        CONSISTENT_REQ(ConsistentReqMsg.class),
        CONSISTENT_RES(ConsistentResMsg.class),
        SAME_BCAST_DOM_REQ(SameBroadcastDomainReqMsg.class),
        SAME_BCAST_DOM_RES(SameBroadcastDomainResMsg.class),
        OUT_SW_PORT_REQ(OutgoingSwitchPortReqMsg.class),
        OUT_SW_PORT_RES(OutgoingSwitchPortResMsg.class),
        IN_SW_PORT_REQ(IncomingSwitchPortReqMsg.class),
        IN_SW_PORT_RES(IncomingSwitchPortResMsg.class),
        ALLOWED_OUT_BCAST_PORT_REQ(AllowedOutgoingBroadcastPortReqMsg.class),
        ALLOWED_OUT_BCAST_PORT_RES(AllowedOutgoingBroadcastPortResMsg.class),
        ALLOWED_IN_BCAST_PORT_REQ(AllowedIncomingBroadcastPortReqMsg.class),
        ALLOWED_IN_BCAST_PORT_RES(AllowedIncomingBroadcastPortResMsg.class),
        SWS_IN_OF_DOM_REQ_MSG(SwitchesInOpenflowDomainReqMsg.class),
        SWS_IN_OF_DOM_RES_MSG(SwitchesInOpenflowDomainResMsg.class),
        PORTS_WITH_LINKS_REQ(PortsWithLinksReqMsg.class),
        PORTS_WITH_LINKS_RES(PortsWithLinksResMsg.class),
        BCAST_PORTS_REQ_MSG(BroadcastPortsReqMsg.class),
        BCAST_PORTS_RES_MSG(BroadcastPortsResMsg.class),
        PORTS_REQ_MSG(PortsReqMsg.class),
        PORTS_RES_MSG(PortsResMsg.class),
        BCAST_DOM_PORTS_REQ(BroadcastDomainPortsReqMsg.class),
        BCAST_DOM_PORTS_RES(BroadcastDomainPortsResMsg.class),
        TUNNEL_PORTS_REQ(TunnelPortsReqMsg.class),
        TUNNEL_PORTS_RES(TunnelPortsResMsg.class),
        BLOCKED_PORTS_REQ(BlockedPortsReqMsg.class),
        BLOCKED_PORTS_RES(BlockedPortsResMsg.class),
        ;

        // Actual message type
        public final Class type;

        /**
         * Register call type with implementation.
         *
         * @param msgType Message type (Class)
         */
        private CallType(Class msgType) {
            this.type = msgType;
        }

    }

    @Override
    public final ServiceType getServiceType() {
        return ServiceType.TOPOLOGY;
    }

    @Override
    public int getLength() {
        return ServiceCallMsg.HEADER_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        buf.writeInt(ServiceType.TOPOLOGY.ordinal());
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return TopologyServiceMsg.parse(buf);
    }

    public static TopologyServiceMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < ServiceCallMsg.HEADER_LEN) {
            return null;
        }

        int callTypeNum = buf.readInt();
        CallType callType;
        try {
            callType = CallType.values()[callTypeNum];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException(String.format("Unknown call type '%d'", callTypeNum));
        }

        switch (callType) {
            case GET_LAST_UPDATE_TIME_REQ:
                return LastUpdateTimeReqMsg.parse(buf);
            case GET_LAST_UPDATE_TIME_RES:
                return LastUpdateTimeResMsg.parse(buf);
            case ATTCH_PT_PORT_REQ:
                return AttachmentPointPortReqMsg.parse(buf);
            case ATTCH_PT_PORT_RES:
                return AttachmentPointPortResMsg.parse(buf);
            case OF_DOM_ID_REQ:
                return OpenflowDomainIdReqMsg.parse(buf);
            case OF_DOM_ID_RES:
                return OpenflowDomainIdResMsg.parse(buf);
            case L2_DOM_ID_REQ:
                return L2DomainIdReqMsg.parse(buf);
            case L2_DOM_ID_RES:
                return L2DomainIdResMsg.parse(buf);
            case SAME_OF_DOM_REQ:
                return SameOpenflowDomainReqMsg.parse(buf);
            case SAME_OF_DOM_RES:
                return SameOpenflowDomainResMsg.parse(buf);
            case SAME_L2_DOM_REQ:
                return SameL2DomainReqMsg.parse(buf);
            case SAME_L2_DOM_RES:
                return SameL2DomainResMsg.parse(buf);
            case BCAST_DOM_PORT_REQ:
                return BroadcastDomainPortReqMsg.parse(buf);
            case BCAST_DOM_PORT_RES:
                return BroadcastDomainPortResMsg.parse(buf);
            case ALLOWED_REQ:
                return AllowedReqMsg.parse(buf);
            case ALLOWED_RES:
                return AllowedResMsg.parse(buf);
            case IN_BCAST_ALLOWED_REQ:
                return IncomingBroadcastAllowedReqMsg.parse(buf);
            case IN_BCAST_ALLOWED_RES:
                return IncomingBroadcastAllowedResMsg.parse(buf);
            case CONSISTENT_REQ:
                return ConsistentReqMsg.parse(buf);
            case CONSISTENT_RES:
                return ConsistentResMsg.parse(buf);
            case SAME_BCAST_DOM_REQ:
                return SameBroadcastDomainReqMsg.parse(buf);
            case SAME_BCAST_DOM_RES:
                return SameBroadcastDomainResMsg.parse(buf);
            case OUT_SW_PORT_REQ:
                return OutgoingSwitchPortReqMsg.parse(buf);
            case OUT_SW_PORT_RES:
                return OutgoingSwitchPortResMsg.parse(buf);
            case IN_SW_PORT_REQ:
                return IncomingSwitchPortReqMsg.parse(buf);
            case IN_SW_PORT_RES:
                return IncomingSwitchPortResMsg.parse(buf);
            case ALLOWED_OUT_BCAST_PORT_REQ:
                return AllowedOutgoingBroadcastPortReqMsg.parse(buf);
            case ALLOWED_OUT_BCAST_PORT_RES:
                return AllowedOutgoingBroadcastPortResMsg.parse(buf);
            case ALLOWED_IN_BCAST_PORT_REQ:
                return AllowedIncomingBroadcastPortReqMsg.parse(buf);
            case ALLOWED_IN_BCAST_PORT_RES:
                return AllowedIncomingBroadcastPortResMsg.parse(buf);
            case SWS_IN_OF_DOM_REQ_MSG:
                return SwitchesInOpenflowDomainReqMsg.parse(buf);
            case SWS_IN_OF_DOM_RES_MSG:
                return SwitchesInOpenflowDomainResMsg.parse(buf);
            case PORTS_WITH_LINKS_REQ:
                return PortsWithLinksReqMsg.parse(buf);
            case PORTS_WITH_LINKS_RES:
                return PortsWithLinksResMsg.parse(buf);
            case PORTS_REQ_MSG:
                return PortsReqMsg.parse(buf);
            case PORTS_RES_MSG:
                return PortsResMsg.parse(buf);
            case BCAST_DOM_PORTS_REQ:
                return BroadcastDomainPortsReqMsg.parse(buf);
            case BCAST_DOM_PORTS_RES:
                return BroadcastDomainPortsResMsg.parse(buf);
            case TUNNEL_PORTS_REQ:
                return TunnelPortsReqMsg.parse(buf);
            case TUNNEL_PORTS_RES:
                return TunnelPortsResMsg.parse(buf);
            case BLOCKED_PORTS_REQ:
                return BlockedPortsReqMsg.parse(buf);
            case BLOCKED_PORTS_RES:
                return BlockedPortsResMsg.parse(buf);
            default:
                throw new RuntimeException(String.format("Unknown call type '%d'", callTypeNum));
        }
    }

    @Override
    public short getAppPort() {
        return 0;
    }

}

class LastUpdateTimeReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = Short.SIZE / 8;

    public final short appPort;

    public LastUpdateTimeReqMsg(short appPort) {
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + LastUpdateTimeReqMsg.HEADER_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.GET_LAST_UPDATE_TIME_REQ.ordinal());
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return LastUpdateTimeReqMsg.parse(buf);
    }

    @SuppressWarnings("unused")
    public static LastUpdateTimeReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < LastUpdateTimeReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final short appPort = buf.readShort();
        return new LastUpdateTimeReqMsg(appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class LastUpdateTimeResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE) / 8;

    public final Date  date;

    public LastUpdateTimeResMsg(Date d) {
        this.date = d;
    }

    @Override
    public int getLength() {
        return super.getLength() + LastUpdateTimeResMsg.HEADER_LEN + LastUpdateTimeResMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.GET_LAST_UPDATE_TIME_RES.ordinal());
        buf.writeLong(this.date.getTime());
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return LastUpdateTimeResMsg.parse(buf);
    }

    public static LastUpdateTimeResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < LastUpdateTimeResMsg.PAYLOAD_LEN) {
            return null;
        }

        long time = buf.readLong();
        return new LastUpdateTimeResMsg(new Date(time));
    }

}

class AttachmentPointPortReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE + Short.SIZE*2 + Byte.SIZE) / 8;

    public final long    switchid;
    public final short   port;
    public final boolean tunnelEnabled;
    public final short   appPort;

    public AttachmentPointPortReqMsg(long switchid, short port, boolean tunnelEnabled, short appPort) {
        this.switchid = switchid;
        this.port = port;
        this.tunnelEnabled = tunnelEnabled;
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + AttachmentPointPortReqMsg.HEADER_LEN + AttachmentPointPortReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.ATTCH_PT_PORT_REQ.ordinal());
        buf.writeLong(this.switchid);
        buf.writeShort(this.port);
        buf.writeByte(this.tunnelEnabled ? 1 : 0);
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return AttachmentPointPortReqMsg.parse(buf);
    }

    public static AttachmentPointPortReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < AttachmentPointPortReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final long switchid = buf.readLong();
        final short port = buf.readShort();
        final boolean tunnelEnabled = buf.readByte() == 1;
        final short appPort = buf.readShort();
        return new AttachmentPointPortReqMsg(switchid, port, tunnelEnabled, appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class AttachmentPointPortResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = Byte.SIZE / 8;

    public final boolean isAttachmentPointPort;

    public AttachmentPointPortResMsg(boolean isAttachmentPointPort) {
        this.isAttachmentPointPort = isAttachmentPointPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + AttachmentPointPortResMsg.HEADER_LEN + AttachmentPointPortResMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.ATTCH_PT_PORT_RES.ordinal());
        buf.writeByte(this.isAttachmentPointPort ? 1 : 0);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return AttachmentPointPortResMsg.parse(buf);
    }

    public static AttachmentPointPortResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < AttachmentPointPortResMsg.PAYLOAD_LEN) {
            return null;
        }

        boolean isAttachmentPointPort = buf.readByte() == 1;
        return new AttachmentPointPortResMsg(isAttachmentPointPort);
    }

}

class OpenflowDomainIdReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE + Byte.SIZE + Short.SIZE) / 8;

    public final long    switchid;
    public final boolean tunnelEnabled;
    public final short   appPort;

    public OpenflowDomainIdReqMsg(long switchid, boolean tunnelEnabled, short appPort) {
        this.switchid = switchid;
        this.tunnelEnabled = tunnelEnabled;
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + OpenflowDomainIdReqMsg.HEADER_LEN + OpenflowDomainIdReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.OF_DOM_ID_REQ.ordinal());
        buf.writeLong(this.switchid);
        buf.writeByte(this.tunnelEnabled ? 1 : 0);
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return OpenflowDomainIdReqMsg.parse(buf);
    }

    public static OpenflowDomainIdReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < OpenflowDomainIdReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final long switchid = buf.readLong();
        final boolean tunnelEnabled = buf.readByte() == 1;
        final short appPort = buf.readShort();
        return new OpenflowDomainIdReqMsg(switchid, tunnelEnabled, appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class OpenflowDomainIdResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final  int HEADER_LEN = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = Long.SIZE / 8;

    public final long ofDomainId;

    public OpenflowDomainIdResMsg(long ofDomainId) {
        this.ofDomainId = ofDomainId;
    }

    @Override
    public int getLength() {
        return super.getLength() + OpenflowDomainIdResMsg.HEADER_LEN + OpenflowDomainIdResMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.OF_DOM_ID_RES.ordinal());
        buf.writeLong(this.ofDomainId);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return OpenflowDomainIdResMsg.parse(buf);
    }

    public static OpenflowDomainIdResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < OpenflowDomainIdResMsg.PAYLOAD_LEN) {
            return null;
        }

        long ofDomainId = buf.readLong();
        return new OpenflowDomainIdResMsg(ofDomainId);
    }

}

class L2DomainIdReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE + Byte.SIZE + Short.SIZE) / 8;

    public final long    switchid;
    public final boolean tunnelEnabled;
    public final short   appPort;

    public L2DomainIdReqMsg(long switchid, boolean tunnelEnabled, short appPort) {
        this.switchid = switchid;
        this.tunnelEnabled = tunnelEnabled;
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + L2DomainIdReqMsg.HEADER_LEN + L2DomainIdReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.L2_DOM_ID_REQ.ordinal());
        buf.writeLong(this.switchid);
        buf.writeByte(this.tunnelEnabled ? 1 : 0);
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return L2DomainIdReqMsg.parse(buf);
    }

    public static L2DomainIdReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < L2DomainIdReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final long switchid = buf.readLong();
        final boolean tunnelEnabled = buf.readByte() == 1;
        final short appPort = buf.readShort();
        return new L2DomainIdReqMsg(switchid, tunnelEnabled, appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class L2DomainIdResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = Long.SIZE / 8;

    public final long l2DomainId;

    public L2DomainIdResMsg(long ofDomainId) {
        this.l2DomainId = ofDomainId;
    }

    @Override
    public int getLength() {
        return super.getLength() + L2DomainIdResMsg.HEADER_LEN + L2DomainIdResMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.L2_DOM_ID_RES.ordinal());
        buf.writeLong(this.l2DomainId);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return L2DomainIdResMsg.parse(buf);
    }

    public static L2DomainIdResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < L2DomainIdResMsg.PAYLOAD_LEN) {
            return null;
        }

        long l2DomainId = buf.readLong();
        return new L2DomainIdResMsg(l2DomainId);
    }

}

class SameOpenflowDomainReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE*2 + Byte.SIZE + Short.SIZE) / 8;

    public final long    switch1;
    public final long    switch2;
    public final boolean tunnelEnabled;
    public final short   appPort;

    public SameOpenflowDomainReqMsg(long switch1, long switch2, boolean tunnelEnabled, short appPort) {
        this.switch1 = switch1;
        this.switch2 = switch2;
        this.tunnelEnabled = tunnelEnabled;
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + SameOpenflowDomainReqMsg.HEADER_LEN + SameOpenflowDomainReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.SAME_OF_DOM_REQ.ordinal());
        buf.writeLong(this.switch1);
        buf.writeLong(this.switch2);
        buf.writeByte(this.tunnelEnabled ? 1 : 0);
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return SameOpenflowDomainReqMsg.parse(buf);
    }

    public static SameOpenflowDomainReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < SameOpenflowDomainReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final long switch1 = buf.readLong();
        final long switch2 = buf.readLong();
        final boolean tunnelEnabled = buf.readByte() == 1;
        final short appPort = buf.readShort();
        return new SameOpenflowDomainReqMsg(switch1, switch2, tunnelEnabled, appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class SameOpenflowDomainResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = Byte.SIZE / 8;

    public final boolean inSameOpenflowDomain;

    public SameOpenflowDomainResMsg(boolean inSameOpenflowDomain) {
        this.inSameOpenflowDomain = inSameOpenflowDomain;
    }

    @Override
    public int getLength() {
        return super.getLength() + SameOpenflowDomainResMsg.HEADER_LEN + SameOpenflowDomainResMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.SAME_OF_DOM_RES.ordinal());
        buf.writeByte(this.inSameOpenflowDomain ? 1 : 0);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return SameOpenflowDomainResMsg.parse(buf);
    }

    public static SameOpenflowDomainResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < SameOpenflowDomainResMsg.PAYLOAD_LEN) {
            return null;
        }

        boolean inSameOpenflowDomain = buf.readByte() == 1;
        return new SameOpenflowDomainResMsg(inSameOpenflowDomain);
    }

}

class SameL2DomainReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE*2 + Byte.SIZE + Short.SIZE) / 8;

    public final long    switch1;
    public final long    switch2;
    public final boolean tunnelEnabled;
    public final short   appPort;

    public SameL2DomainReqMsg(long switch1, long switch2, boolean tunnelEnabled, short appPort) {
        this.switch1 = switch1;
        this.switch2 = switch2;
        this.tunnelEnabled = tunnelEnabled;
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + SameL2DomainReqMsg.HEADER_LEN + SameL2DomainReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.SAME_L2_DOM_REQ.ordinal());
        buf.writeLong(this.switch1);
        buf.writeLong(this.switch2);
        buf.writeByte(this.tunnelEnabled ? 1 : 0);
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return SameL2DomainReqMsg.parse(buf);
    }

    public static SameL2DomainReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < SameL2DomainReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final long switch1 = buf.readLong();
        final long switch2 = buf.readLong();
        final boolean tunnelEnabled = buf.readByte() == 1;
        final short appPort = buf.readShort();
        return new SameL2DomainReqMsg(switch1, switch2, tunnelEnabled, appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class SameL2DomainResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = Byte.SIZE / 8;

    public final boolean inSameL2Domain;

    public SameL2DomainResMsg(boolean inSameL2Domain) {
        this.inSameL2Domain = inSameL2Domain;
    }

    @Override
    public int getLength() {
        return super.getLength() + SameL2DomainResMsg.HEADER_LEN + SameL2DomainResMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.SAME_L2_DOM_RES.ordinal());
        buf.writeByte(this.inSameL2Domain ? 1 : 0);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return SameL2DomainResMsg.parse(buf);
    }

    public static SameL2DomainResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < SameL2DomainResMsg.PAYLOAD_LEN) {
            return null;
        }

        boolean inSameL2Domain = buf.readByte() == 1;
        return new SameL2DomainResMsg(inSameL2Domain);
    }

}

class BroadcastDomainPortReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE + Short.SIZE*2 + Byte.SIZE) / 8;

    public final long    switchid;
    public final short   port;
    public final boolean tunnelEnabled;
    public final short   appPort;

    public BroadcastDomainPortReqMsg(long switchid, short port, boolean tunnelEnabled, short appPort) {
        this.switchid = switchid;
        this.port = port;
        this.tunnelEnabled = tunnelEnabled;
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + BroadcastDomainPortReqMsg.HEADER_LEN + BroadcastDomainPortReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.BCAST_DOM_PORT_REQ.ordinal());
        buf.writeLong(this.switchid);
        buf.writeShort(this.port);
        buf.writeByte(this.tunnelEnabled ? 1 : 0);
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return BroadcastDomainPortReqMsg.parse(buf);
    }

    public static BroadcastDomainPortReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < BroadcastDomainPortReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final long switchid = buf.readLong();
        final short port = buf.readShort();
        final boolean tunnelEnabled = buf.readByte() == 1;
        final short appPort = buf.readShort();
        return new BroadcastDomainPortReqMsg(switchid, port, tunnelEnabled, appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class BroadcastDomainPortResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = Byte.SIZE / 8;

    public final boolean isBroadcastDomainPort;

    public BroadcastDomainPortResMsg(boolean isBroadcastDomainPort) {
        this.isBroadcastDomainPort = isBroadcastDomainPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + BroadcastDomainPortResMsg.HEADER_LEN + BroadcastDomainPortResMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.BCAST_DOM_PORT_RES.ordinal());
        buf.writeByte(this.isBroadcastDomainPort ? 1 : 0);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return BroadcastDomainPortResMsg.parse(buf);
    }

    public static BroadcastDomainPortResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < BroadcastDomainPortResMsg.PAYLOAD_LEN) {
            return null;
        }

        boolean isBroadcastDomainPort = buf.readByte() == 1;
        return new BroadcastDomainPortResMsg(isBroadcastDomainPort);
    }

}

class AllowedReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE + Short.SIZE*2 + Byte.SIZE) / 8;

    public final long    switchid;
    public final short   port;
    public final boolean tunnelEnabled;
    public final short   appPort;

    public AllowedReqMsg(long switchid, short port, boolean tunnelEnabled, short appPort) {
        this.switchid = switchid;
        this.port = port;
        this.tunnelEnabled = tunnelEnabled;
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + AllowedReqMsg.HEADER_LEN + AllowedReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.ALLOWED_REQ.ordinal());
        buf.writeLong(this.switchid);
        buf.writeShort(this.port);
        buf.writeByte(this.tunnelEnabled ? 1 : 0);
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return AllowedReqMsg.parse(buf);
    }

    public static AllowedReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < AllowedReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final long switchid = buf.readLong();
        final short port = buf.readShort();
        final boolean tunnelEnabled = buf.readByte() == 1;
        final short appPort = buf.readShort();
        return new AllowedReqMsg(switchid, port, tunnelEnabled, appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class AllowedResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = Byte.SIZE / 8;

    public final boolean isAllowed;

    public AllowedResMsg(boolean isAllowed) {
        this.isAllowed = isAllowed;
    }

    @Override
    public int getLength() {
        return super.getLength() + AllowedResMsg.HEADER_LEN + AllowedResMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.ALLOWED_RES.ordinal());
        buf.writeByte(this.isAllowed ? 1 : 0);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return AllowedResMsg.parse(buf);
    }

    public static AllowedResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < AllowedResMsg.PAYLOAD_LEN) {
            return null;
        }

        boolean isAllowed = buf.readByte() == 1;
        return new AllowedResMsg(isAllowed);
    }

}

class IncomingBroadcastAllowedReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE + Short.SIZE*2 + Byte.SIZE) / 8;

    public final long    switchid;
    public final short   port;
    public final boolean tunnelEnabled;
    public final short   appPort;

    public IncomingBroadcastAllowedReqMsg(long switchid, short port, boolean tunnelEnabled, short appPort) {
        this.switchid = switchid;
        this.port = port;
        this.tunnelEnabled = tunnelEnabled;
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + IncomingBroadcastAllowedReqMsg.HEADER_LEN +
               IncomingBroadcastAllowedReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.IN_BCAST_ALLOWED_REQ.ordinal());
        buf.writeLong(this.switchid);
        buf.writeShort(this.port);
        buf.writeByte(this.tunnelEnabled ? 1 : 0);
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return IncomingBroadcastAllowedReqMsg.parse(buf);
    }

    public static IncomingBroadcastAllowedReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < IncomingBroadcastAllowedReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final long switchid = buf.readLong();
        final short port = buf.readShort();
        final boolean tunnelEnabled = buf.readByte() == 1;
        final short appPort = buf.readShort();
        return new IncomingBroadcastAllowedReqMsg(switchid, port, tunnelEnabled, appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class IncomingBroadcastAllowedResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = Byte.SIZE / 8;

    public final boolean isIncomingBroadcastAllowed;

    public IncomingBroadcastAllowedResMsg(boolean isIncomingBroadcastAllowed) {
        this.isIncomingBroadcastAllowed = isIncomingBroadcastAllowed;
    }

    @Override
    public int getLength() {
        return super.getLength() + IncomingBroadcastAllowedResMsg.HEADER_LEN +
               IncomingBroadcastAllowedResMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.IN_BCAST_ALLOWED_RES.ordinal());
        buf.writeByte(this.isIncomingBroadcastAllowed ? 1 : 0);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return IncomingBroadcastAllowedResMsg.parse(buf);
    }

    public static IncomingBroadcastAllowedResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < IncomingBroadcastAllowedResMsg.PAYLOAD_LEN) {
            return null;
        }

        boolean isIncomingBroadcastAllowed = buf.readByte() == 1;
        return new IncomingBroadcastAllowedResMsg(isIncomingBroadcastAllowed);
    }

}

class ConsistentReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE * 2 + Short.SIZE * 3 + Byte.SIZE) / 8;

    public final long    oldSw;
    public final short   oldPort;
    public final long    newSw;
    public final short   newPort;
    public final boolean tunnelEnabled;
    public final short   appPort;

    public ConsistentReqMsg(long oldSw, short oldPort, long newSw, short newPort, boolean tunnelEnabled,
                            short appPort) {
        this.oldSw = oldSw;
        this.oldPort = oldPort;
        this.newSw = newSw;
        this.newPort = newPort;
        this.tunnelEnabled = tunnelEnabled;
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + ConsistentReqMsg.HEADER_LEN + ConsistentReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.CONSISTENT_REQ.ordinal());
        buf.writeLong(this.oldSw);
        buf.writeShort(this.oldPort);
        buf.writeLong(this.newSw);
        buf.writeShort(this.newPort);
        buf.writeByte(this.tunnelEnabled ? 1 : 0);
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return ConsistentReqMsg.parse(buf);
    }

    public static ConsistentReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < ConsistentReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final long oldSw = buf.readLong();
        final short oldPort = buf.readShort();
        final long newSw = buf.readLong();
        final short newPort = buf.readShort();
        final boolean tunnelEnabled = buf.readByte() == 1;
        final short appPort = buf.readShort();
        return new ConsistentReqMsg(oldSw, oldPort, newSw, newPort, tunnelEnabled, appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class ConsistentResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = Byte.SIZE / 8;

    public final boolean isConsistent;

    public ConsistentResMsg(boolean isConsistent) {
        this.isConsistent = isConsistent;
    }

    @Override
    public int getLength() {
        return super.getLength() + ConsistentResMsg.HEADER_LEN + ConsistentResMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.CONSISTENT_RES.ordinal());
        buf.writeByte(this.isConsistent ? 1 : 0);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return ConsistentResMsg.parse(buf);
    }

    public static ConsistentResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < ConsistentResMsg.PAYLOAD_LEN) {
            return null;
        }

        boolean isConsistent = buf.readByte() == 1;
        return new ConsistentResMsg(isConsistent);
    }

}

class SameBroadcastDomainReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE*2 + Short.SIZE*3 + Byte.SIZE) / 8;

    public final long    s1;
    public final short   p1;
    public final long    s2;
    public final short   p2;
    public final boolean tunnelEnabled;
    public final short   appPort;

    public SameBroadcastDomainReqMsg(long s1, short p1, long s2, short p2, boolean tunnelEnabled, short appPort) {
        this.s1 = s1;
        this.p1 = p1;
        this.s2 = s2;
        this.p2 = p2;
        this.tunnelEnabled = tunnelEnabled;
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + SameBroadcastDomainReqMsg.HEADER_LEN + SameBroadcastDomainReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.SAME_BCAST_DOM_REQ.ordinal());
        buf.writeLong(this.s1);
        buf.writeShort(this.p1);
        buf.writeLong(this.s2);
        buf.writeShort(this.p2);
        buf.writeByte(this.tunnelEnabled ? 1 : 0);
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return SameBroadcastDomainReqMsg.parse(buf);
    }

    public static SameBroadcastDomainReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < SameBroadcastDomainReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final long s1 = buf.readLong();
        final short p1 = buf.readShort();
        final long s2 = buf.readLong();
        final short p2 = buf.readShort();
        final boolean tunnelEnabled = buf.readByte() == 1;
        final short appPort = buf.readShort();
        return new SameBroadcastDomainReqMsg(s1, p1, s2, p2, tunnelEnabled, appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class SameBroadcastDomainResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = Byte.SIZE / 8;

    public final boolean isInSameBroadcastDomain;

    public SameBroadcastDomainResMsg(boolean isInSameBroadcastDomain) {
        this.isInSameBroadcastDomain = isInSameBroadcastDomain;
    }

    @Override
    public int getLength() {
        return super.getLength() + SameBroadcastDomainResMsg.HEADER_LEN + SameBroadcastDomainResMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.SAME_BCAST_DOM_RES.ordinal());
        buf.writeByte(this.isInSameBroadcastDomain ? 1 : 0);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return SameBroadcastDomainResMsg.parse(buf);
    }

    public static SameBroadcastDomainResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < SameBroadcastDomainResMsg.PAYLOAD_LEN) {
            return null;
        }

        boolean isInSameBroadcastDomain = buf.readByte() == 1;
        return new SameBroadcastDomainResMsg(isInSameBroadcastDomain);
    }

}

class OutgoingSwitchPortReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE * 2 + Short.SIZE * 3 + Byte.SIZE) / 8;

    public final long    src;
    public final short   srcPort;
    public final long    dst;
    public final short   dstPort;
    public final boolean tunnelEnabled;
    public final short   appPort;

    public OutgoingSwitchPortReqMsg(long src, short srcPort, long dst, short dstPort, boolean tunnelEnabled,
                                    short appPort) {
        this.src = src;
        this.srcPort = srcPort;
        this.dst = dst;
        this.dstPort = dstPort;
        this.tunnelEnabled = tunnelEnabled;
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + OutgoingSwitchPortReqMsg.HEADER_LEN + OutgoingSwitchPortReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.OUT_SW_PORT_REQ.ordinal());
        buf.writeLong(this.src);
        buf.writeShort(this.srcPort);
        buf.writeLong(this.dst);
        buf.writeShort(this.dstPort);
        buf.writeByte(this.tunnelEnabled ? 1 : 0);
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return OutgoingSwitchPortReqMsg.parse(buf);
    }

    public static OutgoingSwitchPortReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < OutgoingSwitchPortReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final long src = buf.readLong();
        final short srcPort = buf.readShort();
        final long dst = buf.readLong();
        final short dstPort = buf.readShort();
        final boolean tunnelEnabled = buf.readByte() == 1;
        final short appPort = buf.readShort();
        return new OutgoingSwitchPortReqMsg(src, srcPort, dst, dstPort, tunnelEnabled, appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class OutgoingSwitchPortResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE + Short.SIZE) / 8;

    public final NodePortTuple outgoingSwitchPort;

    public OutgoingSwitchPortResMsg(NodePortTuple outgoingSwitchPort) {
        this.outgoingSwitchPort = outgoingSwitchPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + OutgoingSwitchPortResMsg.HEADER_LEN + OutgoingSwitchPortResMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.OUT_SW_PORT_RES.ordinal());
        buf.writeLong(this.outgoingSwitchPort.getNodeId());
        buf.writeShort(this.outgoingSwitchPort.getPortId());
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return OutgoingSwitchPortResMsg.parse(buf);
    }

    public static OutgoingSwitchPortResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < OutgoingSwitchPortResMsg.PAYLOAD_LEN) {
            return null;
        }

        long nodeId = buf.readLong();
        short portId = buf.readShort();
        return new OutgoingSwitchPortResMsg(new NodePortTuple(nodeId, portId));
    }

}

class IncomingSwitchPortReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE * 2 + Short.SIZE * 3 + Byte.SIZE) / 8;

    public final long    src;
    public final short   srcPort;
    public final long    dst;
    public final short   dstPort;
    public final boolean tunnelEnabled;
    public final short   appPort;

    public IncomingSwitchPortReqMsg(long src, short srcPort, long dst, short dstPort, boolean tunnelEnabled,
                                    short appPort) {
        this.src = src;
        this.srcPort = srcPort;
        this.dst = dst;
        this.dstPort = dstPort;
        this.tunnelEnabled = tunnelEnabled;
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + IncomingSwitchPortReqMsg.HEADER_LEN + IncomingSwitchPortReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.IN_SW_PORT_REQ.ordinal());
        buf.writeLong(this.src);
        buf.writeShort(this.srcPort);
        buf.writeLong(this.dst);
        buf.writeShort(this.dstPort);
        buf.writeByte(this.tunnelEnabled ? 1 : 0);
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return IncomingSwitchPortReqMsg.parse(buf);
    }

    public static IncomingSwitchPortReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < IncomingSwitchPortReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final long src = buf.readLong();
        final short srcPort = buf.readShort();
        final long dst = buf.readLong();
        final short dstPort = buf.readShort();
        final boolean tunnelEnabled = buf.readByte() == 1;
        final short appPort = buf.readShort();
        return new IncomingSwitchPortReqMsg(src, srcPort, dst, dstPort, tunnelEnabled, appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class IncomingSwitchPortResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE + Short.SIZE) / 8;

    public final NodePortTuple incomingSwitchPort;

    public IncomingSwitchPortResMsg(NodePortTuple incomingSwitchPort) {
        this.incomingSwitchPort = incomingSwitchPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + IncomingSwitchPortResMsg.HEADER_LEN + IncomingSwitchPortResMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.IN_SW_PORT_RES.ordinal());
        buf.writeLong(this.incomingSwitchPort.getNodeId());
        buf.writeShort(this.incomingSwitchPort.getPortId());
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return IncomingSwitchPortResMsg.parse(buf);
    }

    public static IncomingSwitchPortResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < IncomingSwitchPortResMsg.PAYLOAD_LEN) {
            return null;
        }

        long nodeId = buf.readLong();
        short portId = buf.readShort();
        return new IncomingSwitchPortResMsg(new NodePortTuple(nodeId, portId));
    }

}

class AllowedOutgoingBroadcastPortReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE * 2 + Short.SIZE * 3 + Byte.SIZE) / 8;

    public final long    src;
    public final short   srcPort;
    public final long    dst;
    public final short   dstPort;
    public final boolean tunnelEnabled;
    public final short   appPort;

    public AllowedOutgoingBroadcastPortReqMsg(long src, short srcPort, long dst, short dstPort,
                                              boolean tunnelEnabled, short appPort) {
        this.src = src;
        this.srcPort = srcPort;
        this.dst = dst;
        this.dstPort = dstPort;
        this.tunnelEnabled = tunnelEnabled;
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() +
               AllowedOutgoingBroadcastPortReqMsg.HEADER_LEN +
               AllowedOutgoingBroadcastPortReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.ALLOWED_OUT_BCAST_PORT_REQ.ordinal());
        buf.writeLong(this.src);
        buf.writeShort(this.srcPort);
        buf.writeLong(this.dst);
        buf.writeShort(this.dstPort);
        buf.writeByte(this.tunnelEnabled ? 1 : 0);
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return AllowedOutgoingBroadcastPortReqMsg.parse(buf);
    }

    public static AllowedOutgoingBroadcastPortReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < AllowedOutgoingBroadcastPortReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final long src = buf.readLong();
        final short srcPort = buf.readShort();
        final long dst = buf.readLong();
        final short dstPort = buf.readShort();
        final boolean tunnelEnabled = buf.readByte() == 1;
        final short appPort = buf.readShort();
        return new AllowedOutgoingBroadcastPortReqMsg(src, srcPort, dst, dstPort, tunnelEnabled, appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class AllowedOutgoingBroadcastPortResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE + Short.SIZE) / 8;

    public final NodePortTuple allowedOutgoingBroadcastPort;

    public AllowedOutgoingBroadcastPortResMsg(NodePortTuple allowedOutgoingBroadcastPort) {
        this.allowedOutgoingBroadcastPort = allowedOutgoingBroadcastPort;
    }

    @Override
    public int getLength() {
        return super.getLength() +
               AllowedOutgoingBroadcastPortResMsg.HEADER_LEN +
               AllowedOutgoingBroadcastPortResMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.ALLOWED_OUT_BCAST_PORT_RES.ordinal());
        buf.writeLong(this.allowedOutgoingBroadcastPort.getNodeId());
        buf.writeShort(this.allowedOutgoingBroadcastPort.getPortId());
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return AllowedOutgoingBroadcastPortResMsg.parse(buf);
    }

    public static AllowedOutgoingBroadcastPortResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < AllowedOutgoingBroadcastPortResMsg.PAYLOAD_LEN) {
            return null;
        }

        long nodeId = buf.readLong();
        short portId = buf.readShort();
        return new AllowedOutgoingBroadcastPortResMsg(new NodePortTuple(nodeId, portId));
    }

}

class AllowedIncomingBroadcastPortReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE + Short.SIZE * 2 + Byte.SIZE) / 8;

    public final long    src;
    public final short   srcPort;
    public final boolean tunnelEnabled;
    public final short   appPort;

    public AllowedIncomingBroadcastPortReqMsg(long src, short srcPort, boolean tunnelEnabled, short appPort) {
        this.src = src;
        this.srcPort = srcPort;
        this.tunnelEnabled = tunnelEnabled;
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() +
               AllowedIncomingBroadcastPortReqMsg.HEADER_LEN +
               AllowedIncomingBroadcastPortReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.ALLOWED_IN_BCAST_PORT_REQ.ordinal());
        buf.writeLong(this.src);
        buf.writeShort(this.srcPort);
        buf.writeByte(this.tunnelEnabled ? 1 : 0);
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return AllowedIncomingBroadcastPortReqMsg.parse(buf);
    }

    public static AllowedIncomingBroadcastPortReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < AllowedIncomingBroadcastPortReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final long src = buf.readLong();
        final short srcPort = buf.readShort();
        final boolean tunnelEnabled = buf.readByte() == 1;
        final short appPort = buf.readShort();
        return new AllowedIncomingBroadcastPortReqMsg(src, srcPort, tunnelEnabled, appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class AllowedIncomingBroadcastPortResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE + Short.SIZE) / 8;

    public final NodePortTuple allowedIncomingBroadcastPort;

    public AllowedIncomingBroadcastPortResMsg(NodePortTuple allowedOutgoingBroadcastPort) {
        this.allowedIncomingBroadcastPort = allowedOutgoingBroadcastPort;
    }

    @Override
    public int getLength() {
        return super.getLength() +
               AllowedIncomingBroadcastPortResMsg.HEADER_LEN +
               AllowedIncomingBroadcastPortResMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.ALLOWED_IN_BCAST_PORT_RES.ordinal());
        buf.writeLong(this.allowedIncomingBroadcastPort.getNodeId());
        buf.writeShort(this.allowedIncomingBroadcastPort.getPortId());
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return AllowedIncomingBroadcastPortResMsg.parse(buf);
    }

    public static AllowedIncomingBroadcastPortResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < AllowedIncomingBroadcastPortResMsg.PAYLOAD_LEN) {
            return null;
        }

        long nodeId = buf.readLong();
        short portId = buf.readShort();
        return new AllowedIncomingBroadcastPortResMsg(new NodePortTuple(nodeId, portId));
    }

}

class SwitchesInOpenflowDomainReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE + Byte.SIZE + Short.SIZE) / 8;

    public final long    switchid;
    public final boolean tunnelEnabled;
    public final short   appPort;

    public SwitchesInOpenflowDomainReqMsg(long switchid, boolean tunnelEnabled, short appPort) {
        this.switchid = switchid;
        this.tunnelEnabled = tunnelEnabled;
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() +
               SwitchesInOpenflowDomainReqMsg.HEADER_LEN +
               SwitchesInOpenflowDomainReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.SWS_IN_OF_DOM_REQ_MSG.ordinal());
        buf.writeLong(this.switchid);
        buf.writeByte(this.tunnelEnabled ? 1 : 0);
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return SwitchesInOpenflowDomainReqMsg.parse(buf);
    }

    public static SwitchesInOpenflowDomainReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < SwitchesInOpenflowDomainReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final long switchid = buf.readLong();
        final boolean tunnelEnabled = buf.readByte() == 1;
        final short appPort = buf.readShort();
        return new SwitchesInOpenflowDomainReqMsg(switchid, tunnelEnabled, appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }
}

class SwitchesInOpenflowDomainResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN           = Integer.SIZE / 8;
    private static final int PAYLOAD_PREAMBLE_LEN = Integer.SIZE / 8;
    private static final int PAYLOAD_ITEM_LEN     = Long.SIZE / 8;

    public final int       payload_len;
    public final Set<Long> switchesInOpenflowDomain;

    public SwitchesInOpenflowDomainResMsg(Set<Long> switchesInOpenflowDomain) {
        this.switchesInOpenflowDomain = switchesInOpenflowDomain;
        this.payload_len = switchesInOpenflowDomain.size() * SwitchesInOpenflowDomainResMsg.PAYLOAD_ITEM_LEN;
    }

    @Override
    public int getLength() {
        return super.getLength() +
               SwitchesInOpenflowDomainResMsg.HEADER_LEN +
               SwitchesInOpenflowDomainResMsg.PAYLOAD_PREAMBLE_LEN +
               this.payload_len;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.SWS_IN_OF_DOM_RES_MSG.ordinal());
        buf.writeInt(this.switchesInOpenflowDomain.size());
        for (Long sw : this.switchesInOpenflowDomain) {
            buf.writeLong(sw);
        }
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return SwitchesInOpenflowDomainResMsg.parse(buf);
    }

    public static SwitchesInOpenflowDomainResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < SwitchesInOpenflowDomainResMsg.PAYLOAD_PREAMBLE_LEN) {
            return null;
        }

        int numPayloadItems = buf.readInt();
        int payload_len = numPayloadItems * SwitchesInOpenflowDomainResMsg.PAYLOAD_ITEM_LEN;

        if (buf.readableBytes() < payload_len) {
            return null;
        }

        Set<Long> switchesInOpenflowDomain = new HashSet<Long>(numPayloadItems);
        for (int i = 0; i < numPayloadItems; i++) {
            switchesInOpenflowDomain.add(buf.readLong());
        }
        return new SwitchesInOpenflowDomainResMsg(switchesInOpenflowDomain);
    }

}

class PortsWithLinksReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE + Byte.SIZE + Short.SIZE) / 8;

    public final long    switchid;
    public final boolean tunnelEnabled;
    public final short   appPort;

    public PortsWithLinksReqMsg(long switchid, boolean tunnelEnabled, short appPort) {
        this.switchid = switchid;
        this.tunnelEnabled = tunnelEnabled;
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + PortsWithLinksReqMsg.HEADER_LEN + PortsWithLinksReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.PORTS_WITH_LINKS_REQ.ordinal());
        buf.writeLong(this.switchid);
        buf.writeByte(this.tunnelEnabled ? 1 : 0);
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return PortsWithLinksReqMsg.parse(buf);
    }

    public static PortsWithLinksReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < PortsWithLinksReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final long switchid = buf.readLong();
        final boolean tunnelEnabled = buf.readByte() == 1;
        final short appPort = buf.readShort();
        return new PortsWithLinksReqMsg(switchid, tunnelEnabled, appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class PortsWithLinksResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN           = Integer.SIZE / 8;
    private static final int PAYLOAD_PREAMBLE_LEN = Integer.SIZE / 8;
    private static final int PAYLOAD_ITEM_LEN     = Short.SIZE / 8;

    public final int        payload_len;
    public final Set<Short> portsWithLinks;

    public PortsWithLinksResMsg(Set<Short> portsWithLinks) {
        this.portsWithLinks = portsWithLinks;
        this.payload_len = portsWithLinks.size() * PortsWithLinksResMsg.PAYLOAD_ITEM_LEN;
    }

    @Override
    public int getLength() {
        return super.getLength() +
               PortsWithLinksResMsg.HEADER_LEN +
               PortsWithLinksResMsg.PAYLOAD_PREAMBLE_LEN +
               this.payload_len;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.PORTS_WITH_LINKS_RES.ordinal());
        buf.writeInt(this.portsWithLinks.size());
        for (Short sw : this.portsWithLinks) {
            buf.writeShort(sw);
        }
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return PortsWithLinksResMsg.parse(buf);
    }

    public static PortsWithLinksResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < PortsWithLinksResMsg.PAYLOAD_PREAMBLE_LEN) {
            return null;
        }

        int numPayloadItems = buf.readInt();
        int payload_len = numPayloadItems * PortsWithLinksResMsg.PAYLOAD_ITEM_LEN;

        if (buf.readableBytes() < payload_len) {
            return null;
        }

        Set<Short> portsWithLinks = new HashSet<Short>(numPayloadItems);
        for (int i = 0; i < numPayloadItems; i++) {
            portsWithLinks.add(buf.readShort());
        }
        return new PortsWithLinksResMsg(portsWithLinks);
    }

}

class BroadcastPortsReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE * 2 + Short.SIZE * 2 + Byte.SIZE) / 8;

    public final long    targetSw;
    public final long    src;
    public final short   srcPort;
    public final boolean tunnelEnabled;
    public final short   appPort;

    public BroadcastPortsReqMsg(long targetSw, long src, short srcPort, boolean tunnelEnabled, short appPort) {
        this.targetSw = targetSw;
        this.src = src;
        this.srcPort = srcPort;
        this.tunnelEnabled = tunnelEnabled;
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + BroadcastPortsReqMsg.HEADER_LEN + BroadcastPortsReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.BCAST_PORTS_REQ_MSG.ordinal());
        buf.writeLong(this.targetSw);
        buf.writeLong(this.src);
        buf.writeShort(this.srcPort);
        buf.writeByte(this.tunnelEnabled ? 1 : 0);
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return BroadcastPortsReqMsg.parse(buf);
    }

    public static BroadcastPortsReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < BroadcastPortsReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final long targetSw = buf.readLong();
        final long src = buf.readLong();
        final short srcPort = buf.readShort();
        final boolean tunnelEnabled = buf.readByte() == 1;
        final short appPort = buf.readShort();
        return new BroadcastPortsReqMsg(targetSw, src, srcPort, tunnelEnabled, appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class BroadcastPortsResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN           = Integer.SIZE / 8;
    private static final int PAYLOAD_PREAMBLE_LEN = Integer.SIZE / 8;
    private static final int PAYLOAD_ITEM_LEN     = Short.SIZE / 8;

    public final int        payload_len;
    public final Set<Short> broadcastPorts;

    public BroadcastPortsResMsg(Set<Short> broadcastPorts) {
        this.broadcastPorts = broadcastPorts;
        this.payload_len = broadcastPorts.size() * BroadcastPortsResMsg.PAYLOAD_ITEM_LEN;
    }

    @Override
    public int getLength() {
        return super.getLength() +
               BroadcastPortsResMsg.HEADER_LEN +
               BroadcastPortsResMsg.PAYLOAD_PREAMBLE_LEN +
               this.payload_len;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.BCAST_PORTS_RES_MSG.ordinal());
        buf.writeInt(this.broadcastPorts.size());
        for (Short sw : this.broadcastPorts) {
            buf.writeShort(sw);
        }
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return BroadcastPortsResMsg.parse(buf);
    }

    public static BroadcastPortsResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < BroadcastPortsResMsg.PAYLOAD_PREAMBLE_LEN) {
            return null;
        }

        int numPayloadItems = buf.readInt();
        int payload_len = numPayloadItems * BroadcastPortsResMsg.PAYLOAD_ITEM_LEN;

        if (buf.readableBytes() < payload_len) {
            return null;
        }

        Set<Short> broadcastPorts = new HashSet<Short>(numPayloadItems);
        for (int i = 0; i < numPayloadItems; i++) {
            broadcastPorts.add(buf.readShort());
        }
        return new BroadcastPortsResMsg(broadcastPorts);
    }

}

class PortsReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE + Short.SIZE) / 8;

    public final long  sw;
    public final short appPort;

    public PortsReqMsg(long sw, short appPort) {
        this.sw = sw;
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + PortsReqMsg.HEADER_LEN + PortsReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.PORTS_REQ_MSG.ordinal());
        buf.writeLong(this.sw);
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return PortsReqMsg.parse(buf);
    }

    public static PortsReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < PortsReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final long sw = buf.readLong();
        final short appPort = buf.readShort();
        return new PortsReqMsg(sw, appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class PortsResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN           = Integer.SIZE / 8;
    private static final int PAYLOAD_PREAMBLE_LEN = Integer.SIZE / 8;
    private static final int PAYLOAD_ITEM_LEN     = Short.SIZE / 8;

    public final int        payload_len;
    public final Set<Short> ports;

    public PortsResMsg(Set<Short> ports) {
        this.ports = ports;
        this.payload_len = ports.size() * PortsResMsg.PAYLOAD_ITEM_LEN;
    }

    @Override
    public int getLength() {
        return super.getLength() +
               PortsResMsg.HEADER_LEN +
               PortsResMsg.PAYLOAD_PREAMBLE_LEN +
               this.payload_len;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.PORTS_RES_MSG.ordinal());
        buf.writeInt(this.ports.size());
        for (Short sw : this.ports) {
            buf.writeShort(sw);
        }
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return PortsResMsg.parse(buf);
    }

    public static PortsResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < PortsResMsg.PAYLOAD_PREAMBLE_LEN) {
            return null;
        }

        int numPayloadItems = buf.readInt();
        int payload_len = numPayloadItems * PortsResMsg.PAYLOAD_ITEM_LEN;

        if (buf.readableBytes() < payload_len) {
            return null;
        }

        Set<Short> ports = new HashSet<Short>(numPayloadItems);
        for (int i = 0; i < numPayloadItems; i++) {
            ports.add(buf.readShort());
        }
        return new PortsResMsg(ports);
    }

}

class BroadcastDomainPortsReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = Short.SIZE / 8;

    public final short appPort;

    public BroadcastDomainPortsReqMsg(short appPort) {
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() +
               BroadcastDomainPortsReqMsg.HEADER_LEN +
               BroadcastDomainPortsReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.BCAST_DOM_PORTS_REQ.ordinal());
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return BroadcastDomainPortsReqMsg.parse(buf);
    }

    @SuppressWarnings("unused")
    public static BroadcastDomainPortsReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < BroadcastDomainPortsReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final short appPort = buf.readShort();
        return new BroadcastDomainPortsReqMsg(appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class BroadcastDomainPortsResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN           = Integer.SIZE / 8;
    private static final int PAYLOAD_PREAMBLE_LEN = Integer.SIZE / 8;
    private static final int PAYLOAD_ITEM_LEN     = (Long.SIZE + Short.SIZE) / 8;

    public final int                payload_len;
    public final Set<NodePortTuple> broadcastDomainPorts;

    public BroadcastDomainPortsResMsg(Set<NodePortTuple> broadcastDomainPorts) {
        this.broadcastDomainPorts = broadcastDomainPorts;
        this.payload_len = broadcastDomainPorts.size() * BroadcastDomainPortsResMsg.PAYLOAD_ITEM_LEN;
    }

    @Override
    public int getLength() {
        return super.getLength() +
               BroadcastDomainPortsResMsg.HEADER_LEN +
               BroadcastDomainPortsResMsg.PAYLOAD_PREAMBLE_LEN +
               this.payload_len;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.BCAST_DOM_PORTS_RES.ordinal());
        buf.writeInt(this.broadcastDomainPorts.size());
        for (NodePortTuple tup : this.broadcastDomainPorts) {
            buf.writeLong(tup.getNodeId());
            buf.writeShort(tup.getPortId());
        }
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return BroadcastDomainPortsResMsg.parse(buf);
    }

    public static BroadcastDomainPortsResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < BroadcastDomainPortsResMsg.PAYLOAD_PREAMBLE_LEN) {
            return null;
        }

        int numPayloadItems = buf.readInt();
        int payload_len = numPayloadItems * BroadcastDomainPortsResMsg.PAYLOAD_ITEM_LEN;

        if (buf.readableBytes() < payload_len) {
            return null;
        }

        Set<NodePortTuple> broadcastDomainPorts = new HashSet<NodePortTuple>(numPayloadItems);
        for (int i = 0; i < numPayloadItems; i++) {
            broadcastDomainPorts.add(new NodePortTuple(buf.readLong(), buf.readShort()));
        }
        return new BroadcastDomainPortsResMsg(broadcastDomainPorts);
    }

}

class TunnelPortsReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = Short.SIZE / 8;

    public final short appPort;

    public TunnelPortsReqMsg(short appPort) {
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + TunnelPortsReqMsg.HEADER_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.TUNNEL_PORTS_REQ.ordinal());
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return TunnelPortsReqMsg.parse(buf);
    }

    @SuppressWarnings("unused")
    public static TunnelPortsReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < TunnelPortsReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final short appPort = buf.readShort();
        return new TunnelPortsReqMsg(appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class TunnelPortsResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN           = Integer.SIZE / 8;
    private static final int PAYLOAD_PREAMBLE_LEN = Integer.SIZE / 8;
    private static final int PAYLOAD_ITEM_LEN     = (Long.SIZE + Short.SIZE) / 8;

    public final int                payload_len;
    public final Set<NodePortTuple> tunnelPorts;

    public TunnelPortsResMsg(Set<NodePortTuple> tunnelPorts) {
        this.tunnelPorts = tunnelPorts;
        this.payload_len = tunnelPorts.size() * TunnelPortsResMsg.PAYLOAD_ITEM_LEN;
    }

    @Override
    public int getLength() {
        return super.getLength() +
               TunnelPortsResMsg.HEADER_LEN +
               TunnelPortsResMsg.PAYLOAD_PREAMBLE_LEN +
               this.payload_len;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.TUNNEL_PORTS_RES.ordinal());
        buf.writeInt(this.tunnelPorts.size());
        for (NodePortTuple tup : this.tunnelPorts) {
            buf.writeLong(tup.getNodeId());
            buf.writeShort(tup.getPortId());
        }
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return TunnelPortsResMsg.parse(buf);
    }

    public static TunnelPortsResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < TunnelPortsResMsg.PAYLOAD_PREAMBLE_LEN) {
            return null;
        }

        int numPayloadItems = buf.readInt();
        int payload_len = numPayloadItems * TunnelPortsResMsg.PAYLOAD_ITEM_LEN;

        if (buf.readableBytes() < payload_len) {
            return null;
        }

        Set<NodePortTuple> tunnelPorts = new HashSet<NodePortTuple>(numPayloadItems);
        for (int i = 0; i < numPayloadItems; i++) {
            tunnelPorts.add(new NodePortTuple(buf.readLong(), buf.readShort()));
        }
        return new TunnelPortsResMsg(tunnelPorts);
    }

}

class BlockedPortsReqMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = Short.SIZE / 8;

    public final short appPort;

    public BlockedPortsReqMsg(short appPort) {
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + BlockedPortsReqMsg.HEADER_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.BLOCKED_PORTS_REQ.ordinal());
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return BlockedPortsReqMsg.parse(buf);
    }

    @SuppressWarnings("unused")
    public static BlockedPortsReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < BlockedPortsReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final short appPort = buf.readShort();
        return new BlockedPortsReqMsg(appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class BlockedPortsResMsg extends TopologyServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN           = Integer.SIZE / 8;
    private static final int PAYLOAD_PREAMBLE_LEN = Integer.SIZE / 8;
    private static final int PAYLOAD_ITEM_LEN     = (Long.SIZE + Short.SIZE) / 8;

    public final int                payload_len;
    public final Set<NodePortTuple> blockedPorts;

    public BlockedPortsResMsg(Set<NodePortTuple> blockedPorts) {
        this.blockedPorts = blockedPorts;
        this.payload_len = blockedPorts.size() * BlockedPortsResMsg.PAYLOAD_ITEM_LEN;
    }

    @Override
    public int getLength() {
        return super.getLength() +
               BlockedPortsResMsg.HEADER_LEN +
               BlockedPortsResMsg.PAYLOAD_PREAMBLE_LEN +
               this.payload_len;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.BLOCKED_PORTS_RES.ordinal());
        buf.writeInt(this.blockedPorts.size());
        for (NodePortTuple tup : this.blockedPorts) {
            buf.writeLong(tup.getNodeId());
            buf.writeShort(tup.getPortId());
        }
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return BlockedPortsResMsg.parse(buf);
    }

    public static BlockedPortsResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < BlockedPortsResMsg.PAYLOAD_PREAMBLE_LEN) {
            return null;
        }

        int numPayloadItems = buf.readInt();
        int payload_len = numPayloadItems * BlockedPortsResMsg.PAYLOAD_ITEM_LEN;

        if (buf.readableBytes() < payload_len) {
            return null;
        }

        Set<NodePortTuple> blockedPorts = new HashSet<NodePortTuple>(numPayloadItems);
        for (int i = 0; i < numPayloadItems; i++) {
            blockedPorts.add(new NodePortTuple(buf.readLong(), buf.readShort()));
        }
        return new BlockedPortsResMsg(blockedPorts);
    }

}
