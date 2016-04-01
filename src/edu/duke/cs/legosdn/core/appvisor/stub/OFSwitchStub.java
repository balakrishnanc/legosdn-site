package edu.duke.cs.legosdn.core.appvisor.stub;

import edu.duke.cs.legosdn.core.Defaults;
import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsgExternalizable;
import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsgType;
import edu.duke.cs.legosdn.core.appvisor.dplane.UnknownMsgTypeException;
import edu.duke.cs.legosdn.core.log.Recorder;
import edu.duke.cs.legosdn.core.util.ByteBufferBackedOutStream;
import net.floodlightcontroller.core.*;
import net.floodlightcontroller.core.internal.Controller;
import net.floodlightcontroller.debugcounter.IDebugCounterService;
import net.floodlightcontroller.threadpool.IThreadPoolService;
import net.floodlightcontroller.util.OrderedCollection;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.nustaq.serialization.FSTObjectOutput;
import org.openflow.protocol.*;
import org.openflow.protocol.statistics.OFDescriptionStatistics;
import org.openflow.protocol.statistics.OFStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Mock switch that dumps whatever the application writes on the data plane.
 */
public class OFSwitchStub extends OFSwitchBase {

    private final static Logger logger = LoggerFactory.getLogger(OFSwitchStub.class);

    private static final File MLOG_FILE =
            new File(String.format("%s/%s-m.log",
                                   Defaults.APP_LOGS_PATH,
                                   OFSwitchStub.class.getCanonicalName()));

    private static Recorder recorder;

    // UDP socket for receiving data plane communication
    private final DatagramChannel channel;

    // Buffer for sending data plane messages
    private ByteBuffer outBuffer;

    // Proxy address
    private static SocketAddress proxyAddr;

    // Flag indicating if a replay is in progress
    private static boolean replayInProgress;

    /**
     * Initialize OFSwitchStub with a switch ID.
     *
     * @param swId datapath ID
     */
    public OFSwitchStub(long swId, DatagramChannel channel, ByteBuffer outBuffer) {
        this.datapathId = swId;
        this.stringId = String.format("%d", this.datapathId);
        this.channel = channel;
        this.outBuffer = outBuffer;
    }

    /**
     * Change proxy's socket address.
     *
     * @param proxyAddr Socket address of proxy
     */
    public static void changeProxyAddr(SocketAddress proxyAddr) {
        OFSwitchStub.proxyAddr = proxyAddr;
    }

    /**
     * Change message logging recorder.
     *
     * @param recorder Instance of Recorder.
     */
    public static void changeRecorder(Recorder recorder) {
        OFSwitchStub.recorder = recorder;
    }

    /**
     * @param replayInProgress If set to True message/event is considered part of a replay
     */
    public static void setReplayInProgress(boolean replayInProgress) {
        OFSwitchStub.replayInProgress = replayInProgress;
    }

