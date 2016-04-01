package edu.duke.cs.legosdn.core.faults;

import edu.duke.cs.legosdn.core.Defaults;
import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsg;
import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IListener;
import net.floodlightcontroller.core.IOFSwitch;
import org.openflow.protocol.OFMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * NthPacketFaultInjector injects a fault for every Nth inbound message received from a switch.
 */
public class NthPacketFaultInjector extends DeterministicFaultInjector {

    private static final Logger logger = LoggerFactory.getLogger(NthPacketFaultInjector.class);

    // Path to crash indicator flag file
    private static final File CRASH_FLAG_FILE =
            new File(String.format("%s/%s.flag",
                                   Defaults.CRASH_IND_BASE_DIR, NthPacketFaultInjector.class.getCanonicalName()));

    // User-defined configuration parameter key to define 'faultRate'
    public static final String NUM_MSGS_BETWEEN_FAULTS_KEY =
            String.format("%s.msgsbetweenfaults", NthPacketFaultInjector.class.getCanonicalName());

    protected long numMsgsBetweenFaults;

    /**
     * Initialize NthPacketFaultInjector.
     */
    public NthPacketFaultInjector() {
        this.numMsgsBetweenFaults = Long.MAX_VALUE;
    }

    /**
     * Set number of messages between faults.
     *
     * @param numMsgsBetweenFaults Number of messages between faults
     */
    public void setNumMsgsBetweenFaults(long numMsgsBetweenFaults) {
        this.numMsgsBetweenFaults = numMsgsBetweenFaults;
        if (this.numMsgsBetweenFaults <= 0) {
            this.numMsgsBetweenFaults = Long.MAX_VALUE;

            if (logger.isWarnEnabled()) {
                logger.warn("NthPacketFaultInjector> Invalid value '{}' for numMsgsBetweenFaults;" +
                            " using default value '{}'!", numMsgsBetweenFaults, this.numMsgsBetweenFaults);
            }
        }
    }

    @Override
    public void configure(Properties properties) {
        final String numMsgsBetweenFaultsParam = properties.getProperty(NUM_MSGS_BETWEEN_FAULTS_KEY);
        if (numMsgsBetweenFaultsParam == null) {
            this.setNumMsgsBetweenFaults(Long.MAX_VALUE);
        }

        try {
            final long numMsgsBetweenFaults = Long.parseLong(numMsgsBetweenFaultsParam);
            this.setNumMsgsBetweenFaults(numMsgsBetweenFaults);
        } catch (NumberFormatException e) {
            this.setNumMsgsBetweenFaults(Long.MAX_VALUE);

            if (logger.isWarnEnabled()) {
                logger.warn(String.format("configure> invalid value '%s' for '%s'; using default value '%d'",
                                          numMsgsBetweenFaultsParam, NUM_MSGS_BETWEEN_FAULTS_KEY, Long.MAX_VALUE));
            }
        }
    }

    @Override
    public void receive(IOFSwitch sw, DPlaneMsg msg) {
        this.numReceived = msg.getAppMsgId();
        this.generateFault();
    }

    @Override
    public IListener.Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
        // Application probably running directly inside the controller!
        // Cannot maintain state; update internal counters which will be reset on a crash!
        this.numReceived++;
        this.generateFault();
        return IListener.Command.CONTINUE;
    }

    /**
     * Generate fault based on some heuristics.
     */
    private void generateFault() {
        if (this.numReceived % this.numMsgsBetweenFaults == 0) {
            throw new RuntimeException(this.toString());
        }
    }

    @Override
    public String toString() {
        return String.format("NthPacketFaultInjector<%d>", this.numMsgsBetweenFaults);
    }

}
