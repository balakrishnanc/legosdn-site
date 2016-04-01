package edu.duke.cs.legosdn.core.netlog;

import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsgExternalizable;
import org.openflow.protocol.OFFlowMod;
import org.openflow.protocol.OFMatch;
import org.openflow.protocol.OFPacketIn;
import org.openflow.protocol.OFPacketOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * XDepGraph represents the network-wide transaction logs.
 */
public class XDepGraph {

    private static final Logger logger = LoggerFactory.getLogger(XDepGraph.class);

    /**
     * DepGraphNode represents the top-level and internal nodes in the network-wide transaction dependency graph.
     * @param <K>
     * @param <V>
     */
    public static final class DepGraphNode<K, V> {

        /* By default, the branching factor at each level is set to start at 64 */
        private static final int DEF_BRANCHING_FACTOR = 64;

        // FIXME: Replace the Map with a Trie.
        private final ConcurrentMap<K, V> level;

        /**
         * Initialize DepGraphNode.
         */
        public DepGraphNode() {
            this.level = new ConcurrentHashMap<K, V>(DEF_BRANCHING_FACTOR);
        }

        /**
         * Retrieve an internal node from the graph using a key.
         *
         * @param key Key for lookup
         * @return Value, if any, associated with the key
         */
        public V lookup(K key) {
            return this.level.get(key);
        }

        /**
         * Update graph with an internal or leaf node.
         *
         * @param key Key associated with the node that is updated
         * @param val New value for update
         * @return
         */
        public synchronized V update(K key, V val) {
            return this.level.put(key, val);
        }

    }

    /**
     * DepGraphLeaf represents that last level in the graph where the transaction history is stored.
     * @param <T>
     */
    public static final class DepGraphLeaf<T> {

        /* By default, transaction history starts with support for tracking 64 different updates on a switch */
        public static final int DEF_HISTORY_SZ = 4096;

        public final Queue<T> log;

        /**
         * Initialize DepGraphLeaf.
         */
        public DepGraphLeaf() {
            this.log = new LinkedBlockingQueue<T>(DEF_HISTORY_SZ);
        }

    }

    /* Root of the network-wide transaction dependency graph */
    private final DepGraphNode<Long, DepGraphNode> root;

    /**
     * Initialize network-wide transaction dependency graph.
     */
    public XDepGraph() {
        this.root = new DepGraphNode<Long, DepGraphNode>();
    }

    /**
     * Prepare for a new transaction or update the graph with a current transaction.
     *
     * @param m Flow attributes
     * @param sw Switch identifier
     * @param nm Network transaction log
     * @return Leaf of the graph where the update landed
     */
    @SuppressWarnings("unchecked")
    private synchronized DepGraphLeaf update(final OFMatch m, final Long sw, final NetLogCommit nm) {
        if (this.root.lookup(sw) == null) {
            this.root.update(sw, new DepGraphNode<Integer, DepGraphNode>());
        }

        final DepGraphNode<Integer, DepGraphNode> srcMap = (DepGraphNode<Integer, DepGraphNode>) this.root.lookup(sw);
        final Integer src = m.getNetworkSource();
        if (srcMap.lookup(src) == null) {
            srcMap.update(src, new DepGraphNode<Integer, DepGraphNode>());
        }

        final DepGraphNode<Integer, DepGraphLeaf> dstMap = (DepGraphNode<Integer, DepGraphLeaf>) srcMap.lookup(src);
        final Integer dst = m.getNetworkDestination();
        if (dstMap.lookup(dst) == null) {
            dstMap.update(dst, new DepGraphLeaf<NetLogCommit>());
        }

        if (nm != null) {
            dstMap.lookup(dst).log.add(nm);
        }
        return dstMap.lookup(dst);
    }

    /**
     * Prepare for a new transaction.
     *
     * @param nm Network transaction log (initiate message)
     * @return Leaf of the graph where the update would finally be stored
     */
    public synchronized DepGraphLeaf update(NetLogInitiate nm) {
        if (logger.isDebugEnabled()) {
            logger.debug("update> NetLogInitiate({})", nm.getMsg().getMsgType());
        }

        OFPacketIn ofPacketIn = (OFPacketIn) nm.getMsg().getMsgPayload();

        OFMatch m = new OFMatch();
        m.loadFromPacket(ofPacketIn.getPacketData(), ofPacketIn.getInPort());

        return this.update(m, nm.getMsg().getSwitchID(), null);
    }

