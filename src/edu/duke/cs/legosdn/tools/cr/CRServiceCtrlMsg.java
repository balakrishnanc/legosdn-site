package edu.duke.cs.legosdn.tools.cr;

import edu.duke.cs.legosdn.tools.cr.proto.CRCtrlMsg;
import org.jboss.netty.buffer.ChannelBuffer;

public interface CRServiceCtrlMsg {

    /**
     * Write message out on the wire.
     *
     * @param buf Buffer to write out the message
     */
    public void writeTo(ChannelBuffer buf);

    /**
     * Read message from the data on the wire.
     *
     * @param buf Buffer containing data stream read from the wire.
     * @return Instance of {@link edu.duke.cs.legosdn.tools.cr.proto.CRCtrlMsg}, if buffer contained sufficient data; otherwise, null.
     */
    public CRCtrlMsg readFrom(ChannelBuffer buf);

    /**
     * @return length of message (in bytes)
     */
    public int getLength();

}
