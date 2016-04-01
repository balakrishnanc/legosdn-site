package edu.duke.cs.legosdn.tools.cr.proto;

import edu.duke.cs.legosdn.core.appvisor.cplane.RemoteEndpt;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * C/R service control message to register a remote application and prepare for a C/R operation.
 */
public class RegisterProcess extends CRCtrlMsg {

    public final RemoteEndpt proc;

    /**
     * Initialize RegisterProcess message with details on the remote application endpoint.
     *
     * @param proc Remote application endpoint
     */
    public RegisterProcess(RemoteEndpt proc) {
        super(CRCtrlMsgType.REGISTER_PROC);
        this.proc = proc;
    }

    @Override
    public void writeTo(ChannelBuffer buf) {
        super.writeTo(buf);
        proc.writeTo(buf);
    }

    @Override
    public RegisterProcess readFrom(ChannelBuffer buf) {
        return RegisterProcess.parse(buf);
    }

    public static RegisterProcess parse(ChannelBuffer buf) {
        RemoteEndpt proc = RemoteEndpt.readFrom(buf);
        return new RegisterProcess(proc);
    }

    @Override
    public int getLength() {
        return super.getLength() + this.proc.getLength();
    }

}
