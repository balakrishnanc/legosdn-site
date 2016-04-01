package edu.duke.cs.legosdn.tools.cr.srv;

import edu.duke.cs.legosdn.core.Defaults;
import edu.duke.cs.legosdn.core.appvisor.cplane.RemoteEndpt;
import edu.duke.cs.legosdn.core.appvisor.stub.AppLoader;
import edu.duke.cs.legosdn.tools.cr.CRServiceProvider;
import edu.duke.cs.legosdn.tools.cr.proto.*;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * CRService is a wrapper around a binary that implements the checkpoint and restore functionality.
 */
public class CRService implements CRServiceProvider<CRCtrlMsg> {

    private static final Logger logger = LoggerFactory.getLogger(CRService.class);

    // Service configuration
    private final CRServiceConfig config;

    private final ServerBootstrap crBootstrap;
    // Server side C/R control channel handler
    private final CRCtrlChHandler crCtrlChHandler;
    private       Channel         crCtrlCh;

    private static final ChannelFactory CR_CTRL_CHANNEL_FACTORY =
            new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
                                              Executors.newCachedThreadPool());

    private static final CRCtrlMsgEncoder CR_CTRL_MSG_ENCODER = new CRCtrlMsgEncoder();
    private static final CRCtrlMsgDecoder CR_CTRL_MSG_DECODER = new CRCtrlMsgDecoder();

    // Custom shutdown hook/notifier
    private final Object  haltHook;
    private       boolean haltRequested;

    private final ProcessBuilder processBuilder;

    /**
     * Initialize the CRService.
     */
    private CRService(CRServiceConfig crServiceConfig) {
        this.haltHook = new Object();
        this.haltRequested = false;

        this.config = crServiceConfig;
        this.crBootstrap = new ServerBootstrap(CR_CTRL_CHANNEL_FACTORY);
        this.crCtrlChHandler = new CRCtrlChHandler(this);
        this.configureService();
        this.deferCleanup();

        this.processBuilder = new ProcessBuilder();
    }

    /**
     * Configure the service.
     */
    private void configureService() {
        this.crBootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(CR_CTRL_MSG_ENCODER, CR_CTRL_MSG_DECODER, crCtrlChHandler);
            }

        });
        this.crBootstrap.setOption("reuseAddr", true);
        this.crBootstrap.setOption("child.keepAlive", true);
        this.crBootstrap.setOption("child.tcpNoDelay", true);
        this.crBootstrap.setOption("child.sendBufferSize", Defaults.CHANNEL_BUF_SZ);
        this.crBootstrap.setOption("child.receiveBufferSize", Defaults.CHANNEL_BUF_SZ);
    }

    /**
     * Cleanup.
     */
    private void cleanup() {
        this.crCtrlChHandler.dumpServiceTimes();
        this.stopService();

        if (logger.isInfoEnabled()) {
            logger.info("cleanup> shutting down C/R service ...");
        }

        CR_CTRL_CHANNEL_FACTORY.releaseExternalResources();

        if (logger.isWarnEnabled()) {
            logger.warn("cleanup> C/R service is shutdown");
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
     * Start C/R service.
     */
    public void startService() {
        this.crCtrlCh =
                this.crBootstrap.bind(new InetSocketAddress(InetAddress.getLoopbackAddress(),
                                                            this.config.servicePort));

        if (logger.isInfoEnabled()) {
            logger.info("startService> service started");
        }
    }

    /**
     * Run C/R service.
     */
    public void runService() {
        while (true) {
            synchronized (this.haltHook) {
                if (this.haltRequested) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("runService> halt requested!");
                    }

                    break;
                } else {
                    try {
                        this.haltHook.wait(Defaults.APP_SHUTDOWN_WAIT);
                    } catch (InterruptedException e) {
                        logger.error("runService> failed to continue running the service; {}",
                                     e.getLocalizedMessage());

                        this.haltRequested = true;
                        break;
                    }
                }
            }
        }

        if (logger.isWarnEnabled()) {
            logger.warn("runService> received halt notification!");
        }
    }

    /**
     * Stop C/R service.
     */
    public void stopService() {
        if (this.crCtrlCh == null) {
            return;
        }

        if (logger.isInfoEnabled()) {
            logger.info("stopService> service is shutting down!");
        }

        this.crCtrlChHandler.terminateConnections();

        if (this.crCtrlCh.isConnected()) {
            this.crCtrlCh.disconnect();

            if (logger.isWarnEnabled()) {
                logger.warn("stopService> terminated client connections to listener!");
            }
        }

        if (this.crCtrlCh.isOpen()) {
            if (logger.isInfoEnabled()) {
                logger.info("stopService> no open connections to listener");
            }

            this.crCtrlCh.close();
        }

        this.crCtrlCh.unbind();

        if (logger.isInfoEnabled()) {
            logger.info("stopService> listener is shutdown");
        }
    }

    /**
     * Print usage information on output stream.
     *
     * @param out   output stream
     */
    public static void showUsage(PrintStream out) {
        out.println(String.format("usage: %s <properties file>", CRService.class.getCanonicalName()));
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            showUsage(System.err);
            System.exit(1);
        }

        CRServiceConfig crServiceConfig = null;
        try {
            crServiceConfig = CRServiceConfig.loadFromProperties(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.printf("Failed to configure service! %s\n", e.getLocalizedMessage());
            System.exit(1);
        }

        if (!CRService.checkBinaryPath(crServiceConfig.binaryPath)) {
            System.err.printf("Install C/R binary!\n");
            System.exit(1);
        }

        final CRService crService = new CRService(crServiceConfig);
        crService.startService();
        crService.runService();
    }

    @Override
    public CRCtrlMsg checkServiceStatus(CRCtrlMsg req) {
        if (CRService.checkBinaryPath(this.config.binaryPath)) {
            return new ServiceReply(ServiceReply.Status.OK);
        }
        return new ServiceReply(ServiceReply.Status.FAIL);
    }

    @Override
    public CRCtrlMsg registerProcess(CRCtrlMsg m) {
        RegisterProcess req = (RegisterProcess) m;
        if (CRService.registerProcess(this.config, req.proc)) {
            return new ServiceReply(ServiceReply.Status.OK);
        }
        return new ServiceReply(ServiceReply.Status.FAIL);
    }

    @Override
    public CRCtrlMsg checkpointProcess(CRCtrlMsg m) {
        CheckpointProcess req = (CheckpointProcess) m;
        if (CRService.checkpointProcess(this.config, req.proc, this.processBuilder)) {
            return new ServiceReply(ServiceReply.Status.OK);
        }
        return new ServiceReply(ServiceReply.Status.FAIL);
    }

    @Override
    public CRCtrlMsg restoreProcess(CRCtrlMsg m) {
        RestoreProcess req = (RestoreProcess) m;
        if (CRService.restoreProcess(this.config, req.proc, this.processBuilder)) {
            return new ServiceReply(ServiceReply.Status.OK);
        }
        return new ServiceReply(ServiceReply.Status.FAIL);
    }

    /**
     * Return path of the checkpoint storage location.
     *
     * @param basePath Base path to store application checkpoints
     * @param appId Application identifier
     * @return Checkpoint storage path associated with the given application
     */
    private static String getCheckpointStorePath(String basePath, String appId) {
        return String.format("%s%c%s", basePath, File.separatorChar, appId);
    }

    /**
     * Check if the binary path is valid.
     *
     * @param binaryPath Absolute path of the checkpoint/restore binary
     * @return true, if path is valid; false, otherwise
     */
    private static boolean checkBinaryPath(String binaryPath) {
        File binaryFile = new File(binaryPath);
        if (!binaryFile.exists() || !binaryFile.isFile()) {
            if (logger.isWarnEnabled()) {
                logger.warn("checkBinaryPath> cannot find C/R binary at {}", binaryPath);
            }
            return false;
        }
        if (!binaryFile.canRead()) {
            if (logger.isWarnEnabled()) {
                logger.warn("checkBinaryPath> cannot read C/R binary at {}", binaryPath);
            }
            return false;
        }
        if (!binaryFile.canExecute()) {
            if (logger.isWarnEnabled()) {
                logger.warn("checkBinaryPath> cannot execute C/R binary at {}", binaryPath);
            }
            return false;
        }
        return true;
    }

    /**
     * Register an application process with the C/R service.
     *
     * @param config C/R service configuration
     * @param proc Application process details
     * @return true, if the registration was successful; false, otherwise.
     */
    private static boolean registerProcess(CRServiceConfig config, RemoteEndpt proc) {
        // Create a location on the filesystem for storing checkpoints, if required
        String storePathname = CRService.getCheckpointStorePath(config.storagePath, proc.appId);
        File storePath = new File(storePathname);

        if (storePath.exists()) {
            if (storePath.isDirectory()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("registerProcess> storage path {} exists", storePath);
                }

                return true;
            } else {
                logger.error("registerProcess> storage path {} is not a directory!", storePath);

                return false;
            }
        }

        if (!storePath.mkdir()) {
            logger.error("registerProcess> failed to create storage path {}!", storePath);

            return false;
        }

        if (logger.isInfoEnabled()) {
            logger.info("registerProcess> storage path {} created", storePath);
        }

        return true;
    }

    /**
     * Checkpoint an application process using a C/R binary.
     *
     * @param config C/R service configuration
     * @param proc Application process details
     * @return true, if the checkpoint operation was successful; false, otherwise.
     */
    private static boolean checkpointProcess(CRServiceConfig config, RemoteEndpt proc, ProcessBuilder processBuilder) {
        processBuilder.command("/usr/bin/sudo",     // Run command as 'root'
                               config.binaryPath,   // Path of C/R binary
                               "dump",              // Select checkpoint operation
                               "-j",                // Application process is attached to a shell
                               "-D",                // Set checkpoint storage path
                               CRService.getCheckpointStorePath(config.storagePath, proc.appId),
                               "-t",                // Set PID
                               String.valueOf(proc.pid),
                               "-R");               // Do not halt the application process after a checkpoint
        if (logger.isDebugEnabled()) {
            logger.debug("checkpointProcess> {}", proc.appId);
        }

        try {
            return CRService.executeCommand(processBuilder);
        } catch (Exception e) {
            logger.error("checkpointProcess> Failed to checkpoint {}; {}", proc.appId, e.getLocalizedMessage());
        }

        return false;
    }

    /**
     * Restore an application process from a previous checkpoint using a C/R binary.
     *
     * @param config C/R service configuration
     * @param proc Application process details
     * @return true, if the restore operation was successful; false, otherwise.
     */
    private static boolean restoreProcess(CRServiceConfig config, RemoteEndpt proc, ProcessBuilder processBuilder) {
        processBuilder.command("/usr/bin/sudo",     // Run command as 'root'
                               config.binaryPath,   // Path of C/R binary
                               "restore",           // Select restore operation
                               "-j",                // Application process is attached to a shell
                               "-D",                // Set checkpoint storage path
                               CRService.getCheckpointStorePath(config.storagePath, proc.appId),
                               " --pidfile ",       // Set PID file path
                               AppLoader.getPIDFilepath(proc),
                               "-d");               // Detach the C/R binary from the restored process image

        if (logger.isDebugEnabled()) {
            logger.debug("restoreProcess> {}", proc.appId);
        }

        try {
            return CRService.executeCommand(processBuilder);
        } catch (Exception e) {
            logger.error("restoreProcess> Failed to restore {}; {}", proc.appId, e.getLocalizedMessage());
        }

        return false;
    }

    /**
     * Execute command and return a boolean indicating if execution was successful or not.
     *
     * @param processBuilder Instance of ProcessBuilder configured to execute the remote command.
     * @return true, if command executed successfully; false, otherwise
     * @throws IOException
     * @throws InterruptedException
     */
    private static boolean executeCommand(ProcessBuilder processBuilder) throws IOException, InterruptedException {
        final long tBeg = System.currentTimeMillis();
        final Process crProcess = processBuilder.start();
        final int exitStatus = crProcess.waitFor();
        final long tEnd = System.currentTimeMillis();

        if (logger.isDebugEnabled()) {
            logger.debug("executeCommand> spent {}ms; exit-code:{}",
                         tEnd - tBeg, exitStatus);
        }

        final BufferedReader brOut = new BufferedReader(new InputStreamReader(crProcess.getErrorStream()));
        String outLine;
        while ((outLine = brOut.readLine()) != null) {
            System.out.printf("  ~> %s\n", outLine);
        }
        System.out.flush();
        brOut.close();

        if (exitStatus == 0) {
            return true;
        }

        final BufferedReader brErr = new BufferedReader(new InputStreamReader(crProcess.getErrorStream()));
        String errLine;
        while ((errLine = brErr.readLine()) != null) {
            System.err.printf("  ~> %s\n", errLine);
        }
        System.err.flush();
        brErr.close();

        return false;
    }

}
