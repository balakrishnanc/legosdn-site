package edu.duke.cs.legosdn.core.appvisor.cplane;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.util.CharsetUtil;

/**
 * Remote endpoint contact information.
 */
public class RemoteEndpt implements Comparable {

    // Process ID of the isolated application endpoint
    public final int pid;

    public final String host;
    // Data plane port
    public final int port;

    // Canonical name of the remote listener class
    public final String name;

    // Unique remote endpoint identifier
    public final String appId;

    // Representing the 'name' and 'host' fields internally as a sequence of bytes
    private byte[] nameBytes;
    private byte[] hostBytes;

    // Flag indicating if application is stateless
    public final boolean isStateless;

    // Length of the fixed part of the message
    private static final int PAYLOAD_FIXED_PART_SZ = (Integer.SIZE / 8) * 5;

    /**
     * Instantiate a new remote endpoint.
     *
     * @param pid Process identifier
     * @param host Host name of the machine where the remote application is running
     * @param port Port number of the data channel listener of the remote application
     * @param name Name of the remote application
     * @param isStateless True, if application is stateless; false, otherwise
     */
    public RemoteEndpt(final int pid, final String host, final int port, final String name,
                       final boolean isStateless) {
        this.pid = pid;
        this.name = name;
        this.host = host;
        this.port = port;
        this.appId = String.format("%s_%d", name, port);
        this.isStateless = isStateless;
    }

    /**
     * Instantiate a new stateful remote endpoint.
     *
     * @param pid Process identifier
     * @param host Host name of the machine where the remote application is running
     * @param port Port number of the data channel listener of the remote application
     * @param name Name of the remote application
     */
    public RemoteEndpt(final int pid, final String host, final int port, final String name) {
        this(pid, host, port, name, false);
    }

    @Override
    public String toString() {
        return String.format("%s@%s:%d", this.name, this.host, this.port);
    }

    @Override
    public int compareTo(Object o) {
        RemoteEndpt that = (RemoteEndpt) o;

        int cmp = 0;
        if (this.pid < that.pid) {
            cmp = -1;
        } else if (this.pid > that.pid) {
            cmp = 1;
        }

        return cmp;
    }

    /**
     * @return length of message (in bytes)
     */
    public int getLength() {
        // Calculate the length of the message (in bytes)
        if (this.nameBytes == null) {
            this.nameBytes = this.name.getBytes(CharsetUtil.UTF_8);
            this.hostBytes = this.host.getBytes(CharsetUtil.UTF_8);
        }

        //           PID: int
        //          port: int
        //   host-length: int
        //   name length: int
        //          host: *
        //          name: *
        // is-stateless?: int
        return RemoteEndpt.PAYLOAD_FIXED_PART_SZ + nameBytes.length + hostBytes.length;
    }

    /**
     * Write message out on the wire.
     *
     * @param buf Buffer to write out the message
     */
    public void writeTo(ChannelBuffer buf) {
        // Wire format:
        // <PID><port><host length><name length><host><name><is-stateless?>
        buf.writeInt(this.pid);
        buf.writeInt(this.port);
        buf.writeInt(this.hostBytes.length);
        buf.writeInt(this.nameBytes.length);
        buf.writeBytes(this.hostBytes);
        buf.writeBytes(this.nameBytes);
        buf.writeInt(this.isStateless ? 1 : 0);
    }

    /**
     * Read message from the data on the wire.
     *
     * @param buf Buffer containing data stream read from the wire.
     * @return Instance of {@link RemoteEndpt},
     * if buffer contained sufficient data; otherwise, null.
     */
    public static RemoteEndpt readFrom(ChannelBuffer buf) {
        if (buf.readableBytes() < RemoteEndpt.PAYLOAD_FIXED_PART_SZ) {
            return null;
        }

        final int pid = buf.readInt();
        final int port = buf.readInt();
        final int hostLen = buf.readInt();
        final int nameLen = buf.readInt();

        if (buf.readableBytes() < hostLen) {
            return null;
        }

        final byte[] hostBytes = new byte[hostLen];
        buf.readBytes(hostBytes);
        final String host = new String(hostBytes, CharsetUtil.UTF_8);

        if (buf.readableBytes() < nameLen) {
            return null;
        }
        final byte[] nameBytes = new byte[nameLen];
        buf.readBytes(nameBytes);
        final String name = new String(nameBytes, CharsetUtil.UTF_8);

        final boolean isStateless = buf.readInt() == 1 ? true : false;

        return new RemoteEndpt(pid, host, port, name, isStateless);
    }

    /**
     * Return a new remote endpoint that is exactly the same as the current instance except for the PID.
     *
     * @param pid New process ID
     * @return Instance of remote endpoint with the new process ID
     */
    public RemoteEndpt changePID(int pid) {
        return new RemoteEndpt(pid, this.host, this.port, this.name, this.isStateless);
    }

}
