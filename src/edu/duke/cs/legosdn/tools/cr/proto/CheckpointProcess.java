package edu.duke.cs.legosdn.tools.cr.proto;

import edu.duke.cs.legosdn.core.appvisor.cplane.RemoteEndpt;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * C/R service control message to checkpoint a running application.
 */
public class CheckpointProcess extends CRCtrlMsg {

    public final RemoteEndpt proc;

    /**
     * Initialize CheckpointProcess message with details on the remote application endpoint.
     *
     * @param proc Remote application endpoint
     */
    public CheckpointProcess(RemoteEndpt proc) {
        super(CRCtrlMsgType.CHECKPOINT_PROC);
        this.proc = proc;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        proc.writeTo(buf);
    }

    @Override
    public CheckpointProcess readFrom(ChannelBuffer buf) {
        return CheckpointProcess.parse(buf);
    }

    public static CheckpointProcess parse(ChannelBuffer buf) {
        RemoteEndpt proc = RemoteEndpt.readFrom(buf);
        return new CheckpointProcess(proc);
    }

    @Override
    public int getLength() {
        return super.getLength() + this.proc.getLength();
    }

}
