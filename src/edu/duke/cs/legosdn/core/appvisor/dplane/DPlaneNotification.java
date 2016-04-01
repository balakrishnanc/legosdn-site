package edu.duke.cs.legosdn.core.appvisor.dplane;

import java.io.Externalizable;

/**
 * DPlaneNotification defines the necessary methods required to be implemented
 *  by all data plane notification messages.
 */
public interface DPlaneNotification extends Externalizable {

    /**
     * @return Type of data plane message
     */
    public DPlaneMsgType getMsgType();

    /**
     * @return Identifier of switch associated with the notification
     */
    public long getSwitchId();

    /**
     *
     * @param switchId Identifier of switch associated with the notification
     */
    public void setSwitchId(long switchId);

}
