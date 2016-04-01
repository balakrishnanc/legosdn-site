package edu.duke.cs.legosdn.tools.cr.proto;

/**
 * Represents failures encountered when message format received is different from that expected.
 *
 * @author bala
 */
public class CRCtrlMsgFormatError extends RuntimeException {

    /**
     * Create a new exception instance.
     *
     * @param msg Error description.
     */
    public CRCtrlMsgFormatError(String msg) {
        super(msg);
    }

    /**
     * Create a new exception instance.
     *
     * @param msg Error description
     * @param e Exception instance (for stack traces)
     */
    public CRCtrlMsgFormatError(String msg, Throwable e) {
        super(msg, e);
    }

}
