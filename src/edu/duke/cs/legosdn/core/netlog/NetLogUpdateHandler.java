package edu.duke.cs.legosdn.core.netlog;

import edu.duke.cs.legosdn.core.Defaults;
import edu.duke.cs.legosdn.core.util.ByteBufferBackedOutStream;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.nustaq.serialization.util.FSTInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NetLogUpdateHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NetLogUpdateHandler.class);

    // Network-wide transactions log
    private final XDepGraph xDepGraph;

    // Channel over which NetLog updates are delivered
    private final SocketChannel socketChannel;

    // Buffer for receiving and sending netlog messages
    private final ByteBuffer inBuffer, outBuffer;

    private static final NetLogMsg NETLOG_STATUS_OK = new NetLogMsg(new NetLogStatus(true));

    /**
     * Instantiate NetLogUpdateHandler with connection from TransactionManager.
     *
     * @param xDepGraph Network-wide transaction log
     * @param socketChannel Connection from TransactionManager.
     * @see TransactionMgr
     */
    public NetLogUpdateHandler(XDepGraph xDepGraph, SocketChannel socketChannel) {
        this.xDepGraph = xDepGraph;
        this.socketChannel = socketChannel;
        this.inBuffer = ByteBuffer.allocate(Defaults.CHANNEL_BUF_SZ);
        this.outBuffer = ByteBuffer.allocate(Defaults.CHANNEL_BUF_SZ);
    }

    /**
     * Handle NetLogInitiate message from TransactionManager.
     *
     * @param m Instance of NetLogInitiate message
     * @see NetLogInitiate
     */
    private NetLogMsg handleNetLogInitiate(NetLogInitiate m) {
        if (logger.isDebugEnabled()) {
            logger.info("handleNetLogInitiate> switch {} => SDN-App {}", m.getMsg().getSwitchID(), m.getAppId());
        }

        this.xDepGraph.update(m);
        return NETLOG_STATUS_OK;
    }

    /**
     * Handle NetLogCommit message from TransactionManager.
     *
     * @param m Instance of NetLogCommit message
     * @see NetLogCommit
     */
    private NetLogMsg handleNetLogCommit(NetLogCommit m) {
        this.xDepGraph.update(m);
        return null;
    }

    /**
     * Handle NetLogRollback message from TransactionManager.
     *
     * @param m Instance of NetLogRollback message
     * @see NetLogRollback
     */
    private NetLogMsg handleNetLogRollback(NetLogRollback m) {
        return new NetLogMsg(this.xDepGraph.revert(m));
    }

    /**
     * Handle NetLogMsg from TransactionManager.
     *
     * @param m Instance of NetLogMsg received from TransactionManager.
     * @return Response message from NetLog
     * @throws IOException
     * @see NetLogMsg
     * @see TransactionMgr
     */
    public NetLogMsg handleMessage(NetLogMsg m) throws IOException {
        switch (m.getNetLogMsgType()) {
            case NETLOG_INITIATE:
                return this.handleNetLogInitiate((NetLogInitiate) m.getNetLogMsg());
            case NETLOG_COMMIT:
                return this.handleNetLogCommit((NetLogCommit) m.getNetLogMsg());
            case NETLOG_ROLLBACK:
                return this.handleNetLogRollback((NetLogRollback) m.getNetLogMsg());
            default:
                throw new RuntimeException("Invalid NetLogMsg!");
        }
    }

    /**
     * Send NetLog's response to controller.
     *
     * @param m NetLog's response message
     * @throws IOException
     */
    private void respondWith(NetLogMsg m) throws IOException {
        this.serialize(m);
        this.socketChannel.write(this.outBuffer);
    }

    /**
     * Serialize NetLogMsg to send it over the wire.
     *
     * @param m NetLog's response message
     * @throws IOException
     */
    private void serialize(NetLogMsg m) throws IOException {
        this.outBuffer.clear();
        final FSTObjectOutput out = new FSTObjectOutput(new ByteBufferBackedOutStream(this.outBuffer));
        out.writeObject(m);
        out.close();
        this.outBuffer.flip();
    }

    /**
     * Read data from socket channel and deserialize an instance of NetLogMsg from it.
     *
     * @return Instance of NetLogMsg deserialized using data read from the channel
     * @throws IOException
     */
    private NetLogMsg deserialize() throws IOException {
        this.inBuffer.clear();

        final int n = this.socketChannel.read(this.inBuffer);

        if (logger.isTraceEnabled()) {
            logger.trace("deserialize> read {} bytes", n);
        }

        final FSTObjectInput in = new FSTObjectInput(new FSTInputStream(this.inBuffer.array()));
        try {
            return (NetLogMsg) in.readObject();
        } catch (ClassNotFoundException e) {
            logger.error("deserialize> deserialization failed! {}", e.getLocalizedMessage());
            throw new IOException("Failed to deserialize NetLogMsg!", e);
        } finally {
            in.close();
        }
    }

    @Override
    public void run() {
        int exitStatus = 0;
        try {
            while (true) {
                final NetLogMsg req = this.deserialize();
                final NetLogMsg res = this.handleMessage(req);
                if (res != null) {
                    this.respondWith(res);
                }
            }
        } catch (Exception e) {
            logger.error("NetLogUpdateHandler> crashed! {}", e.getLocalizedMessage());
            exitStatus = 1;
        } finally {
            this.cleanup();
        }

        if (exitStatus == 0) {
            if (logger.isInfoEnabled()) {
                logger.info("NetLogUpdateHandler> exited OK!");
            }

            return;
        }

        logger.error("NetLogUpdateHandler> exited with errors!");
    }

    /**
     * Cleanup.
     */
    private void cleanup() {
        if (this.socketChannel == null) {
            return;
        }

        if (!(this.socketChannel.isConnected() ||
              this.socketChannel.isOpen())) {
            return;
        }

        try {
            this.socketChannel.close();
        } catch (IOException e) {
            // NOTE: At this point, we cannot do anything!
            e.printStackTrace();
        }
    }

}
