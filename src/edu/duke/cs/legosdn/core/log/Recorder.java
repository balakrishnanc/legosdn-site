
package edu.duke.cs.legosdn.core.log;

import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsg;
import org.openflow.protocol.OFMessage;

import java.io.File;

/**
 * Allows quick logging of messages to file. The file/output handles are not not kept open forever.
 */
public interface Recorder {

    /**
     * Log inbound message to file for debugging.
     *
     * @param m Message
     * @param f Log file
     */
    public void logInMsg(String m, File f);

    /**
     * Log outbound message to file for debugging.
     *
     * @param m Message
     * @param f Log file
     */
    public void logOutMsg(String m, File f);

    /**
     * Log message to file for debugging.
     *
     * @param m Message
     * @param d Direction of message (for debugging)
     * @param f Log file
     */
    public void logMsg(String m, String d, File f);

    /**
     * Log message to file for debugging.
     *
     * @param m Inbound data-plane message.
     * @param f Log file
     */
    public void logMsg(DPlaneMsg m, File f);

    /**
     * Log outbound message to file for debugging.
     *
     * @param m Inbound data-plane message.
     * @param f Log file
     */
    public void logOutMsg(DPlaneMsg m, File f);

    /**
     * Log inbound message to file for debugging.
     *
     * @param m Inbound data-plane message.
     * @param f Log file
     */
    public void logInMsg(DPlaneMsg m, File f);

    /**
     * Log message to file for debugging.
     *
     * @param m Inbound data-plane message.
     * @param d Direction of message (for debugging)
     * @param f Log file
     */
    public void logMsg(DPlaneMsg m, String d, File f);

    /**
     * Log inbound message to file for debugging.
     *
     * @param s Switch Identifier.
     * @param m Inbound data-plane message.
     * @param f Log file
     */
    public void logInMsg(Long s, OFMessage m, File f);

    /**
     * Log outbound message to file for debugging.
     *
     * @param s Switch Identifier.
     * @param m Inbound data-plane message.
     * @param f Log file
     */
    public void logOutMsg(Long s, OFMessage m, File f);

    /**
     * Log message to file for debugging.
     *
     * @param s Switch Identifier.
     * @param m Inbound data-plane message.
     * @param d Direction of message (for debugging)
     * @param f Log file
     */
    public void logMsg(Long s, OFMessage m, String d, File f);

}
