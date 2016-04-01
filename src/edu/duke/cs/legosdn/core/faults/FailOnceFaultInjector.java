
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
import java.util.Properties;

public class FailOnceFaultInjector extends DeterministicFaultInjector {

    private static final Logger logger = LoggerFactory.getLogger(FailOnceFaultInjector.class);

    // Path to crash indicator flag file
    private static final File CRASH_FLAG_FILE =
            new File(String.format("%s/%s.flag",
                                   Defaults.CRASH_IND_BASE_DIR, FailOnceFaultInjector.class.getCanonicalName()));

    // User-defined configuration parameter key to define 'faultRate'
    public static final String FAULT_AFTER =
            String.format("%s.fault_after", FailOnceFaultInjector.class.getCanonicalName());

    public static final int DEF_WHEN_TO_FAULT = 1000;

    protected long whenToFault;

    /**
     * Initialize FailOnceFaultInjector.
     */
    public FailOnceFaultInjector() {
        this.whenToFault = DEF_WHEN_TO_FAULT;
    }

    /**
     * Set number of messages after which to fault
     *
     * @param whenToFault Number of messages after which to fault
     */
    public void setWhenToFault(long whenToFault) {
        this.whenToFault = whenToFault;
        if (this.whenToFault <= 0) {
            this.whenToFault = DEF_WHEN_TO_FAULT;
        }
        if (logger.isInfoEnabled()) {
            logger.info("FailOnceFaultInjector> Fault set to occur after {} messages.", this.whenToFault);
        }
    }

    @Override
    public void configure(Properties properties) {
        final String whenToFaultParam = properties.getProperty(FAULT_AFTER);
        if (whenToFaultParam != null) {
            try {
                final long whenToFault = Long.parseLong(whenToFaultParam);
                this.setWhenToFault(whenToFault);
            } catch (NumberFormatException e) {
                this.setWhenToFault(DEF_WHEN_TO_FAULT);

                if (logger.isWarnEnabled()) {
                    logger.warn(String.format("configure> invalid value '%s' for '%s'; using default value '%d'",
                                              whenToFaultParam, FAULT_AFTER, this.whenToFault));
                }
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
        if (CRASH_FLAG_FILE.exists())
            return;

        if (this.numReceived % this.whenToFault != 0) {
            return;
        }

        try {
            CRASH_FLAG_FILE.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException(this.toString());
    }

    @Override
    public String toString() {
        return String.format("FailOnceFaultInjector<%d>", this.whenToFault);
    }

}