    /**
     * Set IFloodlightProviderService for this switch instance
     * Called immediately after instantiation
     *
     * @param controller
     */
    @Override
    public void setFloodlightProvider(Controller controller) {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Set IThreadPoolService for this switch instance
     * Called immediately after instantiation
     *
     * @param threadPool
     */
    @Override
    public void setThreadPoolService(IThreadPoolService threadPool) {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Set debug counter service for per-switch counters
     * Called immediately after instantiation
     *
     * @param debugCounters
     * @throws IDebugCounterService.CounterException
     */
    @Override
    public void setDebugCounterService(IDebugCounterService debugCounters)
            throws IDebugCounterService.CounterException {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Called when OFMessage enters pipeline. Returning true cause the message
     * to be dropped.
     *
     * @param ofm
     * @return
     */
    @Override
    public boolean inputThrottled(OFMessage ofm) {
        // Switch on the stub side is never throttled!
        return false;
    }

    /**
     * Return if the switch is currently overloaded. The definition of
     * overload refers to excessive traffic in the control path, namely
     * a high packet in rate.
     *
     * @return
     */
    @Override
    public boolean isOverloaded() {
        // Switch on the stub side is never overloaded!
        return false;
    }

    /**
     * Write OFMessage to the output stream, subject to switch rate limiting.
     * The message will be handed to the floodlightProvider for possible filtering
     * and processing by message listeners
     *
     * @param msg
     * @param cntx
     * @throws java.io.IOException
     */
    @Override
    public void writeThrottled(OFMessage msg, FloodlightContext cntx) throws IOException {
        if (OFSwitchStub.replayInProgress) {
            return;
        }
        this.write(msg, cntx);
    }

    /**
     * Writes the list of messages to the output stream, subject to rate limiting.
     * The message will be handed to the floodlightProvider for possible filtering
     * and processing by message listeners.
     *
     * @param msglist
     * @param cntx
     * @throws java.io.IOException
     */
    @Override
    public void writeThrottled(List<OFMessage> msglist, FloodlightContext cntx) throws IOException {
        if (OFSwitchStub.replayInProgress) {
            return;
        }
        this.write(msglist, cntx);
    }

    /**
     * Log channel writes.
     *
     * @param t Type of message written to channel
     * @return Instance of ChannelFutureListener
     */
    private static final ChannelFutureListener LogChannelWrite(final OFType t) {
        return new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (logger.isTraceEnabled()) {
                    logger.trace("write> {}", t);
                }
            }
        };
    }

    /**
     * Writes to the OFMessage to the output stream, bypassing rate limiting.
     * The message will be handed to the floodlightProvider for possible filtering
     * and processing by message listeners
     *
     * @param m
     * @param bc
     */
    @Override
    public void write(OFMessage m, FloodlightContext bc) {
        OFSwitchStub.recorder.logOutMsg(this.datapathId, m, MLOG_FILE);

        if (OFSwitchStub.replayInProgress) {
            OFSwitchStub.recorder.logOutMsg(String.format("%s replay? %b",
                                                          m.getType(),
                                                          OFSwitchStub.replayInProgress),
                                            MLOG_FILE);

            return;
        }

        this.outBuffer.clear();
        final FSTObjectOutput out = new FSTObjectOutput(new ByteBufferBackedOutStream(this.outBuffer));

        final DPlaneMsgExternalizable msg;
        switch (m.getType()) {
            case FLOW_MOD:
                msg = new DPlaneMsgExternalizable(DPlaneMsgType.OFFLOW_MOD, this.datapathId, m);
                break;
            case PACKET_OUT:
                msg = new DPlaneMsgExternalizable(DPlaneMsgType.OFPACKET_OUT, this.datapathId, m);
                break;
            case STATS_REQUEST:
                msg = new DPlaneMsgExternalizable(DPlaneMsgType.OFPACKET_OUT, this.datapathId, m);
                break;
            default:
                final String err = String.format("unknown message '%s' written to channel!", m.getType());
                logger.error("write> {}", err);
                throw new UnknownMsgTypeException(err);
        }

        try {
            out.writeObject(msg);
            out.close();

            this.outBuffer.flip();
            final int n = this.channel.send(this.outBuffer, OFSwitchStub.proxyAddr);

            OFSwitchStub.recorder.logOutMsg(String.format("%s #bytes: %d",
                                                          m.getType(), n), MLOG_FILE);

            if (logger.isDebugEnabled()) {
                logger.debug("write> wrote {}; {} bytes", m.getType(), n);
            }
        } catch (IOException e) {
            logger.error("write> failed to serialize and send message {}; {}",
                         msg.getMsgType(), e.getLocalizedMessage());
            // FIXME: Create new data plane exception type for this scenario.
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes the list of messages to the output stream, bypassing rate limiting.
     * The message will be handed to the floodlightProvider for possible filtering
     * and processing by message listeners.
     *
     * @param msglist
     * @param cntx
     */
    @Override
    public void write(List<OFMessage> msglist, FloodlightContext cntx) {
        if (this.replayInProgress) {
            return;
        }
        for (OFMessage msg : msglist) {
            this.write(msg, cntx);
        }
    }

    /**
     * @throws java.io.IOException
     */
    @Override
    public void disconnectOutputStream() {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Returns switch features from features Reply
     *
     * @return
     */
    @Override
    public int getBuffers() {
        return 0;
    }

    @Override
    public int getActions() {
        return 0;
    }

    @Override
    public int getCapabilities() {
        return 0;
    }

    @Override
    public byte getTables() {
        return 0;
    }

    /**
     * @return a copy of the description statistics for this switch
     */
    @Override
    public OFDescriptionStatistics getDescriptionStatistics() {
        return null;
    }

    /**
     * Set the OFFeaturesReply message returned by the switch during initial
     * handshake.
     *
     * @param featuresReply
     */
    @Override
    public void setFeaturesReply(OFFeaturesReply featuresReply) {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Get list of all enabled ports. This will typically be different from
     * the list of ports in the OFFeaturesReply, since that one is a static
     * snapshot of the ports at the time the switch connected to the controller
     * whereas this listenPort list also reflects the listenPort status messages that have
     * been received.
     *
     * @return Unmodifiable list of ports not backed by the underlying collection
     */
    @Override
    public Collection<ImmutablePort> getEnabledPorts() {
        return null;
    }

    /**
     * Get list of the listenPort numbers of all enabled ports. This will typically
     * be different from the list of ports in the OFFeaturesReply, since that
     * one is a static snapshot of the ports at the time the switch connected
     * to the controller whereas this listenPort list also reflects the listenPort status
     * messages that have been received.
     *
     * @return Unmodifiable list of ports not backed by the underlying collection
     */
    @Override
    public Collection<Short> getEnabledPortNumbers() {
        return null;
    }

    /**
     * Retrieve the listenPort object by the listenPort number. The listenPort object
     * is the one that reflects the listenPort status updates that have been
     * received, not the one from the features reply.
     *
     * @param portNumber
     * @return listenPort object
     */
    @Override
    public ImmutablePort getPort(short portNumber) {
        return null;
    }

    /**
     * Retrieve the listenPort object by the listenPort name. The listenPort object
     * is the one that reflects the listenPort status updates that have been
     * received, not the one from the features reply.
     * Port names are case insentive
     *
     * @param portName
     * @return listenPort object
     */
    @Override
    public ImmutablePort getPort(String portName) {
        return null;
    }

    /**
     * Add or modify a switch listenPort.
     * This is called by the core controller
     * code in response to a OFPortStatus message. It should not typically be
     * called by other floodlight applications.
     * <p/>
     * OFPPR_MODIFY and OFPPR_ADD will be treated as equivalent. The OpenFlow
     * spec is not clear on whether portNames are portNumbers are considered
     * authoritative identifiers. We treat portNames <-> portNumber mappings
     * as fixed. If they change, we delete all previous conflicting ports and
     * add all new ports.
     *
     * @param ps the listenPort status message
     * @return the ordered Collection of changes "applied" to the old ports
     * of the switch according to the PortStatus message. A single PortStatus
     * message can result in multiple changes.
     * If portName <-> portNumber mappings have
     * changed, the iteration order ensures that delete events for old
     * conflicting appear before before events adding new ports
     */
    @Override
    public OrderedCollection<PortChangeEvent> processOFPortStatus(OFPortStatus ps) {
        return null;
    }

    /**
     * Get list of all ports. This will typically be different from
     * the list of ports in the OFFeaturesReply, since that one is a static
     * snapshot of the ports at the time the switch connected to the controller
     * whereas this listenPort list also reflects the listenPort status messages that have
     * been received.
     *
     * @return Unmodifiable list of ports
     */
    @Override
    public Collection<ImmutablePort> getPorts() {
        return null;
    }

    /**
     * @param portNumber
     * @return Whether a listenPort is enabled per latest listenPort status message
     * (not configured down nor link down nor in spanning tree blocking state)
     */
    @Override
    public boolean portEnabled(short portNumber) {
        return false;
    }

    /**
     * @param portName@return Whether a listenPort is enabled per latest listenPort status message
     *                        (not configured down nor link down nor in spanning tree blocking state)
     */
    @Override
    public boolean portEnabled(String portName) {
        return false;
    }

    /**
     * Compute the changes that would be required to replace the old ports
     * of this switch with the new ports
     *
     * @param ports new ports to set
     * @return the ordered collection of changes "applied" to the old ports
     * of the switch in order to set them to the new set.
     * If portName <-> portNumber mappings have
     * changed, the iteration order ensures that delete events for old
     * conflicting appear before before events adding new ports
     */
    @Override
    public OrderedCollection<PortChangeEvent> comparePorts(Collection<ImmutablePort> ports) {
        return null;
    }

    /**
     * Replace the ports of this switch with the given ports.
     *
     * @param ports new ports to set
     * @return the ordered collection of changes "applied" to the old ports
     * of the switch in order to set them to the new set.
     * If portName <-> portNumber mappings have
     * changed, the iteration order ensures that delete events for old
     * conflicting appear before before events adding new ports
     */
    @Override
    public OrderedCollection<PortChangeEvent> setPorts(Collection<ImmutablePort> ports) {
        return null;
    }

    /**
     * Returns a Future object that can be used to retrieve the asynchronous
     * OFStatisticsReply when it is available.
     *
     * @param request statistics request
     * @return Future object wrapping OFStatisticsReply
     * @throws java.io.IOException
     */
    @Override
    public Future<List<OFStatistics>> queryStatistics(OFStatisticsRequest request) throws IOException {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Returns a Future object that can be used to retrieve the asynchronous
     * OFStatisticsReply when it is available.
     *
     * @return Future object wrapping OFStatisticsReply
     * @throws java.io.IOException
     */
    @Override
    public Future<OFFeaturesReply> querySwitchFeaturesReply() throws IOException {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Deliver the featuresReply future reply
     *
     * @param reply the reply to deliver
     */
    @Override
    public void deliverOFFeaturesReply(OFMessage reply) {
        throw new RuntimeException("Method not implemented!");
    }

    @Override
    public void cancelFeaturesReply(int transactionId) {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Check if the switch is connected to this controller. Whether a switch
     * is connected is independent of whether the switch is active
     *
     * @return whether the switch is still disconnected
     */
    @Override
    public boolean isConnected() {
        // Switch is never connected to 'this' controller and never in 'MASTER' role.
        return false;
    }

    /**
     * Check if the switch is active. I.e., the switch is connected to this
     * controller and is in master role
     *
     * @return
     */
    @Override
    public boolean isActive() {
        // Switch is never connected to 'this' controller and never in 'MASTER' role.
        return false;
    }

    /**
     * Set whether the switch is connected
     *
     * @param connected whether the switch is connected
     */
    @Override
    public void setConnected(boolean connected) {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Get the current role of the controller for the switch
     *
     * @return the role of the controller
     */
    @Override
    public IFloodlightProviderService.Role getHARole() {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Set switch's HA role to role. The haRoleReplyReceived indicates
     * if a reply was received from the switch (error replies excluded).
     * <p/>
     * If role is null, the switch should close the channel connection.
     *
     * @param role
     */
    @Override
    public void setHARole(IFloodlightProviderService.Role role) {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Deliver the statistics future reply
     *
     * @param reply the reply to deliver
     */
    @Override
    public void deliverStatisticsReply(OFStatisticsReply reply) {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Cancel the statistics reply with the given transaction ID
     *
     * @param transactionId the transaction ID
     */
    @Override
    public void cancelStatisticsReply(int transactionId) {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Cancel all statistics replies
     */
    @Override
    public void cancelAllStatisticsReplies() {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Clear all flowmods on this switch
     */
    @Override
    public void clearAllFlowMods() {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Update broadcast cache
     *
     * @param entry
     * @param port
     * @return true if there is a cache hit
     * false if there is no cache hit.
     */
    @Override
    public boolean updateBroadcastCache(Long entry, Short port) {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Get the portBroadcastCacheHits
     *
     * @return
     */
    @Override
    public Map<Short, Long> getPortBroadcastHits() {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Send a flow statistics request to the switch. This call returns after
     * sending the stats. request to the switch.
     *
     * @param request flow statistics request message
     * @param xid     transaction id, must be obtained by using the getXid() API.
     * @param caller  the caller of the API. receive() callback of this
     *                caller would be called when the reply from the switch is received.
     * @return the transaction id for the message sent to the switch. The
     * transaction id can be used to match the response with the request. Note
     * that the transaction id is unique only within the scope of this switch.
     * @throws java.io.IOException
     */
    @Override
    public void sendStatsQuery(OFStatisticsRequest request, int xid, IOFMessageListener caller)
            throws IOException {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Flush all flows queued for this switch in the current thread.
     * NOTE: The contract is limited to the current thread
     */
    @Override
    public void flush() {
        if (logger.isInfoEnabled()) {
            logger.info("flush: call not implemented");
        }
    }

    /**
     * Set the SwitchProperties based on it's description
     *
     * @param description
     */
    @Override
    public void setSwitchProperties(OFDescriptionStatistics description) {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Return the type of OFPort
     *
     * @param port_num
     * @return
     */
    @Override
    public OFPortType getPortType(short port_num) {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Can the listenPort be turned on without forming a new loop?
     *
     * @param port_num
     * @return
     */
    @Override
    public boolean isFastPort(short port_num) {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Return whether write throttling is enabled on the switch
     */
    @Override
    public boolean isWriteThrottleEnabled() {
        return false;
    }

    /**
     * Set the flow table full flag in the switch
     *
     * @param isFull
     */
    @Override
    public void setTableFull(boolean isFull) {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Set the suggested priority to use when installing access flows in
     * this switch.
     *
     * @param prio
     */
    @Override
    public void setAccessFlowPriority(short prio) {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Set the suggested priority to use when installing core flows in
     * this switch.
     *
     * @param prio
     */
    @Override
    public void setCoreFlowPriority(short prio) {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Get the suggested priority to use when installing access flows in
     * this switch.
     */
    @Override
    public short getAccessFlowPriority() {
        return 0;
    }

    /**
     * Get the suggested priority to use when installing core flows in
     * this switch.
     */
    @Override
    public short getCoreFlowPriority() {
        return 0;
    }

    /**
     * Start this switch driver's sub handshake. This might be a no-op but
     * this method must be called at least once for the switch to be become
     * ready.
     * This method must only be called from the I/O thread
     *
     * @throws net.floodlightcontroller.core.SwitchDriverSubHandshakeAlreadyStarted if the sub-handshake has already
     *                                                                              been started
     */
    @Override
    public void startDriverHandshake() {
        throw new RuntimeException("Method not implemented!");
    }

    /**
     * Check if the sub-handshake for this switch driver has been completed.
     * This method can only be called after startDriverHandshake()
     * <p/>
     * This methods must only be called from the I/O thread
     *
     * @return true if the sub-handshake has been completed. False otherwise
     * @throws net.floodlightcontroller.core.SwitchDriverSubHandshakeNotStarted if startDriverHandshake() has not been
     *                                                                          called yet.
     */
    @Override
    public boolean isDriverHandshakeComplete() {
        return false;
    }

    /**
     * Pass the given OFMessage to the driver as part of this driver's
     * sub-handshake. Must not be called after the handshake has been completed
     * This methods must only be called from the I/O thread
     *
     * @param msg The message that the driver should process
     * @throws net.floodlightcontroller.core.SwitchDriverSubHandshakeCompleted  if isDriverHandshake() returns false
     *                                                                          before this method call
     * @throws net.floodlightcontroller.core.SwitchDriverSubHandshakeNotStarted if startDriverHandshake() has not been
     *                                                                          called yet.
     */
    @Override
    public void processDriverHandshakeMessage(OFMessage msg) {
        throw new RuntimeException("Method not implemented!");
    }
}
