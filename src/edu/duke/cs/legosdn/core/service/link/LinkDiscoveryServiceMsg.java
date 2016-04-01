package edu.duke.cs.legosdn.core.service.link;

import edu.duke.cs.legosdn.core.appvisor.cplane.ServiceCallMsg;
import edu.duke.cs.legosdn.core.appvisor.cplane.ServiceType;
import net.floodlightcontroller.linkdiscovery.LinkInfo;
import net.floodlightcontroller.routing.Link;
import org.jboss.netty.buffer.ChannelBuffer;

import java.util.HashMap;
import java.util.Map;

public abstract class LinkDiscoveryServiceMsg implements ServiceCallMsg {

    public static enum CallType {

        TUNNEL_PORT_REQ(TunnelPortReqMsg.class),
        TUNNEL_PORT_RES(TunnelPortResMsg.class),
        LINKS_REQ(LinksReqMsg.class),
        LINKS_RES(LinksResMsg.class),
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
    public ServiceType getServiceType() {
        return ServiceType.LINKDISCOVERY;
    }

    @Override
    public int getLength() {
        return ServiceCallMsg.HEADER_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        buf.writeInt(ServiceType.LINKDISCOVERY.ordinal());
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return LinkDiscoveryServiceMsg.parse(buf);
    }

    public static LinkDiscoveryServiceMsg parse(ChannelBuffer buf) {
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
            case TUNNEL_PORT_REQ:
                return TunnelPortReqMsg.parse(buf);
            case TUNNEL_PORT_RES:
                return TunnelPortResMsg.parse(buf);
            case LINKS_REQ:
                return LinksReqMsg.parse(buf);
            case LINKS_RES:
                return LinksResMsg.parse(buf);
            default:
                throw new RuntimeException(String.format("Unknown call type '%d'", callTypeNum));
        }
    }

    @Override
    public short getAppPort() {
        return 0;
    }

}

class TunnelPortReqMsg extends LinkDiscoveryServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = (Long.SIZE + Short.SIZE*2) / 8;

    public final long  sw;
    public final short port;
    public final short appPort;

    public TunnelPortReqMsg(long sw, short port, short appPort) {
        this.sw = sw;
        this.port = port;
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + TunnelPortReqMsg.HEADER_LEN + TunnelPortReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.TUNNEL_PORT_REQ.ordinal());
        buf.writeLong(this.sw);
        buf.writeShort(this.port);
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return TunnelPortReqMsg.parse(buf);
    }

    public static TunnelPortReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < TunnelPortReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final long sw = buf.readLong();
        final short port = buf.readShort();
        final short appPort = buf.readShort();
        return new TunnelPortReqMsg(sw, port, appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class TunnelPortResMsg extends LinkDiscoveryServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = Byte.SIZE / 8;

    public final boolean isTunnelPort;

    public TunnelPortResMsg(boolean isTunnelPort) {
        this.isTunnelPort = isTunnelPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + TunnelPortResMsg.HEADER_LEN + TunnelPortResMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.TUNNEL_PORT_RES.ordinal());
        buf.writeByte(this.isTunnelPort ? 1 : 0);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return TunnelPortResMsg.parse(buf);
    }

    public static TunnelPortResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < TunnelPortResMsg.PAYLOAD_LEN) {
            return null;
        }

        final boolean isTunnelPort = buf.readByte() == 1;
        return new TunnelPortResMsg(isTunnelPort);
    }

}

class LinksReqMsg extends LinkDiscoveryServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN  = Integer.SIZE / 8;
    private static final int PAYLOAD_LEN = Short.SIZE / 8;

    public final short appPort;

    public LinksReqMsg(short appPort) {
        this.appPort = appPort;
    }

    @Override
    public int getLength() {
        return super.getLength() + LinksReqMsg.HEADER_LEN + LinksReqMsg.PAYLOAD_LEN;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.LINKS_REQ.ordinal());
        buf.writeShort(this.appPort);
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return LinksReqMsg.parse(buf);
    }

    public static LinksReqMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < LinksReqMsg.PAYLOAD_LEN) {
            return null;
        }

        final short appPort = buf.readShort();
        return new LinksReqMsg(appPort);
    }

    @Override
    public short getAppPort() {
        return this.appPort;
    }

}

class LinksResMsg extends LinkDiscoveryServiceMsg implements ServiceCallMsg {

    private static final int HEADER_LEN           = Integer.SIZE / 8;
    private static final int PAYLOAD_PREAMBLE_LEN = Integer.SIZE / 8;
    private static final int PAYLOAD_ITEM_LEN     = (((Long.SIZE + Short.SIZE) * 2) +   // Link
                                                     (Long.SIZE * 3)                    // LinkInfo
                                                    ) / 8;

    public final Map<Link, LinkInfo> links;

    public LinksResMsg(Map<Link, LinkInfo> links) {
        this.links = links;
    }

    @Override
    public int getLength() {
        return super.getLength() +
               LinksResMsg.HEADER_LEN +
               LinksResMsg.PAYLOAD_PREAMBLE_LEN +
               LinksResMsg.PAYLOAD_ITEM_LEN * this.links.size();
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(CallType.LINKS_RES.ordinal());
        buf.writeInt(this.links.size());
        for (Map.Entry<Link, LinkInfo> item : this.links.entrySet()) {
            final Link link = item.getKey();
            buf.writeLong(link.getSrc());
            buf.writeShort(link.getSrcPort());
            buf.writeLong(link.getDst());
            buf.writeShort(link.getDstPort());

            final LinkInfo linkInfo = item.getValue();
            buf.writeLong(linkInfo.getFirstSeenTime() == null ? 0 : linkInfo.getFirstSeenTime());
            buf.writeLong(linkInfo.getUnicastValidTime() == null ? 0 : linkInfo.getUnicastValidTime());
            buf.writeLong(linkInfo.getMulticastValidTime() == null ? 0 : linkInfo.getMulticastValidTime());
        }
    }

    @Override
    public Object readFrom(ChannelBuffer buf) {
        return LinksResMsg.parse(buf);
    }

    public static LinksResMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < LinksResMsg.PAYLOAD_PREAMBLE_LEN) {
            return null;
        }

        final int numLinks = buf.readInt();
        final int payloadLen = numLinks * LinksResMsg.PAYLOAD_ITEM_LEN;

        if (buf.readableBytes() < payloadLen) {
            return null;
        }

        Map<Link, LinkInfo> links = new HashMap<Link, LinkInfo>(numLinks);
        for (int i = 0; i < numLinks; i++) {
            final long src = buf.readLong();
            final short srcPort = buf.readShort();
            final long dst = buf.readLong();
            final short dstPort = buf.readShort();
            final Link link = new Link(src, srcPort, dst, dstPort);

            final long firstSeenTime = buf.readLong();
            final long lastLldpReceivedTime = buf.readLong();
            final long lastBddpReceivedTime = buf.readLong();
            final LinkInfo linkInfo = new LinkInfo(firstSeenTime, lastLldpReceivedTime, lastBddpReceivedTime);

            links.put(link, linkInfo);
        }

        return new LinksResMsg(links);
    }

}
