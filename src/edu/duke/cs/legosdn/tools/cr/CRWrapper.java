package edu.duke.cs.legosdn.tools.cr;

import edu.duke.cs.legosdn.core.appvisor.cplane.RemoteEndpt;
import edu.duke.cs.legosdn.core.appvisor.stub.AppLoader;
import edu.duke.cs.legosdn.tools.cr.srv.CRServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * CRWrapper is a wrapper around a binary that implements the checkpoint and restore functionality.
 */
public class CRWrapper {

    private static final Logger logger = LoggerFactory.getLogger(CRWrapper.class);

    // Service configuration
    private final CRServiceConfig config;

    private final ProcessBuilder processBuilder;

    private final Map<String, Long> chkptCounters;

    /**
     * Initialize the CRService.
     */
    public CRWrapper() {
        this.config = new CRServiceConfig("/opt/criu/criu", 9080, "/legosdn/checkpoints");
        this.processBuilder = new ProcessBuilder();
        this.chkptCounters = new HashMap<String, Long>(8);
    }

    /**
     * Return path of the checkpoint storage location.
     *
     * @param basePath Base path to store application checkpoints
     * @param appId Application identifier
     * @return Checkpoint storage path associated with the given application
     */
    private static String getCheckpointStorePath(String basePath, String appId) {
        return String.format("%s%c%s", basePath, File.separatorChar, appId);
    }

    /**
     * Return path of the checkpoint storage location.
     *
     * @param basePath Base path to store application checkpoints
     * @param appId Application identifier
     * @param n Checkpoint counter
     * @return Checkpoint storage path associated with the given application
     */
    private static String getIncrCheckpointStorePath(String basePath, String appId, long n) {
        return String.format("%s%c%s%c%d", basePath, File.separatorChar, appId, File.separatorChar, n);
    }

    /**
     * Check if the binary path is valid.
     *
     * @param binaryPath Absolute path of the checkpoint/restore binary
     * @return true, if path is valid; false, otherwise
     */
    private static boolean checkBinaryPath(String binaryPath) {
        File binaryFile = new File(binaryPath);
        if (!binaryFile.exists() || !binaryFile.isFile()) {
            if (logger.isWarnEnabled()) {
                logger.warn("checkBinaryPath> cannot find C/R binary at {}", binaryPath);
            }
            return false;
        }
        if (!binaryFile.canRead()) {
            if (logger.isWarnEnabled()) {
                logger.warn("checkBinaryPath> cannot read C/R binary at {}", binaryPath);
            }
            return false;
        }
        if (!binaryFile.canExecute()) {
            if (logger.isWarnEnabled()) {
                logger.warn("checkBinaryPath> cannot execute C/R binary at {}", binaryPath);
            }
            return false;
        }
        return true;
    }

    /**
     * Register an application process with the C/R service.
     *
     * @param proc Application process details
     * @return true, if the registration was successful; false, otherwise.
     */
    public boolean registerProcess(RemoteEndpt proc) {
        if (!CRWrapper.checkBinaryPath(config.binaryPath)) {
            return false;
        }

        this.chkptCounters.put(proc.appId, new Long(1));

        // Create a location on the filesystem for storing checkpoints, if required
        String storePathname = CRWrapper.getCheckpointStorePath(config.storagePath, proc.appId);
        File storePath = new File(storePathname);

        if (storePath.exists()) {
            if (storePath.isDirectory()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("registerProcess> storage path {} exists", storePath);
                }

                return true;
            } else {
                logger.error("registerProcess> storage path {} is not a directory!", storePath);

                return false;
            }
        }

        if (!storePath.mkdir()) {
            logger.error("registerProcess> failed to create storage path {}!", storePath);

            return false;
        }

        if (logger.isInfoEnabled()) {
            logger.info("registerProcess> storage path {} created", storePath);
        }

