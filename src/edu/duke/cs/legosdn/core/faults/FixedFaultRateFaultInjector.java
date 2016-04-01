
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
import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * FixedFaultRateFaultInjector generates faults at a user-defined rate.
 */
public class FixedFaultRateFaultInjector extends RandomFaultInjector {

    private static final Logger logger = LoggerFactory.getLogger(FixedFaultRateFaultInjector.class);

    // Path to crash indicator flag file
    private static final File CRASH_FLAG_FILE =
            new File(String.format("%s/%s.flag",
                                   Defaults.CRASH_IND_BASE_DIR, FixedFaultRateFaultInjector.class.getCanonicalName()));

    // No faults; fault-rate value indicating that there will be no faults generated
    private static final float NO_FAULTS = -1.0f;

    // Number of messages initially to skip
    // (for the application to process something before generating faults)
    private static final long NUM_MSGS_TO_SKIP = 32;

    // User-defined configuration parameter key to define 'faultRate'
    public static final String FAULT_RATE_KEY =
            String.format("%s.faultrate", FixedFaultRateFaultInjector.class.getCanonicalName());

    protected float faultRate;
    protected long  numMsgs;

    /**
     * Initialize FixedFaultRateFaultInjector.
     */
    public FixedFaultRateFaultInjector() {
        this.faultRate = NO_FAULTS;
        this.numMsgs = 0;
    }

    /**
     * Set fault rate.
     *
     * @param faultRate Fault rate
     */
    public void setFaultRate(float faultRate) {
        this.faultRate = faultRate;
        if (this.faultRate > 0 && this.faultRate < 1) {
            return;
        }

        if (this.faultRate == 0) {
            this.faultRate = NO_FAULTS;
            return;
        }

        this.faultRate = NO_FAULTS;

        if (logger.isWarnEnabled()) {
            logger.warn("setFaultRate> invalid value '{}' for fault rate; no faults will be generated!", faultRate);
        }
    }

    @Override
    public void configure(Properties properties) {
        final String faultRateParam = properties.getProperty(FAULT_RATE_KEY);
        if (faultRateParam == null) {
            this.setFaultRate(0);
        }

        try {
            final float faultRate = Float.parseFloat(faultRateParam);
            this.setFaultRate(faultRate);
        } catch (NumberFormatException e) {
            this.setFaultRate(0);

            if (logger.isWarnEnabled()) {
                logger.warn("configure> invalid value '{}' for '{}'; no faults will be generated!",
                            faultRateParam, FAULT_RATE_KEY);
            }
        }
    }

    @Override
    public void receive(IOFSwitch sw, DPlaneMsg msg) {
        final float f = new Random(new Date().getTime()).nextFloat();
        if (f > 0.1) {
            return;
        }
        this.generateFault();
    }

    @Override
    public IListener.Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
        final float f = new Random(new Date().getTime()).nextFloat();
        if (f > 0.1)
            return IListener.Command.CONTINUE;
        else
            this.generateFault();
        return IListener.Command.STOP;
    }

    /**
     * Generate fault based on some heuristics.
     */
    private void generateFault() {
        this.numMsgs++;

        if (this.numMsgs <= FixedFaultRateFaultInjector.NUM_MSGS_TO_SKIP) {
            return;
        }

        final float f = new Random(new Date().getTime()).nextFloat();
        if (f < this.faultRate) {
            throw new RuntimeException(this.toString());
        }
    }

    @Override
    public String toString() {
        return String.format("FixedFaultRateFaultInjector<%f>", this.faultRate);
    }

}
