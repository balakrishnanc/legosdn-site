package edu.duke.cs.legosdn.core.appvisor.dplane;

/**
 * Exception encountered when unknown message type is sent/received on the wire.
 *
 * @author bala
 */
public class UnknownMsgTypeException extends RuntimeException {

    /**
     * Create a new exception instance.
     *
     * @param msg Error description.
     */
    public UnknownMsgTypeException(String msg) {
        super(msg);
    }

    /**
     * Create a new exception instance.
     *
     * @param msg Error description
     * @param e Exception instance (for stack traces)
     */
    public UnknownMsgTypeException(String msg, Throwable e) {
        super(msg, e);
    }

}
