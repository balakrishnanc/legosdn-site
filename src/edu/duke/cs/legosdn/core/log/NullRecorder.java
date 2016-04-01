
package edu.duke.cs.legosdn.core.log;

import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsg;
import org.openflow.protocol.OFMessage;

import java.io.File;

public class NullRecorder implements Recorder {

    private static final NullRecorder INSTANCE;

    static {
        INSTANCE = new NullRecorder();
    }

    private NullRecorder() {
        /* Avoid explicitly instantiating class. */
    }

    public static Recorder getInstance() {
        return INSTANCE;
    }

    @Override
    public void logInMsg(String m, File f) {

    }

    @Override
    public void logOutMsg(String m, File f) {

    }

    @Override
    public void logMsg(String m, String d, File f) {

    }

    @Override
    public void logMsg(DPlaneMsg m, File f) {

    }

    @Override
    public void logOutMsg(DPlaneMsg m, File f) {

    }

    @Override
    public void logInMsg(DPlaneMsg m, File f) {

    }

    @Override
    public void logMsg(DPlaneMsg m, String d, File f) {

    }

    @Override
    public void logInMsg(Long s, OFMessage m, File f) {

    }

    @Override
    public void logOutMsg(Long s, OFMessage m, File f) {

    }

    @Override
    public void logMsg(Long s, OFMessage m, String d, File f) {

    }

}
