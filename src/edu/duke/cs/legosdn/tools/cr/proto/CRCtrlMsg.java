package edu.duke.cs.legosdn.tools.cr.proto;

import edu.duke.cs.legosdn.tools.cr.CRServiceCtrlMsg;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * Checkpoint and restore service control messages.
 */
public class CRCtrlMsg implements CRServiceCtrlMsg {

    private static final int HEADER_LEN = Integer.SIZE / 8;

    // Message type
    public final CRCtrlMsgType msgType;

    /**
     * Create a C/R service control message.
     *
     * @param type Message type
     */
    public CRCtrlMsg(CRCtrlMsgType type) {
        this.msgType = type;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        // Wire format:
        // <message type identifier>
        buf.writeInt(this.msgType.ordinal());
    }

    @Override
    public CRCtrlMsg readFrom(ChannelBuffer buf) {
        return CRCtrlMsg.parse(buf);
    }

    public static CRCtrlMsg parse(ChannelBuffer buf) {
        if (buf.readableBytes() < CRCtrlMsg.HEADER_LEN) {
            return null;
        }

        buf.markReaderIndex();

        // Identify message type
        CRCtrlMsgType type;
        int typeOrdinal = buf.readInt();
        try {
            type = CRCtrlMsgType.values()[typeOrdinal];
        } catch (ArrayIndexOutOfBoundsException e) {
            String errDesc = String.format("Unknown message type (ordinal: %d)", typeOrdinal);
            throw new CRCtrlMsgFormatError(errDesc, e);
        }

        // Parse message payload
        switch (type) {
            case SERVICE_CHECK:
                return ServiceCheck.parse(buf);
            case REGISTER_PROC:
                return RegisterProcess.parse(buf);
            case CHECKPOINT_PROC:
                return CheckpointProcess.parse(buf);
            case RESTORE_PROC:
                return RestoreProcess.parse(buf);
            case SERVICE_REPLY:
                return ServiceReply.parse(buf);
            default:
                String errDesc = String.format("Unknown message type (ordinal: %d)", type);
                throw new CRCtrlMsgFormatError(errDesc, new RuntimeException());
        }
    }

    @Override
    public int getLength() {
        return CRCtrlMsg.HEADER_LEN;
    }

}
