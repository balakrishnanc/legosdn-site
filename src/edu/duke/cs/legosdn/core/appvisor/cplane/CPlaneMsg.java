
package edu.duke.cs.legosdn.core.appvisor.cplane;

import edu.duke.cs.legosdn.core.service.link.LinkDiscoveryServiceMsg;
import edu.duke.cs.legosdn.core.service.topology.TopologyServiceMsg;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * Top-level message type representing various control plane messages exchanged over the wire.
 */
public class CPlaneMsg {

    // Message type
    public final CPlaneMsgType type;
    // Message payload
    public final Object        payload;

    // Header (message type) length (in bytes)
    private static final int HEADER_LEN = Integer.SIZE / 8;

    /**
     * Create a new control plane message specifying the type and payload.
     *
     * @param msgType    type of control plane message
     * @param msgPayload message payload
     */
    public CPlaneMsg(CPlaneMsgType msgType, Object msgPayload) {
        this.type = msgType;
        this.payload = msgPayload;
    }

    /**
     * Write message out on the wire.
     *
     * @param buf Buffer to write out the message
     */
    public void writeTo(ChannelBuffer buf) {
        // Wire format:
        // <message type identifier><message>
        buf.writeInt(this.type.ordinal());
        switch (type) {
            case EP_REMOTEINFO:
                ((RemoteEndpt) this.payload).writeTo(buf);
                break;
            case OF_ADD_MSGSUB:
            case OF_DEL_MSGSUB:
                ((OFMsgSubscription) this.payload).writeTo(buf);
                break;
            case SERVICE_CALL_MSG:
                ((ServiceCallMsg) this.payload).writeTo(buf);
                break;
        }
    }

    /**
     * Read message from the data on the wire.
     *
     * @param buf Buffer containing data stream read from the wire.
     * @return Instance of {@link CPlaneMsg}, if buffer contained sufficient data; otherwise, null.
     * @throws CPlaneMsgException
     */
    public static CPlaneMsg readFrom(ChannelBuffer buf) throws CPlaneMsgException {
        if (buf.readableBytes() < CPlaneMsg.HEADER_LEN) {
            return null;
        }

        buf.markReaderIndex();

        // Control plane message type
        int typeVal = buf.readInt();
        CPlaneMsgType msgType;

        try {
            msgType = CPlaneMsgType.values()[typeVal];
        } catch (ArrayIndexOutOfBoundsException e) {
            buf.resetReaderIndex();
            String errDesc = String.format("Unknown message type (ordinal: %d)", typeVal);
            throw new CPlaneMsgException(errDesc, e);
        }

        Object msgPayload = null;
        switch (msgType) {
            case EP_REMOTEINFO:
                msgPayload = RemoteEndpt.readFrom(buf);
                break;
            case OF_ADD_MSGSUB:
            case OF_DEL_MSGSUB:
                msgPayload = OFMsgSubscription.readFrom(buf);
                break;
            case SERVICE_CALL_MSG:
                if (buf.readableBytes() < ServiceCallMsg.HEADER_LEN) {
                    break;
                }

                int serviceTypeNum = buf.readInt();
                ServiceType serviceType;
                try {
                    serviceType = ServiceType.values()[serviceTypeNum];
                } catch (ArrayIndexOutOfBoundsException e) {
                    buf.resetReaderIndex();
                    String errDesc = String.format("Unknown service type (ordinal: %d)", serviceTypeNum);
                    throw new CPlaneMsgException(errDesc, e);
                }

                switch (serviceType) {
                    case TOPOLOGY:
                        msgPayload = TopologyServiceMsg.parse(buf);
                        break;
                    case LINKDISCOVERY:
                        msgPayload = LinkDiscoveryServiceMsg.parse(buf);
                        break;
                    default:
                        throw new CPlaneMsgException(String.format("Service '%s' is not supported!", serviceType));
                }
                break;
        }

        if (msgPayload == null) {
            buf.resetReaderIndex();
            return null;
        }

        return new CPlaneMsg(msgType, msgPayload);
    }

    /**
     * @return length of message (in bytes)
     */
    public int getLength() {
        switch (type) {
            case EP_REMOTEINFO:
                return CPlaneMsg.HEADER_LEN + ((RemoteEndpt) this.payload).getLength();
            case OF_ADD_MSGSUB:
            case OF_DEL_MSGSUB:
                return CPlaneMsg.HEADER_LEN + ((OFMsgSubscription) this.payload).getLength();
            case SERVICE_CALL_MSG:
                return CPlaneMsg.HEADER_LEN + ((ServiceCallMsg) this.payload).getLength();
            default:
                // NOTE: Control should never reach here.
                return -1;
        }
    }

}
