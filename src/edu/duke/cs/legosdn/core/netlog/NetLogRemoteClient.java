package edu.duke.cs.legosdn.core.netlog;

import edu.duke.cs.legosdn.core.Defaults;
import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsgExternalizable;
import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsgType;
import edu.duke.cs.legosdn.core.util.ByteBufferBackedOutStream;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.nustaq.serialization.util.FSTInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * NetLogRemoteClient abstracts the code used for communicating with NetLog running remotely as a separate component.
 */
public class NetLogRemoteClient implements NetLogClient {

    private static final Logger logger = LoggerFactory.getLogger(NetLogRemoteClient.class);

    // Channel to communicate with remote NetLog component
    private final SocketChannel socketChannel;

    // Buffer for receiving and sending NetLog messages
    private final ByteBuffer inBuffer, outBuffer;

    /**
     * Initialize NetLogRemoteClient.
     *
     * @throws IOException
     */
    public NetLogRemoteClient() throws IOException {
        this.inBuffer = ByteBuffer.allocate(Defaults.CHANNEL_BUF_SZ);
        this.outBuffer = ByteBuffer.allocate(Defaults.CHANNEL_BUF_SZ);
        this.socketChannel = SocketChannel.open();
        this.connectToNetLog();
    }

    /**
     * Open a persistent connection to NetLog.
     *
     * @throws IOException
     */
    private void connectToNetLog() throws IOException {
        // NOTE: Run data plane client in blocking mode
        this.socketChannel.configureBlocking(false);
        this.socketChannel.socket().setKeepAlive(true);
        this.socketChannel.socket().setSoTimeout((int) Defaults.APP_DP_RESP_WAIT_TIME);
        this.socketChannel.socket().setTcpNoDelay(true);
        this.socketChannel.socket().setSendBufferSize(Defaults.CHANNEL_BUF_SZ);
        this.socketChannel.socket().setReceiveBufferSize(Defaults.CHANNEL_BUF_SZ);

        this.socketChannel.connect(NetLog.NETLOG_ADDR);
        while (!this.socketChannel.isConnected()) {
            this.socketChannel.finishConnect();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("TransactionMgr> connected to NetLog");
        }
    }

    /**
     * Serialize an instance of NetLogMsg.
     *
     * @param netLogMsg Instance of NetLogMsg
     * @throws IOException
     */
    private void serialize(NetLogMsg netLogMsg) throws IOException {
        this.outBuffer.clear();
        final FSTObjectOutput out = new FSTObjectOutput(new ByteBufferBackedOutStream(this.outBuffer));
        out.writeObject(netLogMsg);
        out.close();
        this.outBuffer.flip();
    }

    /**
     * De-serialize an instance of NetLogMsg from the data received over the wire.
     *
     * @return NetLogMsg received over the wire
     * @throws IOException
     */
    private NetLogMsg deserialize() throws IOException {
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
    public NetLogMsg sendNetLogInitiate(MessageLog messageLog) throws IOException {
        if (DPlaneMsgType.isNotification(messageLog.msg.getMsgType())) {
            /* Nothing to be done to notifications */
            return NETLOG_STATUS_OK;
        }

        final NetLogInitiate m = new NetLogInitiate(messageLog.msg, messageLog.app);
        final NetLogMsg netLogMsg = new NetLogMsg(m);

        try {
            this.serialize(netLogMsg);
        } catch (IOException e) {
            logger.error("sendNetLogInitiate> failed to serialize NetLogInitiate! {}", e.getLocalizedMessage());
            throw e;
        }
        this.socketChannel.write(this.outBuffer);

        if (logger.isTraceEnabled()) {
            logger.trace("sendNetLogInitiate> {} => {}", m.getMsg().getSwitchID(), m.getAppId());
        }

        /*/
        while (true) {
            this.inBuffer.clear();
            final int n = this.socketChannel.read(this.inBuffer);

            if (n > 0) {
                break;
            }

            if (logger.isTraceEnabled()) {
                logger.trace("deserialize> read {} bytes", n);
            }
        }

        try {
            final NetLogMsg nm = this.deserialize();

            if (nm.getNetLogMsgType() != NetLogMsgType.NETLOG_STATUS) {
                final String err = String.format("Expected '%s', received '%s'",
                                                 NetLogMsgType.NETLOG_STATUS, nm.getNetLogMsgType());
                logger.error(String.format("sendNetLogInitiate> %s", err));
                throw new IOException(err);
            }

            final NetLogStatus netLogStatus = (NetLogStatus) nm.getNetLogMsg();
            if (!netLogStatus.isOk()) {
                logger.error("sendNetLogInitiate> transaction cannot proceed!");
                throw new IOException("Transaction cannot proceed!");
            }
        } catch (IOException e) {
            logger.error("sendNetLogInitiate> failed to serialize NetLogInitiate! {}", e.getLocalizedMessage());
            throw e;
        }
        //*/

        return NETLOG_STATUS_OK;
    }

    @Override
    public NetLogMsg sendNetLogCommit(MessageLog messageLog) throws IOException {
        final NetLogCommit m = new NetLogCommit(messageLog.msg, messageLog.app);
        final NetLogMsg netLogMsg = new NetLogMsg(m);

        try {
            this.serialize(netLogMsg);
        } catch (IOException e) {
            logger.error("sendNetLogCommit> failed to serialize NetLogCommit! {}", e.getLocalizedMessage());
            throw e;
        }
        this.socketChannel.write(this.outBuffer);

        if (logger.isTraceEnabled()) {
            logger.trace("sendNetLogCommit> {} => {}", m.getMsg().getSwitchID(), m.getAppId());
        }

        return null;
    }

    @Override
    public NetLogMsg sendNetLogRollback(List<MessageLog> messageLogs) throws IOException {
        if (messageLogs.isEmpty()) {
            return null;
        }

        // Application identifier should be the same across all messages.
        String appId = null;

        List<DPlaneMsgExternalizable> msgs = new ArrayList<DPlaneMsgExternalizable>(messageLogs.size());
        for (MessageLog m : messageLogs) {
            appId = m.app;
            msgs.add(m.msg);
        }

        final NetLogRollback m = new NetLogRollback(msgs, appId);
        final NetLogMsg netLogMsg = new NetLogMsg(m);

        try {
            this.serialize(netLogMsg);
        } catch (IOException e) {
            logger.error("sendNetLogRollback> failed to serialize NetLogRollback! {}", e.getLocalizedMessage());
            throw e;
        }
        this.socketChannel.write(this.outBuffer);

        // FIXME: What about rollback responses back from NetLog?

        if (logger.isTraceEnabled()) {
            logger.trace("sendNetLogRollback> {} rollback messages => {}", m.getMsgs().size(), m.getAppId());
        }

        return null;
    }

}
