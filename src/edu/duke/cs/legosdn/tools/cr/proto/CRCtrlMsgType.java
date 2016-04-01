package edu.duke.cs.legosdn.tools.cr.proto;

/**
 * Checkpoint/restore control message types.
 */
public enum CRCtrlMsgType {

    SERVICE_CHECK(ServiceCheck.class),          // Service status check
    REGISTER_PROC(RegisterProcess.class),       // Register a process with the C/R service
    CHECKPOINT_PROC(CheckpointProcess.class),   // Checkpoint a process
    RESTORE_PROC(RestoreProcess.class),         // Restore a process using previous checkpoint
    SERVICE_REPLY(ServiceReply.class),          // Service response to control message
    ;

    // Actual message type
    public final Class type;

    /**
     * Instantiate C/R control message type constants.
     *
     * @param msgType Message type (Class)
     */
    private CRCtrlMsgType(Class msgType) {
        this.type = msgType;
    }

}
