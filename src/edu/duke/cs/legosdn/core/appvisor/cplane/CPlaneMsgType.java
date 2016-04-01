
package edu.duke.cs.legosdn.core.appvisor.cplane;

import org.openflow.protocol.OFType;

/**
 * Control plane message types.
 */
public enum CPlaneMsgType {

    OF_ADD_MSGSUB(OFType.class),                // Request to add new OF-message subscription at an endpoint
    OF_DEL_MSGSUB(OFType.class),                // Request to delete OF-message subscription at an endpoint
    EP_REMOTEINFO(RemoteEndpt.class),           // Remote endpoint information
    SERVICE_CALL_MSG(ServiceCallMsg.class);     // Service Calls via RPC

    // Actual message type
    public final Class type;

    /**
     * Instantiate data plane message type constants.
     *
     * @param msgType Message type (Class)
     */
    private CPlaneMsgType(Class msgType) {
        this.type = msgType;
    }

}
