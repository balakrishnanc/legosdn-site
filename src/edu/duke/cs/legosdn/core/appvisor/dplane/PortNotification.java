package edu.duke.cs.legosdn.core.appvisor.dplane;

import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.ImmutablePort;
import org.jboss.netty.buffer.ChannelBuffer;
import org.openflow.protocol.OFPhysicalPort;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.charset.Charset;

/**
 * Port-related notifications
 */
public class PortNotification implements DPlaneNotification {

    // Hardware address length (in bytes)
    private static int HW_ADDR_LEN = 6;

    // Length of the fixed part of the payload
    public static int MIN_PAYLOAD_LEN = (HW_ADDR_LEN +
                                         (Integer.SIZE + Long.SIZE +
                                          Short.SIZE +
                                          Integer.SIZE +                // Length of port name
                                          Integer.SIZE * 6) / 8);

    private IOFSwitch.PortChangeType type;
    private long             switchId;
    private ImmutablePort    port;

    /**
     * Initialize PortNotification.
     */
    public PortNotification() {
        /* Default constructor; required for deserializers. */
    }

    /**
     * Initialize PortNotification.
     *
     * @param switchId Switch identifier
     * @param port Port number
     * @param type Notification type
     */
    public PortNotification(long switchId, ImmutablePort port, IOFSwitch.PortChangeType type) {
        this.switchId = switchId;
        this.port = port;
        this.type = type;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(this.type.ordinal());
        out.writeLong(this.switchId);
        // OFPhysicalPort
        final OFPhysicalPort ofPhysicalPort = this.port.toOFPhysicalPort();
        // ImmutablePort
        out.writeShort(ofPhysicalPort.getPortNumber());
        DPlaneMsgExternalizable.writeBytes(out, ofPhysicalPort.getHardwareAddress());
        DPlaneMsgExternalizable.writeString(out, ofPhysicalPort.getName());
        out.writeInt(ofPhysicalPort.getConfig());
        out.writeInt(ofPhysicalPort.getState());
        out.writeInt(ofPhysicalPort.getCurrentFeatures());
        out.writeInt(ofPhysicalPort.getAdvertisedFeatures());
        out.writeInt(ofPhysicalPort.getSupportedFeatures());
        out.writeInt(ofPhysicalPort.getPeerFeatures());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.type = IOFSwitch.PortChangeType.values()[in.readInt()];
        this.switchId = in.readLong();

        // OFPhysicalPort
        final OFPhysicalPort ofPhysicalPort = new OFPhysicalPort();
        ofPhysicalPort.setPortNumber(in.readShort());
        final byte[] hardwareAddress = new byte[HW_ADDR_LEN];
        DPlaneMsgExternalizable.readBytes(in, hardwareAddress);
        ofPhysicalPort.setHardwareAddress(hardwareAddress);
        ofPhysicalPort.setName(DPlaneMsgExternalizable.readString(in));
        ofPhysicalPort.setConfig(in.readInt());
        ofPhysicalPort.setState(in.readInt());
        ofPhysicalPort.setCurrentFeatures(in.readInt());
        ofPhysicalPort.setAdvertisedFeatures(in.readInt());
        ofPhysicalPort.setSupportedFeatures(in.readInt());
        ofPhysicalPort.setPeerFeatures(in.readInt());

        // ImmutablePort
        this.port = ImmutablePort.fromOFPhysicalPort(ofPhysicalPort);
    }

    @Override
    public DPlaneMsgType getMsgType() {
        return DPlaneMsgType.PORT_NOTIF;
    }

    @Override
    public long getSwitchId() {
        return this.switchId;
    }

    @Override
    public void setSwitchId(long switchId) {
        this.switchId = switchId;
    }

    /**
     * @return Port change type (instance of PortChangeType)
     * @see net.floodlightcontroller.core.IOFSwitch.PortChangeType
     */
    public IOFSwitch.PortChangeType getPortChangeType() {
        return type;
    }

    /**
     * @return Port (instance of ImmutablePort) associated with the notification
     * @see net.floodlightcontroller.core.ImmutablePort
     */
    public ImmutablePort getPort() {
        return port;
    }

    /**
     * Write instance to an output stream.
     *
     * @param out Netty channel buffer
     */
    public void writeTo(ChannelBuffer out) {
        out.writeInt(this.type.ordinal());
        out.writeLong(this.switchId);
        // OFPhysicalPort
        final OFPhysicalPort ofPhysicalPort = this.port.toOFPhysicalPort();
        // ImmutablePort
        out.writeShort(ofPhysicalPort.getPortNumber());
        DPlaneMsgExternalizable.writeBytes(out, ofPhysicalPort.getHardwareAddress());
        DPlaneMsgExternalizable.writeString(out, ofPhysicalPort.getName());
        out.writeInt(ofPhysicalPort.getConfig());
        out.writeInt(ofPhysicalPort.getState());
        out.writeInt(ofPhysicalPort.getCurrentFeatures());
        out.writeInt(ofPhysicalPort.getAdvertisedFeatures());
        out.writeInt(ofPhysicalPort.getSupportedFeatures());
        out.writeInt(ofPhysicalPort.getPeerFeatures());
    }

    /**
     * Read instance data from an input stream
     *
     * @param in Netty channel buffer
     */
    public void readFrom(ChannelBuffer in) {
        this.type = IOFSwitch.PortChangeType.values()[in.readInt()];
        this.switchId = in.readLong();

        // OFPhysicalPort
        final OFPhysicalPort ofPhysicalPort = new OFPhysicalPort();
        ofPhysicalPort.setPortNumber(in.readShort());
        final byte[] hardwareAddress = new byte[6];
        DPlaneMsgExternalizable.readBytes(in, hardwareAddress);
        ofPhysicalPort.setHardwareAddress(hardwareAddress);
        ofPhysicalPort.setName(DPlaneMsgExternalizable.readString(in));
        ofPhysicalPort.setConfig(in.readInt());
        ofPhysicalPort.setState(in.readInt());
        ofPhysicalPort.setCurrentFeatures(in.readInt());
        ofPhysicalPort.setAdvertisedFeatures(in.readInt());
        ofPhysicalPort.setSupportedFeatures(in.readInt());
        ofPhysicalPort.setPeerFeatures(in.readInt());

        // ImmutablePort
        this.port = ImmutablePort.fromOFPhysicalPort(ofPhysicalPort);
    }

    /**
     * @return Size of message (in bytes)
     */
    public int getLength() {
        final int portNameLength = this.port.getName() == null ? 0 :
                this.port.getName().getBytes(Charset.forName("UTF-8")).length;
        return MIN_PAYLOAD_LEN + portNameLength;
    }

    /**
     * @param dPlaneMsgType Data plane message type
     * @return True, if the message type is a port notification; false, otherwise
     */
    public static boolean isPortNotification(DPlaneMsgType dPlaneMsgType) {
        return dPlaneMsgType == DPlaneMsgType.PORT_NOTIF;
    }

}
