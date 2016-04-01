package edu.duke.cs.legosdn.core;

public interface Defaults {

    /* AppProxy Proxy connection parameters. */
    int PROXY_PORT = 8833;

    /* NetLog connection parameters. */
    int NETLOG_PORT = 9933;

    /* AppProxy Stub connection parameters. */
    int STUB_PORT = 9901;

    int CHANNEL_BUF_SZ = 128 * 1024; // 128KB

    /* Time (in milliseconds) to sleep while waiting for application responses in control plane. */
    long APP_CP_RESP_WAIT_TIME = 15;

    /* Time (in milliseconds) to sleep while waiting for application responses in data plane. */
    long APP_DP_RESP_WAIT_TIME = 30;

    /* Time (in milliseconds) to sleep while waiting for checkpoint or restore operation to complete. */
    long CR_RESP_WAIT_TIME = 250;

    /* Time to wait when connection is still in progress (in milliseconds). */
    long CONN_WAIT_TIMEOUT = 30;

    /* Application registration timeout (in milliseconds). */
    long APP_REGN_TIMEOUT = 10;

    /* Time to wait to process application shutdown (in milliseconds). */
    long APP_SHUTDOWN_WAIT = 6000;

    /* Time (in milliseconds) to periodically check the health of the stub. */
    long APP_HEALTH_CHECK_INTVAL = 10;

    /* Default checkpoint frequency; one checkpoint generated for every APP_CHECKPT_FREQ messages per application */
    int APP_CHKPT_FREQ = 10;

    /* Number of times we can attempt to make an application process an inbound message */
    int MAX_RETRY_ATTEMPTS = 3;

    /* By default, do not enable invertible transforms. */
    boolean INVERT_XFORMS = false;

    /* By default, do not enable event transforms. */
    boolean DISABLE_XFORMS = false;

    /* By default, do not disable per-app network state management. */
    boolean DISABLE_PER_APP_NS = false;

    /* By default, do not enable message logs. */
    boolean ENABLE_MLOG = false;

    /* By default, do not disable replay (after recovery) */
    boolean DISABLE_REPLAY = false;

    /* By default, do not disable NetLog */
    boolean DISABLE_NETLOG = false;

    /* By default, run NetLog remotely (do not use NetLog locally within AppVisor-Proxy) */
    boolean USE_LOCAL_NETLOG = false;

    /* By default, do no log time spent in rebooting proxy */
    boolean LOG_CTRLR_REBOOTS = false;

    /* By default, do no log time spent in rebooting stub */
    boolean LOG_APP_REBOOTS = false;

    /* By default, do not log time spent in restoring stub */
    boolean LOG_APP_RESTORES = false;

    /* Default value for the C/R service listener port. */
    int CR_DEF_SERVICE_PORT = 9080;

    /* LegoSDN runtime directory path. */
    String BASE_RUNTIME_PATH = "/legosdn/runtime";

    /* Location for application PID files. */
    String APP_PID_DIR_PATH = BASE_RUNTIME_PATH + "/stubs";

    /* Path to location containing runtime tools. */
    String TOOLS_PATH = BASE_RUNTIME_PATH + "/tools";

    /* Shell-script to restart AppVisor-Stub in the background (or as a daemon) */
    String RESTART_WRAPPER = TOOLS_PATH + "/daemonize.sh";

    /* Shell-script to launch the AppVisor-Stub */
    String STUB_LAUNCHER = TOOLS_PATH + "/apploader-lomem.sh";

    /* Path to location containing timers. */
    String TIMERS_PATH = BASE_RUNTIME_PATH + "/timers";

    /* Path to location containing counters. */
    String COUNTERS_PATH = BASE_RUNTIME_PATH + "/counters";

    /* Path to location containing crash indicator files. */
    String CRASH_IND_BASE_DIR = BASE_RUNTIME_PATH + "/crash-indicators";

    /* Path to location containing crash flags. */
    String CRASH_FLAGS_PATH = BASE_RUNTIME_PATH + "/crash-flags";

    /* Path to location containing application's runtime data. */
    String APP_RUNTIME_PATH = BASE_RUNTIME_PATH + "/app";

    /* Path to location containing application resources. */
    String APP_RESOURCES_DIR = APP_RUNTIME_PATH + "/resources";

    /* Path to location containing application logs. */
    String APP_LOGS_PATH = APP_RUNTIME_PATH + "/logs";

}
