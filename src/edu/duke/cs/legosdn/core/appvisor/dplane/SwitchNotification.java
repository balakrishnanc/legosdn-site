package edu.duke.cs.legosdn.core.appvisor.dplane;

import org.jboss.netty.buffer.ChannelBuffer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Switch-related notifications.
 */
public class SwitchNotification implements DPlaneNotification {

    public static int PAYLOAD_LEN = (Integer.SIZE + Long.SIZE) / 8;

    private NotificationType type;
    private long             switchId;

    /**
     * Initialize SwitchNotification.
     */
    public SwitchNotification() {
        /* Default constructor; required for deserializers. */
    }

    /**
     * Initialize SwitchNotification.
     *
     * @param switchId Switch identifier
     * @param type Notification type
     */
    public SwitchNotification(long switchId, NotificationType type) {
        this.switchId = switchId;
        this.type = type;
    }

    /**
     * Type of switch notification.
     */
    public static enum NotificationType {
        SWITCH_ADDED,       // Switch was discovered by the controller
        SWITCH_ACTIVATED,   // Switch becomes active in controller (in *MASTER* mode)
        SWITCH_REMOVED,     // Switch was disconnected from the controller
        SWITCH_CHANGED,     // Attributes or features of the switch has changed
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(this.type.ordinal());
        out.writeLong(this.switchId);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.type = NotificationType.values()[in.readInt()];
        this.switchId = in.readLong();
    }

    @Override
    public DPlaneMsgType getMsgType() {
        return DPlaneMsgType.SWITCH_NOTIF;
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
     * @return Switch notification type
     */
    public NotificationType getNotifType() {
        return type;
    }

    /**
     * Write instance to an output stream.
     *
     * @param out Netty channel buffer
     */
    public void writeTo(ChannelBuffer out) {
        out.writeInt(this.type.ordinal());
        out.writeLong(this.switchId);
    }

    /**
     * Read instance data from an input stream
     *
     * @param in Netty channel buffer
     */
    public void readFrom(ChannelBuffer in) {
        this.type = NotificationType.values()[in.readInt()];
        this.switchId = in.readLong();
    }

    /**
     * @return Size of message (in bytes)
     */
    public int getLength() {
        return PAYLOAD_LEN;
    }

    /**
     * @param dPlaneMsgType Data plane message type
     * @return True, if the message type is a switch notification; false, otherwise
     */
    public static boolean isSwitchNotification(DPlaneMsgType dPlaneMsgType) {
        return dPlaneMsgType == DPlaneMsgType.SWITCH_NOTIF;
    }

}
