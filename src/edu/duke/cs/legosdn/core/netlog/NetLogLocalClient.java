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
public class NetLogLocalClient implements NetLogClient {

    private static final Logger logger = LoggerFactory.getLogger(NetLogLocalClient.class);

    private final XDepGraph xDepGraph;

    // Singleton instance
    private static NetLogLocalClient instance;

    static {
        NetLogLocalClient.instance = null;
    }

    /**
     * @return Instance of NetLogLocalClient.
     */
    public static synchronized NetLogLocalClient getInstance() {
        if (NetLogLocalClient.instance == null) {
            NetLogLocalClient.instance = new NetLogLocalClient();
        }
        return NetLogLocalClient.instance;
    }

    /**
     * Initialize NetLogLocalClient.
     *
     * @throws java.io.IOException
     */
    private NetLogLocalClient() {
        this.xDepGraph = new XDepGraph();
    }

    @Override
    public NetLogMsg sendNetLogInitiate(MessageLog messageLog) throws IOException {
        if (DPlaneMsgType.isNotification(messageLog.msg.getMsgType())) {
            /* Nothing to be done to notifications */
            return NETLOG_STATUS_OK;
        }

        final NetLogInitiate m = new NetLogInitiate(messageLog.msg, messageLog.app);
        this.xDepGraph.update(m);
        return NETLOG_STATUS_OK;
    }

    @Override
    public NetLogMsg sendNetLogCommit(MessageLog messageLog) throws IOException {
        final NetLogCommit m = new NetLogCommit(messageLog.msg, messageLog.app);
        this.xDepGraph.update(m);
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
        return new NetLogMsg(this.xDepGraph.revert(m));
    }

}
