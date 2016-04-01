package edu.duke.cs.legosdn.tools.cr.cli;

import edu.duke.cs.legosdn.core.Defaults;
import edu.duke.cs.legosdn.core.appvisor.cplane.RemoteEndpt;
import edu.duke.cs.legosdn.core.appvisor.stub.AppLoader;
import edu.duke.cs.legosdn.tools.cr.CRClientProvider;
import edu.duke.cs.legosdn.tools.cr.proto.*;
import edu.duke.cs.legosdn.tools.cr.srv.CRServiceConfig;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * CRServiceClient provides methods to talk to a C/R service provider.
 */
public class CRNettyTCPClient implements CRClientProvider {

    private static final Logger logger = LoggerFactory.getLogger(CRNettyTCPClient.class);

    private final ClientBootstrap            crBootstrap;
    // Client side C/R control channel handler
    private final CRCtrlChHandler<CRCtrlMsg> chHandler;
    // Channel to communicate with the C/R service
    private       Channel                    channel;

    private static final ChannelFactory CR_CTRL_CHANNEL_FACTORY =
            new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
                                              Executors.newCachedThreadPool(),
                                              1);

    private static final CRCtrlMsgEncoder CR_CTRL_MSG_ENCODER = new CRCtrlMsgEncoder();
    private static final CRCtrlMsgDecoder CR_CTRL_MSG_DECODER = new CRCtrlMsgDecoder();

    // Custom shutdown hook/notifier
    private final Object  haltHook;
    private       boolean haltRequested;

    /**
     * Initialize C/R service client.
     */
    public CRNettyTCPClient() {
        this.haltHook = new Object();
        this.haltRequested = false;

        this.crBootstrap = new ClientBootstrap(CR_CTRL_CHANNEL_FACTORY);
        this.chHandler = new CRCtrlChHandler<CRCtrlMsg>(this);
        this.channel = null;
        this.configureClient();

        this.deferCleanup();
    }

    private void configureClient() {
        this.crBootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(CR_CTRL_MSG_ENCODER, CR_CTRL_MSG_DECODER, chHandler);
            }

        });
        // NOTE: connection timeout option does not seem to work in Netty v3.2.6.
        this.crBootstrap.setOption("connectTimeoutMillis", Defaults.CONN_WAIT_TIMEOUT);
        this.crBootstrap.setOption("tcpNoDelay", true);
        this.crBootstrap.setOption("keepAlive", true);
        this.crBootstrap.setOption("sendBufferSize", Defaults.CHANNEL_BUF_SZ);
        this.crBootstrap.setOption("receiveBufferSize", Defaults.CHANNEL_BUF_SZ);
    }

    @Override
    public boolean connect() {
        // FIXME: Handle scenario where C/R service is running on a non-default port.
        final InetSocketAddress srvAddr =
                new InetSocketAddress(InetAddress.getLoopbackAddress(), Defaults.CR_DEF_SERVICE_PORT);

        ChannelFuture cplaneChFuture = this.crBootstrap.connect(srvAddr);

        cplaneChFuture.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (f.isSuccess()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("connect> Connected to C/R service");
                    }
                } else {
                    logger.error("connect> Failed to connect to C/R service!");
                }
            }

        });

        // Wait while connection attempt succeeds/fails
        while (!cplaneChFuture.isDone());
        this.channel = cplaneChFuture.getChannel();
        return this.channel.isConnected();
    }

    @Override
    public void disconnect() {
        if (this.channel == null) {
            return;
        }
        if (!this.channel.isConnected()) {
            return;
        }
        if (!this.channel.isOpen()) {
            return;
        }
        this.channel.close();
    }

    /**
     * Cleanup.
     */
    private void cleanup() {
        this.disconnect();

        if (logger.isInfoEnabled()) {
            logger.info("cleanup> shutting down C/R client ...");
        }

        CR_CTRL_CHANNEL_FACTORY.releaseExternalResources();

        if (logger.isWarnEnabled()) {
            logger.warn("cleanup> C/R client is shutdown");
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

    @Override
    public boolean isServiceReady() {
        this.channel.write(new ServiceCheck());
        ServiceReply reply = (ServiceReply) this.chHandler.waitForMessage();
        if (reply == null) {
            // FIXME: Something went wrong with the C/R service; should we initiate halt?
            if (logger.isWarnEnabled()) {
                logger.warn("isServiceReady> received no response!");
            }
            return false;
        }
        return reply.isOK();
    }

    @Override
    public boolean registerProcess(RemoteEndpt proc) {
        this.channel.write(new RegisterProcess(proc));
        ServiceReply reply = (ServiceReply) this.chHandler.waitForMessage();
        if (reply == null) {
            // FIXME: Something went wrong with the C/R service; should we initiate halt?
            if (logger.isWarnEnabled()) {
                logger.warn("registerProcess> received no response!");
            }
            return false;
        }
        return reply.isOK();
    }

    @Override
    public boolean checkpointProcess(RemoteEndpt proc, Integer appNum) {
        this.channel.write(new CheckpointProcess(proc));
        ServiceReply reply = (ServiceReply) this.chHandler.waitForMessage();
        if (reply == null) {
            // FIXME: Something went wrong with the C/R service; should we initiate halt?
            if (logger.isWarnEnabled()) {
                logger.warn("checkpointProcess> received no response!");
            }
            return false;
        }
        return reply.isOK();
    }

    @Override
    public int restoreProcess(RemoteEndpt proc, Integer appNum) {
        this.channel.write(new RestoreProcess(proc));
        ServiceReply reply = (ServiceReply) this.chHandler.waitForMessage();
        if (reply == null) {
            // FIXME: Something went wrong with the C/R service; should we initiate halt?
            if (logger.isWarnEnabled()) {
                logger.warn("restoreProcess> received no response!");
            }
            return CRClientProvider.INVALID_PROC_PID;
        }

        // FIXME: Return the actual PID of the restored process.
        return reply.isOK() ? AppLoader.loadPIDFromFile(proc) : CRClientProvider.INVALID_PROC_PID;
    }

}
