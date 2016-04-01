package edu.duke.cs.legosdn.core.netlog;

import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsgExternalizable;
import net.floodlightcontroller.core.IOFSwitch;

/**
 * MessageLog allows an OpenFlow message to be bundled with an application that either generated the message or
 *  intended to process it.
 */
public class MessageLog {

    public final IOFSwitch               sw;
    public final DPlaneMsgExternalizable msg;
    public final String                  app;

    /**
     * Initialize MessageLog.
     *
     * @param sw Switch
     * @param msg Message
     * @param app Application ID
     */
    public MessageLog(IOFSwitch sw, DPlaneMsgExternalizable msg, String app) {
        if (msg == null || app == null) {
            throw new RuntimeException("Cannot initialize MessageLog with null values!");
        }

        this.sw = sw;
        this.msg = msg;
        this.app = app;
    }

}
