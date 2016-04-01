package edu.duke.cs.legosdn.core.appvisor.dplane;

import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsgException;

/**
 * Exception encountered when a wrong message type is received on the wire from the remote applications.
 *
 * @author bala
 */
public class InvalidOutboundMsgException extends DPlaneMsgException {

    /**
     * Create a new exception instance.
     *
     * @param msg Error description.
     */
    public InvalidOutboundMsgException(String msg) {
        super(msg);
    }

    /**
     * Create a new exception instance.
     *
     * @param msg Error description
     * @param e Exception instance (for stack traces)
     */
    public InvalidOutboundMsgException(String msg, Throwable e) {
        super(msg, e);
    }

}
