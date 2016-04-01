
package edu.duke.cs.legosdn.core.appvisor.proxy;

import edu.duke.cs.legosdn.core.Defaults;
import edu.duke.cs.legosdn.core.appvisor.cplane.CPlaneMsgDecoder;
import edu.duke.cs.legosdn.core.appvisor.cplane.CPlaneMsgEncoder;
import edu.duke.cs.legosdn.core.appvisor.cplane.CPlaneProxy;
import edu.duke.cs.legosdn.core.appvisor.cplane.RemoteEndpt;
import edu.duke.cs.legosdn.core.appvisor.dplane.*;
import edu.duke.cs.legosdn.core.faults.IEventTransformer;
import edu.duke.cs.legosdn.core.log.FileRecorder;
import edu.duke.cs.legosdn.core.log.NullRecorder;
import edu.duke.cs.legosdn.core.log.Recorder;
import edu.duke.cs.legosdn.core.netlog.TransactionMgr;
import edu.duke.cs.legosdn.core.util.ByteBufferBackedOutStream;
import edu.duke.cs.legosdn.tools.cr.CRClientProvider;
import edu.duke.cs.legosdn.tools.cr.cli.CRClientFactory;
import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.nustaq.serialization.util.FSTInputStream;
import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Registry of remote listener endpoints.
 * <p/>
 * NOTE: Floodlight ships with an old netty-3.x library!
 */
public class RemoteRegistry implements CPlaneProxy {

    private static final Logger logger = LoggerFactory.getLogger(RemoteRegistry.class);

    private static final File DEV_NULL = new File("/dev/null");

    private static final File WLOG_FILE =
            new File(String.format("%s/%s.log",
                                   Defaults.APP_LOGS_PATH,
                                   RemoteRegistry.class.getCanonicalName()));

    // Exit code when halting on C/R failures
    private static final int CR_FAILURE_EXIT = 2;

    private Recorder recorder;

    private final IFloodlightProviderService floodlightProvider;

    private final ServerBootstrap cplaneBootstrap;

    // Expected number of applications and switches for priming data structures, to reduce start up costs.
    private static final int DEFAULT_NUM_APPS     = 8;
    private static final int DEFAULT_NUM_SWITCHES = 64;

    // List of active isolated applications
    protected final ConcurrentMap<String, RemoteEndpt> activeApps;
    protected final ConcurrentMap<Short, String>       appPorts;

    // Applications and associated set of message subscriptions
    protected final ConcurrentMap<String, Set<OFType>>                   appsSubscriptions;
    // Buffer of messages received between checkpoints
    protected final ConcurrentMap<String, List<DPlaneMsgExternalizable>> appReplayBuffers;

    // Backup of expired applications' subscriptions
    protected final ConcurrentMap<String, Set<OFType>>                   appsSubscriptionsBackup;
    // Backup of expired applications' replay buffers
    protected final ConcurrentMap<String, List<DPlaneMsgExternalizable>> appReplayBuffersBackup;

    // Socket addresses of applications
    // NOTE: Thread-safety is not a requirement!
    protected final Map<String, InetSocketAddress> appAddrBook;

    // Application numbers
    // NOTE: Thread-safety is not a requirement!
    protected final Map<String, Integer> appNum;
    protected final AtomicInteger        appCounter;

    // Datagram channels to communicate with the remote applications (one channel per application)
    // NOTE: Threads will need to synchronize access to this data structure.
    protected final Map<String, DatagramChannel> appChannels;

    // Application specific locks; to synchronize threads accessing resources associated with the same application.
    protected final Map<String, Object> appLocks;
    // Application registration locks; to synchronize threads attempting to restore or restart applications
    protected final Map<String, Object> appRegLocks;

    // Periodic task to check on the health of remote applications
    protected final Timer appHealthChecker;

    // Event transformer implementation
    protected IEventTransformer transformer;

    // Allow inversion of transforms?
    protected boolean invertTransforms;

    // Per application message counters
    protected final Map<String, AtomicLong>                     appInMsgCounters;
    protected final Map<String, Map<DPlaneMsgType, AtomicLong>> appMsgTypeCounters;
    protected final Map<String, AtomicLong>                     appOutMsgCounters;

    // Switch specific locks; to synchronize threads accessing resources associated with the same switch
    protected final Map<Long, Object> swLocks;

    // NIO channel buffers for use with the datagram channels (two buffers per switch connection)
    // NOTE: Switch can reuse the buffers when sending the same message to multiple applications.
    protected final Map<Long, ByteBuffer> swInBuffers;
    protected final Map<Long, ByteBuffer> swOutBuffers;

    // Mapping of switch identifiers to switch objects
    // NOTE: This allows to write the received responses from the application to the proper switch.
    protected final ConcurrentMap<Long, IOFSwitch> switchBoard;

    // Transaction managers (one per switch connection)
    // NOTE: Transaction manager takes care of handling the transactions between the switch and the applications
    protected final ConcurrentMap<Long, TransactionMgr> switchTxnMgr;

    // Flag indicating whether replay (after recovery is disabled)
    protected boolean disableReplay;

    // Flag indicating whether the checkpoint and restore functionality is enabled
    protected boolean          crEnabled;
    // Checkpoint and restore client implementation
    protected CRClientProvider crClient;
    // Checkpoint frequency (one checkpoint per some 'N' messages per application)
    protected int              chkptFreq;
    // Halt on checkpoint/restore failures?
    protected boolean          haltOnCRFailure;

    /* Average time spent in connection setup */
    private final AtomicLong aggSetupTime;
    private final AtomicLong aggTeardownTime;
    private final AtomicLong numConns;

    /* Average time spent in message transfers */
    private final AtomicLong aggMsgXferTime;
    private final AtomicLong numMsgXfers;

    // Faults and recovery counters per message type
    // Fault counters are incremented only for faults induced on the original inbound message
    private final Map<String, AtomicLong> faultCounters;
    private final Map<String, AtomicLong> recoveryCounters;
    private final Map<String, AtomicLong> transformCounters;

    // Restart timings
    private final Map<String, ConcurrentLinkedQueue<Long>> appRestartTimes;
    // Application restore timings
    private final Map<String, ConcurrentLinkedQueue<Long>> appRestoreTimes;
    // Application replay timings
    private final Map<String, ConcurrentLinkedQueue<Long>> appReplayTimes;
    // Checkpoint recovery timings
    private final Map<String, ConcurrentLinkedQueue<Long>> chkptRecoveryTimes;
    // Crash detect timings
    private final Map<String, ConcurrentLinkedQueue<Long>> crashDetectTimes;

