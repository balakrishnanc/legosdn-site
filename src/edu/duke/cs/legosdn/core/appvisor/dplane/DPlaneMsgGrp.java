package edu.duke.cs.legosdn.core.appvisor.dplane;

import org.jboss.netty.buffer.ChannelBuffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds a group of data plane messages.
 *
 * @author bala
 */
public class DPlaneMsgGrp {

    // Message type
    public final int             size;
    // Message payload
    public final List<DPlaneMsg> msgs;
    // Message length
    public final int             length;

    /**
     * Create a new data plane message group to bundle together a list of messages.
     *
     * @param size number of messages in the group
     * @param msgs data plane messages in the group
     */
    public DPlaneMsgGrp(int size, List<DPlaneMsg> msgs) {
        this.size = size;
        this.msgs = new ArrayList<DPlaneMsg>(msgs);

        int msgLength = 4;
        for (DPlaneMsg msg : this.msgs) {
            msgLength += msg.getLength();
        }
        this.length = msgLength;
    }

    /**
     * Create a new data plane message group containing a single message.
     *
     * @param msg data plane message bundled in the group
     */
    public DPlaneMsgGrp(DPlaneMsg msg) {
        this(1, new ArrayList<DPlaneMsg>(Collections.singletonList(msg)));
    }

    /**
     * Write message out on the wire.
     *
     * @param buf Buffer to write out the message
     */
    public void writeTo(ChannelBuffer buf) {
        // Wire format:
        // <message type identifier><message-1, message-2, ...>
        buf.writeInt(this.size);
        for (DPlaneMsg msg : this.msgs) {
            msg.writeTo(buf);
        }
    }

    /**
     * Read message from the data on the wire.
     *
     * @param buf Buffer containing data stream read from the wire.
     * @return Instance of {@link DPlaneMsgGrp}, if buffer contained sufficient data; otherwise, null.
     * @throws DPlaneMsgException
     */
    public static DPlaneMsgGrp readFrom(ChannelBuffer buf) throws DPlaneMsgException {
        if (buf.readableBytes() < 4) {
            return null;
        }

        int pos = buf.readerIndex();

        // Total number of messages in the group
        int size = buf.readInt();
        // Number of messages read
        int numRead;

        List<DPlaneMsg> msgs = new ArrayList<DPlaneMsg>();
        for (numRead = 0; numRead < size; numRead++) {
            DPlaneMsg msg = DPlaneMsg.readFrom(buf);
            if (msg == null) {
                break;
            }

            msgs.add(msg);
        }

        if (numRead != size) {
            buf.readerIndex(pos);
            return null;
        }

        return new DPlaneMsgGrp(size, msgs);
    }

}
