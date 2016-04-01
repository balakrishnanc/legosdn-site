
package edu.duke.cs.legosdn.core.faults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EventTransformerFactory supports dynamic loading of event transformer implementations.
 */
public final class EventTransformerFactory {

    private static final Logger logger = LoggerFactory.getLogger(EventTransformerFactory.class);

    private static IEventTransformer transformer;

    /**
     * Retrieve an instance of EventTransformer.
     *
     * @param disabled True, if event transformations are disabled.
     * @param klass EventTransformer class.
     * @return Instance of IEventTransformer.
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static synchronized IEventTransformer getInstance(boolean disabled, String klass)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (transformer != null) {
            return transformer;
        }

        if (disabled) {
            transformer = new NullTransformer();
            if (logger.isInfoEnabled()) {
                logger.info("Event transformations are disabled; using {}",
                            NullTransformer.class.getCanonicalName());
            }
        } else if (klass == null) {
            transformer = new BasicTransformer();
            if (logger.isInfoEnabled()) {
                logger.info("Event transformations are enabled; using {}",
                            BasicTransformer.class.getCanonicalName());
            }
        } else {
            Class k = EventTransformerFactory.class.getClassLoader().loadClass(klass);
            transformer = (IEventTransformer) k.newInstance();
            if (logger.isInfoEnabled()) {
                logger.info("Event transformations are enabled; loaded {}", k);
            }
        }

        return transformer;
    }

}
