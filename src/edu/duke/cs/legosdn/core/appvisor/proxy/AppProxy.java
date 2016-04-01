package edu.duke.cs.legosdn.core.appvisor.proxy;

import edu.duke.cs.legosdn.core.Defaults;
import edu.duke.cs.legosdn.core.faults.EventTransformerFactory;
import edu.duke.cs.legosdn.core.faults.IEventTransformer;
import edu.duke.cs.legosdn.core.netlog.TransactionMgr;
import edu.duke.cs.legosdn.core.service.link.LinkDiscoveryServiceProxy;
import edu.duke.cs.legosdn.core.service.topology.TopologyServiceProxy;
import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.linkdiscovery.ILinkDiscoveryService;
import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * AppProxy is an application with no business-logic. It simple dispatches every inbound message to all applications
 * that have subscribed to receive that message. Subscriptions are based on the type of the messages.
 */
public class AppProxy implements IOFMessageListener, IFloodlightModule {

    private static final Logger logger = LoggerFactory.getLogger(AppProxy.class);

    protected IFloodlightProviderService floodlightProvider;
    private   ProxySwitchListener        proxySwitchListener;
    private   ProxyLinkDiscoveryListener proxyLinkDiscoveryListener;

    // Registry of remote/isolated endpoints
    private RemoteRegistry appRegistry;

    // Registry of service proxies
    private ServiceRegistry srvRegistry;

    // Flag indicating if per-app network state management should be disabled.
    private boolean disablePerAppNS;

    // Event transformer class name.
    private String transformerKlass;

    // Flag indicating if event transformer should be disabled.
    private boolean disableXforms;

    /**
     * AppVisor Proxy specific configuration parameters.
     */
    public static enum AppProxyConfig {

        // Checkpoint frequency
        CHKPT_FREQ("chkpt_freq", String.valueOf(Defaults.APP_CHKPT_FREQ)),
        // Enable/disable replay (after recovery)
        DISABLE_REPLAY("disable_replay", String.valueOf(Defaults.DISABLE_REPLAY)),
        // Enable/disable NetLog
        DISABLE_NETLOG("disable_netlog", String.valueOf(Defaults.DISABLE_NETLOG)),
        // Use Local NetLog (runs within the controller process)?
        USE_LOCAL_NETLOG("use_local_netlog", String.valueOf(Defaults.USE_LOCAL_NETLOG)),
        // Allow transformation inversions.
        INVERT_XFORMS("invert_xforms", String.valueOf(Defaults.INVERT_XFORMS)),
        // Disable event transformer.
        DISABLE_XFORMS("disable_xforms", String.valueOf(Defaults.DISABLE_XFORMS)),
        // Custom event transformer.
        TRANSFORMER("transformer", null),
        // Disable per-app network state.
        DISABLE_PER_APP_NS("disable_per_app_ns", String.valueOf(Defaults.DISABLE_PER_APP_NS)),
        // Enable/disable logging of messages to file for debugging.
        ENABLE_MLOG("enable_msglog", String.valueOf(Defaults.ENABLE_MLOG)),
        // Halt controller when C/R fails?
        HALT_ON_CR_FAIL("halt_on_cr_fail", String.valueOf(false));

        private final String key;
        private final String defVal;

        /**
         * Initialize configuration option.
         *
         * @param key Parameter key (for parsing from a configuration file or retrieving from properties)
         * @param defVal Default value for the parameter
         */
        private AppProxyConfig(String key, String defVal) {
            this.key = key;
            this.defVal = defVal;
        }

        /**
         * Load the configuration option value from given map of configuration parameters and value.
         *
         * @param configParams Map of configuration parameters and values.
         * @return Configuration option value
         */
        public String fromConfig(Map<String, String> configParams) {
            if (configParams.containsKey(this.key)) {
                return configParams.get(this.key);
            }
            return this.defVal;
        }

    }

    @Override
    public Collection<Class<? extends IFloodlightService>> getModuleServices() {
        // AppProxy does not provide services!
        return null;
    }

