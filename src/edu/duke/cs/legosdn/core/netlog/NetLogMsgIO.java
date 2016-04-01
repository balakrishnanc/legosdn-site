package edu.duke.cs.legosdn.core.netlog;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * NetLogMsgIO defines methods required to be implemented by NetLog communication messages.
 */
public interface NetLogMsgIO {

    /**
     * Write object state to output channel.
     *
     * @param out Output channel
     * @throws IOException
     */
    public void writeTo(ObjectOutput out) throws IOException;

    /**
     * Read object state from input channel.
     *
     * @param in Input channel
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void readFrom(ObjectInput in) throws IOException, ClassNotFoundException;

}