    /**
     * Instantiate a new server.
     *
     * @return new server instance
     */
    private static ServerBootstrap createServer() {
        // Threads to process control (registration/de-registration) messages from isolated applications.
        return new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newFixedThreadPool(2),
                                                                     Executors.newFixedThreadPool(4),
                                                                     4));
    }

    /**
     * Initialize RemoteRegistry.
     *
     * @param moduleContext FloodLight module context
     * @param serviceRegistry Service proxy registry
     */
    @SuppressWarnings("unused")
    public RemoteRegistry(FloodlightModuleContext moduleContext, final ServiceRegistry serviceRegistry)
            throws IOException {
        this.floodlightProvider = moduleContext.getServiceImpl(IFloodlightProviderService.class);

        this.activeApps = new ConcurrentHashMap<String, RemoteEndpt>(DEFAULT_NUM_APPS);
        this.appPorts = new ConcurrentHashMap<Short, String>(DEFAULT_NUM_APPS);
        this.appsSubscriptions = new ConcurrentHashMap<String, Set<OFType>>(DEFAULT_NUM_APPS);
        this.appsSubscriptionsBackup = new ConcurrentHashMap<String, Set<OFType>>(DEFAULT_NUM_APPS);
        this.appReplayBuffers = new ConcurrentHashMap<String, List<DPlaneMsgExternalizable>>(DEFAULT_NUM_APPS);
        this.appReplayBuffersBackup = new ConcurrentHashMap<String, List<DPlaneMsgExternalizable>>(DEFAULT_NUM_APPS);
        this.appAddrBook = new HashMap<String, InetSocketAddress>(DEFAULT_NUM_APPS);
        this.appNum = new HashMap<String, Integer>(DEFAULT_NUM_APPS);
        this.appCounter = new AtomicInteger(0);
        this.appChannels = new HashMap<String, DatagramChannel>(DEFAULT_NUM_APPS);
        this.appLocks = new HashMap<String, Object>(DEFAULT_NUM_APPS);
        this.appRegLocks = new HashMap<String, Object>(DEFAULT_NUM_APPS);

        this.appHealthChecker = new Timer();
        this.startAppHealthChecker();

        this.appInMsgCounters = new HashMap<String, AtomicLong>(DEFAULT_NUM_APPS);
        this.appMsgTypeCounters = new HashMap<String, Map<DPlaneMsgType, AtomicLong>>(DEFAULT_NUM_APPS);
        this.appOutMsgCounters = new HashMap<String, AtomicLong>(DEFAULT_NUM_APPS);

        this.switchBoard = new ConcurrentHashMap<Long, IOFSwitch>(DEFAULT_NUM_SWITCHES);
        this.switchTxnMgr = new ConcurrentHashMap<Long, TransactionMgr>(DEFAULT_NUM_SWITCHES);
        this.swLocks = new HashMap<Long, Object>(DEFAULT_NUM_SWITCHES);
        this.swInBuffers = new HashMap<Long, ByteBuffer>(DEFAULT_NUM_SWITCHES);
        this.swOutBuffers = new HashMap<Long, ByteBuffer>(DEFAULT_NUM_SWITCHES);

        this.crClient = null;
        this.crEnabled = false;
        this.chkptFreq = Defaults.APP_CHKPT_FREQ;

        this.cplaneBootstrap = RemoteRegistry.createServer();
        this.configureCPlaneServer(serviceRegistry);

        this.aggSetupTime = new AtomicLong(0);
        this.aggTeardownTime = new AtomicLong(0);
        this.numConns = new AtomicLong(0);

        this.aggMsgXferTime = new AtomicLong(0);
        this.numMsgXfers = new AtomicLong(0);

        this.faultCounters = new HashMap<String, AtomicLong>(DPlaneMsgType.values().length);
        this.recoveryCounters = new HashMap<String, AtomicLong>(DPlaneMsgType.values().length);
        this.transformCounters = new HashMap<String, AtomicLong>(DPlaneMsgType.values().length);

        this.appRestartTimes = new HashMap<String, ConcurrentLinkedQueue<Long>>(DEFAULT_NUM_APPS);
        this.appRestoreTimes = new HashMap<String, ConcurrentLinkedQueue<Long>>(DEFAULT_NUM_APPS);
        this.appReplayTimes = new HashMap<String, ConcurrentLinkedQueue<Long>>(DEFAULT_NUM_APPS);
        this.chkptRecoveryTimes = new HashMap<String, ConcurrentLinkedQueue<Long>>(DEFAULT_NUM_APPS);
        this.crashDetectTimes = new HashMap<String, ConcurrentLinkedQueue<Long>>(DEFAULT_NUM_APPS);

        this.initCounters();

        if (logger.isInfoEnabled()) {
            logger.info("RemoteRegistry> initialized.");
        }
    }

    /**
     * Enable/disable message logging to file.
     *
     * @param enableMLog If true message logging to file is enabled; disabled, otherwise
     */
    public void setMsgLogging(boolean enableMLog) {
        if (enableMLog)
            this.recorder = FileRecorder.getInstance();
        else
            this.recorder = NullRecorder.getInstance();
    }

    /**
     * Enable or disable transformation inversion.
     *
     * @param invertTransforms True, to allow inversion of transforms; false, otherwise
     */
    public void setInvertTransforms(boolean invertTransforms) {
        this.invertTransforms = invertTransforms;
    }

    /**
     * Initialize fault and recovery counters.
     */
    private void initCounters() {
        for (DPlaneMsgType dPlaneMsgType : DPlaneMsgType.values()) {
            if (!DPlaneMsgType.isNotification(dPlaneMsgType)) {
                this.faultCounters.put(dPlaneMsgType.toString(), new AtomicLong(0L));
                this.recoveryCounters.put(dPlaneMsgType.toString(), new AtomicLong(0L));
                this.transformCounters.put(dPlaneMsgType.toString(), new AtomicLong(0L));
            } else {
                switch (dPlaneMsgType) {
                    case SWITCH_NOTIF:
                        for (SwitchNotification.NotificationType notifType :
                                SwitchNotification.NotificationType.values()) {
                            String typeStr = String.format("%s.%s", dPlaneMsgType.toString(), notifType.toString());
                            this.faultCounters.put(typeStr, new AtomicLong(0L));
                            this.recoveryCounters.put(typeStr, new AtomicLong(0L));
                            this.transformCounters.put(typeStr, new AtomicLong(0L));
                        }
                        break;
                    case PORT_NOTIF:
                        for (IOFSwitch.PortChangeType notifType:
                                IOFSwitch.PortChangeType.values()) {
                            String typeStr = String.format("%s.%s", dPlaneMsgType.toString(), notifType.toString());
                            this.faultCounters.put(typeStr, new AtomicLong(0L));
                            this.recoveryCounters.put(typeStr, new AtomicLong(0L));
                            this.transformCounters.put(typeStr, new AtomicLong(0L));
                        }
                        break;
                    default:
                        this.faultCounters.put(dPlaneMsgType.toString(), new AtomicLong(0L));
                        this.recoveryCounters.put(dPlaneMsgType.toString(), new AtomicLong(0L));
                        this.transformCounters.put(dPlaneMsgType.toString(), new AtomicLong(0L));
                }
            }
        }
    }

    /**
     * Register a transformer.
     *
     * @param transformer Transformation module
     */
    public void registerTransformer(IEventTransformer transformer) {
        this.transformer = transformer;
    }

    /**
     * Configure control plane listener.
     *
     * @param serviceRegistry Service proxy registry
     */
    private void configureCPlaneServer(final ServiceRegistry serviceRegistry) {
        final CPlaneProxy cplaneProxy = this;

        final CPlaneMsgEncoder cplaneMsgEncoder = new CPlaneMsgEncoder();
        final CPlaneMsgDecoder cplaneMsgDecoder = new CPlaneMsgDecoder();

        this.cplaneBootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            @Override
            public ChannelPipeline getPipeline() {
                return Channels.pipeline(cplaneMsgEncoder,
                                         cplaneMsgDecoder,
                                         new CPlaneHandler(cplaneProxy, serviceRegistry));
            }

        });
        this.cplaneBootstrap.setOption("reuseAddress", true);
        this.cplaneBootstrap.setOption("child.keepAlive", false);
        this.cplaneBootstrap.setOption("child.tcpNoDelay", true);
        this.cplaneBootstrap.setOption("child.sendBufferSize", Defaults.CHANNEL_BUF_SZ);
    }

    /**
     * @param host Host name of the machine where the application is running
     * @param port Application data plane listen port
     * @return Generate a string that uniquely identifies the endpoint.
     */
    public static String endptInfo(final String host, final int port) {
        return String.format("%s:%d", host, port);
    }

    /**
     * Initialize C/R service.
     *
     * @return true, if the initialization was successful; false, otherwise
     */
    private static boolean initCRService(CRClientProvider crClient) {
        final boolean isConnected = crClient.connect();
        if (isConnected) {
            if (logger.isInfoEnabled()) {
                logger.info("initCRService> connected to C/R service.");
            }
            crClient.disconnect();
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn("initCRService> Failed to connect to C/R service. C/R feature is disabled!");
            }
            return false;
        }

        if (crClient.isServiceReady()) {
            if (logger.isInfoEnabled()) {
                logger.info("initCRService> C/R feature is enabled!");
            }
            return true;
        }

        if (logger.isWarnEnabled()) {
            logger.warn("initCRService> C/R service is not ready. C/R feature is disabled!");
        }

        crClient.disconnect();

        return false;
    }

    /**
     * Set checkpoint frequency.
     *
     * @param chkptFreq Number of messages per application for each checkpoint
     */
    public void setChkptFreq(int chkptFreq) {
        this.chkptFreq = chkptFreq;

        if (this.chkptFreq <= 0) {
            this.crEnabled = false;

            if (logger.isWarnEnabled()) {
                logger.warn("setChkptFreq> checkpointing disabled!");
            }

            return;
        }

        this.crEnabled = true;

        this.crClient = CRClientFactory.getInstance();
        this.crEnabled = RemoteRegistry.initCRService(this.crClient);

        if (logger.isInfoEnabled()) {
            logger.info("Configured to checkpoint applications for every {} message(s)", this.chkptFreq);
        }
    }

    /**
     * Enable/disable replay.
     *
     * @param disableReplay If true, replay is disabled; otherwise, replay is enabled.
     */
    public void setDisableReplay(boolean disableReplay) {
        this.disableReplay = disableReplay;

        if (this.disableReplay) {
            if (logger.isWarnEnabled()) {
                logger.warn("setChkptFreq> replay of messages (after recovery) is disabled!");
            }
        }
    }

    /**
     * Set whether controller should be halted on checkpoint/restore failures.
     *
     * @param haltOnCRFailure If true, controller is halted on checkpoint/restore failures; false, otherwise
     */
    public void setHaltOnCRFailure(boolean haltOnCRFailure) {
        this.haltOnCRFailure = haltOnCRFailure;
    }

    /**
     * Start the remote registry.
     */
    public void start() {
        InetSocketAddress sa = new InetSocketAddress(Defaults.PROXY_PORT);
        new DefaultChannelGroup().add(this.cplaneBootstrap.bind(sa));

        if (logger.isInfoEnabled()) {
            logger.info("Registry for remote isolated applications listening on {}", sa);
        }
    }

    /**
     * Stop the remote registry.
     */
    public void stop() {
        this.stopAppHealthChecker();

        if (logger.isInfoEnabled()) {
            logger.info("Registry for remote isolated applications is shut down");
        }
    }

    /**
     * Check if process belonging to the given PID is alive via the proc filesystem.
     *
     * @param pid Process identifier
     * @return True, if process is alive; False, otherwise
     */
    private static boolean isPIDAlive(int pid) {
        return new File(String.format("/proc/%d", pid)).exists();
    }

    /**
     * Initialize a timer task to periodically check whether the remote applications are alive.
     */
    public void startAppHealthChecker() {
        final TimerTask appHealthCheckTask = new TimerTask() {

            @Override
            public void run() {
                for (Map.Entry<String, RemoteEndpt> activeApp : activeApps.entrySet()) {
                    synchronized (appRegLocks.get(activeApp.getKey())) {
                        // Retrieve the application metadata again inside the critical region!
                        final RemoteEndpt re = activeApps.get(activeApp.getKey());

                        if (RemoteRegistry.isPIDAlive(re.pid)) {
                            continue;
                        }
                        recoverApp(re, System.currentTimeMillis());

                        if (logger.isInfoEnabled()) {
                            logger.info("AppHealthChecker> dealt with the death of {}", re.appId);
                        }
                    }
                }
            }

        };

        this.appHealthChecker.scheduleAtFixedRate(appHealthCheckTask,
                                                  Defaults.APP_HEALTH_CHECK_INTVAL,
                                                  Defaults.APP_HEALTH_CHECK_INTVAL);
    }

    /**
     * Stop the period application health checker.
     */
    public void stopAppHealthChecker() {
        this.appHealthChecker.cancel();
    }

    /**
     * Dump some basic statistics gathered on the current run.
     */
    protected final void dumpRunInfo() {
        final StringBuilder buf = new StringBuilder();
        buf.append("> \n");
        buf.append("> -----[ Details on the current run ]-----\n");
        buf.append(String.format(">    Number of connections: %d\n", this.numConns.longValue()));
        buf.append(String.format(">       Average setup time: %.2fms\n",
                                 this.aggSetupTime.floatValue() / this.numConns.longValue()));
        buf.append(String.format(">    Average teardown time: %.2fms\n",
                                 this.aggTeardownTime.floatValue() / this.numConns.longValue()));
        buf.append(String.format(">       Number of messages: %d\n", this.numMsgXfers.longValue()));
        buf.append(String.format(">    Average transfer time: %.2fms\n",
                                 this.aggMsgXferTime.floatValue() / this.numMsgXfers.longValue()));
        buf.append("> ------------------------------\n");
        buf.append("> \n");

        System.out.println(buf.toString());
    }

    /**
     * Writer counters to file.
     *
     * @param fileName Absolute path to output file
     * @param counters Counters
     */
    private void writeCountersToFile(String fileName, Map<String, AtomicLong> counters) {
        final String filePath = String.format("%s/%s", Defaults.COUNTERS_PATH, fileName);
        final File counterFile = new File(filePath);
        FileWriter writer = null;
        try {
            writer = new FileWriter(counterFile);

            for (Map.Entry<String, AtomicLong> entry : counters.entrySet()) {
                writer.write(String.format("%s %d\n", entry.getKey(), entry.getValue().longValue()));
            }
        } catch (IOException e) {
            System.err.printf("Failed to write counters! %s\n", e.getLocalizedMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    /* Ignore error! */
                }
            }
        }
    }

    /**
     * Writer counters per message type to file.
     *
     * @param fileName Absolute path to output file
     * @param counters Counters
     */
    private void writeTypeCountersToFile(String fileName, Map<String, Map<DPlaneMsgType, AtomicLong>> counters) {
        final String filePath = String.format("%s/%s", Defaults.COUNTERS_PATH, fileName);
        final File counterFile = new File(filePath);
        FileWriter writer = null;
        try {
            writer = new FileWriter(counterFile);

            for (Map.Entry<String, Map<DPlaneMsgType, AtomicLong>> appCounter : counters.entrySet()) {
                for (Map.Entry<DPlaneMsgType, AtomicLong> msgTypeCounter : appCounter.getValue().entrySet()) {
                    writer.write(String.format("%s %s %d\n",
                                               appCounter.getKey(),
                                               msgTypeCounter.getKey().toString(),
                                               msgTypeCounter.getValue().longValue()));
                }
            }
        } catch (IOException e) {
            System.err.printf("Failed to write counters! %s\n", e.getLocalizedMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    /* Ignore error! */
                }
            }
        }
    }

    /**
     * Write recovery times to file.
     *
     * @param fileName Absolute path to output file
     * @param times Recovery times for different applications
     */
    private void writeTimersToFile(String fileName, Map<String, ConcurrentLinkedQueue<Long>> times) {
        final String filePath = String.format("%s/%s", Defaults.TIMERS_PATH, fileName);
        final File outFile = new File(filePath);
        FileWriter writer = null;
        try {
            writer = new FileWriter(outFile);

            for (Map.Entry<String, ConcurrentLinkedQueue<Long>> entry : times.entrySet()) {
                for (Long t : entry.getValue()) {
                    writer.write(String.format("%s %d\n", entry.getKey(), t));
                }
            }
        } catch (IOException e) {
            System.err.printf("Failed to write recovery times! %s\n", e.getLocalizedMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    /* Ignore error! */
                }
            }
        }
    }

    /**
     * Cleanup.
     */
    public void cleanup() {
        this.stop();
        this.dumpRunInfo();
        if (this.crEnabled) {
            this.crClient.disconnect();
        }
        this.writeCountersToFile("fault-counters.txt", this.faultCounters);
        this.writeCountersToFile("recovery-counters.txt", this.recoveryCounters);
        this.writeCountersToFile("transform-counters.txt", this.transformCounters);
        this.writeCountersToFile("inbound-per-app-counters.txt", this.appInMsgCounters);
        this.writeTypeCountersToFile("inbound-per-app-mtype-counters.txt", this.appMsgTypeCounters);
        this.writeCountersToFile("outbound-per-app-counters.txt", this.appOutMsgCounters);
        this.writeTimersToFile("app-restart-times.txt", this.appRestartTimes);
        this.writeTimersToFile("app-restore-times.txt", this.appRestoreTimes);
        this.writeTimersToFile("app-replay-times.txt", this.appReplayTimes);
        this.writeTimersToFile("app-recovery-times.txt", this.chkptRecoveryTimes);
        this.writeTimersToFile("app-detect-times.txt", this.crashDetectTimes);
    }

    /**
     * Cleanup on a deferred thread.
     *
     */
    public void deferCleanup() {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                cleanup();
            }

        });
    }

    /**
     * Register remote endpoint.
     *
     * @param re Remote endpoint
     * @throws IOException
     */
    private void registerEndpoint(RemoteEndpt re) throws IOException {
        // NOTE: Threads must ensure to hold an application specific lock prior to calling unregisterEndpoint!

        final String appId = re.appId;

        this.appChannels.put(appId, this.createDPlaneChannel());
        this.activeApps.putIfAbsent(appId, re);
        this.appPorts.putIfAbsent((short) re.port, appId);
        this.appsSubscriptions.putIfAbsent(appId, new HashSet<OFType>());
        // Capacity of replay buffers will depend on the checkpoint frequency
        this.appReplayBuffers.putIfAbsent(appId, new ArrayList<DPlaneMsgExternalizable>(this.chkptFreq));
        this.appAddrBook.put(appId, new InetSocketAddress(InetAddress.getLoopbackAddress(), re.port));
    }

    /**
     * Initialize application specific timers.
     * NOTE: Hold application specific lock before calling this function.
     *
     * @param appId Application ID
     */
    private void initAppTimers(String appId) {
        this.appRestartTimes.put(appId, new ConcurrentLinkedQueue<Long>());
        this.appRestoreTimes.put(appId, new ConcurrentLinkedQueue<Long>());
        this.appReplayTimes.put(appId, new ConcurrentLinkedQueue<Long>());
        this.chkptRecoveryTimes.put(appId, new ConcurrentLinkedQueue<Long>());
        this.crashDetectTimes.put(appId, new ConcurrentLinkedQueue<Long>());
    }

    /**
     * Initialize application specific counters.
     * NOTE: Hold application specific lock and application registration before calling this function.
     *
     * @param appId Application ID
     */
    private void initAppCounters(String appId) {
        // If application is restarted then we have to skip some of the bookkeeping operations
        if (this.appInMsgCounters.containsKey(appId)) {
            return;
        }

        this.appInMsgCounters.put(appId, new AtomicLong(0L));
        this.appMsgTypeCounters.put(appId, new HashMap<DPlaneMsgType, AtomicLong>());
        this.appOutMsgCounters.put(appId, new AtomicLong(0L));
    }

    @Override
    public void registerEndpoint(String appId, RemoteEndpt re, Channel ctrlChannel) {
        try {
            if (!this.appNum.containsKey(appId))
                this.appNum.put(appId, this.appCounter.incrementAndGet());

            final Object appLock;
            // If application is restarted then we have to reuse the lock
            if (!this.appLocks.containsKey(appId))
                appLock = new Object();
            else
                appLock = this.appLocks.get(appId);

            synchronized (appLock) {
                if (!this.appLocks.containsKey(appId))
                    this.appLocks.put(appId, appLock);

                if (!this.chkptRecoveryTimes.containsKey(appId))
                    this.initAppTimers(appId);

                final Object appRegLock;
                if (!this.appRegLocks.containsKey(appId)) {
                    appRegLock = new Object();
                    this.appRegLocks.put(appId, appRegLock);
                } else
                    // If application is restarted then we have to reuse the lock
                    appRegLock = this.appRegLocks.get(appId);

                synchronized (appRegLock) {
                    this.initAppCounters(appId);
                    this.registerEndpoint(re);
                }

                if (this.crEnabled) {
                    if (!this.crClient.registerProcess(re)) {
                        this.crEnabled = false;

                        if (logger.isWarnEnabled()) {
                            logger.warn("registerEndpoint> Registration failed. C/R feature is disabled!");
                        }
                    } else {
                        this.checkpointApp(re);
                    }
                }
            }
        } catch (IOException e) {
            // NOTE: Failed to create datagram channel; no point in letting application register!
            e.printStackTrace();
            logger.error("Failed to create datagram channel for {}", appId);

            // FIXME: Let the application know that the registration failed!
            ctrlChannel.disconnect();
        }
    }

    @Override
    public void unregisterEndpoint(String appId) {
        // NOTE: Threads must ensure to hold an application specific lock prior to calling unregisterEndpoint!

        if (this.activeApps.get(appId) == null) {
            if (logger.isWarnEnabled()) {
                logger.warn("unregisterEndpoint> failed to unregister; {} is not registered!", appId);
            }

            return;
        }

        RemoteEndpt endpt = this.activeApps.remove(appId);

        if (logger.isWarnEnabled()) {
            logger.warn("Endpoint's data plane listener at {}:{} is down", endpt.host, endpt.port);
        }

        // Remove application address as it is no longer required.
        this.appAddrBook.remove(appId);

        // Clear all subscriptions
        this.appsSubscriptions.remove(appId);
        // Remove replay-buffers
        this.appReplayBuffers.remove(appId);

        try {
            this.closeDPlaneChannel(this.appChannels.get(appId));
        } catch (IOException e) {
            // NOTE: Alert network operator to the error, but do not crash!
            e.printStackTrace();
            logger.error("Failed to close datagram channel associated with {}", appId);
        }
    }

    /**
     * Backs up an application's message subscriptions, counters, etc.
     *
     * @param appId Remote application identifier
     */
    private void backupAppData(String appId) {
        this.appsSubscriptionsBackup.putIfAbsent(appId, this.appsSubscriptions.get(appId));

        if (logger.isDebugEnabled()) {
            logger.debug("backupAppData> backed up {} subscriptions of {}",
                         this.appsSubscriptionsBackup.get(appId).size(), appId);
        }

        this.appReplayBuffersBackup.putIfAbsent(appId, this.appReplayBuffers.get(appId));

        if (logger.isDebugEnabled()) {
            logger.debug("backupAppData> backed up replay-buffers of {}", appId);
        }
    }

    /**
     * Restores an application's message subscriptions, counters, etc.
     *
     * @param appId Remote application identifier
     */
    private void restoreAppData(String appId) {
        this.appsSubscriptions.get(appId).addAll(this.appsSubscriptionsBackup.remove(appId));

        if (logger.isDebugEnabled()) {
            logger.debug("restoreAppData> restored {} subscriptions of {}",
                         this.appsSubscriptions.get(appId).size(), appId);
        }

        this.appReplayBuffers.get(appId).addAll(this.appReplayBuffersBackup.remove(appId));

        if (logger.isDebugEnabled()) {
            logger.debug("restoreAppData> restored replay-buffers of {}", appId);
        }
    }

    @Override
    public void addMsgSubscription(String appId, edu.duke.cs.legosdn.core.appvisor.cplane.OFMsgSubscription msgSub) {
        this.appsSubscriptions.get(appId).add(msgSub.getMsgType());
    }

    @Override
    public void delMsgSubscription(String appId, edu.duke.cs.legosdn.core.appvisor.cplane.OFMsgSubscription msgSub) {
        this.appsSubscriptions.get(appId).remove(msgSub.getMsgType());
    }

    /**
     * Prepare transaction manager and channel buffers prior to delivering the message.
     *
     * @param switchId Switch identifier.
     * @throws IOException
     */
    private void prepareChannels(Long switchId) throws IOException {
        if (this.switchTxnMgr.containsKey(switchId)) {
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("prepareChannels> discovered new switch (%d)", switchId));
        }

        try {
            synchronized (this.switchTxnMgr) {
                if (!this.swLocks.containsKey(switchId)) {
                    this.swLocks.put(switchId, switchId);
                }

                if (!this.switchTxnMgr.containsKey(switchId)) {
                    this.switchTxnMgr.put(switchId, new TransactionMgr());
                }

                // Buffers are always created in pairs; check one to see if the pair exists
                if (!this.swInBuffers.containsKey(switchId)) {
                    this.swInBuffers.put(switchId, ByteBuffer.allocate(Defaults.CHANNEL_BUF_SZ));
                    this.swOutBuffers.put(switchId, ByteBuffer.allocate(Defaults.CHANNEL_BUF_SZ));
                }
            }
        } catch (IOException e) {
            logger.error("prepareChannels> failed to initialize transaction manager! {}", e.getLocalizedMessage());
            throw e;
        }
    }

    /**
     * Broadcast message to remote applications, as required.
     *
     * @param sw  Switch
     * @param msg OpenFlow message received from the switch
     * @param cntx Floodlight context
     * @return Value indicating whether further applications can continue processing the message or not.
     * @throws java.io.IOException
     */
    public IListener.Command broadcastMsg(final IOFSwitch sw, final OFMessage msg, final FloodlightContext cntx)
            throws IOException {
        final Long switchId = getSwitchID(sw);
        // FIXME: Use putIfAbsent and remove entry when switch disconnects!
        this.switchBoard.put(switchId, sw);
        this.prepareChannels(switchId);
        return this.sendMsgToApps(sw, msg, cntx);
    }

    /**
     * Broadcast notification to remote applications, as required.
     *
     * @param sw  Switch
     * @param msg Notification message
     * @throws IOException
     */
    public void broadcastNotif(final IOFSwitch sw, final DPlaneNotification msg) throws IOException {
        /* NOTE: Switch instance might be null for notifications. */
        if (sw != null) {
            this.switchBoard.put(sw.getId(), sw);
        }
        final Long switchId = getSwitchID(sw);
        this.prepareChannels(switchId);
        this.sendNotifToApps(sw, msg);
    }

    /**
     * Serialize message received from the switch to dispatch it to the applications.
     *
     * @param swId Switch identifier
     * @param msg Data plane (serializable) message
     * @throws IOException
     */
    public void serializeMessage(long swId, DPlaneMsgExternalizable msg) throws IOException {
        final ByteBuffer buffer = this.swOutBuffers.get(swId);
        buffer.clear();
        final FSTObjectOutput out = new FSTObjectOutput(new ByteBufferBackedOutStream(buffer));
        out.writeObject(msg);
        out.close();
        buffer.flip();
    }

    /**
     * De-serialize the application responses.
     *
     * @param swId Switch identifier
     * @return Data plane message
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public DPlaneMsg deserializeMessage(long swId) throws IOException, ClassNotFoundException {
        final ByteBuffer buffer = this.swInBuffers.get(swId);
        final FSTObjectInput in = new FSTObjectInput(new FSTInputStream(buffer.array()));
        final DPlaneMsg m = (DPlaneMsg) in.readObject();
        in.close();
        return m;
    }

    /**
     * Create new datagram channel to dispatch message from the proxy to stub.
     *
     * @return Instance of DatagramChannel
     * @throws IOException
     */
    private DatagramChannel createDPlaneChannel() throws IOException {
        final long tBeg = System.currentTimeMillis();

        final DatagramChannel dplaneClient = DatagramChannel.open();
        dplaneClient.socket().setSoTimeout((int) Defaults.APP_DP_RESP_WAIT_TIME);
        // NOTE: Is setting the buffer sizes even required for UDP transports?
        dplaneClient.socket().setSendBufferSize(Defaults.CHANNEL_BUF_SZ);
        dplaneClient.socket().setReceiveBufferSize(Defaults.CHANNEL_BUF_SZ);
        // NOTE: Run data plane client in blocking mode
        dplaneClient.configureBlocking(true);

        final long tEnd = System.currentTimeMillis();
        this.aggSetupTime.addAndGet(tEnd - tBeg);
        this.numConns.incrementAndGet();

        return dplaneClient;
    }

    /**
     * Send the data plane message to the stub and retrieve responses from the stub.
     *
     * @param swId Switch identifier
     * @param dplaneClient Socket or channel to send and receive the data plane messages
     * @param appId Remote application identifier
     * @param cntx Floodlight context
     * @return CONTINUE or STOP based on application's response.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private IListener.Command sendAndReceiveDPlaneMsg(final long swId,
                                                      final DatagramChannel dplaneClient,
                                                      final String appId,
                                                      final FloodlightContext cntx)
            throws IOException, ClassNotFoundException {
        final long tBeg = System.currentTimeMillis();

        final TransactionMgr txnMgr = this.switchTxnMgr.get(swId);

        final ByteBuffer outBuffer = this.swOutBuffers.get(swId);
        final int b = outBuffer.limit();
        final int n = dplaneClient.send(outBuffer, this.appAddrBook.get(appId));
        if (n == 0) {
            if (logger.isWarnEnabled()) {
                logger.warn("sendAndReceiveDPlaneMsg> sent zero of {} bytes to stub!", b);
            }
        }

        if (logger.isTraceEnabled()) {
            logger.trace("sendAndReceiveDPlaneMsg> sent {} bytes to {}", n, appId);
        }

        final ByteBuffer inBuffer = this.swInBuffers.get(swId);
        IListener.Command cmd = null;
        while (cmd == null) {
            inBuffer.clear();
            dplaneClient.receive(inBuffer);

            final DPlaneMsg m = this.deserializeMessage(swId);

            this.recorder.logMsg(dplaneClient.getLocalAddress().toString(), "-", WLOG_FILE);
            this.recorder.logOutMsg(m, WLOG_FILE);

            if (logger.isTraceEnabled()) {
                logger.trace("sendAndReceiveDPlaneMsg> received {} from application", m.getMsgType());
            }

            switch (m.getMsgType()) {
                case LISTENER_CMD:
                    cmd = (IListener.Command) m.getMsgPayload();
                    break;
                default:
                    if (!this.switchBoard.containsKey(m.getSwitchID()))
                        this.switchBoard.putIfAbsent(m.getSwitchID(),
                                                     this.floodlightProvider.getSwitch(m.getSwitchID()));
                    final IOFSwitch sw = this.switchBoard.get(m.getSwitchID());

                    if (logger.isTraceEnabled()) {
                        logger.warn(String.format("sendAndReceiveDPlaneMsg> %s => sw: %d",
                                                  m.getMsgType(), m.getSwitchID()));
                    }

                    // FIXME: Writes to the same switch from multiple threads could be disastrous!
                    sw.write((OFMessage) m.getMsgPayload(), cntx);
                    // TODO: Flushing after every write could lower performance!
                    sw.flush();

                    final DPlaneMsgExternalizable extMsg =
                            new DPlaneMsgExternalizable(m.getMsgType(), m.getSwitchID(), m.getMsgPayload());
                    txnMgr.update(sw, extMsg, appId);

                    this.appOutMsgCounters.get(appId).incrementAndGet();
                    if (!this.appMsgTypeCounters.get(appId).containsKey(extMsg.getMsgType()))
                        this.appMsgTypeCounters.get(appId).put(extMsg.getMsgType(), new AtomicLong(0L));
                    this.appMsgTypeCounters.get(appId).get(extMsg.getMsgType()).incrementAndGet();

                    if (logger.isTraceEnabled()) {
                        logger.trace("sendAndReceiveDPlaneMsg> sending {} to {}",
                                     ((OFMessage) m.getMsgPayload()).getType(),
                                     m.getSwitchID());
                    }
            }
        }

        long tEnd = System.currentTimeMillis();
        this.aggMsgXferTime.addAndGet(tEnd - tBeg);
        this.numMsgXfers.incrementAndGet();

        return cmd;
    }

    /**
     * Close datagram channel.
     *
     * @param dplaneClient Instance of DatagramChannel used to dispatch message from the proxy to stub
     * @throws IOException
     */
    private void closeDPlaneChannel(final DatagramChannel dplaneClient) throws IOException {
        long tBeg = System.currentTimeMillis();

        dplaneClient.close();

        long tEnd = System.currentTimeMillis();
        this.aggTeardownTime.addAndGet(tEnd - tBeg);
    }

    /**
     * Checkpoint state of remote endpoint.
     *
     * @param re Remote endpoint
     */
    private void checkpointApp(final RemoteEndpt re) {
        if (re.isStateless) {
            return;
        }

        synchronized (this.appLocks.get(re.appId)) {
            final long msgCount = this.appInMsgCounters.get(re.appId).get();

            if (this.crEnabled && ((msgCount % this.chkptFreq) == 0)) {
                if (this.crClient.checkpointProcess(re, this.appNum.get(re.appId))) {
                    if (msgCount == 0) {
                        if (logger.isInfoEnabled()) {
                            logger.info("checkpointApp> saved initial checkpoint for {}", re.appId);
                        }
                    } else {
                        if (logger.isTraceEnabled()) {
                            logger.trace("checkpointApp> saved checkpoint for {}", re.appId);
                        }
                    }

                    this.appReplayBuffers.get(re.appId).clear();
                } else {
                    if (logger.isErrorEnabled()) {
                        logger.error("checkpointApp> failed to save checkpoint for {}", re.appId);
                    }

                    this.crEnabled = false;

                    if (logger.isErrorEnabled()) {
                        logger.error("checkpointApp> C/R feature is disabled!");
                    }

                    this.handleCRFailure();
                }
            }
        }
    }

    /**
     * Recover dead application either using restart or checkpoint-restore followed by a replay.
     *
     * @param re Remote endpoint
     * @param appCrashTime Time at which the caller thinks application crashed.
     * @return CONTINUE if everything recovery was successful; STOP, otherwise
     */
    private IListener.Command recoverApp(RemoteEndpt re, Long appCrashTime) {
        synchronized (this.appRegLocks.get(re.appId)) {
            // Application identifier will never change, even if application was restarted.
            final String appId = re.appId;

            // Retrieve the application metadata again inside the critical region!
            re = activeApps.get(appId);
            if (re == null) {
                if (logger.isErrorEnabled()) {
                    logger.error("recoverApp> application {} has been unregistered!", appId);
                }

                return IListener.Command.STOP;
            }

            if (RemoteRegistry.isPIDAlive(re.pid)) {
                logger.warn("recoverApp> application {} appears restored!", appId);
                return IListener.Command.CONTINUE;
            }

            this.crashDetectTimes.get(re.appId).add(appCrashTime);

            // At this point, if there is any thread attempting to send data or waiting on a response
            //  from a remote application, it will soon encounter an exception on the call since the
            //  communication channel will get closed when application is unregistered.
            // That thread will then proceed to get a lock (which is held by the recovered thread at this
            //  point) to restore or restart the application.

            if (!re.isStateless) {
                // Backup stateful application's registration and subscription data.
                backupAppData(appId);
            }
            unregisterEndpoint(appId);

            if (logger.isWarnEnabled()) {
                logger.warn("recoverApp> reviving dead application {}", appId);
            }

            if (re.isStateless) {
                // Stateless Applications

                IListener.Command cmd = null;
                try {
                    final long tBeg = System.currentTimeMillis();
                    if (!restartStatelessApp(re)) {
                        cmd = IListener.Command.STOP;
                        logger.error("recoverApp> failed to restart {}!", re.toString());
                    }
                    final long tEnd = System.currentTimeMillis();
                    this.appRestartTimes.get(re.appId).add(tEnd - tBeg);
                    cmd = IListener.Command.CONTINUE;
                } catch (IOException e) {
                    logger.error("recoverApp> Failed to restart {}! {}",
                                 re.toString(), e.getLocalizedMessage());
                } finally {
                    if (cmd == null)
                        cmd = IListener.Command.STOP;
                }
                return cmd;
            }

            // Stateful applications

            if (!this.crEnabled) {
                logger.warn("recoverApp> cannot recover {}; C/R disabled!", re.appId);
                return IListener.Command.STOP;
            }

            IListener.Command cmd = null;
            try {
                final long tBeg, tEnd;
                tBeg = System.currentTimeMillis();
                this.restoreApp(re);
                tEnd = System.currentTimeMillis();
                this.appRestoreTimes.get(re.appId).add(tEnd - tBeg);
                cmd = IListener.Command.CONTINUE;
            } catch (IOException e) {
                // Disable C/R
                this.crEnabled = false;

                logger.error("recoverApp> Failed to restore {}! {}; C/R disabled!",
                             re.toString(), e.getLocalizedMessage());
            } finally {
                if (cmd == null) {
                    cmd = IListener.Command.STOP;
                    return cmd;
                }
            }

            try {
                if (!this.disableReplay) {
                    final long tBeg, tEnd;
                    tBeg = System.currentTimeMillis();
                    cmd = this.replayMsgs(re.appId);
                    tEnd = System.currentTimeMillis();
                    this.appReplayTimes.get(re.appId).add(tEnd - tBeg);
                }
            } catch (IOException e) {
                // Disable C/R
                this.crEnabled = false;

                logger.error("recoverApp> Failed to replay messages to {}! {}; C/R disabled!",
                             re.toString(), e.getLocalizedMessage());
            } finally {
                if (cmd == null)
                    cmd = IListener.Command.STOP;
            }
            return cmd;
        }
    }

    /**
     * Restart a stateless remote endpoint.
     *
     * @param re Remote endpoint
     * @return True, if the restart was successful; false, otherwise
     * @throws IOException
     */
    private boolean restartStatelessApp(RemoteEndpt re) throws IOException {
        if (logger.isInfoEnabled()) {
            logger.info("restartStatelessApp> restarting application {}", re.toString());
        }

        final ProcessBuilder processBuilder = new ProcessBuilder();
        // FIXME: Find a way to figure out SDN-App launcher script dynamically; avoid hard-coded value
        processBuilder.command(Defaults.RESTART_WRAPPER, Defaults.STUB_LAUNCHER, re.name);
        final Process process = processBuilder.start();
        processBuilder.redirectInput(DEV_NULL);
        processBuilder.redirectError(DEV_NULL);
        processBuilder.redirectOutput(DEV_NULL);
        try {
            return process.waitFor() == 0;
        } catch (InterruptedException e) {
            throw new IOException("Failed while waiting for stub to restart!", e);
        }
    }

    /**
     * Restore remote endpoint from last checkpoint.
     *
     * @param re Remote endpoint
     * @throws IOException
     */
    private void restoreApp(final RemoteEndpt re) throws IOException {
        final int restoredPid = this.crClient.restoreProcess(re, this.appNum.get(re.appId));

        final boolean procCrashed = !isPIDAlive(restoredPid);
        if (procCrashed) {
            logger.warn(String.format("restoreApp> restored process (%d) suffered premature death!",
                                      restoredPid));
        }

        if (restoredPid == CRClientProvider.INVALID_PROC_PID || procCrashed) {
            if (logger.isErrorEnabled()) {
                logger.error("restoreApp> failed to restore {} from checkpoint", re.appId);
            }

            this.crEnabled = false;

            if (logger.isErrorEnabled()) {
                logger.error("restoreApp> C/R feature is disabled!");
            }

            this.handleCRFailure();
            return;
        }

        final RemoteEndpt restoredRe = re.changePID(restoredPid);

        // Re-register application and restore its subscriptions
        this.registerEndpoint(restoredRe);
        this.restoreAppData(restoredRe.appId);

        if (logger.isInfoEnabled()) {
            logger.info("restoreApp> successfully restored {} from checkpoint", restoredRe.appId);
        }
    }

    /**
     * Broadcast message on the data plane.
     *
     * @param sw  Switch
     * @param ofMsg OpenFlow message received from the switch
     * @param cntx Floodlight context
     * @return Value indicating whether further applications can continue processing the message or not.
     * @throws java.io.IOException
     */
    private IListener.Command sendMsgToApps(final IOFSwitch sw, final OFMessage ofMsg,
                                            final FloodlightContext cntx)
            throws IOException {
        // Continue passing the message for processing with all registered applications, by default
        IListener.Command cmd = IListener.Command.CONTINUE;

        // It is safe to query appPorts without a lock,
        //  since this data structure is 'add only'
        final Set<Short> regAppPorts = new TreeSet<Short>(this.appPorts.keySet());
        for (final short appPort : regAppPorts) {
            final String appId = this.appPorts.get(appPort);
            final RemoteEndpt re;
            // Before checking the subscriptions of the application hold the application's
            //  registration lock to ensure that we do not attempt to query the state of
            //  an application that is currently in the process of being recovered.
            while (this.activeApps.get(appId) == null) {
                if (logger.isTraceEnabled()) {
                    logger.trace("sendMsgToApps> waiting for {} to be recovered  ...", appId);
                }

                if (this.appPorts.get(appPort) == null)
                    break;
            }
            if (this.appPorts.get(appPort) == null)
                continue;
            synchronized (this.appRegLocks.get(appId)) {
                re = this.activeApps.get(appId);

                if (logger.isInfoEnabled()) {
                    logger.info(String.format("sendMsgToApps> attempting to send %s to %s ...",
                                              ofMsg.getType().toString(), appId));
                }

                if (!this.appsSubscriptions.get(appId).contains(ofMsg.getType())) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("sendMsgToApps> {} is not subscribed to receive {}",
                                     re.toString(), ofMsg.getType().toString());
                    }

                    continue;
                }
            }

            final DPlaneMsgType dPlaneMsgType;
            try {
                dPlaneMsgType = DPlaneMsgType.mapTypeTo(ofMsg.getType());
            } catch (UnknownMsgTypeException e) {
                if (logger.isWarnEnabled()) {
                    logger.warn("App {} subscribed to received unknown message type '%s'",
                                re.toString(), ofMsg.getType().toString());
                }
                continue;
            }
            final DPlaneMsg msg = new DPlaneMsgExternalizable(dPlaneMsgType, sw.getId(), ofMsg);

            if (logger.isTraceEnabled()) {
                logger.trace("sendMsgToApps> Dispatch {} to {}", msg.getMsgType(), re.toString());
            }

            final DPlaneMsgExternalizable extMsg = (DPlaneMsgExternalizable) msg;

            synchronized (this.swLocks.get(sw.getId())) {
                if (re.isStateless) {
                    cmd = this.tryOrTransform(sw, re, extMsg);
                } else {
                    synchronized (this.appLocks.get(appId)) {
                        synchronized (this.appRegLocks.get(appId)) {
                            if (this.appReplayBuffers.containsKey(appId))
                                this.appReplayBuffers.get(appId).add(extMsg);
                        }
                        cmd = this.tryOrTransform(sw, re, extMsg);
                    }
                }
            }

            if (IListener.Command.STOP.equals(cmd)) {
                break;
            }
        }

        return cmd;
    }

    /**
     * Broadcast notification on the data plane.
     *
     * @param sw  Switch
     * @param notifMsg Notification message
     * @throws IOException
     */
    private void sendNotifToApps(final IOFSwitch sw, final DPlaneNotification notifMsg) throws IOException {
        /* WARNING: 'sw' can be null! */
        final long swId = getSwitchID(sw);

        // It is safe to query appPorts without a lock,
        //  since this data structure is 'add only'
        final Set<Short> regAppPorts = new TreeSet<Short>(this.appPorts.keySet());
        for (final short appPort : regAppPorts) {
            final String appId = this.appPorts.get(appPort);
            final RemoteEndpt re;
            // Before retrieving the endpoint associated with the application hold application's
            //  registration lock to ensure that we do not attempt to query the state of
            //  an application that is currently in the process of being recovered.
            while (this.activeApps.get(appId) == null) {
                if (logger.isTraceEnabled()) {
                    logger.trace("sendMsgToApps> waiting for {} to be recovered  ...", appId);
                }

                if (this.appPorts.get(appPort) == null)
                    break;
            }
            if (this.appPorts.get(appPort) == null)
                continue;
            synchronized (this.appRegLocks.get(appId)) {
                re = this.activeApps.get(appId);

                if (logger.isInfoEnabled()) {
                    logger.info(String.format("sendNotifToApps> attempting to send %s to %s ...",
                                              notifMsg.getMsgType().toString(), appId));
                }

                // TODO: Check if the application is subscribed for the notification.
            }

            final DPlaneMsgType msgType = notifMsg.getMsgType();
            final DPlaneMsg msg;
            switch (msgType) {
                case SWITCH_NOTIF:
                case PORT_NOTIF:
                case LINK_NOTIF:
                    msg = new DPlaneMsgExternalizable(msgType, swId, notifMsg);
                    break;
                default:
                    final String err = String.format("sendNotifToApps> Invalid notification type %s!", msgType);
                    throw new IOException(err);
            }

            if (logger.isTraceEnabled()) {
                logger.trace("sendNotifToApps> Dispatch {} to {}", msgType, re.toString());
            }

            final DPlaneMsgExternalizable extMsg = (DPlaneMsgExternalizable) msg;

            synchronized (this.swLocks.get(swId)) {
                if (re.isStateless) {
                    this.tryOrTransform(sw, re, extMsg);
                } else {
                    synchronized (this.appLocks.get(appId)) {
                        synchronized (this.appRegLocks.get(appId)) {
                            if (this.appReplayBuffers.containsKey(appId))
                                this.appReplayBuffers.get(appId).add(extMsg);
                        }
                        this.tryOrTransform(sw, re, extMsg);
                    }
                }
            }
        }
    }

    /**
     * Try to send the original inbound message to the application, and if that fails, transform the message into an
     *  equivalent message and deliver that to the application.
     *
     * @param sw Switch
     * @param re Remote endpoint
     * @param inMsg Inbound message to be processed by the application
     * @return STOP or CONTINUE
     * @throws IOException
     */
    private IListener.Command tryOrTransform(IOFSwitch sw, RemoteEndpt re, DPlaneMsgExternalizable inMsg)
            throws IOException {
        final long beg = System.currentTimeMillis();
        List<DPlaneMsgExternalizable> priorExtMsgs = null;
        List<DPlaneMsgExternalizable> extMsgs;
        if (this.invertTransforms) {
            extMsgs = this.transformer.verify(inMsg, re.appId);
        } else {
            extMsgs = new ArrayList<DPlaneMsgExternalizable>();
            extMsgs.add(inMsg);
        }

        long appCrashTime = 0;
        final Long swId = getSwitchID(sw);
        final TransactionMgr txnMgr = this.switchTxnMgr.get(getSwitchID(sw));
        // Counter indicating how many attempts have been made so far to deliver the message to the application
        int attempt = 0;
        IListener.Command cmd = null;
        while (cmd == null && attempt < Defaults.MAX_RETRY_ATTEMPTS) {
            /* NOTE: Enable automatic transaction commits only for the first attempt and not the retries, as the
             *  retries may involve multiple messages which should be bundled into a single transaction.
             */
            final boolean autoCommit = attempt < 1 && extMsgs.size() == 1;
            txnMgr.begin();

            for (DPlaneMsgExternalizable m : extMsgs) {
                m.setAppMsgId(this.appInMsgCounters.get(re.appId).incrementAndGet());
                if (!this.appMsgTypeCounters.get(re.appId).containsKey(m.getMsgType()))
                    this.appMsgTypeCounters.get(re.appId).put(m.getMsgType(), new AtomicLong(0L));
                this.appMsgTypeCounters.get(re.appId).get(m.getMsgType()).incrementAndGet();

                this.recorder.logInMsg(m, WLOG_FILE);

                this.serializeMessage(swId, m);
                cmd = this.interactWithApp(sw, re.appId, m, null, autoCommit, false);

                if (logger.isTraceEnabled()) {
                    logger.trace("tryOrTransform> received response {}!", cmd);
                }

                if (txnMgr.isCancelled() || cmd == null) {
                    if (!txnMgr.isCancelled()) txnMgr.cancel();
                    appCrashTime = System.currentTimeMillis();
                    /* If transaction was aborted or no response was received for even one of the messages,
                     *  discard the list, and attempt another transformation. The rationale behind this
                     *  implementation is that the list usually will be a singleton, unless transformer was invoked
                     *  to generate a list of equivalent messages. If one of the transformed messages is still
                     *  crashing the application, there's no point in trying the rest.
                     */
                    if (attempt == 0) {
                        /* Crash induced by the original message */
                        this.faultCounters.get(m.getPayloadType()).incrementAndGet();
                    }
                    break;
                }
            }

            if (!txnMgr.isCancelled() && cmd != null) {
                if (!autoCommit) txnMgr.commit();

                this.checkpointApp(re);

                if (attempt > 0) {
                    this.recoveryCounters.get(inMsg.getPayloadType()).incrementAndGet();
                }
                break;
            }

            // Rollback the current transaction
            txnMgr.rollback();

            if (this.recoverApp(re, appCrashTime) == IListener.Command.STOP)
                break;

            // Try once again
            attempt++;

            if (attempt >= Defaults.MAX_RETRY_ATTEMPTS) {
                logger.error("tryOrTransform> exceeded retry attempts!");

                synchronized (this.appRegLocks.get(re.appId)) {
                    unregisterEndpoint(re.appId);
                }
                break;
            }

            priorExtMsgs = extMsgs;
            /* NOTE: Always use the original crash-inducing message for transformations */
            extMsgs = this.transformer.transform(inMsg, re.appId, attempt);

            if (extMsgs == null) {
                if (logger.isWarnEnabled()) {
                    logger.warn("tryOrTransform> No transformations available! retrying last message...");
                }

                extMsgs = priorExtMsgs;
                continue;
            }

            if (extMsgs.get(0).getMsgType().equals(inMsg.getMsgType())) {
                /* NOTE: We have circled back to the type of the original data plane message. */
                logger.error("tryOrTransform> exhausted all available transformations! retrying last message...");

                extMsgs = priorExtMsgs;
                continue;
            }

            this.transformCounters.get(inMsg.getPayloadType()).incrementAndGet();
            cmd = null;
        }

        final long end = System.currentTimeMillis();
        if (attempt > 0) {
            chkptRecoveryTimes.get(re.appId).add(end - beg);
        }
        return (cmd == null) ? IListener.Command.STOP : cmd;
    }

    /**
     * Try to replay the messages since the last checkpoint. Replay is only applicable for **stateful** applications.
     *
     * @param appId Remote application identifier
     * @return STOP or CONTINUE
     * @throws IOException
     */
    private IListener.Command replayMsgs(String appId) throws IOException {
        if (!this.crEnabled) {
            if (logger.isWarnEnabled()) {
                logger.warn(String.format("replayMsgs> skipping replay for '%s'; C/R disabled!", appId));
            }

            return IListener.Command.CONTINUE;
        }

        // Remove the crash inducing message from the replay list.
        if (this.appReplayBuffers.get(appId) != null && !this.appReplayBuffers.get(appId).isEmpty()) {
            final int lastMsgPos = this.appReplayBuffers.get(appId).size() - 1;
            this.appReplayBuffers.get(appId).remove(lastMsgPos);
        }

        if (this.appReplayBuffers.get(appId) == null || this.appReplayBuffers.get(appId).isEmpty()) {
            if (logger.isInfoEnabled()) {
                logger.info("replayMsgs> Nothing to replay!");
            }

            return IListener.Command.CONTINUE;
        }

        final List<DPlaneMsgExternalizable> msgsSinceLastChkpt = this.appReplayBuffers.get(appId);

        if (logger.isInfoEnabled()) {
            logger.info(String.format("replayMsgs> %d message to replay for '%s'!", msgsSinceLastChkpt.size(), appId));
        }

        IListener.Command cmd = null;
        // Position of last message successfully replayed
        int lastReplayOK = msgsSinceLastChkpt.size();

        final boolean inReplayMode = true;
        final boolean autoCommit = false;
        while (cmd == null) {
            long faultBeg = 0;
            int msgId = 0;
            for (DPlaneMsgExternalizable msg : msgsSinceLastChkpt) {
                msg.setIsReplay(inReplayMode);

                if (logger.isDebugEnabled()) {
                    logger.debug("replayMsgs> replaying {}!", msg.getMsgType());
                }

                if (msgId >= lastReplayOK) {
                    // Successfully replayed all or a portion of the replay buffer
                    cmd = IListener.Command.CONTINUE;
                    break;
                }

                msg.setAppMsgId(this.appInMsgCounters.get(appId).incrementAndGet());
                this.serializeMessage(msg.getSwitchID(), msg);
                final IOFSwitch sw = this.switchBoard.get(msg.getSwitchID());
                cmd = this.interactWithApp(sw, appId, msg, null, autoCommit, inReplayMode);
                if (cmd != null) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("replayMsgs> received response {}!", cmd);
                    }

                    continue;
                }

                faultBeg = System.currentTimeMillis();

                // Replay did not succeed!
                lastReplayOK = msgId;

                // Handle crashes encountered during replay
                synchronized (this.appRegLocks.get(appId)) {
                    final RemoteEndpt re = activeApps.get(appId);

                    if (re == null) {
                        if (logger.isErrorEnabled()) {
                            logger.error("replayMsgs> application {} has been unregistered!", appId);
                        }

                        cmd = IListener.Command.STOP;
                        break;
                    }

                    if (RemoteRegistry.isPIDAlive(re.pid)) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("replayMsgs> application {} appears restored!", appId);
                        }
                    } else {
                        unregisterEndpoint(appId);

                        if (logger.isWarnEnabled()) {
                            logger.warn("replayMsgs> reviving dead application {}", appId);
                        }

                        if (this.crEnabled) {
                            this.restoreApp(re);

                            final long faultEnd = System.currentTimeMillis();
                            chkptRecoveryTimes.get(re.appId).add(faultEnd - faultBeg);
                        } else {
                            logger.error("replayMsgs> cannot revive stateful application {} since C/R is disabled!",
                                         appId);
                            /* TODO: What should we do after an application crash, if checkpointing is disabled? */
                            cmd = IListener.Command.STOP;
                            break;
                        }
                    }
                }

                break;
            }
        }

        return (cmd == null) ? IListener.Command.STOP : cmd;
    }

    /**
     * Sends a message to the remote application.
     *
     * @param sw Switch
     * @param appId Application Identifier
     * @param extMsg Message to send
     * @param cntx FloodLight context
     * @param autoCommit if True, transaction is committed automatically after successful completion
     * @param inReplayMode Set to True if in replay mode; otherwise, set to False
     * @return Value indicating whether further applications can continue processing the message or not.
     */
    private IListener.Command interactWithApp(final IOFSwitch sw, String appId, final DPlaneMsgExternalizable extMsg,
                                              final FloodlightContext cntx, boolean autoCommit, boolean inReplayMode) {
        IListener.Command cmd;
        final TransactionMgr txnMgr = this.switchTxnMgr.get(getSwitchID(sw));
        try {
            // There are no transactions, in replay mode.
            if (!inReplayMode) txnMgr.update(sw, extMsg, appId);

            if (inReplayMode) {
                if (logger.isDebugEnabled()) {
                    logger.debug("interactWithApp> replaying {}", extMsg.getMsgType());
                }
            }

            final DatagramChannel dplaneClient = this.appChannels.get(appId);
            cmd = this.sendAndReceiveDPlaneMsg(getSwitchID(sw), dplaneClient, appId, cntx);

            if (inReplayMode) {
                if (logger.isDebugEnabled()) {
                    logger.debug("interactWithApp> replay-response: %s", cmd);
                }
            }

            if (!inReplayMode && autoCommit) {
                // There are no transactions, in replay mode.
                txnMgr.commit();
            }
        } catch (ClassNotFoundException e) {
            cmd = null;
            if (!inReplayMode) txnMgr.cancel();

            logger.error("interactWithApp> Failed to parse application response! {}",
                         e.getLocalizedMessage());
        } catch (IOException e) {
            cmd = null;
            if (!inReplayMode) txnMgr.cancel();

            if (logger.isWarnEnabled())
                logger.warn("interactWithApp> Failed while waiting for app response to {}!",
                            extMsg.getMsgType().toString());
        }
        return cmd;
    }

    /**
     * Halt on C/R failures, if module is configured accordingly. Otherwise, ignore the failure.
     */
    private void handleCRFailure() {
        if (!this.haltOnCRFailure) return;
        logger.error("Halting due to C/R failure!");
        System.exit(CR_FAILURE_EXIT);
    }

    @Override
    public String getAppID(short appPort) {
        return this.appPorts.get(appPort);
    }

    /**
     * Get switch identifier, if it exists. Otherwise, return a unique internal identifier.
     *
     * @param sw Switch instance
     * @return Switch identifier
     */
    private static long getSwitchID(IOFSwitch sw) {
        return sw == null ? DPlaneMsg.INTERNAL_QUEUE : sw.getId();
    }

}
