package edu.duke.cs.legosdn.core.netlog;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * NetLogMsg defines the format of the messages exchanged between NetLog and the AppVisor-Proxy.
 */
public class NetLogMsg implements Externalizable {

    private NetLogMsgType netLogMsgType;
    private NetLogMsgIO   netLogMsg;

    /**
     * Initialize NetLogMsg.
     */
    public NetLogMsg() {
        /* NOTE: Required for the serialization library! */
        this.netLogMsgType = null;
        this.netLogMsg = null;
    }

    /**
     * Initialize NetLogMsg.
     *
     * @param msg NetLogInitiate message.
     */
    public NetLogMsg(NetLogInitiate msg) {
        this.netLogMsgType = NetLogMsgType.NETLOG_INITIATE;
        this.netLogMsg = msg;
    }

    /**
     * Initialize NetLogMsg.
     *
     * @param msg NetLogStatus message.
     */
    public NetLogMsg(NetLogStatus msg) {
        this.netLogMsgType = NetLogMsgType.NETLOG_STATUS;
        this.netLogMsg = msg;
    }

    /**
     * Initialize NetLogMsg.
     *
     * @param msg NetLogCommit message.
     */
    public NetLogMsg(NetLogCommit msg) {
        this.netLogMsgType = NetLogMsgType.NETLOG_COMMIT;
        this.netLogMsg = msg;
    }

    /**
     * Initialize NetLogMsg.
     *
     * @param msg NetLogRollback message.
     */
    public NetLogMsg(NetLogRollback msg) {
        this.netLogMsgType = NetLogMsgType.NETLOG_ROLLBACK;
        this.netLogMsg = msg;
    }

    /**
     * @return NetLogMsg type.
     */
    NetLogMsgType getNetLogMsgType() {
        return netLogMsgType;
    }

    /**
     * @return NetLogCommit or NetLogRollback message.
     */
    NetLogMsgIO getNetLogMsg() {
        return netLogMsg;
    }

    @Override
    public final void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(this.netLogMsgType.ordinal());
        this.netLogMsg.writeTo(out);
    }

    @Override
    public final void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        final int type = in.readInt();
        try {
            this.netLogMsgType = NetLogMsgType.values()[type];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IOException("Unknown NetLogMsg type!");
        }

        switch (netLogMsgType) {
            case NETLOG_INITIATE:
                this.netLogMsg = new NetLogInitiate();
                this.netLogMsg.readFrom(in);
                break;
            case NETLOG_STATUS:
                this.netLogMsg = new NetLogStatus();
                this.netLogMsg.readFrom(in);
                break;
            case NETLOG_COMMIT:
                this.netLogMsg = new NetLogCommit();
                this.netLogMsg.readFrom(in);
                break;
            case NETLOG_ROLLBACK:
                this.netLogMsg = new NetLogRollback();
                this.netLogMsg.readFrom(in);
                break;
            default:
                // NOTE: Control should never reach this point!
                final String err = String.format("Missing handler for NetLog message type '%s'", this.netLogMsgType);
                throw new RuntimeException(err);
        }
    }

}
