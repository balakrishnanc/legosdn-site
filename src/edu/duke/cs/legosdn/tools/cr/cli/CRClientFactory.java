package edu.duke.cs.legosdn.tools.cr.cli;

import edu.duke.cs.legosdn.tools.cr.CRClientProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * CRClientFactory constructs an instance of C/R client provider.
 */
public class CRClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(CRUDPClient.class);

    /**
     * @return an instance of CRClientProvider
     */
    public static CRClientProvider getInstance() {
        try {
            CRClientProvider crClientProvider = new CRTCPClient();
            if (crClientProvider.connect()) {
                crClientProvider.disconnect();
                return crClientProvider;
            }
        } catch (IOException e) {
            logger.error("getInstance> Failed to initialize CRUDPClient; {}", e.getLocalizedMessage());
        }

        logger.warn("getInstance> Failed to initialize CRUDPClient; switching to CRNettyTCPClient");
        return new CRNettyTCPClient();
    }

}
