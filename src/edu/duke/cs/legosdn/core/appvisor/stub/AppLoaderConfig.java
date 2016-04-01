package edu.duke.cs.legosdn.core.appvisor.stub;

import edu.duke.cs.legosdn.core.Defaults;
import edu.duke.cs.legosdn.core.faults.FaultInjector;
import edu.duke.cs.legosdn.core.faults.NoFaultInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * AppLoaderConfig class helps to bundle the configuration parameters required for initializing the AppLoader and the
 * application within it.
 *
 * @author bala
 */
public class AppLoaderConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppLoaderConfig.class);

    // Application name
    public final String appName;

    // Listen port
    public final int listenPort;

    // Stateless flag
    public final boolean stateless;

    // Fault injector class
    public final FaultInjector faultInjector;

    // Message logging switch
    public final boolean enableMLog;

    // Module configuration
    public final Map<String, String> modConf;

    /* Configuration property keys */
    private static final String APP_NAME            = "app.name";
    private static final String APP_PORT            = "app.port";
    private static final String APP_STATELESS       = "app.stateless";
    private static final String FAULT_INJECTOR_NAME = "fault.injector.class";
    private static final String ENABLE_MLOG         = "msglog.enabled";

    /* Error messages */
    private static final String APP_NAME_ERR =
            String.format("'%s' should be the canonical name of a valid application class", APP_NAME);
    private static final String APP_PORT_ERR =
            String.format("'%s' should be a valid number in the range [1024, 65535]", APP_PORT);

    /**
     * Create a new instance of {@link AppLoaderConfig}
     */
    public AppLoaderConfig(String appName) {
        this(appName, Defaults.STUB_PORT, false, new NoFaultInjector(), false,
             new HashMap<String, String>());
    }

    /**
     * Create a new instance of {@link AppLoaderConfig}
     *
     * @param appName    Fully qualified name of application
     * @param listenPort Port on which {@link AppLoader} listens
     * @param stateless True, if application is stateless; False, otherwise
     * @param faultInjector Fault injector class
     * @param enableMLog If true enable message logging to file; disable, otherwise
     */
    public AppLoaderConfig(String appName, int listenPort, boolean stateless, FaultInjector faultInjector,
                           boolean enableMLog, Map<String, String> modConf) {
        this.appName = appName.trim();
        this.listenPort = listenPort;
        this.stateless = stateless;
        this.faultInjector = faultInjector;
        this.enableMLog = enableMLog;
        this.modConf = modConf;
    }

    /**
     * Generate application configuration based on properties.
     *
     * @param properties Properties load from a file
     * @return AppLoaderConfig instance
     */
    public static AppLoaderConfig loadFromProperties(Properties properties) {
        final String appName = properties.getProperty(APP_NAME);

        if (appName == null || appName.trim().equals("")) {
            System.err.println(APP_NAME_ERR);
            System.exit(1);
        }

        final String port = properties.getProperty(APP_PORT);
        if (port == null) {
            /* Use default application stub port. */
            return new AppLoaderConfig(appName);
        }

        final int appPort;
        try {
            appPort = Integer.parseInt(port);

            if (appPort < 1024 || appPort > 65535) {
                System.err.println(APP_PORT_ERR);
                System.exit(1);
                return null;
            }
        } catch (NumberFormatException e) {
            System.err.println(APP_PORT_ERR);
            System.exit(1);
            return null;
        }

        final boolean stateless = Boolean.parseBoolean(properties.getProperty(APP_STATELESS, "false"));

        final String faultInjectorName =
                properties.getProperty(FAULT_INJECTOR_NAME, NoFaultInjector.class.getCanonicalName());
        final FaultInjector faultInjector = loadFaultInjector(faultInjectorName);
        faultInjector.configure(properties);
        if (logger.isInfoEnabled()) {
            logger.info("loadFaultInjector> using {}", faultInjector.toString());
        }

        final boolean enableMlog = Boolean.parseBoolean(properties.getProperty(ENABLE_MLOG, "false"));

        final String appNamePfx = appName + ".";
        final Map<String, String> modConf = new HashMap<String, String>();
        for (Object o : properties.keySet()) {
            final String p = (String) o;
            if (!p.startsWith(appNamePfx))
                continue;
            final String k = p.replace(appNamePfx, "");
            final String v = properties.getProperty(p);
            modConf.put(k, v);

            if (logger.isInfoEnabled()) {
                logger.info("property> {}: {}", k, v);
            }
        }
        return new AppLoaderConfig(appName, appPort, stateless, faultInjector, enableMlog, modConf);
    }

    /**
     * Load fault injector clas dynamically.
     *
     * @param faultInjectorName Fault injector class name
     * @return Instance FaultInjector dynamically loaded based on application configuration
     */
    private static FaultInjector loadFaultInjector(String faultInjectorName) {
        try {
            final ClassLoader cl = Thread.currentThread().getContextClassLoader();
            final Class c = cl.loadClass(faultInjectorName);
            return (FaultInjector) c.newInstance();
        } catch (Exception e) {
            logger.error("loadFaultInjector> failed to load fault injector {}; {}",
                         faultInjectorName, e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

}
