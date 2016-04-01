package edu.duke.cs.legosdn.core.netlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * NetLogDummyClient mocks a NetLog client but does not do anything
 */
public class NetLogDummyClient implements NetLogClient {

    private static final Logger logger = LoggerFactory.getLogger(NetLogDummyClient.class);

    @Override
    public NetLogMsg sendNetLogInitiate(MessageLog messageLog) throws IOException {
        if (logger.isTraceEnabled()) {
            logger.trace("sendNetLogInitiate> {} => {}", messageLog.msg.getSwitchID(), messageLog.app);
        }

        return NETLOG_STATUS_OK;
    }

    @Override
    public NetLogMsg sendNetLogCommit(MessageLog messageLog) throws IOException {
        if (logger.isTraceEnabled()) {
            logger.trace("sendNetLogCommit> {} => {}", messageLog.msg.getSwitchID(), messageLog.app);
        }

        return null;
    }

    @Override
    public NetLogMsg sendNetLogRollback(List<MessageLog> messageLogs) throws IOException {
        if (logger.isTraceEnabled()) {
            logger.trace("sendNetLogRollback> {} rollback messages => {}", messageLogs.size(), messageLogs.get(0).app);
        }

        return null;
    }

}
