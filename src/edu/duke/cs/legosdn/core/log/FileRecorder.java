
package edu.duke.cs.legosdn.core.log;

import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsg;
import edu.duke.cs.legosdn.core.appvisor.dplane.LinkDiscoveryNotification;
import edu.duke.cs.legosdn.core.appvisor.dplane.PortNotification;
import edu.duke.cs.legosdn.core.appvisor.dplane.SwitchNotification;
import net.floodlightcontroller.core.IListener;
import net.floodlightcontroller.packet.IPv4;
import org.openflow.protocol.OFFlowMod;
import org.openflow.protocol.OFMessage;
import org.openflow.protocol.action.OFAction;
import org.openflow.protocol.action.OFActionOutput;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Utility methods to log messages to file, opening and closing the file, prior and after the logging.
 */
public final class FileRecorder implements Recorder {

    private static final FileRecorder INSTANCE;

    static {
        INSTANCE = new FileRecorder();
    }

    private FileRecorder() {
        /* Avoid explicitly instantiating class. */
    }

    public static Recorder getInstance() {
        return INSTANCE;
    }

    @Override
    public void logInMsg(String m, File f) {
        logMsg(m, ">>", f);
    }

    @Override
    public void logOutMsg(String m, File f) {
        logMsg(m, "<<", f);
    }

    @Override
    public void logMsg(String m, String d, File f) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(f, true));
            pw.println(String.format("%d  %s %s", System.currentTimeMillis(), d, m));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    @Override
    public void logMsg(DPlaneMsg m, File f) {
        logOutMsg(m, f);
    }

    @Override
    public void logOutMsg(DPlaneMsg m, File f) {
        logMsg(m, "<<", f);
    }

    @Override
    public void logInMsg(DPlaneMsg m, File f) {
        logMsg(m, ">>", f);
    }

    @Override
    public void logMsg(DPlaneMsg m, String d, File f) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(f, true));
            switch (m.getMsgType()) {
                case OFPACKET_IN:
                    break;
                case OFFLOW_REMOVED:
                    break;
                case OFPACKET_OUT:
                    break;
                case OFFLOW_MOD: {
                    final OFFlowMod ofFlowMod = (OFFlowMod) m.getMsgPayload();
                    final List<OFAction> actions = ofFlowMod.getActions();
                    final int dst = ofFlowMod.getMatch().getNetworkDestination();
                    pw.println(String.format("%d  %s %s Sw-%d:%d => %s",
                                             System.currentTimeMillis(),
                                             d,
                                             m.getMsgType().toString(),
                                             m.getSwitchID(),
                                             ((OFActionOutput) actions.get(0)).getPort(),
                                             IPv4.fromIPv4Address(dst)));
                    break;
                }
                case OFSTATS_REQUEST:
                    break;
                case OFSTATS_REPLY:
                    break;
                case SWITCH_NOTIF: {
                    final SwitchNotification swNotif = (SwitchNotification) m.getMsgPayload();
                    pw.println(String.format("%d  %s %s Sw-%d %s",
                                             System.currentTimeMillis(),
                                             d,
                                             m.getMsgType().toString(),
                                             m.getSwitchID(),
                                             swNotif.getNotifType().toString()));
                    break;
                }
                case PORT_NOTIF: {
                    final PortNotification portNotif = (PortNotification) m.getMsgPayload();
                    pw.println(String.format("%d  %s %s Sw-%d:%d %s",
                                             System.currentTimeMillis(),
                                             d,
                                             m.getMsgType().toString(),
                                             m.getSwitchID(),
                                             portNotif.getPort().getPortNumber(),
                                             portNotif.getPortChangeType().toString()));
                    break;
                }
                case LINK_NOTIF: {
                    final LinkDiscoveryNotification linkNotif = (LinkDiscoveryNotification) m.getMsgPayload();
                    pw.println(String.format("%d  %s %s #updates: %d",
                                             System.currentTimeMillis(),
                                             d,
                                             m.getMsgType().toString(),
                                             linkNotif.getNumLinkDiscoveryUpdates()));
                    break;
                }
                case LISTENER_CMD: {
                    final IListener.Command cmd = (IListener.Command) m.getMsgPayload();
                    pw.println(String.format("%d  %s %s %s",
                                             System.currentTimeMillis(),
                                             d,
                                             m.getMsgType().toString(),
                                             cmd.toString()));
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    @Override
    public void logInMsg(Long s, OFMessage m, File f) {
        logMsg(s, m, ">>", f);
    }

    @Override
    public void logOutMsg(Long s, OFMessage m, File f) {
        logMsg(s, m, "<<", f);
    }

    @Override
    public void logMsg(Long s, OFMessage m, String d, File f) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(f, true));
            switch (m.getType()) {
                case HELLO:
                    break;
                case ERROR:
                    break;
                case ECHO_REQUEST:
                    break;
                case ECHO_REPLY:
                    break;
                case VENDOR:
                    break;
                case FEATURES_REQUEST:
                    break;
                case FEATURES_REPLY:
                    break;
                case GET_CONFIG_REQUEST:
                    break;
                case GET_CONFIG_REPLY:
                    break;
                case SET_CONFIG:
                    break;
                case PACKET_IN:
                    break;
                case FLOW_REMOVED:
                    break;
                case PORT_STATUS:
                    break;
                case PACKET_OUT:
                    break;
                case FLOW_MOD: {
                    final OFFlowMod ofFlowMod = (OFFlowMod) m;
                    final List<OFAction> actions = ofFlowMod.getActions();
                    final int dst = ofFlowMod.getMatch().getNetworkDestination();
                    pw.println(String.format("%d  %s %s Sw-%d:%d => %s",
                                             System.currentTimeMillis(),
                                             d,
                                             m.getType().toString(),
                                             s,
                                             ((OFActionOutput) actions.get(0)).getPort(),
                                             IPv4.fromIPv4Address(dst)));
                    break;
                }
                case PORT_MOD:
                    break;
                case STATS_REQUEST:
                    break;
                case STATS_REPLY:
                    break;
                case BARRIER_REQUEST:
                    break;
                case BARRIER_REPLY:
                    break;
                case QUEUE_GET_CONFIG_REQUEST:
                    break;
                case QUEUE_GET_CONFIG_REPLY:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

}
