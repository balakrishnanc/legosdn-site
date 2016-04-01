package edu.duke.cs.legosdn.tools.cr.proto;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * C/R service control message response.
 */
public class ServiceReply extends CRCtrlMsg {

    private static final int PAYLOAD_LEN = Integer.SIZE / 8;

    public enum Status {
        OK,     // Service is OK and can accept actions, or action (checkpoint/restore) was successful
        FAIL,   // Service is not OK and cannot accept actions, or action (checkpoint/restore) was unsuccessful
        ;
    }

    private final Status status;

    /**
     * Initialize ServiceReply with status.
     *
     * @param status Status indicating success or failure of service query.
     */
    public ServiceReply(Status status) {
        super(CRCtrlMsgType.SERVICE_REPLY);
        this.status = status;
    }

    /**
     * @return true, if the status in reply was set to OK; false, otherwise.
     */
    public boolean isOK() {
        return this.status.equals(Status.OK);
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        buf.writeInt(this.status.ordinal());
    }

    @Override
    public ServiceReply readFrom(ChannelBuffer buf) throws CRCtrlMsgFormatError {
        return ServiceReply.parse(buf);
    }

    public static ServiceReply parse(ChannelBuffer buf) {
        if (buf.readableBytes() < ServiceReply.PAYLOAD_LEN) {
            return null;
        }

        Status status;
        int typeOrdinal = buf.readInt();
        try {
            status = Status.values()[typeOrdinal];
        } catch (ArrayIndexOutOfBoundsException e) {
            String errDesc = String.format("Unknown message type (ordinal: %d)", typeOrdinal);
            throw new CRCtrlMsgFormatError(errDesc, e);
        }

        return new ServiceReply(status);
    }

    /**
     * @return length of message (in bytes)
     */
    public int getLength() {
        return super.getLength() + ServiceReply.PAYLOAD_LEN;
    }

}
