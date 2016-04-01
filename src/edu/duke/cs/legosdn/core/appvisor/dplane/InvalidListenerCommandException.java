package edu.duke.cs.legosdn.core.appvisor.dplane;

import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsgException;

/**
 * Exception encountered when an invalid value is received on the wire
 *  for the payload of a listener command message.
 *
 * @see edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsgType
 * @author bala
 */
public class InvalidListenerCommandException extends DPlaneMsgException {

    /**
     * Create a new exception instance.
     *
     * @param msg Error description.
     */
    public InvalidListenerCommandException(String msg) {
        super(msg);
    }

    /**
     * Create a new exception instance.
     *
     * @param msg Error description
     * @param e Exception instance (for stack traces)
     */
    public InvalidListenerCommandException(String msg, Throwable e) {
        super(msg, e);
    }

}
