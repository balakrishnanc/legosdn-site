package edu.duke.cs.legosdn.core.netlog;

import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsgExternalizable;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * NetLogInitiate bundles an OpenFlow message (inbound from switch to application) with the application that
 * is intended to process it and the network device (switch) where it originated from.
 */
public class NetLogInitiate implements NetLogMsgIO {

    // Data plane message (inbound from switch to application)
    private DPlaneMsgExternalizable msg;
    // Unique application identifier
    private String                  appId;

    private static final String UNINITIALIZED_APPID = "";

    /**
     * Initialize NetLogInitiate with no state.
     */
    public NetLogInitiate() {
        this(new DPlaneMsgExternalizable(), UNINITIALIZED_APPID);
    }

    /**
     * Initialize NetLogInitiate.
     *
     * @param msg Outbound data plane message
     * @param appId Application identifier
     */
    public NetLogInitiate(final DPlaneMsgExternalizable msg, final String appId) {
        this.msg = msg;
        this.appId = appId;
    }

    /**
     * @return Inbound data plane message
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