    /**
     * Update the graph with a current transaction.
     *
     * @param nm Network transaction log (commit message)
     * @return Leaf of the graph where the update was stored
     */
    public synchronized DepGraphLeaf update(NetLogCommit nm) {
        if (logger.isDebugEnabled()) {
            logger.debug("update> NetLogCommit({})", nm.getMsg().getMsgType());
        }

        final OFMatch m;

        switch (nm.getMsg().getMsgType()) {
            case OFPACKET_OUT:
                final OFPacketOut ofPacketOut = (OFPacketOut) nm.getMsg().getMsgPayload();
                if (ofPacketOut.getPacketData() == null) {
                    m = null;
                    break;
                }
                m = new OFMatch();
                m.loadFromPacket(ofPacketOut.getPacketData(), ofPacketOut.getInPort());
                break;
            case OFFLOW_MOD:
                final OFFlowMod ofFlowMod = (OFFlowMod) nm.getMsg().getMsgPayload();
                m = ofFlowMod.getMatch();
                break;
            case OFSTATS_REQUEST:
                // TODO: Handle OFSTATS_REQUEST in NetLog.
                m = null;
                break;
            // Inbound messages will not be an input to this method.
            case OFPACKET_IN:
            case OFFLOW_REMOVED:
            case OFSTATS_REPLY:
            case SWITCH_NOTIF:
            case PORT_NOTIF:
            case LINK_NOTIF:
            case LISTENER_CMD:
            default:
                m = null;
                break;
        }

        if (m == null) return null;
        return this.update(m, nm.getMsg().getSwitchID(), nm);
    }

    /**
     * Revert the changes made to the graph by a particular application.
     *
     * @param nm Rollback message containing instances of messages issued by the application.
     * @return List of additional rollbacks that are required to bring the network back to a consistent state.
     */
    @SuppressWarnings("unchecked")
    public synchronized NetLogRollback revert(NetLogRollback nm) {
        final String appId = nm.getAppId();
        final List<DPlaneMsgExternalizable> swUndos = new ArrayList<DPlaneMsgExternalizable>(8);

        for (DPlaneMsgExternalizable msg : nm.getMsgs()) {
            if (logger.isDebugEnabled()) {
                logger.debug("update> NetLogRollback({})", msg.getMsgType());
            }

            switch (msg.getMsgType()) {
                case OFPACKET_OUT: {
                    OFPacketOut ofPacketOut = (OFPacketOut) msg.getMsgPayload();

                    OFMatch m = new OFMatch();
                    m.loadFromPacket(ofPacketOut.getPacketData(), ofPacketOut.getInPort());

                    DepGraphLeaf<NetLogCommit> history =
                            ((DepGraphNode<Integer, DepGraphNode<Integer, DepGraphLeaf<NetLogCommit>>>)
                                    this.root.lookup(msg.getSwitchID()))
                                    .lookup(m.getNetworkSource())
                                    .lookup(m.getNetworkDestination());

                    if (!history.log.peek().getAppId().equals(appId)) {
                        continue;
                    }

                    final NetLogCommit netLogCommit = history.log.poll();
                    swUndos.add(netLogCommit.getMsg());

                    break;
                }
                case OFFLOW_MOD: {
                    OFFlowMod ofFlowMod = (OFFlowMod) msg.getMsgPayload();

                    OFMatch m = ofFlowMod.getMatch();

                    DepGraphLeaf<NetLogCommit> history =
                            ((DepGraphNode<Integer, DepGraphNode<Integer, DepGraphLeaf<NetLogCommit>>>)
                                    this.root.lookup(msg.getSwitchID()))
                                    .lookup(m.getNetworkSource())
                                    .lookup(m.getNetworkDestination());

                    if (!history.log.peek().getAppId().equals(appId)) {
                        continue;
                    }

                    final NetLogCommit netLogCommit = history.log.poll();
                    swUndos.add(netLogCommit.getMsg());

                    break;
                }
                default:
                    break;
            }
        }

        return new NetLogRollback(swUndos, appId);
    }

}
