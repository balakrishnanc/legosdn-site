
package edu.duke.cs.legosdn.core.appvisor.cplane;

public class CPlaneMsgException extends Exception {

    /**
     * Create a new exception instance.
     *
     * @param msg Error description
     */
    public CPlaneMsgException(String msg) {
        super(msg);
    }

    /**
     * Create a new exception instance.
     *
     * @param msg Error description
     * @param e Exception instance (for stack traces)
     */
    public CPlaneMsgException(String msg, Throwable e) {
        super(msg, e);
    }

}