    @Override
    public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
        // AppProxy does not provide any service implementations!
        return null;
    }

    @Override
    public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
        Collection<Class<? extends IFloodlightService>> c = new ArrayList<Class<? extends IFloodlightService>>();
        c.add(IFloodlightProviderService.class);
        return c;
    }

    /**
     * Configures proxy with the configuration parameters loaded from the controller's 'properties' file.
     *
     * @param configParams Map of configuration parameters and values.
     */
    private void configure(Map<String, String> configParams) {
        final int chkptFreq = Integer.parseInt(AppProxyConfig.CHKPT_FREQ.fromConfig(configParams));
        this.appRegistry.setChkptFreq(chkptFreq);

        final boolean disableNetLog = Boolean.parseBoolean(AppProxyConfig.DISABLE_NETLOG.fromConfig(configParams));
        TransactionMgr.setDisableNetLog(disableNetLog);

        if (logger.isInfoEnabled()) {
            logger.info("configure> NetLog {}!", disableNetLog ? "disabled" : "enabled");
        }

        final boolean useLocalNetLog = Boolean.parseBoolean(AppProxyConfig.USE_LOCAL_NETLOG.fromConfig(configParams));
        TransactionMgr.setUseLocalNetLog(useLocalNetLog);

        if (!disableNetLog) {
            if (logger.isInfoEnabled()) {
                logger.info("configure> NetLog running {}", useLocalNetLog ? "locally" : "remotely");
            }
        }

        final boolean disableReplay = Boolean.parseBoolean(AppProxyConfig.DISABLE_REPLAY.fromConfig(configParams));
        this.appRegistry.setDisableReplay(disableReplay);

        final boolean haltOnCRFail = Boolean.parseBoolean(AppProxyConfig.HALT_ON_CR_FAIL.fromConfig(configParams));
        if (logger.isInfoEnabled()) {
            logger.info(String.format("configure> Halt on C/R failures? %s", haltOnCRFail ? "yes" : "no"));
        }
        this.appRegistry.setHaltOnCRFailure(haltOnCRFail);

        final boolean enableMLog = Boolean.parseBoolean(AppProxyConfig.ENABLE_MLOG.fromConfig(configParams));
        this.appRegistry.setMsgLogging(enableMLog);

        this.disablePerAppNS = Boolean.parseBoolean(AppProxyConfig.DISABLE_PER_APP_NS.fromConfig(configParams));
        this.transformerKlass = AppProxyConfig.TRANSFORMER.fromConfig(configParams);
        this.disableXforms = Boolean.parseBoolean(AppProxyConfig.DISABLE_XFORMS.fromConfig(configParams));

        final boolean invertTransforms = Boolean.parseBoolean(AppProxyConfig.INVERT_XFORMS.fromConfig(configParams));
        if (logger.isInfoEnabled()) {
            logger.info(String.format("configure> Transformation inversion is %s",
                                      invertTransforms ? "enabled" : "disabled"));
        }
        this.appRegistry.setInvertTransforms(invertTransforms);
    }

    @Override
    public void init(FloodlightModuleContext context) throws FloodlightModuleException {
        this.srvRegistry = new ServiceRegistry();
        this.srvRegistry.addServiceCallProxy(new TopologyServiceProxy(context));
        this.srvRegistry.addServiceCallProxy(new LinkDiscoveryServiceProxy(context));

        try {
            this.appRegistry = new RemoteRegistry(context, this.srvRegistry);

            this.configure(context.getConfigParams(this));
        } catch (IOException e) {
            logger.error("init> failed to initialize RemoteRegistry; {}", e.getLocalizedMessage());
            throw new FloodlightModuleException(e);
        } finally {
            this.appRegistry.deferCleanup();
        }

        this.floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
        IEventTransformer transformer;
        try {
            transformer = EventTransformerFactory.getInstance(this.disableXforms, this.transformerKlass);
        } catch (Exception e) {
            throw new FloodlightModuleException("Failed to load event transformer module!", e);
        }
        this.appRegistry.registerTransformer(transformer);
        final IAppAwareLinkDiscoveryService linkDiscSrvc =
                (IAppAwareLinkDiscoveryService) context.getServiceImpl(ILinkDiscoveryService.class);
        if (this.disablePerAppNS) {
            linkDiscSrvc.disableAppAwareness();

            if (logger.isInfoEnabled()) {
                logger.info(String.format("init> Per-app network state management is %s",
                                          this.disablePerAppNS ? "disabled" : "enabled"));
            }
        }
        transformer.registerService(linkDiscSrvc);

        this.deferCleanup();
    }

    @Override
    public void startUp(FloodlightModuleContext context) throws FloodlightModuleException {
        // Ensure that Proxy can listen to all types of "relevant" OpenFlow messages.
        for (OFType mType : OFType.values()) {
            switch (mType) {
                /* NOTE: Proxy should not subscribe to outgoing messages! */
                case BARRIER_REQUEST:
                case ECHO_REQUEST:
                case FEATURES_REQUEST:
                case FLOW_MOD:
                case GET_CONFIG_REQUEST:
                case PACKET_OUT:
                case QUEUE_GET_CONFIG_REQUEST:
                case STATS_REQUEST:
                /* NOTE: Proxy should not subscribe to low-level messages! */
                case HELLO:
                case SET_CONFIG:
                case VENDOR:
                    continue;
            }

            this.floodlightProvider.addOFMessageListener(mType, this);

            if (logger.isDebugEnabled()) {
                logger.debug("startup> subscribed to {} message", mType);
            }
        }

        // Register switch listener
        this.proxySwitchListener = ProxySwitchListener.getInstance(this.appRegistry);
        this.floodlightProvider.addOFSwitchListener(this.proxySwitchListener);
        // Register LinkDiscovery listener
        this.proxyLinkDiscoveryListener = ProxyLinkDiscoveryListener.getInstance(this.appRegistry);
        context.getServiceImpl(ILinkDiscoveryService.class).addListener(this.proxyLinkDiscoveryListener);

        this.appRegistry.start();
    }

    @Override
    public Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
        try {
            return this.appRegistry.broadcastMsg(sw, msg, cntx);
        } catch (IOException e) {
            logger.error("receive> failed to broadcast '{}' to applications; {}",
                         msg.getType(), e.getLocalizedMessage());
            e.printStackTrace();

            return Command.STOP;
        }
    }

    @Override
    public String getName() {
        return AppProxy.class.getCanonicalName();
    }

    @Override
    public boolean isCallbackOrderingPrereq(OFType type, String name) {
        return false;
    }

    @Override
    public boolean isCallbackOrderingPostreq(OFType type, String name) {
        return false;
    }

    /**
     * Cleanup on a deferred thread.
     */
    private void deferCleanup() {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                appRegistry.stop();
            }

        });
    }

}
