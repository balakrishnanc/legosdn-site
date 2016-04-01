package edu.duke.cs.legosdn.core.appvisor.stub;

import edu.duke.cs.legosdn.core.appvisor.cplane.CPlaneMsg;
import edu.duke.cs.legosdn.core.appvisor.cplane.CPlaneMsgType;
import edu.duke.cs.legosdn.core.appvisor.cplane.CPlaneStub;
import edu.duke.cs.legosdn.core.appvisor.cplane.OFMsgSubscription;
import net.floodlightcontroller.core.*;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFType;
import org.openflow.protocol.factory.BasicFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FloodlightProviderStub implements IFloodlightProviderService {

    protected final BasicFactory factory;
    // Application identifier
    private final   String       appId;

    // Stub to communication with the controller
    private final CPlaneStub                            cPlane;
    // Switch isolator one per data channel connection from controller-stub
    final         ConcurrentHashMap<Long, OFSwitchStub> switches;

    /**
     * Initialize FloodlightProviderStub with a communication channel.
     */
    FloodlightProviderStub(String appId, CPlaneStub stub) {
        this.appId = appId;
        this.cPlane = stub;
        this.factory = BasicFactory.getInstance();
        this.switches = new ConcurrentHashMap<Long, OFSwitchStub>(512);
    }

    @Override
    public void addOFMessageListener(OFType type, IOFMessageListener listener) {
        final OFMsgSubscription msgSub = new OFMsgSubscription(type, this.appId);
        final Channel ch = this.cPlane.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.OF_ADD_MSGSUB, msgSub)).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                ch.close();
            }
        });
    }

    @Override
    public void removeOFMessageListener(OFType type, IOFMessageListener listener) {
        final OFMsgSubscription msgSub =
                new OFMsgSubscription(type, OFMsgSubscription.SubscriptionStatus.DEL_SUBSCRIPTION, this.appId);
        final Channel ch = this.cPlane.openCPlaneChannel();
        ch.write(new CPlaneMsg(CPlaneMsgType.OF_DEL_MSGSUB, msgSub)).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                ch.close();
            }
        });
    }

    @Override
    public Map<OFType, List<IOFMessageListener>> getListeners() {
        return null;
    }

    @Override
    public IOFSwitch getSwitch(long dpid) {
        return this.switches.get(dpid);
    }

    @Override
    public Set<Long> getAllSwitchDpids() {
        return this.switches.keySet();
    }

    @Override
    public Map<Long, IOFSwitch> getAllSwitchMap() {
        return null;
    }

    @Override
    public Role getRole() {
        return null;
    }

    @Override
    public RoleInfo getRoleInfo() {
        return null;
    }

    @Override
    public Map<String, String> getControllerNodeIPs() {
        return null;
    }

    @Override
    public void setRole(Role role, String changeDescription) {

    }

    @Override
    public void addOFSwitchListener(IOFSwitchListener listener) {

    }

    @Override
    public void removeOFSwitchListener(IOFSwitchListener listener) {

    }

    @Override
    public void addHAListener(IHAListener listener) {

    }

    @Override
    public void removeHAListener(IHAListener listener) {

    }

    @Override
    public void addReadyForReconcileListener(IReadyForReconcileListener l) {

    }

    @Override
    public void terminate() {

    }

    @Override
    public boolean injectOfMessage(IOFSwitch sw, OFMessage msg) {
        return false;
    }

    @Override
    public boolean injectOfMessage(IOFSwitch sw, OFMessage msg, FloodlightContext bContext) {
        return false;
    }

    @Override
    public void handleOutgoingMessage(IOFSwitch sw, OFMessage m, FloodlightContext bc) {

    }

    @Override
    public BasicFactory getOFMessageFactory() {
        return this.factory;
    }

    @Override
    public void run() {

    }

    @Override
    public void addInfoProvider(String type, IInfoProvider provider) {

    }

    @Override
    public void removeInfoProvider(String type, IInfoProvider provider) {

    }

    @Override
    public Map<String, Object> getControllerInfo(String type) {
        return null;
    }

    @Override
    public long getSystemStartTime() {
        return 0;
    }

    @Override
    public void setAlwaysClearFlowsOnSwActivate(boolean value) {

    }

    @Override
    public Map<String, Long> getMemory() {
        return null;
    }

    @Override
    public Long getUptime() {
        return null;
    }

    @Override
    public void addOFSwitchDriver(String desc, IOFSwitchDriver driver) {

    }

    @Override
    public void addSwitchEvent(long switchDPID, String reason, boolean flushNow) {

    }

    @Override
    public Set<String> getUplinkPortPrefixSet() {
        return null;
    }
}
