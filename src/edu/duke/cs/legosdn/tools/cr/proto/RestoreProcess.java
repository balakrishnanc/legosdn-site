package edu.duke.cs.legosdn.tools.cr.proto;

import edu.duke.cs.legosdn.core.appvisor.cplane.RemoteEndpt;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * C/R service control message to restore an application from an previous checkpoint.
 */
public class RestoreProcess extends CRCtrlMsg {

    public final RemoteEndpt proc;

    /**
     * Initialize RestoreProcess message with details on the remote application endpoint.
     *
     * @param proc Remote application endpoint
     */
    public RestoreProcess(RemoteEndpt proc) {
        super(CRCtrlMsgType.RESTORE_PROC);
        this.proc = proc;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        proc.writeTo(buf);
    }

    @Override
    public RestoreProcess readFrom(ChannelBuffer buf) {
        return RestoreProcess.parse(buf);
    }

    public static RestoreProcess parse(ChannelBuffer buf) {
        RemoteEndpt proc = RemoteEndpt.readFrom(buf);
        return new RestoreProcess(proc);
    }

    @Override
    public int getLength() {
        return super.getLength() + this.proc.getLength();
    }

}
