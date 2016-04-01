package edu.duke.cs.legosdn.core.appvisor.dplane;

import java.io.IOException;

/**
 * Base class for exceptions encountered in the data plane.
 *
 * @author bala
 */
public class DPlaneMsgException extends IOException {

    /**
     * Create a new exception instance.
     *
     * @param msg Error description
     */
    public DPlaneMsgException(String msg) {
        super(msg);
    }

    /**
     * Create a new exception instance.
     *
     * @param msg Error description
     * @param e Exception instance (for stack traces)
     */
    public DPlaneMsgException(String msg, Throwable e) {
        super(msg, e);
    }

}
