package edu.duke.cs.legosdn.core.netlog;

/**
 * NetLogMsgType defines the messages supported by NetLog.
 */
public enum NetLogMsgType {

    NETLOG_INITIATE(NetLogInitiate.class),
    NETLOG_STATUS(NetLogStatus.class),
    NETLOG_COMMIT(NetLogCommit.class),
    NETLOG_ROLLBACK(NetLogRollback.class),
    ;

    public final Class<? extends NetLogMsgIO> type;

    /**
     * Initialize NetLogMsgType.
     *
     * @param type NetLog message class.
     */
    private NetLogMsgType(Class<? extends NetLogMsgIO> type) {
        this.type = type;
    }

}
