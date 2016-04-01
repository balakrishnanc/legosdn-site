package edu.duke.cs.legosdn.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple wrapper to restart a command on failure.
 */
public class RestartOnFailure {

    private static final List<Long> CrashTimes = new ArrayList<Long>();

    /**
     * @return Usage string
     */
    public static String showUsage() {
        return String.format("Usage: %s <command>", RestartOnFailure.class.getCanonicalName());
    }

    /**
     * Write crash times to file.
     *
     * @param fileName Absolute path to output file
     * @param times Crash times
     */
    private static void writeTimersToFile(String fileName, List<Long> times) {
        final File outFile = new File(fileName);
        FileWriter writer = null;
        try {
            writer = new FileWriter(outFile);

            for (Long t : CrashTimes) {
                writer.write(String.format("%d\n", t));
            }
        } catch (IOException e) {
            System.err.printf("Failed to write crash times! %s\n", e.getLocalizedMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    /* Ignore error! */
                }
            }
        }
    }

    public static void cleanup() {
        writeTimersToFile("/legosdn/runtime/timers/ctrlr-crash-times.txt", CrashTimes);
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                cleanup();
            }

        });

        if (args.length != 1) {
            System.err.println(RestartOnFailure.showUsage());
            System.exit(1);
        }

        File cmd = new File(args[0]);
        if (!cmd.exists()) {
            System.err.printf("Error: '%s' - missing!\n", cmd.getAbsolutePath());
            System.exit(1);
        }
        if (!cmd.isFile()) {
            System.err.printf("Error: '%s' - not a file!\n", cmd.getAbsolutePath());
            System.exit(1);
        }
        if (!cmd.canRead() || !cmd.canExecute()) {
            System.err.printf("Error: '%s' - not executable; check permissions!\n", cmd.getAbsolutePath());
            System.exit(1);
        }

        while (true) {
            int r = 0;
            try {
                System.out.printf("RestartOnFailure> %s\n", cmd.getAbsolutePath());
                while (true) {
                    final Process p = Runtime.getRuntime().exec(cmd.getAbsolutePath());
                    final int exitCode = p.waitFor();
                    System.out.printf("%d  $? %d\n", ++r, exitCode);
                    if (exitCode == 0) {
                        break;
                    }
                    final long crashTime = System.currentTimeMillis();
                    CrashTimes.add(crashTime);
                }
            } catch (InterruptedException e) {
                System.err.printf("Error: interrupted while running '%s'; %s \n",
                                  cmd.getAbsolutePath(), e.getLocalizedMessage());
                System.exit(2);
            } catch (IOException e) {
                System.err.printf("Error: failed to run '%s'; %s \n",
                                  cmd.getAbsolutePath(), e.getLocalizedMessage());
                System.exit(2);
            }
        }
    }

}
