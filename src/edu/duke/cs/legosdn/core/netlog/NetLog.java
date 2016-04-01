package edu.duke.cs.legosdn.core.netlog;

import edu.duke.cs.legosdn.core.Defaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * NetLog provides a TCP server that listens for logs of rules/policies installed to the network by different
 * applications. NetLog understands conflicts or dependencies across different flows
 */
public class NetLog {

    private static final Logger logger = LoggerFactory.getLogger(NetLog.class);

    public static final InetSocketAddress NETLOG_ADDR =
            new InetSocketAddress(InetAddress.getLoopbackAddress(), Defaults.NETLOG_PORT);

    // Network socket for receiving data plane communication
    private final ServerSocketChannel listener;

    // Transaction log
    private final XDepGraph xDepGraph;

    /**
     * Initialize NetLog.
     * @throws IOException
     */
    public NetLog() throws IOException {
        this.xDepGraph = new XDepGraph();

        this.listener = ServerSocketChannel.open();
        // NOTE: Run listener in blocking mode
        this.listener.configureBlocking(true);
        this.listener.socket().setSoTimeout((int) Defaults.APP_DP_RESP_WAIT_TIME);
        this.listener.socket().setReuseAddress(true);
        this.listener.socket().setReceiveBufferSize(Defaults.CHANNEL_BUF_SZ);
        this.listener.socket().bind(NetLog.NETLOG_ADDR);

        this.deferCleanup();
    }

    /**
     * Start the NetLog service.
     */
    public void start() {
        if (logger.isInfoEnabled()) {
            logger.info("start> NetLog started");
        }

        while (true) {
            try {
                final SocketChannel socketChannel = this.listener.accept();

                if (logger.isDebugEnabled()) {
                    logger.debug("NetLog> Connection from {}", socketChannel.getRemoteAddress());
                }

                new Thread(new NetLogUpdateHandler(this.xDepGraph, socketChannel)).start();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("NetLog> failed to accept connection; {}", e.getLocalizedMessage());

                System.exit(1);
            }
        }
    }

    /**
     * Stop the NetLog service.
     */
    public void stop() {
        try {
            this.cleanup();
        } catch (IOException e) {
            logger.warn("stop> failed to cleanup properly; {}", e.getLocalizedMessage());
        }
    }

    /**
     * Cleanup.
     */
    private void cleanup() throws IOException {
        if (this.listener.isOpen()) {
            this.listener.close();
        }

        if (logger.isWarnEnabled()) {
            logger.warn("cleanup> listener is closed");
        }
    }

    /**
     * Cleanup on a deferred thread.
     */
    private void deferCleanup() {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                try {
                    cleanup();
                } catch (IOException e) {
                    logger.error("deferCleanup> {}", e.getLocalizedMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        NetLog netLog = null;
        try {
            netLog = new NetLog();
            netLog.start();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("NetLog> initialization failed; {}", e.getLocalizedMessage());
            System.exit(1);
        } finally {
            if (netLog == null) {
                return;
            }

            netLog.stop();
        }
    }

}
