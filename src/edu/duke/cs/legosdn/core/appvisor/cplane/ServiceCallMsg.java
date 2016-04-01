package edu.duke.cs.legosdn.core.appvisor.cplane;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * Interface for service call messages.
 */
public interface ServiceCallMsg {

    public static final int HEADER_LEN = Integer.SIZE / 8;

    /**
     * @return type of service
     */
    public ServiceType getServiceType();

    /**
     * @return length of message (in bytes)
     */
    public int getLength();

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
     * @return object reconstructed from the wire
     * if buffer contained sufficient data; otherwise, null.
     */
    public Object readFrom(ChannelBuffer buf);

    /**
     * @return port on which the application listens for data plane messages; 0, for responses.
     */
    public short getAppPort();

}
