package edu.duke.cs.legosdn.core.appvisor.dplane;

import net.floodlightcontroller.linkdiscovery.ILinkDiscovery;
import org.jboss.netty.buffer.ChannelBuffer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

/**
 * Link discovery notifications.
 */
public class LinkDiscoveryNotification implements DPlaneNotification {

    // Length of one link discovery update
    private static int LINK_DISCOVERY_UPDATE_LEN = (Long.SIZE * 2 + Short.SIZE * 2 + Integer.SIZE * 3) / 8;

    // Length of the fixed part of the payload
    public static int MIN_PAYLOAD_LEN = Integer.SIZE / 8;   // Number of link discovery updates

    private List<ILinkDiscovery.LDUpdate> linkDiscoveryUpdates;

    /**
     * Initialize LinkDiscoveryNotification.
     */
    public LinkDiscoveryNotification() {
        /* Default constructor; required for deserializers. */
        this.linkDiscoveryUpdates = new ArrayList<ILinkDiscovery.LDUpdate>(0);
    }

    /**
     * Initialize LinkDiscoveryNotification.
     *
     * @param ldUpdate Link discovery update
     */
    public LinkDiscoveryNotification(ILinkDiscovery.LDUpdate ldUpdate) {
        this.linkDiscoveryUpdates = new ArrayList<ILinkDiscovery.LDUpdate>(1);
        this.linkDiscoveryUpdates.add(ldUpdate);
    }

    /**
     * Initialize LinkDiscoveryNotification.
     *
     * @param ldUpdates List of link discovery updates
     */
    public LinkDiscoveryNotification(List<ILinkDiscovery.LDUpdate> ldUpdates) {
        this.linkDiscoveryUpdates = new ArrayList<ILinkDiscovery.LDUpdate>(ldUpdates.size());
        this.linkDiscoveryUpdates.addAll(ldUpdates);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(this.linkDiscoveryUpdates.size());
        for (ILinkDiscovery.LDUpdate update : this.linkDiscoveryUpdates) {
            out.writeLong(update.getSrc());
            out.writeShort(update.getSrcPort());
            out.writeLong(update.getDst());
            out.writeShort(update.getDstPort());
            // NOTE: Source Switch Type is unused!
            if (update.getSrcType() != null)
                // NOTE: Field is mostly NULL!
                out.writeInt(update.getSrcType().ordinal());
            else
                out.writeInt(ILinkDiscovery.SwitchType.BASIC_SWITCH.ordinal());
            if (update.getType() != null)
                // NOTE: For Switch and Port (Updates - UP/DOWN) link type will be NULL.
                out.writeInt(update.getType().ordinal());
            else
                out.writeInt(ILinkDiscovery.LinkType.INVALID_LINK.ordinal());
            out.writeInt(update.getOperation().ordinal());
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        final int sz = in.readInt();
        this.linkDiscoveryUpdates.clear();
        for (int i = 0; i < sz; i++) {
            // LDUpdate
            final long src = in.readLong();
            final short srcPort = in.readShort();
            final long dst = in.readLong();
            final short dstPort = in.readShort();
            // NOTE: Source Switch Type is unused! Even in FloodLight the field is not used by any class.
            final ILinkDiscovery.SwitchType srcType = ILinkDiscovery.SwitchType.values()[in.readInt()];
            final ILinkDiscovery.LinkType type = ILinkDiscovery.LinkType.values()[in.readInt()];
            final ILinkDiscovery.UpdateOperation operation = ILinkDiscovery.UpdateOperation.values()[in.readInt()];

            final ILinkDiscovery.LDUpdate update =
                    new ILinkDiscovery.LDUpdate(src, srcPort, dst, dstPort, type, operation);
            this.linkDiscoveryUpdates.add(update);
        }
    }

    @Override
    public DPlaneMsgType getMsgType() {
        return DPlaneMsgType.LINK_NOTIF;
    }

    @Override
    public long getSwitchId() {
        return DPlaneMsg.INTERNAL_QUEUE;
    }

    @Override
    public void setSwitchId(long switchId) {
    }

    /**
     * Write instance to an output stream.
     *
     * @param out Netty channel buffer
     */
    public void writeTo(ChannelBuffer out) {
        out.writeInt(this.linkDiscoveryUpdates.size());
        for (ILinkDiscovery.LDUpdate update : this.linkDiscoveryUpdates) {
            out.writeLong(update.getSrc());
            out.writeShort(update.getSrcPort());
            out.writeLong(update.getDst());
            out.writeShort(update.getDstPort());
            // NOTE: Source Switch Type is unused!
            out.writeInt(update.getSrcType().ordinal());
            out.writeInt(update.getType().ordinal());
            out.writeInt(update.getOperation().ordinal());
        }
    }

    /**
     * Read instance data from an input stream
     *
     * @param in Netty channel buffer
     */
    public void readFrom(ChannelBuffer in) {
        final int sz = in.readInt();
        this.linkDiscoveryUpdates.clear();
        for (int i = 0; i < sz; i++) {
            // LDUpdate
            final long src = in.readLong();
            final short srcPort = in.readShort();
            final long dst = in.readLong();
            final short dstPort = in.readShort();
            // NOTE: Source Switch Type is unused! Even in FloodLight the field is not used by any class.
            final ILinkDiscovery.SwitchType srcType = ILinkDiscovery.SwitchType.values()[in.readInt()];
            final ILinkDiscovery.LinkType type = ILinkDiscovery.LinkType.values()[in.readInt()];
            final ILinkDiscovery.UpdateOperation operation = ILinkDiscovery.UpdateOperation.values()[in.readInt()];

            final ILinkDiscovery.LDUpdate update =
                    new ILinkDiscovery.LDUpdate(src, srcPort, dst, dstPort, type, operation);
            this.linkDiscoveryUpdates.add(update);
        }
    }

    /**
     * @return Number of link discovery updates.
     */
    public int getNumLinkDiscoveryUpdates() {
        return this.linkDiscoveryUpdates.size();
    }

    /**
     * @return List of link discovery updates
     */
    public List<ILinkDiscovery.LDUpdate> getLinkDiscoveryUpdates() {
        // FIXME: Unsafe export of a mutable list! Use an immutable collection.
        return linkDiscoveryUpdates;
    }

    /**
     * @return Size of message (in bytes)
     */
    public int getLength() {
        final int numUpdates = this.linkDiscoveryUpdates.size();
        return MIN_PAYLOAD_LEN + (numUpdates * LINK_DISCOVERY_UPDATE_LEN);
    }

    /**
     * @param dPlaneMsgType Data plane message type
     * @return True, if the message type is a link discovery notification; false, otherwise
     */
    public static boolean isLinkDiscoveryNotification(DPlaneMsgType dPlaneMsgType) {
        return dPlaneMsgType == DPlaneMsgType.LINK_NOTIF;
    }

}
