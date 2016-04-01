package edu.duke.cs.legosdn.tools.cr.srv;

import edu.duke.cs.legosdn.core.Defaults;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Checkpoint and restore service configuration.
 */
public class CRServiceConfig {

    // Default value for the path of the C/R binary
    public static final String CR_DEF_BINARY_PATH = "/usr/bin/criu";
    // Path of C/R binary
    public final String binaryPath;

    // Listener port for the C/R service  listener port
    public final int servicePort;

    // Default value for path where checkpoints are stored on the filesystem
    // NOTE: Ensure that the path does not end with a slash.
    public static final String CR_DEF_STORAGE_PATH = "/legosdn/checkpoints";
    // Path where checkpoints are stored on the filesystem
    public final String storagePath;

    /**
     * ConfigProperties identifies the keys whose values can be used to instantiate CRServiceConfig.
     */
    public static enum ConfigProperties {

        BINARY_PATH("binary.path"),     // Absolute or relative path of the checkpoint and restore binary
        SERVICE_PORT("service.port"),   // HTTP Service listener port number
        STORAGE_PATH("storage.path"),   // Location on the filesystem where the checkpoints are stored
        ;

        public final String key;

        private ConfigProperties(String key) {
            this.key = key;
        }

    }

    /**
     * Instantiate CRServiceConfig with the absolute path of the C/R binary.
     *
     * @param binaryPath Absolute path of the C/R program
     * @param servicePort C/R service listener port
     * @param storagePath C/R service checkpoint (or snapshot) storage path
     */
    public CRServiceConfig(String binaryPath, int servicePort, String storagePath) {
        this.binaryPath = binaryPath;
        this.servicePort = servicePort;
        this.storagePath = storagePath;
    }

    /**
     * Create an instance of CRServiceConfig from a properties file.
     *
     * @param propsFileName absolute/relative path of a properties file
     * @return Instance of CRServiceConfig
     * @throws IOException
     * @throws CRServiceConfigException
     */
    public static CRServiceConfig loadFromProperties(String propsFileName)
            throws IOException, CRServiceConfigException {
        Properties properties = new Properties();

        final File propsFile = new File(propsFileName);
        FileReader propsRd = null;
        try {
            propsRd = new FileReader(propsFile);
            properties.load(propsRd);
        } catch (FileNotFoundException e) {
            String err = String.format("Cannot find properties file '%s'!", propsFile.getAbsolutePath());
            throw new IOException(err, e);
        } finally {
            if (propsRd != null) {
                propsRd.close();
            }
        }

        String binaryPath = properties.getProperty(ConfigProperties.BINARY_PATH.key);
        if (binaryPath == null) {
            binaryPath = CRServiceConfig.CR_DEF_BINARY_PATH;
        } else if (binaryPath.trim().equals("")) {
            String err = String.format("'%s' cannot be an empty string",
                                       ConfigProperties.BINARY_PATH.key);
            throw new CRServiceConfigException(err, new RuntimeException());
        }

        String portNum = properties.getProperty(ConfigProperties.SERVICE_PORT.key);
        int port;
        if (portNum == null) {
            port = Defaults.CR_DEF_SERVICE_PORT;
        } else {
            try {
                port = Integer.parseInt(portNum);
            } catch (NumberFormatException e) {
                String err = String.format("'%s' should be an integer value between [1024, 65535]",
                                           ConfigProperties.SERVICE_PORT.key);
                throw new CRServiceConfigException(err, e);
            }
        }

        String storagePath = properties.getProperty(ConfigProperties.STORAGE_PATH.key);
        if (storagePath == null) {
            storagePath = CRServiceConfig.CR_DEF_STORAGE_PATH;
        } else if (storagePath.trim().equals("")) {
            String err = String.format("'%s' cannot be an empty string",
                                       ConfigProperties.STORAGE_PATH.key);
            throw new CRServiceConfigException(err, new RuntimeException());
        }

        return new CRServiceConfig(binaryPath, port, storagePath);
    }

}
