package edu.duke.cs.legosdn.core.netlog;

import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsgExternalizable;
import net.floodlightcontroller.core.IOFSwitch;
import org.openflow.protocol.OFFlowMod;
import org.openflow.protocol.OFMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * NetLog provides methods to support network-wide transactions.
 */
public class TransactionMgr {

    private static final Logger logger = LoggerFactory.getLogger(TransactionMgr.class);

    private static boolean disableNetLog;
    private static boolean useLocalNetLog;

    // Transaction ID
    private static final AtomicLong xid;

    static {
        xid = new AtomicLong();
    }

    private static NetLogLocalClient netLogLocalClient;

    static {
        TransactionMgr.netLogLocalClient = null;
    }

    private final NetLogClient netlogClient;

    private boolean active;
    private boolean cancelled;

    // Inbound and outbound buffers
    private List<MessageLog> inbound;
    private List<MessageLog> outbound;

    /**
     * Initialize NetLog.
     * @throws IOException
     */
    public TransactionMgr() throws IOException {
        this.active = false;
        this.cancelled = false;
        this.inbound = new ArrayList<MessageLog>(32);
        this.outbound = new ArrayList<MessageLog>(32);

        if (TransactionMgr.disableNetLog) {
            this.netlogClient = new NetLogDummyClient();
        } else if (TransactionMgr.useLocalNetLog) {
            this.netlogClient = TransactionMgr.getNetLogLocalClient();
        } else {
            this.netlogClient = new NetLogRemoteClient();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("TransactionMgr> initialized");
        }
    }

    /**
     * Create a client to interact with a locally running NetLog.
     *
     * @return Client to interact with local NetLog
     * @throws IOException
     */
    private static synchronized NetLogClient getNetLogLocalClient() {
        if (TransactionMgr.netLogLocalClient == null) {
            TransactionMgr.netLogLocalClient = NetLogLocalClient.getInstance();
        }
        return TransactionMgr.netLogLocalClient;
    }

    /**
     * Enabled or disable use of NetLog.
     *
     * @param disableNetLog if True, NetLog is disabled; otherwise, enabled.
     */
    public static void setDisableNetLog(boolean disableNetLog) {
        TransactionMgr.disableNetLog = disableNetLog;
    }

    /**
     * Enabled or disable local version NetLog (running within AppVisor-Proxy).
     *
     * @param useLocalNetLog if True, NetLog is run locally; otherwise, NetLog is run as a separate component.
     */
    public static void setUseLocalNetLog(boolean useLocalNetLog) {
        TransactionMgr.useLocalNetLog = useLocalNetLog;
    }

    /**
     * Begin transaction.
     */
    public synchronized void begin() {
        this.active = true;
        this.cancelled = false;
    }

    /**
     * @return true, if transaction is active; false, otherwise
     */
    public synchronized boolean isActive() {
        return this.active;
    }

    /**
     * @return true, if transaction is cancelled; false, otherwise
     */
    public synchronized boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * @return true, if transaction is active and not cancelled; false, otherwise
     */
    public synchronized boolean isInProgress() {
        return this.active && !this.cancelled;
    }

    /**
     * Cancel an active transaction.
     */
    public synchronized void cancel() {
        if (!this.active) {
            throw new RuntimeException("Cannot cancel an inactive transaction!");
        }
        this.cancelled = true;
    }

    /**
     * Update current transaction.
     *
     * @param sw Switch
     * @param msg Message
     * @param app Application ID
     * @throws IOException
     */
    public void update(IOFSwitch sw, DPlaneMsgExternalizable msg, String app) throws IOException {
        if (!this.active) {
            throw new RuntimeException("No active transaction to update!");
        }

        final MessageLog messageLog = new MessageLog(sw, msg, app);
        switch (msg.getMsgType()) {
            case OFPACKET_IN:
                this.netlogClient.sendNetLogInitiate(messageLog);
                this.inbound.add(messageLog);
                break;
            case OFFLOW_MOD:
                this.outbound.add(messageLog);
                this.netlogClient.sendNetLogCommit(messageLog);
                break;
            case OFPACKET_OUT:
                this.outbound.add(messageLog);
                this.netlogClient.sendNetLogCommit(messageLog);
                break;
            default:
                break;
        }
    }

    /**
     * Commit transaction.
     */
    public synchronized void commit() throws IOException {
        if (!this.active) {
            throw new RuntimeException("No active transaction to update!");
        }

        // TODO: Can we bundle all commits in one message?
        for (MessageLog messageLog : this.outbound) {
            // FIXME: What if NetLog fails? The IOException thrown does not indicate who is the cause of the issue!
            this.netlogClient.sendNetLogCommit(messageLog);
        }

        this.inbound.clear();
        this.outbound.clear();
        this.active = false;
    }

    /**
     * Rollback transaction.
     */
    public synchronized void rollback() throws IOException {
        if (!this.active) {
            throw new RuntimeException("No active transaction to update!");
        }

        for (MessageLog mLog : this.outbound) {
            final OFMessage msg = TransactionMgr.invert((OFMessage) mLog.msg.getMsgPayload());
            if (msg == null) continue;
            mLog.sw.write(msg, null);
        }

        // FIXME: What if NetLog fails? The IOException thrown does not indicate who is the cause of the issue!
        this.netlogClient.sendNetLogRollback(this.outbound);

        this.inbound.clear();
        this.outbound.clear();
        this.active = false;
    }

    /**
     * Invert an OpenFlow message to undo/revert the effect of the original message.
     *
     * @param msg Original message
     * @return Inverted message to undo the action of the original message
     */
    private static OFMessage invert(OFMessage msg) {
        switch (msg.getType()) {
            case FLOW_MOD:
                return invertFlowMod((OFFlowMod) msg);
            default:
                return null;
        }
    }

    /**
     * Invert a FlowMod message to undo/revert the effect of the original message.
     *
     * @param msg Original FlowMod message
     * @return Inverted FlowMod message to undo the action of the original message
     */
    private static OFMessage invertFlowMod(OFFlowMod msg) {
        switch (msg.getCommand()) {
            case OFFlowMod.OFPFC_ADD:
                msg.setCommand(OFFlowMod.OFPFC_DELETE);
                break;
            case OFFlowMod.OFPFC_DELETE:
                msg.setCommand(OFFlowMod.OFPFC_ADD);
                break;
            case OFFlowMod.OFPFC_MODIFY:
                break;
            default:
                break;
        }
        return msg;
    }

}
