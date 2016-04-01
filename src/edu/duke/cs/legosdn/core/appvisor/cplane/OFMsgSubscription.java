package edu.duke.cs.legosdn.core.appvisor.cplane;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.util.CharsetUtil;
import org.openflow.protocol.OFType;

import java.nio.charset.Charset;

/**
 * OFMsgSubscription is used to represent OF-message subscription or cancellation requests.
 */
public class OFMsgSubscription {

    public static enum SubscriptionStatus {
        ADD_SUBSCRIPTION,
        DEL_SUBSCRIPTION;
    }

    // OF-Message type
    public final OFType             msgType;
    // 1: Add subscription; 0: Cancel subscription
    public final SubscriptionStatus status;
    // Application identifier
    public final int                appIdLen;
    public final String             appId;

    // Length (in bytes) of the fixed size fields
    private static final int FIXED_FIELDS_LEN = (Integer.SIZE * 3) / 8;

    /**
     * Initialize OFMsgSubscription with the OF-message type.
     *
     * @param type type of OF-message subscribed to
     * @param appId application identifier
     */
    public OFMsgSubscription(OFType type, String appId) {
        this(type, SubscriptionStatus.ADD_SUBSCRIPTION, appId);
    }

    /**
     * Initialize OFMsgSubscription with the OF-message type and status indicating whether to add/cancel subscription.
     *
     * @param type type of OF-message subscribed to
     * @param status Subscription status
     */
    public OFMsgSubscription(OFType type, SubscriptionStatus status, String appId) {
        this.msgType = type;
        this.status = status;
        this.appId = appId;
        this.appIdLen = this.appId.getBytes(CharsetUtil.UTF_8).length;
    }

    /**
     * @return Message type subscribed to.
     */
    public OFType getMsgType() {
        return this.msgType;
    }

    /**
     * @return Subscription status: addition or cancellation.
     */
    public SubscriptionStatus getStatus() {
        return this.status;
    }

    /**
     * Write message out on the wire.
     *
     * @param buf Buffer to write out the message
     */
    public void writeTo(ChannelBuffer buf) {
        // Wire format:
        // <message type identifier><operation: add/cancel><ID-len><ID>
        buf.writeInt(this.msgType.ordinal());
        buf.writeInt(this.status.ordinal());
        buf.writeInt(this.appIdLen);
        buf.writeBytes(this.appId.getBytes(CharsetUtil.UTF_8));
    }

    /**
     * Read message from the data on the wire.
     *
     * @param buf Buffer containing data stream read from the wire.
     * @return Instance of {@link OFMsgSubscription},
     * if buffer contained sufficient data; otherwise, null.
     * @throws CPlaneMsgException
     */
    public static OFMsgSubscription readFrom(ChannelBuffer buf) throws CPlaneMsgException {
        if (buf.readableBytes() < OFMsgSubscription.FIXED_FIELDS_LEN) {
            return null;
        }

        OFType type;
        int typeOrdinal = buf.readInt();
        try {
            type = OFType.values()[typeOrdinal];
        } catch (ArrayIndexOutOfBoundsException e) {
            String errDesc = String.format("Unknown message type (ordinal: %d)", typeOrdinal);
            throw new CPlaneMsgException(errDesc, e);
        }

        int statusOrdinal = buf.readInt();
        SubscriptionStatus status;
        try {
            status = SubscriptionStatus.values()[statusOrdinal];
        } catch (ArrayIndexOutOfBoundsException e) {
            String errDesc = String.format("Unknown subscription request (ordinal: %d)", statusOrdinal);
            throw new CPlaneMsgException(errDesc, e);
        }

        int appIdLen = buf.readInt();
        if (buf.readableBytes() < appIdLen) {
            return null;
        }

        byte[] appId = new byte[appIdLen];
        buf.readBytes(appId);

        return new OFMsgSubscription(type, status, new String(appId, CharsetUtil.UTF_8));
    }

    /**
     * @return length of message (in bytes)
     */
    public int getLength() {
        return OFMsgSubscription.FIXED_FIELDS_LEN + this.appIdLen;
    }

}
