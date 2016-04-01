package edu.duke.cs.legosdn.core.appvisor.stub;

import edu.duke.cs.legosdn.core.Defaults;
import edu.duke.cs.legosdn.core.appvisor.cplane.*;
import edu.duke.cs.legosdn.core.appvisor.dplane.*;
import edu.duke.cs.legosdn.core.faults.FaultInjector;
import edu.duke.cs.legosdn.core.log.FileRecorder;
import edu.duke.cs.legosdn.core.log.NullRecorder;
import edu.duke.cs.legosdn.core.log.Recorder;
import edu.duke.cs.legosdn.core.service.link.LinkDiscoveryServiceStub;
import edu.duke.cs.legosdn.core.service.topology.TopologyServiceStub;
import edu.duke.cs.legosdn.core.util.ByteBufferBackedOutStream;
import edu.duke.cs.legosdn.tools.cr.CRClientProvider;
import net.floodlightcontroller.core.*;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.linkdiscovery.ILinkDiscoveryListener;
import net.floodlightcontroller.linkdiscovery.ILinkDiscoveryService;
import net.floodlightcontroller.topology.ITopologyService;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.nustaq.serialization.util.FSTInputStream;
import org.openflow.protocol.OFMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * AppLoader launches an application implementation and connects it to a listener process that listens for
 * incoming events/messages from a dispatcher. On receipt of an event/message the implementation's receive message is
 * invoked and the reply is returned to the dispatcher.
 *
 * @author bala
 */
public class AppLoader implements CPlaneStub {

    private static final Logger logger = LoggerFactory.getLogger(AppLoader.class);

    private static AppLoader INSTANCE;

    private static final int INVALID_PID = -1;

    // Switch isolator one per data channel connection from controller-stub
    private final ConcurrentHashMap<Long, OFSwitchStub> switches;

    private final IOFMessageListener      app;
    private final FloodlightModuleContext moduleContext;
    private final FloodlightContext       flContext;

    // Flag indicating if application needs switch and port notifications
    private final boolean needsSwPortNotifs;
    // Flag indicating if application needs link discovery notifications
    private final boolean needsLinkNotifs;

    protected final FloodlightProviderStub flProviderStub;

    private static final InetSocketAddress PROXY_ADDR =
            new InetSocketAddress(InetAddress.getLoopbackAddress(), Defaults.PROXY_PORT);
    private final ClientBootstrap          cplaneBootstrap;
    private final CPlaneHandler<CPlaneMsg> cplaneHandler;

    private static final ChannelFactory CPLANE_FACTORY =
            new NioClientSocketChannelFactory(Executors.newFixedThreadPool(4),
                                              Executors.newFixedThreadPool(16));

    // Data Plane endpoint information
    private final RemoteEndpt appEndpt;

    // Flag indicating if application has been registered with the controller.
    private       boolean appRegistered;
    // Application registration hook/notifier
    private final Object  appRegistrationHook;

    // Custom shutdown hook/notifier
    private final Object  haltHook;
    private       boolean haltRequested;

    private static final CPlaneMsgEncoder CPLANE_MSG_ENCODER = new CPlaneMsgEncoder();
    private static final CPlaneMsgDecoder CPLANE_MSG_DECODER = new CPlaneMsgDecoder();

    // UDP socket for receiving data plane communication
    private final DatagramChannel dplaneServer;

    // Buffer for receiving and sending data plane messages
    private final ByteBuffer inBuffer, outBuffer;

    private final FaultInjector faultInjector;

    private static final File START_TIME_FILE =
            new File(Defaults.TIMERS_PATH + "/app-start-times.txt");
    private static final File CRASH_TIME_FILE =
            new File(Defaults.TIMERS_PATH + "/app-crash-times.txt");
    private static final File MLOG_FILE =
            new File(String.format("%s/%s-m.log",
                                   Defaults.APP_LOGS_PATH,
                                   AppLoader.class.getCanonicalName()));

    private final Recorder recorder;

    /**
     * Return the path of the file containing the remote endpoint's PID.
     *
     * @param re Remote endpoint
     * @return Path of file containing PID of the remote application
     */
    public static String getPIDFilepath(RemoteEndpt re) {
        return String.format("%s%c%s", Defaults.APP_PID_DIR_PATH, File.separatorChar, re.appId);
    }

