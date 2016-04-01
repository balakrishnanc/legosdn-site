package edu.duke.cs.legosdn.tools.cr.cli;

import edu.duke.cs.legosdn.core.Defaults;
import edu.duke.cs.legosdn.core.appvisor.cplane.RemoteEndpt;
import edu.duke.cs.legosdn.core.util.ByteBufferBackedOutStream;
import edu.duke.cs.legosdn.tools.cr.CRClientProvider;
import edu.duke.cs.legosdn.tools.cr.criu.CriuRPC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * CRTCPClient provides methods to talk to a C/R service provider.
 */
public class CRTCPClient implements CRClientProvider {

    private static final Logger logger = LoggerFactory.getLogger(CRTCPClient.class);

    // Socket to connect to the C/R service
    private SocketChannel socketChannel;

    // C/R service endpoint information
    private final InetSocketAddress crSrvcSocketAddr;

    // Buffers to communicate with the C/R service
    private final ByteBuffer inBuffer;
    private final ByteBuffer outBuffer;

    // Custom shutdown hook/notifier
    private final Object  haltHook;
    private       boolean haltRequested;

    /**
     * Initialize C/R service client.
     */
    public CRTCPClient() throws IOException {
        this.haltHook = new Object();
        this.haltRequested = false;

        this.crSrvcSocketAddr = new InetSocketAddress(InetAddress.getLoopbackAddress(),
                                                      Defaults.CR_DEF_SERVICE_PORT);

        this.inBuffer = ByteBuffer.allocate(Defaults.CHANNEL_BUF_SZ);
        this.outBuffer = ByteBuffer.allocate(Defaults.CHANNEL_BUF_SZ);

        this.deferCleanup();
    }

    /**
     * Initialize channel.
     *
     * @throws IOException
     */
    private void initialize() throws IOException {
        this.socketChannel = SocketChannel.open();
        this.socketChannel.socket().setSoTimeout((int) Defaults.CR_RESP_WAIT_TIME);
        this.socketChannel.socket().setTcpNoDelay(true);
        this.socketChannel.socket().setKeepAlive(false);
        this.socketChannel.configureBlocking(true);
    }

    @Override
    public boolean connect() {
        try {
            this.initialize();
            this.socketChannel.connect(this.crSrvcSocketAddr);

            if (logger.isDebugEnabled()) {
                logger.debug("connect> Connected to C/R service");
            }

            return this.socketChannel.isConnected();
        } catch (IOException e) {
            logger.error("connect> Failed to connect to C/R service! {}", e.getLocalizedMessage());
        }

        return false;
    }

    @Override
    public void disconnect() {
        try {
            if (this.socketChannel.isConnected() || this.socketChannel.isOpen()) {
                this.socketChannel.shutdownOutput();
                this.socketChannel.shutdownInput();
                this.socketChannel.close();
            }

            if (logger.isDebugEnabled()) {
                logger.debug("connect> Disconnected from C/R service");
            }
        } catch (IOException e) {
            logger.error("connect> Failed to disconnect from C/R service! {}", e.getLocalizedMessage());
        }
    }

    /**
     * Cleanup.
     */
    private void cleanup() {
        this.disconnect();

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
        final CriuRPC.criu_req criuReq =
                CriuRPC.criu_req.newBuilder()
                                .setType(CriuRPC.criu_req_type.CHECK)
                                .setNotifySuccess(true)
                                .build();

        try {
            final CriuRPC.criu_resp criuResp = this.callCRService(criuReq);
            return criuResp.getSuccess();
        } catch (IOException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("isServiceReady> Failed to check if C/R service is ready; {}", e.getLocalizedMessage());
            }

            return false;
        }
    }

    @Override
    public boolean registerProcess(RemoteEndpt proc) {
        // FIXME: Prepare output directory and communicate that to the remote process.
        return true;
    }

    /**
     * Call C/R service.
     *
     * @param criuReq C/R request
     * @return C/R service response
     * @throws IOException
     */
    private CriuRPC.criu_resp callCRService(CriuRPC.criu_req criuReq) throws IOException {
        try {
            this.connect();
            this.outBuffer.clear();
            final ByteBufferBackedOutStream outStream = new ByteBufferBackedOutStream(outBuffer);
            criuReq.writeTo(outStream);
            this.outBuffer.flip();
            this.socketChannel.write(this.outBuffer);
        } catch (IOException e) {
            final String err = String.format("callCRService: failed to send C/R request; %s",
                                             e.getLocalizedMessage());
            throw new IOException(err, e);
        }

        int nRd = 0;
        try {
            this.inBuffer.clear();
            nRd = this.socketChannel.read(this.inBuffer);
            this.disconnect();
            final CriuRPC.criu_resp criuResp = CriuRPC.criu_resp.parseFrom(Arrays.copyOf(this.inBuffer.array(),
                                                                                         this.inBuffer.position()));
            return criuResp;
        } catch (IOException e) {
            final String err = String.format("callCRService: failed to receive C/R response (read %d bytes); %s",
                                             nRd, e.getLocalizedMessage());
            throw new IOException(err, e);
        }
    }

    @Override
    public boolean checkpointProcess(RemoteEndpt proc, Integer appNum) {
        final CriuRPC.criu_opts criuOpts =
                CriuRPC.criu_opts.newBuilder()
                                 .setImagesDirFd(0)     // NOTE: This value will be over-ridden in the service!
                                 .setPid(proc.pid)
                                 .setLeaveRunning(true)
                                 .setTrackMem(true)
                                 .setShellJob(true)
                                 .setAppNum(appNum)
                                 .setTcpEstablished(true)
                                 .build();

        final CriuRPC.criu_req criuReq =
                CriuRPC.criu_req.newBuilder()
                                .setType(CriuRPC.criu_req_type.DUMP)
                                .setOpts(criuOpts)
                                .setNotifySuccess(true)
                                .build();

        try {
            final CriuRPC.criu_resp criuResp = this.callCRService(criuReq);
            return criuResp.getSuccess();
        } catch (IOException e) {
            logger.error("checkpointProcess> Failed to checkpoint process '{}'; {}",
                         proc.pid, e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public int restoreProcess(RemoteEndpt proc, Integer appNum) {
        final CriuRPC.criu_opts criuOpts =
                CriuRPC.criu_opts.newBuilder()
                                 .setImagesDirFd(0)
                                 .setPid(proc.pid)
                                 .setShellJob(true)
                                 .setAppNum(appNum)
                                 .setTcpEstablished(true)
                                 .build();

        final CriuRPC.criu_req criuReq =
                CriuRPC.criu_req.newBuilder()
                                .setType(CriuRPC.criu_req_type.RESTORE)
                                .setOpts(criuOpts)
                                .setNotifySuccess(true)
                                .build();

        try {
            final CriuRPC.criu_resp criuResp = this.callCRService(criuReq);
            return criuResp.getSuccess() ? criuResp.getRestore().getPid() : CRClientProvider.INVALID_PROC_PID;
        } catch (IOException e) {
            logger.error("checkpointProcess> Failed to checkpoint {}; {}", proc.pid, e.getLocalizedMessage());
            return CRClientProvider.INVALID_PROC_PID;
        }
    }

}
