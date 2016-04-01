package edu.duke.cs.legosdn.core.netlog;

import java.io.IOException;
import java.util.List;

/**
 * NetLogClient defines the interface to communicate with NetLog.
 */
public interface NetLogClient {

    public static final NetLogMsg NETLOG_STATUS_OK = new NetLogMsg(new NetLogStatus(true));

    /**
     * Send NetLogInitiate message to NetLog listener.
     *
     * @param messageLog Instance of MessageLog
     * @return NetLogMsg indicating if initiation succeeded
     * @throws java.io.IOException
     * @see edu.duke.cs.legosdn.core.netlog.MessageLog
     * @see edu.duke.cs.legosdn.core.netlog.NetLogInitiate
     */
    public NetLogMsg sendNetLogInitiate(MessageLog messageLog) throws IOException;

    /**
     * Send NetLogCommit message to NetLog listener.
     *
     * @param messageLog Instance of MessageLog
     * @return NetLogMsg containing response, if any, to the commit issued
     * @throws java.io.IOException
     * @see edu.duke.cs.legosdn.core.netlog.MessageLog
     * @see edu.duke.cs.legosdn.core.netlog.NetLogCommit
     */
    public NetLogMsg sendNetLogCommit(MessageLog messageLog) throws IOException;

    /**
     * Send NetLogRollback message to NetLog listener.
     *
     * @param messageLogs List of message logs
     * @return NetLogMsg containing additional rollback actions
     * @throws java.io.IOException
     * @see edu.duke.cs.legosdn.core.netlog.MessageLog
     * @see edu.duke.cs.legosdn.core.netlog.NetLogCommit
     */
    public NetLogMsg sendNetLogRollback(List<MessageLog> messageLogs) throws IOException;

}