        return true;
    }

    /**
     * Checkpoint an application process using a C/R binary.
     *
     * @param proc Application process details
     * @return true, if the checkpoint operation was successful; false, otherwise.
     */
    public boolean checkpointProcess(RemoteEndpt proc) {
//        long tBeg = System.currentTimeMillis();
        long n = this.chkptCounters.put(proc.appId, this.chkptCounters.get(proc.appId) + 1);

        String storePathname = CRWrapper.getIncrCheckpointStorePath(config.storagePath, proc.appId, n);
        File storePath = new File(storePathname);
        if (!storePath.exists() && !storePath.mkdir()) {
            logger.error("registerProcess> failed to create storage path {}!", storePath);

            return false;
        }

        if (n > 1) {
            String prev = String.format("../%d", n - 1);
            processBuilder.command("/usr/bin/sudo",     // Run command as 'root'
                                   config.binaryPath,   // Path of C/R binary
                                   "dump",              // Select checkpoint operation
                                   "-j",                // Application process is attached to a shell
                                   "-D",                // Set checkpoint storage path
                                   storePathname,
                                   "-t",                // Set PID
                                   String.valueOf(proc.pid),
                                   "-R",                // Do not halt the application process after a checkpoint
                                   "--track-mem",       // Track memory changes
                                   "--prev-images-dir", // Previous checkpoint path
                                   prev);
        } else {
            processBuilder.command("/usr/bin/sudo",     // Run command as 'root'
                                   config.binaryPath,   // Path of C/R binary
                                   "dump",              // Select checkpoint operation
                                   "-j",                // Application process is attached to a shell
                                   "-D",                // Set checkpoint storage path
                                   storePathname,
                                   "-t",                // Set PID
                                   String.valueOf(proc.pid),
                                   "-R",                // Do not halt the application process after a checkpoint
                                   "--track-mem");      // Track memory changes
        }
        if (logger.isDebugEnabled()) {
            logger.debug("checkpointProcess> {}", proc.appId);
        }

        try {
            boolean ok = CRWrapper.executeCommand(processBuilder);
//            long tEnd = System.currentTimeMillis();
//            System.out.println(tEnd - tBeg);
            return ok;
        } catch (Exception e) {
            logger.error("checkpointProcess> Failed to checkpoint {}; {}", proc.appId, e.getLocalizedMessage());
        }

        return false;
    }

    /**
     * Restore an application process from a previous checkpoint using a C/R binary.
     *
     * @param proc Application process details
     * @return true, if the restore operation was successful; false, otherwise.
     */
    public boolean restoreProcess(RemoteEndpt proc) {
        processBuilder.command("/usr/bin/sudo",     // Run command as 'root'
                               config.binaryPath,   // Path of C/R binary
                               "restore",           // Select restore operation
                               "-j",                // Application process is attached to a shell
                               "-D",                // Set checkpoint storage path
                               CRWrapper.getCheckpointStorePath(config.storagePath, proc.appId),
                               " --pidfile ",       // Set PID file path
                               AppLoader.getPIDFilepath(proc),
                               "-d");               // Detach the C/R binary from the restored process image

        if (logger.isDebugEnabled()) {
            logger.debug("restoreProcess> {}", proc.appId);
        }

        try {
            return CRWrapper.executeCommand(processBuilder);
        } catch (Exception e) {
            logger.error("restoreProcess> Failed to restore {}; {}", proc.appId, e.getLocalizedMessage());
        }

        return false;
    }

    /**
     * Execute command and return a boolean indicating if execution was successful or not.
     *
     * @param processBuilder Instance of ProcessBuilder configured to execute the remote command.
     * @return true, if command executed successfully; false, otherwise
     * @throws java.io.IOException
     * @throws InterruptedException
     */
    private static boolean executeCommand(ProcessBuilder processBuilder) throws IOException, InterruptedException {
        final long tBeg = System.currentTimeMillis();
        final Process crProcess = processBuilder.start();
        final int exitStatus = crProcess.waitFor();
        final long tEnd = System.currentTimeMillis();

        if (logger.isDebugEnabled()) {
            logger.debug("executeCommand> spent {}ms; exit-code:{}",
                         tEnd - tBeg, exitStatus);
        }

//        final BufferedReader brOut = new BufferedReader(new InputStreamReader(crProcess.getErrorStream()));
//        String outLine;
//        while ((outLine = brOut.readLine()) != null) {
//            System.out.printf("  ~> %s\n", outLine);
//        }
//        System.out.flush();
//        brOut.close();

        if (exitStatus == 0) {
            return true;
        }

        final BufferedReader brErr = new BufferedReader(new InputStreamReader(crProcess.getErrorStream()));
        String errLine;
        while ((errLine = brErr.readLine()) != null) {
            System.err.printf("  ~> %s\n", errLine);
        }
        System.err.flush();
        brErr.close();

        return false;
    }

}
