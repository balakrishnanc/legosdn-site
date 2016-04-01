package edu.duke.cs.legosdn.tools.cr;

import edu.duke.cs.legosdn.core.appvisor.cplane.RemoteEndpt;

/**
 * Checkpoint and restore service client implementation.
 */
public interface CRClientProvider {

    /**
     * Connect to the C/R service.
     */
    public boolean connect();

    /**
     * Disconnect from the C/R service.
     */
    public void disconnect();

    /**
     * Check if C/R service is ready.
     *
     * @return true, if C/R service is OK and ready to receive control messages; false, otherwise
     */
    public boolean isServiceReady();

    /**
     * Register a running process.
     *
     * @return true, if the registration was successful; false, otherwise
     */
    public boolean registerProcess(RemoteEndpt proc);

    /**
     * Checkpoint a running process.
     *
     * @param proc Application to checkpoint
     * @param appNum Application Number
     * @return true, if the checkpoint operation was successful; false, otherwise
     */
    public boolean checkpointProcess(RemoteEndpt proc, Integer appNum);

    /* PID value returned hen process restoration fails. */
    public static final int INVALID_PROC_PID = -1;

    /**
     * Restore an application process from a previous checkpoint.
     *
     * @param proc Application to restore
     * @param appNum Application Number
     * @return PID of the restored process, if the restore operation was successful; -1, otherwise.
     */
    public int restoreProcess(RemoteEndpt proc, Integer appNum);

}
