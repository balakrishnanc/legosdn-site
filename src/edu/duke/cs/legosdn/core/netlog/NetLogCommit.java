package edu.duke.cs.legosdn.core.netlog;

import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsgExternalizable;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * NetLogCommit bundles an OpenFlow message (outbound from application to switch) with the application that
 * generated it and the network device (switch) where it is installed.
 */
public class NetLogCommit implements NetLogMsgIO {

    // Data plane message (outbound from application to switch)
    private DPlaneMsgExternalizable msg;
    // Unique application identifier
    private String                  appId;

    private static final String UNINITIALIZED_APPID = "";

    /**
     * Initialize NetLogCommit with no state.
     */
    public NetLogCommit() {
        this(new DPlaneMsgExternalizable(), UNINITIALIZED_APPID);
    }

    /**
     * Initialize NetLogCommit.
     *
     * @param msg Outbound data plane message
     * @param appId Application identifier
     */
    public NetLogCommit(final DPlaneMsgExternalizable msg, final String appId) {
        this.msg = msg;
        this.appId = appId;
    }

    /**
     * @return Outbound data plane message
     */
    public DPlaneMsgExternalizable getMsg() {
        return this.msg;
    }

    /**
     * @return Application identifier
     */
    public String getAppId() {
        return this.appId;
    }

    @Override
    public void writeTo(ObjectOutput out) throws IOException {
        out.writeUTF(this.appId);
        this.msg.writeExternal(out);
    }

    @Override
    public void readFrom(ObjectInput in) throws IOException, ClassNotFoundException {
        this.appId = in.readUTF();
        this.msg.readExternal(in);
    }

}