    /**
     * Load PID from the PID file associated with the remote endpoint.
     *
     * @param re Remote endpoint
     * @return PID of the remote endpoint
     */
    public static int loadPIDFromFile(RemoteEndpt re) {
        final File pidFile = new File(AppLoader.getPIDFilepath(re));
        if (!pidFile.exists() || !pidFile.isFile() || !pidFile.canRead()) {
            return CRClientProvider.INVALID_PROC_PID;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(pidFile));
            final int pid = Integer.parseInt(br.readLine());
            br.close();
            return pid;
        } catch (IOException e) {
            logger.error("loadPIDFromFile> failed to load PID from {}; {}",
                         pidFile.getAbsolutePath(), e.getLocalizedMessage());
            return CRClientProvider.INVALID_PROC_PID;
        }
    }

    /**
     * Create a new {@link AppLoader} instance.
     *
     * @param conf instance of {@link AppLoaderConfig}
     * @throws IOException
     */
    private AppLoader(AppLoaderConfig conf) throws IOException {
        this.appRegistered = false;
        this.appRegistrationHook = new Object();

        this.haltHook = new Object();

        this.moduleContext = new FloodlightModuleContext();
        this.flContext = new FloodlightContext();

        this.cplaneBootstrap = new ClientBootstrap(CPLANE_FACTORY);
        this.cplaneHandler = new CPlaneHandler<CPlaneMsg>();
        this.configureCPlaneClient();

        final int pid = AppLoader.getProcessID();
        if (pid == AppLoader.INVALID_PID) {
            logger.error("AppLoader> failed to retrieve PID!");
            System.exit(1);
        }

        // NOTE: Assuming applications are on the same machine as that of the controller.
        this.appEndpt = new RemoteEndpt(pid,
                                        InetAddress.getLoopbackAddress().getCanonicalHostName(),
                                        conf.listenPort,
                                        conf.appName,
                                        conf.stateless);

        final String pidFilePath = AppLoader.getPIDFilepath(this.appEndpt);
        final FileWriter fw = new FileWriter(pidFilePath);
        fw.write(String.valueOf(this.appEndpt.pid));
        fw.close();

        if (logger.isInfoEnabled()) {
            logger.info("AppLoader> application PID ({}) written to {}", this.appEndpt.pid, pidFilePath);
        }

        this.faultInjector = conf.faultInjector;
        if (conf.enableMLog)
            this.recorder = FileRecorder.getInstance();
        else
            this.recorder = NullRecorder.getInstance();
        OFSwitchStub.changeRecorder(this.recorder);

        this.flProviderStub = new FloodlightProviderStub(this.appEndpt.appId, this);
        this.switches = this.flProviderStub.switches;
        this.moduleContext.addService(IFloodlightProviderService.class,
                                      this.flProviderStub);
        this.moduleContext.addService(ITopologyService.class,
                                      new TopologyServiceStub(this, cplaneHandler));
        this.moduleContext.addService(ILinkDiscoveryService.class,
                                      new LinkDiscoveryServiceStub(this, cplaneHandler));

        if (logger.isInfoEnabled()) {
            logger.info(String.format("AppLoader> started (PID: %d) and listening at %s:%d",
                                      pid,
                                      this.appEndpt.host,
                                      this.appEndpt.port));
            logger.info(String.format("AppLoader> stateless-application? %b", this.appEndpt.isStateless));
        }

        this.inBuffer = ByteBuffer.allocate(Defaults.CHANNEL_BUF_SZ);
        this.outBuffer = ByteBuffer.allocate(Defaults.CHANNEL_BUF_SZ);

        this.dplaneServer = DatagramChannel.open();
        // NOTE: Run data plane listener in blocking mode
        this.dplaneServer.configureBlocking(true);
        // NOTE: Is setting the buffer sizes even required for UDP transports?
        this.dplaneServer.socket().setSendBufferSize(Defaults.CHANNEL_BUF_SZ);
        this.dplaneServer.socket().setReceiveBufferSize(Defaults.CHANNEL_BUF_SZ);
        this.dplaneServer.socket().setReuseAddress(true);
        this.dplaneServer.socket().bind(new InetSocketAddress(InetAddress.getLoopbackAddress(), this.appEndpt.port));

        this.app = this.loadApplication(this.appEndpt.name);
        this.needsSwPortNotifs = this.app instanceof IOFSwitchListener;
        this.needsLinkNotifs = this.app instanceof ILinkDiscoveryListener;
        for (Map.Entry<String, String> modConf : conf.modConf.entrySet()) {
            this.moduleContext.addConfigParam((IFloodlightModule) this.app,
                                              modConf.getKey(), modConf.getValue());
        }
        this.initializeComponents();
    }

    /**
     * Create an instance, if not already existing of Apploader and return it.
     *
     * @param conf AppLoader configuration
     * @return Instance of AppLoader
     * @throws IOException
     */
    public static synchronized AppLoader getInstance(AppLoaderConfig conf) throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new AppLoader(conf);
        }
        return INSTANCE;
    }

    /**
     * Configure control plane client.
     */
    private void configureCPlaneClient() {
        this.cplaneBootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(CPLANE_MSG_ENCODER, CPLANE_MSG_DECODER, cplaneHandler);
            }

        });
        this.cplaneBootstrap.setOption("tcpNoDelay", true);
        this.cplaneBootstrap.setOption("keepAlive", false);
        this.cplaneBootstrap.setOption("sendBufferSize", Defaults.CHANNEL_BUF_SZ);
    }

    /**
     * @return Singleton instance of AppLoader
     */
    public static AppLoader getInstance() {
        return INSTANCE;
    }

    /**
     * Load application class.
     *
     * @param ctrlAppName    controller application to launch
     * @throws IOException
     */
    protected IOFMessageListener loadApplication(String ctrlAppName) throws IOException {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Class c = cl.loadClass(ctrlAppName);
            return (IOFMessageListener) c.newInstance();
        } catch (ClassNotFoundException e) {
            logger.error(String.format("initialize> unable to find class '%s' in CLASSPATH", ctrlAppName));
            throw new IOException(e);
        } catch (InstantiationException e) {
            logger.error(String.format("initialize> unable to instantiate '%s'", ctrlAppName));
            throw new IOException(e);
        } catch (IllegalAccessException e) {
            logger.error(String.format("initialize> failed to instantiate '%s'", ctrlAppName));
            throw new IOException(e);
        }
    }

    /**
     * Record timer to a file.
     *
     * @param timesFile File containing recorded timer values
     * @param crashTime Timer value to be recorded
     */
    public static void recordTime(File timesFile, long crashTime) {
        FileWriter out = null;
        try {
            // Always append to file
            out = new FileWriter(timesFile, true);
            out.write(String.format("%d\n", crashTime));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
                // Ignore error!
            }
        }
    }

    /**
     * Cleanup.
     */
    private void cleanup() {
        if (logger.isInfoEnabled()) {
            logger.info("cleanup> shutting down data plane ...");
        }

        this.closeDataPlane();

        if (logger.isWarnEnabled()) {
            logger.warn("cleanup> data plane is shutdown");
        }

        if (logger.isInfoEnabled()) {
            logger.info("cleanup> shutting down control plane ...");
        }

        CPLANE_FACTORY.releaseExternalResources();

        if (logger.isWarnEnabled()) {
            logger.warn("cleanup> control plane is shutdown");
        }
    }

    /**
     * Cleanup on a deferred thread.
     */
    private void deferCleanup() {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                synchronized (haltHook) {
                    haltRequested = true;
                    haltHook.notify();
                }

                cleanup();
            }
        });
    }

    /**
     * Initialize application, and control and data planes.
     *
     * @throws IOException
     */
    private void initializeComponents() throws IOException {
        // Defer resources cleanup prior to starting control and data planes
        this.deferCleanup();

        this.registerApp();
        synchronized (this.appRegistrationHook) {
            try {
                // NOTE: Do not wait forever for registration to complete!
                this.appRegistrationHook.wait(Defaults.APP_REGN_TIMEOUT);
            } catch (InterruptedException e) {
                this.appRegistered = false;

                System.err.printf("Failed to register application with controller! %s\n", e.getLocalizedMessage());
            }
        }

        if (this.appRegistered) {
            this.initializeApplication();
            AppLoader.recordTime(START_TIME_FILE, System.currentTimeMillis());
            this.startDataPlane();
        }
        this.closeDataPlane();
    }

    /**
     * Registers application with the proxy.
     */
    public void registerApp() {
        final CPlaneMsg cplaneMsg = new CPlaneMsg(CPlaneMsgType.EP_REMOTEINFO, this.appEndpt);
        openCPlaneChannel().write(cplaneMsg).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                synchronized (appRegistrationHook) {
                    appRegistered = channelFuture.isSuccess();
                    appRegistrationHook.notify();
                }
                channelFuture.getChannel().close();
            }
        });
    }

    /**
     * Initialize the application.
     *
     * @throws IOException
     */
    protected void initializeApplication() throws IOException {
        try {
            ((IFloodlightModule) this.app).init(this.moduleContext);
            ((IFloodlightModule) this.app).startUp(this.moduleContext);

            logger.info("Application initialized and waiting for messages...");
        } catch (FloodlightModuleException e) {
            logger.error(String.format("initialize> failed to initialize '%s'", this.appEndpt.name));
            throw new IOException(e);
        }
    }

    /**
     * Print usage information on output stream.
     *
     * @param out   output stream
     */
    public static void showUsage(PrintStream out) {
        out.println(String.format("usage: %s <properties-file>",
                                  AppLoader.class.getCanonicalName()));
    }

    @Override
    public Channel openCPlaneChannel() {
        ChannelFuture cplaneChFuture = this.cplaneBootstrap.connect(AppLoader.PROXY_ADDR);

        cplaneChFuture.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (f.isSuccess()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("openCPlaneChannel> Connected to controller");
                    }
                } else {
                    logger.error("openCPlaneChannel> Failed to connect to controller!");
                }
            }

        });

        cplaneChFuture.getChannel().getCloseFuture().addListener(ChannelFutureListener.CLOSE);

        while (!cplaneChFuture.isDone()) {
            try {
                Thread.sleep(Defaults.CONN_WAIT_TIMEOUT);
            } catch (InterruptedException e) {
                synchronized (haltHook) {
                    haltRequested = true;
                    haltHook.notifyAll();
                }
            }
        }

        return cplaneChFuture.getChannel();
    }

    @Override
    public short getAppPort() {
        return (short) this.appEndpt.port;
    }

    /**
     * Starts a server to listen for messages/events from the dispatcher (controller).
     */
    private void startDataPlane() {
        if (logger.isInfoEnabled()) {
            logger.info("startDataPlane> data plane initialized");
        }

        while (true) {
            try {
                SocketAddress proxyAddr = null;

                this.inBuffer.clear();
                int n = this.inBuffer.position();
                while (n == 0) {
                    proxyAddr = this.dplaneServer.receive(this.inBuffer);
                    n = this.inBuffer.position();

                    if (n == 0) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("on.recv> received zero bytes from proxy!");
                        }
                    }
                }

                final DPlaneMsg m = this.deserializeMsg();
                final boolean isNotification = DPlaneMsgType.isNotification(m.getMsgType());

                if (!this.switches.containsKey(m.getSwitchID())) {
                    // Encountered a new switch in the network!
                    OFSwitchStub sw = new OFSwitchStub(m.getSwitchID(), this.dplaneServer, this.outBuffer);
                    this.switches.put(m.getSwitchID(), sw);
                }

                final OFSwitchStub sw = this.switches.get(m.getSwitchID());
                OFSwitchStub.changeProxyAddr(proxyAddr);
                OFSwitchStub.setReplayInProgress(m.isReplay());

                this.recorder.logInMsg(m, MLOG_FILE);

                if (logger.isDebugEnabled()) {
                    if (m.isReplay()) {
                        logger.debug("on.recv> in REPLAY mode!");
                    }
                }

                if (isNotification) {
                    this.handleNotif(sw, proxyAddr, m);
                } else {
                    this.handleMsg(sw, proxyAddr, m);
                }

                OFSwitchStub.setReplayInProgress(false);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("on.recv> Error encountered in data plane; {}", e.getLocalizedMessage());

                System.exit(1);
            }
        }
    }

    /**
     * Deliver message to application and send response over the data plane back to the proxy.
     *
     * @param sw Switch
     * @param proxyAddr Socket address of the proxy
     * @param m Data plane message
     * @throws IOException
     */
    private void handleMsg(IOFSwitch sw, SocketAddress proxyAddr, final DPlaneMsg m) throws IOException {
        try {
            // Run message through the fault injector
            this.faultInjector.receive(sw, m);
        } catch (RuntimeException e) {
            logger.error("Encountered injected fault!");

            throw e;
        }

        // Let the application process the message and write the responses to the switch.
        final IListener.Command cmd = this.app.receive(sw, (OFMessage) m.getMsgPayload(), this.flContext);

        this.serializeMsg(new DPlaneMsgExternalizable(cmd));
        this.dplaneServer.send(this.outBuffer, proxyAddr);
    }

    /**
     * Deliver message to application and send response over the data plane back to the proxy.
     *
     * @param sw Switch
     * @param proxyAddr Socket address of the proxy
     * @param m Data plane message
     * @throws IOException
     */
    private void handleNotif(IOFSwitch sw, SocketAddress proxyAddr, final DPlaneMsg m) throws IOException {
        // Cast message payload to a notification
        final DPlaneNotification notif = (DPlaneNotification) m.getMsgPayload();

        if (((SwitchNotification.isSwitchNotification(notif.getMsgType()) ||
              PortNotification.isPortNotification(notif.getMsgType())) &&
             !this.needsSwPortNotifs) ||
            (LinkDiscoveryNotification.isLinkDiscoveryNotification(notif.getMsgType()) &&
             !this.needsLinkNotifs)) {
            if (logger.isDebugEnabled()) {
                logger.debug("startDataPlane> dropping notification message {} from {}",
                             m.getMsgType(), m.getSwitchID());
            }

            // NOTE: It is critical that stub sends responses back to proxy, even if notifications are not processed!
            //  Proxy waits for responses from the stub before moving on to processing other messages.
            this.sendNotifResponse(proxyAddr);

            // Skip processed; application does not process notifications
            return;
        }

        try {
            // Run message through the fault injector
            this.faultInjector.receive(sw, m);
        } catch (RuntimeException e) {
            logger.error("Encountered injected fault!");

            throw e;
        }

        switch (notif.getMsgType()) {
            case SWITCH_NOTIF:
                // Treat the application as a switch listener to invoke the necessary calls
                this.handleSwitchNotif((SwitchNotification) notif, (IOFSwitchListener) this.app);
                break;
            case PORT_NOTIF:
                // Treat the application as a switch listener to invoke the necessary calls
                this.handlePortNotif((PortNotification) notif, (IOFSwitchListener) this.app);
                break;
            case LINK_NOTIF:
                // Treat the application as a Link discovery listener to invoke the necessary calls
                this.handleLinkNotif((LinkDiscoveryNotification) notif, (ILinkDiscoveryListener) this.app);
                break;
            default:
                // Control won't reach here!
                throw new RuntimeException(String.format("handleNotif> received unsupported notification '%s'!",
                                                         notif.getMsgType()));
        }
        this.sendNotifResponse(proxyAddr);
    }

    /**
     * Send a notification response to indicate to proxy that the application has processed it.
     *
     * @param proxyAddr Socket address of the proxy
     * @throws IOException
     */
    private void sendNotifResponse(final SocketAddress proxyAddr) throws IOException {
        /* NOTE: To indicate that the application has successfully processed the notification, send CONTINUE */
        this.serializeMsg(new DPlaneMsgExternalizable(IListener.Command.CONTINUE));
        this.dplaneServer.send(this.outBuffer, proxyAddr);
    }

    /**
     * Handle switch-related notification.
     *
     * @param swNotif Switch notification
     * @param switchListener Switch listener to which the notification should be delivered
     */
    private void handleSwitchNotif(SwitchNotification swNotif, IOFSwitchListener switchListener) {
        final Long swId = swNotif.getSwitchId();
        if (!this.switches.containsKey(swId)) {
            // Encountered a new switch in the network!
            OFSwitchStub sw = new OFSwitchStub(swId, this.dplaneServer, this.outBuffer);
            this.switches.put(swId, sw);
        }

        switch (swNotif.getNotifType()) {
            case SWITCH_ADDED:
                switchListener.switchAdded(swId);
                break;
            case SWITCH_ACTIVATED:
                switchListener.switchActivated(swId);
                break;
            case SWITCH_REMOVED:
                switchListener.switchRemoved(swId);
                break;
            case SWITCH_CHANGED:
                switchListener.switchChanged(swId);
                break;
        }
    }

    /**
     * Handle port-related notification.
     *
     * @param portNotif Port notification
     * @param switchListener Switch listener to which the notification should be delivered
     */
    private void handlePortNotif(PortNotification portNotif, IOFSwitchListener switchListener) {
        final Long swId = portNotif.getSwitchId();
        if (!this.switches.containsKey(swId)) {
            // Encountered a new switch in the network!
            OFSwitchStub sw = new OFSwitchStub(swId, this.dplaneServer, this.outBuffer);
            this.switches.put(swId, sw);
        }

        switchListener.switchPortChanged(swId, portNotif.getPort(), portNotif.getPortChangeType());
    }

    /**
     * Handle link discovery notification(s).
     *
     * @param linkNotif Link discovery notification
     * @param linkDiscoveryListener Link discovery listener to which the notification should be delivered
     */
    private void handleLinkNotif(LinkDiscoveryNotification linkNotif, ILinkDiscoveryListener linkDiscoveryListener) {
        if (linkNotif.getNumLinkDiscoveryUpdates() == 1) {
            linkDiscoveryListener.linkDiscoveryUpdate(linkNotif.getLinkDiscoveryUpdates().get(0));
        } else {
            linkDiscoveryListener.linkDiscoveryUpdate(linkNotif.getLinkDiscoveryUpdates());
        }
    }

    /**
     * Serialize a data plane message on to a buffer.
     *
     * @param msg Data plane message
     * @throws IOException
     */
    private void serializeMsg(final DPlaneMsgExternalizable msg) throws IOException {
        try {
            this.outBuffer.clear();
            final FSTObjectOutput out = new FSTObjectOutput(new ByteBufferBackedOutStream(this.outBuffer));
            out.writeObject(msg);
            out.close();
            this.outBuffer.flip();
        } catch (IOException e) {
            final String err = String.format("serializeMsg> failed to serialize %s; %s",
                                             msg.getMsgType(), e.getLocalizedMessage());
            logger.error(err);
            throw new IOException(err, e);
        }
    }

    /**
     * Deserialize a data plane message from the buffer.
     *
     * @return Data plane message
     * @throws IOException
     */
    private DPlaneMsg deserializeMsg() throws IOException {
        try {
            final FSTObjectInput in = new FSTObjectInput(new FSTInputStream(this.inBuffer.array()));
            final DPlaneMsg m = (DPlaneMsg) in.readObject(DPlaneMsg.class);
            in.close();

            if (logger.isTraceEnabled()) {
                logger.trace("deserializeMsg> received {} from proxy", m.getMsgType());
            }

            return m;
        } catch (Exception e) {
            final String err = String.format("deserializeMsg> failed to deserialize message; %s",
                                             e.getLocalizedMessage());
            logger.error(err);
            throw new IOException(e);
        }
    }

    /**
     * Closes data plane channels.
     */
    void closeDataPlane() {
        if (this.dplaneServer.isConnected()) {
            try {
                this.dplaneServer.close();
            } catch (IOException e) {
                logger.error("closeDataPlane> Failed to close data plane listener; {}", e.getLocalizedMessage());
                System.exit(1);
            }
        }

        if (logger.isInfoEnabled()) {
            logger.info("closeDataPlane> Data plane is shutting down!");
        }
    }

    /**
     * Launch application.
     *
     * @param args  command-line arguments.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            showUsage(System.err);
            System.exit(1);
        }

        final Properties properties = new Properties();
        final File configFile = new File(args[0]);
        FileReader configRdr = null;
        try {
            configRdr = new FileReader(configFile);
            properties.load(configRdr);
        } catch (FileNotFoundException e) {
            System.err.printf("main> Cannot find configuration file '%s'\n", configFile.getAbsolutePath());
            System.exit(1);
        } catch (IOException e) {
            System.err.printf("main> Cannot find configuration file '%s'\n", configFile.getAbsolutePath());
            System.exit(1);
        } finally {
            if (configRdr != null) {
                try {
                    configRdr.close();
                } catch (IOException e) {
                    // Just print an error message to standard error; ignore the exception!
                    System.err.printf("main> Failed to close configuration file '%s'\n", configFile.getAbsolutePath());
                }
            }
        }

        int status = 0;
        try {
            AppLoader.getInstance(AppLoaderConfig.loadFromProperties(properties));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.printf("Application crashed! %s\n", e.getMessage());
            status = 1;
            AppLoader.recordTime(CRASH_TIME_FILE, System.currentTimeMillis());
        }
        System.exit(status);
    }

    /**
     * @return PID of the calling process
     */
    public static int getProcessID() {
        // FIXME: Non-portable code! Is there a safe and portable way to retrieve the PID?
        try {
            final int pid = Integer.parseInt(new File("/proc/self").getCanonicalFile().getName());

            if (logger.isTraceEnabled()) {
                logger.trace("getProcessID> PID {} retrieved using /proc");
            }

            return pid;
        } catch (IOException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("getProcessID> failed to retrieve PID using /proc; {}", e.getLocalizedMessage());
            }
        }

        try {
            final int pid = Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@", 2)[0]);

            if (logger.isTraceEnabled()) {
                logger.trace("getProcessID> PID {} retrieved using JMX beans");
            }

            return pid;
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("getProcessID> failed to retrieve PID using JMX beans; {}", e.getLocalizedMessage());
            }
        }

        return AppLoader.INVALID_PID;
    }

}
