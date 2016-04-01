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
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

/**
 * CRUDPClient provides methods to talk to a C/R service provider.
 */
public class CRUDPClient implements CRClientProvider {

    private static final Logger logger = LoggerFactory.getLogger(CRUDPClient.class);

    // Socket to connect to the C/R service
    private final DatagramChannel socketChannel;

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
    public CRUDPClient() throws IOException {
        this.haltHook = new Object();
        this.haltRequested = false;

        this.socketChannel = DatagramChannel.open();
        this.socketChannel.socket().setSoTimeout((int) Defaults.CR_RESP_WAIT_TIME);
        this.socketChannel.configureBlocking(true);

        this.crSrvcSocketAddr = new InetSocketAddress(InetAddress.getLoopbackAddress(),
                                                      Defaults.CR_DEF_SERVICE_PORT);

        this.inBuffer = ByteBuffer.allocate(Defaults.CHANNEL_BUF_SZ);
        this.outBuffer = ByteBuffer.allocate(Defaults.CHANNEL_BUF_SZ);

        this.deferCleanup();
    }

    @Override
    public boolean connect() {
        // NOTE: Since the protocol is UDP, no test is being performed here to check if C/R service is running.
        try {
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
            this.socketChannel.disconnect();

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
//        final CriuRPC.criu_req criuReq =
//                CriuRPC.criu_req.newBuilder()
//                                .setType(CriuRPC.criu_req_type.CHECK)
//                                .setNotifySuccess(true)
//                                .build();
//
//        try {
//            final CriuRPC.criu_resp criuResp = this.callCRService(criuReq);
//            return criuResp.getSuccess();
//        } catch (IOException e) {
//            if (logger.isWarnEnabled()) {
//                logger.warn("isServiceReady> Failed to check if C/R service is ready; {}", e.getLocalizedMessage());
//            }
//
//            return false;
//        }
        return true;
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
        this.outBuffer.clear();
        final ByteBufferBackedOutStream outStream = new ByteBufferBackedOutStream(outBuffer);
        criuReq.writeTo(outStream);
        this.outBuffer.flip();
        this.socketChannel.send(this.outBuffer, this.crSrvcSocketAddr);

        this.inBuffer.clear();
        this.socketChannel.receive(this.inBuffer);
        final CriuRPC.criu_resp criuResp =
                CriuRPC.criu_resp.parseFrom(Arrays.copyOf(this.inBuffer.array(),
                                                          this.inBuffer.position()));

        return criuResp;
    }

    @Override
    public boolean checkpointProcess(RemoteEndpt proc, Integer appNum) {
        final CriuRPC.criu_opts criuOpts =
                CriuRPC.criu_opts.newBuilder()
                                 .setImagesDirFd(0)     // NOTE: This value will be over-ridden in the service!
                                 .setPid(proc.pid)
                                 .setLeaveRunning(true)
                                 .setShellJob(true)
                                 .setAppNum(appNum)
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
            logger.error("checkpointProcess> Failed to checkpoint {}; {}", proc.pid, e.getLocalizedMessage());
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
