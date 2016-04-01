
package edu.duke.cs.legosdn.core.faults;

import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsgExternalizable;
import edu.duke.cs.legosdn.core.appvisor.dplane.LinkDiscoveryNotification;
import edu.duke.cs.legosdn.core.appvisor.dplane.PortNotification;
import edu.duke.cs.legosdn.core.appvisor.dplane.SwitchNotification;

import java.util.List;

/**
 * Null event transformer provides no post-fault or pre-fault transformations.
 */
public class NullTransformer extends BaseEventTransformer {

    @Override
    protected List<DPlaneMsgExternalizable> postFaultTransform(DPlaneMsgExternalizable dPlaneMsg, String appId,
                                                               int attempt) {
        return null;
    }

    @Override
    protected List<DPlaneMsgExternalizable> postFaultTransform(SwitchNotification swNotif, String appId, int attempt) {
        return null;
    }

    @Override
    protected List<DPlaneMsgExternalizable> postFaultTransform(PortNotification portNotif, String appId, int attempt) {
        return null;
    }

    @Override
    protected List<DPlaneMsgExternalizable> postFaultTransform(LinkDiscoveryNotification linkNotif, String appId,
                                                               int attempt) {
        return null;
    }

}
