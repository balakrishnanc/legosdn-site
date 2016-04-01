package edu.duke.cs.legosdn.core.appvisor.dplane;

import net.floodlightcontroller.core.IListener;
import org.jboss.netty.buffer.ChannelBuffer;
import org.openflow.protocol.*;
import org.openflow.protocol.action.*;
import org.openflow.protocol.statistics.*;
import org.openflow.util.U8;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DPlaneMsgExternalizable is a simple wrapper over DPlaneMsg with methods that know how to serialize and deserialize
 * various types of data plane messages.
 *
 * @see DPlaneMsg
 */
public class DPlaneMsgExternalizable extends DPlaneMsg implements Externalizable {

    /**
     * Initialize DPlaneMsgExternalizable.
     *
     * @param msgType Message type
     * @param switchID Switch Identifier
     * @param msgPayload Message payload
     */
    public DPlaneMsgExternalizable(DPlaneMsgType msgType, long switchID, Object msgPayload) {
        super(msgType, switchID, msgPayload);
    }

    /**
     * Initialize DPlaneMsgExternalizable.
     *
     * @param cmd CONTINUE or STOP (Instance of IListener.Command)
     */
    public DPlaneMsgExternalizable(IListener.Command cmd) {
        super(cmd);
    }

    /**
     * Create empty DPlaneMsgExternalizable instance.
     */
    public DPlaneMsgExternalizable() {
        super(null, 0L, null);
    }

    /**
     * Serialize an instance of OFMessage.
     *
     * @param out Output stream
     * @param m Message instance to be serialized
     * @throws IOException
     * @see org.openflow.protocol.OFMessage
     */
    private static void writeOFMessage(ObjectOutput out, OFMessage m) throws IOException {
        out.writeByte(m.getVersion());
        out.writeByte(m.getType().getTypeValue());
        out.writeShort(m.getLength());
        out.writeInt(m.getXid());
    }

    /**
     * Serialize an instance of OFAction.
     *
     * @param out Output stream
     * @param m Instance to be serialized
     * @throws IOException
     * @see org.openflow.protocol.action.OFAction
     */
    private static void writeOFAction(ObjectOutput out, OFAction m) throws IOException {
        final OFActionType ofActionType = m.getType();
        out.writeShort(ofActionType.getTypeValue());
        out.writeShort(m.getLength());
        switch(ofActionType) {
            case OUTPUT: {
                final OFActionOutput actualAction = (OFActionOutput) m;
                out.writeShort(actualAction.getPort());
                out.writeShort(actualAction.getMaxLength());
                break;
            }
            case SET_VLAN_ID: {
                final OFActionVirtualLanIdentifier actualAction = (OFActionVirtualLanIdentifier) m;
                out.writeShort(actualAction.getVirtualLanIdentifier());
                out.writeShort((short) 0);
                break;
            }
            case SET_VLAN_PCP: {
                final OFActionVirtualLanPriorityCodePoint actualAction = (OFActionVirtualLanPriorityCodePoint) m;
                out.writeByte(actualAction.getVirtualLanPriorityCodePoint());
                out.writeShort((short) 0);
                out.writeByte((byte) 0);
                break;
            }
            case STRIP_VLAN: {
                out.writeInt(0);
                break;
            }
            case SET_DL_SRC:
            case SET_DL_DST: {
                final OFActionDataLayer actualAction = (OFActionDataLayer) m;
                final byte[] dataLayerAddress = actualAction.getDataLayerAddress();
                for (int i = 0; i < OFPhysicalPort.OFP_ETH_ALEN; i++) {
                    out.writeByte(dataLayerAddress[i]);
                }
                out.writeInt(0);
                out.writeShort((short) 0);
                break;
            }
            case SET_NW_SRC:
            case SET_NW_DST: {
                final OFActionNetworkLayerAddress actualAction = (OFActionNetworkLayerAddress) m;
                out.writeInt(actualAction.getNetworkAddress());
                break;
            }
            case SET_NW_TOS: {
                final OFActionNetworkTypeOfService actualAction = (OFActionNetworkTypeOfService) m;
                out.writeByte(actualAction.getNetworkTypeOfService());
                out.writeShort((short) 0);
                out.writeByte((byte) 0);
                break;
            }
            case SET_TP_SRC:
            case SET_TP_DST: {
                final OFActionTransportLayer actualAction = (OFActionTransportLayer) m;
                out.writeShort(actualAction.getTransportPort());
                out.writeShort((short) 0);
                break;
            }
            case OPAQUE_ENQUEUE: {
                final OFActionEnqueue actualAction = (OFActionEnqueue) m;
                out.writeShort(actualAction.getPort());
                out.writeShort((short) 0);
                out.writeInt(0);
                out.writeInt(actualAction.getQueueId());
                break;
            }
            case VENDOR: {
                final OFActionVendor actualAction = (OFActionVendor) m;
                out.writeInt(actualAction.getVendor());
                break;
            }
        }
    }

    /**
     * Serialize an instance of OFMatch.
     *
     * @param out Output stream
     * @param m Instance to be serialized
     * @throws IOException
     * @see org.openflow.protocol.OFMatch
     */
    private static void writeOFMatch(ObjectOutput out, OFMatch m) throws IOException {
        out.writeInt(m.getWildcards());
        out.writeShort(m.getInputPort());
        final byte[] dataLayerSource = m.getDataLayerSource();
        for (int n = 0; n < 6; n++) {
            out.writeByte(dataLayerSource[n]);
        }
        final byte[] dataLayerDestination = m.getDataLayerDestination();
        for (int n = 0; n < 6; n++) {
            out.writeByte(dataLayerDestination[n]);
        }
        out.writeShort(m.getDataLayerVirtualLan());
        out.writeByte(m.getDataLayerVirtualLanPriorityCodePoint());
        out.writeShort(m.getDataLayerType());
        out.writeByte(m.getNetworkTypeOfService());
        out.writeByte(m.getNetworkProtocol());
        out.writeInt(m.getNetworkSource());
        out.writeInt(m.getNetworkDestination());
        out.writeShort(m.getTransportSource());
        out.writeShort(m.getTransportDestination());
    }

    /**
     * Serialize an instance of OFPacketIn.
     *
     * @param out Output stream
     * @param m Message instance to be serialized
     * @throws IOException
     * @see org.openflow.protocol.OFPacketIn
     */
    private static void writeOFPacketIn(ObjectOutput out, OFPacketIn m) throws IOException {
        DPlaneMsgExternalizable.writeOFMessage(out, m);
        out.writeInt(m.getBufferId());
        out.writeShort(m.getTotalLength());
        out.writeShort(m.getInPort());
        out.writeByte((byte) m.getReason().ordinal());
        final int sz = m.getPacketData().length;
        out.writeInt(sz);
        final byte[] packetData = m.getPacketData();
        for (int i = 0; i < sz; i++) {
            out.writeByte(packetData[i]);
        }
    }

    /**
     * Serialize an instance of OFFlowRemoved.
     *
     * @param out Output stream
     * @param m Message instance to be serialized
     * @throws IOException
     * @see org.openflow.protocol.OFFlowRemoved
     */
    private static void writeOFFlowRemoved(ObjectOutput out, OFFlowRemoved m) throws IOException {
        DPlaneMsgExternalizable.writeOFMessage(out, m);
        DPlaneMsgExternalizable.writeOFMatch(out, m.getMatch());
        out.writeLong(m.getCookie());
        out.writeShort(m.getPriority());
        out.writeByte((byte) m.getReason().ordinal());
        out.writeByte((byte) 0);
        out.writeInt(m.getDurationSeconds());
        out.writeInt(m.getDurationNanoseconds());
        out.writeShort(m.getIdleTimeout());
        out.writeByte((byte) 0); // pad
        out.writeByte((byte) 0); // pad
        out.writeLong(m.getPacketCount());
        out.writeLong(m.getByteCount());
    }

    /**
     * Serialize an instance of OFPacketOut.
     *
     * @param out Output stream
     * @param m Message instance to be serialized
     * @throws IOException
     * @see org.openflow.protocol.OFPacketOut
     */
    private static void writeOFPacketOut(ObjectOutput out, OFPacketOut m) throws IOException {
        DPlaneMsgExternalizable.writeOFMessage(out, m);
        out.writeInt(m.getBufferId());
        out.writeShort(m.getInPort());
        out.writeShort(m.getActionsLength());
        // Actions
        out.writeInt(m.getActions().size());
        for (OFAction ofAction : m.getActions()) {
            DPlaneMsgExternalizable.writeOFAction(out, ofAction);
        }
        if (m.getPacketData() != null) {
            final int sz = m.getPacketData().length;
            out.writeInt(sz);
            final byte[] packetData = m.getPacketData();
            for (int i = 0; i < sz; i++) {
                out.writeByte(packetData[i]);
            }
        } else {
            out.writeInt(0);
        }
    }

    /**
     * Serialize an instance of OFFlowMod.
     *
     * @param out Output stream
     * @param m Message instance to be serialized
     * @throws IOException
     * @see org.openflow.protocol.OFFlowMod
     */
    private static void writeOFFlowMod(ObjectOutput out, OFFlowMod m) throws IOException {
        DPlaneMsgExternalizable.writeOFMessage(out, m);

        // Match
        DPlaneMsgExternalizable.writeOFMatch(out, m.getMatch());
        // Body
        out.writeLong(m.getCookie());
        out.writeShort(m.getCommand());
        out.writeShort(m.getIdleTimeout());
        out.writeShort(m.getHardTimeout());
        out.writeShort(m.getPriority());
        out.writeInt(m.getBufferId());
        out.writeShort(m.getOutPort());
        out.writeShort(m.getFlags());
        // Actions
        if (m.getActions() != null) {
            out.writeInt(m.getActions().size());
            for (OFAction ofAction : m.getActions()) {
                DPlaneMsgExternalizable.writeOFAction(out, ofAction);
            }
        } else {
            out.writeInt(0);
        }
    }

    /**
     * Serialize an instance of SwitchNotification.
     *
     * @param out Output stream
     * @param m Message instance to be serialized
     * @throws IOException
     * @see edu.duke.cs.legosdn.core.appvisor.dplane.SwitchNotification
     */
    private static void writeSwitchNotification(ObjectOutput out, SwitchNotification m) throws IOException {
        m.writeExternal(out);
    }

    /**
     * Serialize an instance of PortNotification.
     *
     * @param out Output stream
     * @param m Message instance to be serialized
     * @throws IOException
     * @see edu.duke.cs.legosdn.core.appvisor.dplane.PortNotification
     */
    private static void writePortNotification(ObjectOutput out, PortNotification m) throws IOException {
        m.writeExternal(out);
    }

    /**
     * Serialize an instance of LinkDiscoveryNotification.
     *
     * @param out Output stream
     * @param m Message instance to be serialized
     * @throws IOException
     * @see edu.duke.cs.legosdn.core.appvisor.dplane.LinkDiscoveryNotification
     */
    private static void writeLinkNotification(ObjectOutput out, LinkDiscoveryNotification m) throws IOException {
        m.writeExternal(out);
    }

    /**
     * Serialize an instance of OFStatisticsMessageBase
     * 
     * @param out Output stream
     * @param m Message instance to be serialized
     * @throws IOException 
     * @see org.openflow.protocol.OFStatisticsMessageBase
     */
    private static void writeOFStatisticsMessageBase(ObjectOutput out, OFStatisticsMessageBase m) throws IOException{
    	DPlaneMsgExternalizable.writeOFMessage(out, m);
    	out.writeShort( m.getStatisticType().getTypeValue() );
    	out.writeShort( m.getFlags() );
    	if( m.getStatistics() != null ){
    		out.writeInt( m.getStatistics().size() );
    		for( OFStatistics statistic : m.getStatistics() ){
    			DPlaneMsgExternalizable.writeOFStatistic(out, statistic);
    		}
    	}

    }
    
    /**
     * Serialize an instance of OFStatistics
     * 
     * @param out Output stream
     * @param m Statistics Message instance to be serialized
     * @throws IOException
     * @see org.openflow.protocol.statistics.OFStatistics
     */
    private static void writeOFStatistic(ObjectOutput out, OFStatistics m) throws IOException{
    	if (m instanceof OFAggregateStatisticsReply){
    		DPlaneMsgExternalizable.writeOFAggregateStatisticsReply(out, (OFAggregateStatisticsReply) m);
    	} else if (m instanceof OFAggregateStatisticsRequest){
    		DPlaneMsgExternalizable.writeOFAggregateStatisticsRequest(out,(OFAggregateStatisticsRequest) m);
    	} else if (m instanceof OFDescriptionStatistics){
    		DPlaneMsgExternalizable.writeOFDescriptionStatistics(out, (OFDescriptionStatistics) m);
    	} else if (m instanceof OFFlowStatisticsReply){
    		DPlaneMsgExternalizable.writeOFFlowStatisticsReply(out, (OFFlowStatisticsReply) m);
    	} else if (m instanceof OFFlowStatisticsRequest){
    		DPlaneMsgExternalizable.writeOFFlowStatisticsRequest(out, (OFFlowStatisticsRequest) m );
    	} else if (m instanceof OFPortStatisticsReply){
    		DPlaneMsgExternalizable.writeOFPortStatisticsReply(out, (OFPortStatisticsReply) m);
    	} else if (m instanceof OFPortStatisticsRequest){
    		DPlaneMsgExternalizable.writeOFPortStatisticsRequest(out, (OFPortStatisticsRequest) m);
    	} else if (m instanceof OFQueueStatisticsReply){
    		DPlaneMsgExternalizable.writeOFQueueStatisticsReply(out, (OFQueueStatisticsReply) m);
    	} else if (m instanceof OFQueueStatisticsRequest){
    		DPlaneMsgExternalizable.writeOFQueueStatisticsRequest(out, (OFQueueStatisticsRequest) m);
    	} else if (m instanceof OFTableStatistics){
    		DPlaneMsgExternalizable.writeOFTableStatistics(out, (OFTableStatistics) m);
    	} else if (m instanceof OFVendorStatistics){
    		DPlaneMsgExternalizable.writeOFVendorStatistics(out, (OFVendorStatistics) m);
    	} else {
    		throw new UnknownMsgTypeException(String.format("Unsupported Statistic (type: %s)", m.getClass().getName()));
    	}
    }
    
    /**
     * Serialize an instance of OFAggregateStatisticsReply
     * 
     * @param out Output stream
     * @param m Statistics Message instance to be serialized
     * @throws IOException
     * @see org.openflow.protocol.statistics.OFAggregateStatisticsReply
     */
    private static void writeOFAggregateStatisticsReply(ObjectOutput out, OFAggregateStatisticsReply m) throws IOException{
    	out.writeLong( m.getPacketCount() );
		out.writeLong( m.getByteCount() );
		out.writeInt( m.getFlowCount() );
		out.writeInt( 0 ); // pad
    }
    
    /**
     * Serialize an instance of OFAggregateStatisticsRequest
     * 
     * @param out Output stream
     * @param m Statistics Message instance to be serialized
     * @throws IOException
     * @see org.openflow.protocol.statistics.OFAggregateStatisticsRequest
     */
    private static void writeOFAggregateStatisticsRequest(ObjectOutput out, OFAggregateStatisticsRequest m) throws IOException{
		DPlaneMsgExternalizable.writeOFMatch( out, m.getMatch() );
		out.writeByte( m.getTableId() );
		out.writeByte( (byte) 0);
		out.writeShort( m.getOutPort() );
    }
    
    /**
     * Serialize an instance of OFDescriptionStatistics
     * @param out
     * @param m
     * @throws IOException
     * @see org.openflow.protocol.statistics.OFDescriptionStatistics
     */
    private static void writeOFDescriptionStatistics(ObjectOutput out, OFDescriptionStatistics m) throws IOException{
    	DPlaneMsgExternalizable.writeString(out, m.getManufacturerDescription() );
    	DPlaneMsgExternalizable.writeString(out, m.getHardwareDescription() );
    	DPlaneMsgExternalizable.writeString(out, m.getSoftwareDescription() );
    	DPlaneMsgExternalizable.writeString(out, m.getSerialNumber() );
    	DPlaneMsgExternalizable.writeString(out, m.getDatapathDescription() );
    }
    
    /**
     * Serialize an instance of OFFlowStatisticsReply
     * 
     * @param out Output stream
     * @param m Statistics Message instance to be serialized
     * @throws IOException
     * @see org.openflow.protocol.statistics.OFFlowStatisticsReply
     */
    private static void writeOFFlowStatisticsReply(ObjectOutput out, OFFlowStatisticsReply m) throws IOException{
    	out.writeShort( m.getLength() );
		out.writeByte( m.getTableId() );
		out.writeByte( (byte) 0 );
		DPlaneMsgExternalizable.writeOFMatch(out, m.getMatch() );
		out.writeInt( m.getDurationSeconds() );
		out.writeInt( m.getDurationNanoseconds() );
		out.writeShort( m.getPriority() );
		out.writeShort( m.getIdleTimeout() );
		out.writeShort( m.getHardTimeout() );
		out.writeInt( 0 ); // pad
		out.writeShort( (short) 0 ); // pad
		out.writeLong( m.getCookie() );
		out.writeLong( m.getPacketCount() );
		out.writeLong( m.getByteCount() );
		out.writeInt( m.getActions().size() );
		System.out.println( m.getActions().size() );
		for( OFAction action : m.getActions() ){
			DPlaneMsgExternalizable.writeOFAction( out, action);
		}
    }
    
    /**
     * Serialize an instance of OFFlowStatisticsRequest
     * 
     * @param out Output stream
     * @param m Statistics Message instance to be serialized
     * @throws IOException
     * @see org.openflow.protocol.statistics.OFFlowStatisticsRequest
     */
    private static void writeOFFlowStatisticsRequest(ObjectOutput out, OFFlowStatisticsRequest m) throws IOException{
		DPlaneMsgExternalizable.writeOFMatch( out, m.getMatch() );
		out.writeByte( m.getTableId() );
		out.writeByte( (byte) 0 );
		out.writeShort( m.getOutPort() );
    }
    
    /**
     * Serialize an instance of OFPortStatisticsReply
     * 
     * @param out Output stream
     * @param m Statistics Message instance to be serialized
     * @throws IOException
     * @see org.openflow.protocol.statistics.OFPortStatisticsReply
     */
    private static void writeOFPortStatisticsReply(ObjectOutput out, OFPortStatisticsReply m) throws IOException{
        out.writeShort(m.getPortNumber());
        out.writeShort((short) 0); // pad
        out.writeInt(0); // pad
        out.writeLong(m.getreceivePackets());
        out.writeLong(m.getTransmitPackets());
        out.writeLong(m.getReceiveBytes());
        out.writeLong(m.getTransmitBytes());
        out.writeLong(m.getReceiveDropped());
        out.writeLong(m.getTransmitDropped());
        out.writeLong(m.getreceiveErrors());
        out.writeLong(m.getTransmitErrors());
        out.writeLong(m.getReceiveFrameErrors());
        out.writeLong(m.getReceiveOverrunErrors());
        out.writeLong(m.getReceiveCRCErrors());
        out.writeLong(m.getCollisions());
    }
    
    /**
     * Serialize an instance of OFPortStatisticsRequest
     * 
     * @param out Output stream
     * @param m Statistics Message instance to be serialized
     * @throws IOException
     * @see org.openflow.protocol.statistics.OFPortStatisticsRequest
     */
    private static void writeOFPortStatisticsRequest(ObjectOutput out, OFPortStatisticsRequest m) throws IOException{
		out.writeShort( m.getPortNumber() );
		out.writeShort( (short) 0 ); // pad
		out.writeInt( 0 ); // pad
    }
    
    /**
     * Serialize an instance of OFQueueStatisticsReply
     * 
     * @param out Output stream
     * @param m Statistics Message instance to be serialized
     * @throws IOException
     * @see org.openflow.protocol.statistics.OFQueueStatisticsReply
     */
    private static void writeOFQueueStatisticsReply(ObjectOutput out, OFQueueStatisticsReply m) throws IOException{
		out.writeShort(m.getPortNumber());
        out.writeShort((short) 0); // pad
        out.writeInt(m.getQueueId());
        out.writeLong(m.getTransmitBytes());
        out.writeLong(m.getTransmitPackets());
        out.writeLong(m.getTransmitErrors());
    }
    
    /**
     * Serialize an instance of OFQueueStatisticsRequest
     * 
     * @param out Output stream
     * @param m Statistics Message instance to be serialized
     * @throws IOException
     * @see org.openflow.protocol.statistics.OFQueueStatisticsRequest
     */
    private static void writeOFQueueStatisticsRequest(ObjectOutput out, OFQueueStatisticsRequest m) throws IOException{
		out.writeShort( m.getPortNumber() );
		out.writeShort( (short) 0 ); // pad
		out.writeInt( m.getQueueId() );
    }
    
    /**
     * Serialize an instance of OFTableStatistics
     * 
     * @param out Output stream
     * @param m Statistics Message instance to be serialized
     * @throws IOException
     * @see org.openflow.protocol.statistics.OFTableStatistics
     */
    private static void writeOFTableStatistics(ObjectOutput out, OFTableStatistics m) throws IOException{
		out.writeByte(m.getTableId());
        out.writeByte((byte) 0); // pad
        out.writeByte((byte) 0); // pad
        out.writeByte((byte) 0); // pad
        DPlaneMsgExternalizable.writeString(out, m.getName());
        out.writeInt(m.getWildcards());
        out.writeInt(m.getMaximumEntries());
        out.writeInt(m.getActiveCount());
        out.writeLong(m.getLookupCount());
        out.writeLong(m.getMatchedCount());
    }
    
    /**
     * Serialize an instance of OFVendorStatistics
     * 
     * @param out Output stream
     * @param m Statistics Message instance to be serialized
     * @throws IOException
     * @see org.openflow.protocol.statistics.OFVendorStatistics
     */
    private static void writeOFVendorStatistics(ObjectOutput out, OFVendorStatistics m) throws IOException{
		// OFVendorStatistics has no getVendor()!
    	out.writeInt( -1 );
		//out.writeInt( m.getVendor );
		/* OFVendorStatistics has no getBody method
		 *         if (body != null)
         * 				data.writeBytes(body);
		 */
    }
    
    /**
     * Helper method to write (UTF-8) strings. Our string serializer is buggy.
     * @param out Object output stream
     * @param stringToWrite
     * @throws IOException
     */
    public static void writeString(ObjectOutput out, String stringToWrite) throws IOException{
    	out.writeInt( stringToWrite.length() );
    	byte[] string = stringToWrite.getBytes(Charset.forName("UTF-8"));
    	for(int i=0; i<stringToWrite.length(); i++){
    		out.writeByte( string[i] );
    	}
    }

    /**
     * Helper method to write (UTF-8) strings. Our string serializer is buggy.
     * @param out Netty channel buffer
     * @param stringToWrite
     * @throws IOException
     */
    public static void writeString(ChannelBuffer out, String stringToWrite) {
        out.writeInt( stringToWrite.length() );
        byte[] string = stringToWrite.getBytes(Charset.forName("UTF-8"));
        for(int i=0; i<stringToWrite.length(); i++){
            out.writeByte( string[i] );
        }
    }

    /**
     * Serialize an instance of IListener.Command.
     *
     * @param out Output stream
     * @param m Message instance to be serialized
     * @throws IOException
     * @see net.floodlightcontroller.core.IListener.Command
     */
    private static void writeListenerCommand(ObjectOutput out, IListener.Command m) throws IOException {
        out.writeInt(m.ordinal());
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        // Header
        out.writeInt(this.msgType.ordinal());
        out.writeLong(this.appMsgId);
        out.writeLong(this.switchID);
        out.writeInt(this.isReplay ? 1 : 0);

        // Body
        switch (this.msgType) {
            case OFPACKET_IN:
                DPlaneMsgExternalizable.writeOFPacketIn(out, (OFPacketIn) this.msgPayload);
                break;
            case OFFLOW_REMOVED:
                DPlaneMsgExternalizable.writeOFFlowRemoved(out, (OFFlowRemoved) this.msgPayload);
                break;
            case OFPACKET_OUT:
                DPlaneMsgExternalizable.writeOFPacketOut(out, (OFPacketOut) this.msgPayload);
                break;
            case OFFLOW_MOD:
                DPlaneMsgExternalizable.writeOFFlowMod(out, (OFFlowMod) this.msgPayload);
                break;
            case OFSTATS_REQUEST:
            	DPlaneMsgExternalizable.writeOFStatisticsMessageBase( out, (OFStatisticsRequest) this.msgPayload);
            	break;
            case OFSTATS_REPLY:
            	DPlaneMsgExternalizable.writeOFStatisticsMessageBase( out, (OFStatisticsReply) this.msgPayload);
            	break;
            case SWITCH_NOTIF:
                DPlaneMsgExternalizable.writeSwitchNotification(out, (SwitchNotification) this.msgPayload);
                break;
            case PORT_NOTIF:
                DPlaneMsgExternalizable.writePortNotification(out, (PortNotification) this.msgPayload);
                break;
            case LINK_NOTIF:
                DPlaneMsgExternalizable.writeLinkNotification(out, (LinkDiscoveryNotification) this.msgPayload);
                break;
            case LISTENER_CMD:
                DPlaneMsgExternalizable.writeListenerCommand(out, (IListener.Command) this.msgPayload);
                break;
            default:
                throw new UnknownMsgTypeException(String.format("Unsupported payload (type: %s)", this.msgType));
        }
    }

    /**
     * De-serialize an instance of OFMessage using data read from the input stream.
     *
     * @param in Input stream
     * @param m Message instance to be updated using values from the input stream
     * @throws IOException
     * @see org.openflow.protocol.OFMessage
     */
    private static void readOFMessage(ObjectInput in, OFMessage m) throws IOException, ClassNotFoundException {
        m.setVersion(in.readByte());
        m.setType(OFType.values()[in.readByte()]);
        m.setLength(in.readShort());
        m.setXid(in.readInt());
    }

    /**
     * De-serialize an instance of OFAction using data read from the input stream.
     *
     * @param in Input stream
     * @return Instance of OFAction
     * @throws IOException
     * @throws ClassNotFoundException
     * @see org.openflow.protocol.action.OFAction
     */
    private static OFAction readOFAction(ObjectInput in) throws IOException, ClassNotFoundException {
        final OFActionType ofActionType = OFActionType.values()[in.readShort()];

        final OFAction m = ofActionType.newInstance();
        m.setType(ofActionType);
        m.setLength(in.readShort());

        switch (ofActionType) {
            case OUTPUT: {
                final OFActionOutput actualAction = (OFActionOutput) m;
                actualAction.setPort(in.readShort());
                actualAction.setMaxLength(in.readShort());
                break;
            }
            case SET_VLAN_ID: {
                final OFActionVirtualLanIdentifier actualAction = (OFActionVirtualLanIdentifier) m;
                actualAction.setVirtualLanIdentifier(in.readShort());
                in.readShort();
                break;
            }
            case SET_VLAN_PCP: {
                final OFActionVirtualLanPriorityCodePoint actualAction = (OFActionVirtualLanPriorityCodePoint) m;
                actualAction.setVirtualLanPriorityCodePoint(in.readByte());
                in.readShort();
                in.readByte();
                break;
            }
            case STRIP_VLAN: {
                in.readInt();
                break;
            }
            case SET_DL_SRC:
            case SET_DL_DST: {
                final OFActionDataLayer actualAction = (OFActionDataLayer) m;
                final byte[] dataLayerAddress = new byte[OFPhysicalPort.OFP_ETH_ALEN];
                for (int i = 0; i < dataLayerAddress.length; i++) {
                    dataLayerAddress[i] = in.readByte();
                }
                actualAction.setDataLayerAddress(dataLayerAddress);
                in.readInt();
                in.readShort();
                break;
            }
            case SET_NW_SRC:
            case SET_NW_DST: {
                final OFActionNetworkLayerAddress actualAction = (OFActionNetworkLayerAddress) m;
                actualAction.setNetworkAddress(in.readInt());
                break;
            }
            case SET_NW_TOS: {
                final OFActionNetworkTypeOfService actualAction = (OFActionNetworkTypeOfService) m;
                actualAction.setNetworkTypeOfService(in.readByte());
                in.readShort();
                in.readByte();
                break;
            }
            case SET_TP_SRC:
            case SET_TP_DST: {
                final OFActionTransportLayer actualAction = (OFActionTransportLayer) m;
                actualAction.setTransportPort(in.readShort());
                in.readShort();
                break;
            }
            case OPAQUE_ENQUEUE: {
                final OFActionEnqueue actualAction = (OFActionEnqueue) m;
                actualAction.setPort(in.readShort());
                in.readShort();
                in.readInt();
                actualAction.setQueueId(in.readInt());
                break;
            }
            case VENDOR: {
                final OFActionVendor actualAction = (OFActionVendor) m;
                actualAction.setVendor(in.readInt());
                break;
            }
        }

        return m;
    }

    /**
     * De-serialize an instance of OFMatch using data read from the input stream.
     *
     * @param in Input stream
     * @return Instance of OFMatch
     * @throws IOException
     * @see org.openflow.protocol.OFMatch
     */
    private static OFMatch readOFMatch(ObjectInput in) throws IOException {
        final OFMatch match = new OFMatch();
        match.setWildcards(in.readInt());
        match.setInputPort(in.readShort());
        final byte[] dataLayerSource = new byte[6];
        for (int n = 0; n < dataLayerSource.length; n++) {
            dataLayerSource[n] = in.readByte();
        }
        match.setDataLayerSource(dataLayerSource);
        final byte[] dataLayerDestination = new byte[6];
        for (int n = 0; n < dataLayerDestination.length; n++) {
            dataLayerDestination[n] = in.readByte();
        }
        match.setDataLayerDestination(dataLayerDestination);
        match.setDataLayerVirtualLan(in.readShort());
        match.setDataLayerVirtualLanPriorityCodePoint(in.readByte());
        match.setDataLayerType(in.readShort());
        match.setNetworkTypeOfService(in.readByte());
        match.setNetworkProtocol(in.readByte());
        match.setNetworkSource(in.readInt());
        match.setNetworkDestination(in.readInt());
        match.setTransportSource(in.readShort());
        match.setTransportDestination(in.readShort());
        return match;
    }

    /**
     * De-serialize an instance of OFPacketIn using data read from the input stream.
     *
     * @param in Input stream
     * @return Instance of OFPacketIn
     * @throws IOException
     * @throws ClassNotFoundException
     * @see org.openflow.protocol.OFPacketIn
     */
    private static OFPacketIn readOFPacketIn(ObjectInput in) throws IOException, ClassNotFoundException {
        OFPacketIn m = new OFPacketIn();
        DPlaneMsgExternalizable.readOFMessage(in, m);

        m.setBufferId(in.readInt());
        m.setTotalLength(in.readShort());
        m.setInPort(in.readShort());
        m.setReason(OFPacketIn.OFPacketInReason.values()[U8.f(in.readByte())]);
        final int sz = in.readInt();
        // NOTE: Packet data will never be null!
        byte[] packetData = new byte[sz];
        for (int i = 0; i < sz; i++) {
            packetData[i] = in.readByte();
        }
        m.setPacketData(packetData);

        return m;
    }

    /**
     * De-serialize an instance of OFFlowRemoved using data read from the input stream.
     *
     * @param in Input stream
     * @return Instance of OFFlowRemoved
     * @throws IOException
     * @throws ClassNotFoundException
     * @see org.openflow.protocol.OFFlowRemoved
     */
    private static OFFlowRemoved readOFFlowRemoved(ObjectInput in) throws IOException, ClassNotFoundException {
        final OFFlowRemoved m = new OFFlowRemoved();
        DPlaneMsgExternalizable.readOFMessage(in, m);
        m.setMatch(DPlaneMsgExternalizable.readOFMatch(in));
        m.setCookie(in.readLong());
        m.setPriority(in.readShort());
        final int MAX_REASON_INDEX = OFFlowRemoved.OFFlowRemovedReason.values().length;
        int reasonIndex = 0xFF & in.readByte();
        if (reasonIndex >= MAX_REASON_INDEX) {
            reasonIndex = MAX_REASON_INDEX - 1;
        }
        m.setReason(OFFlowRemoved.OFFlowRemovedReason.values()[reasonIndex]);
        in.readByte(); // pad
        m.setDurationSeconds(in.readInt());
        m.setDurationNanoseconds(in.readInt());
        m.setIdleTimeout(in.readShort());
        in.readByte(); // pad
        in.readByte(); // pad
        m.setPacketCount(in.readLong());
        m.setByteCount(in.readLong());

        return m;
    }

    /**
     * De-serialize an instance of OFPacketOut using data read from the input stream.
     *
     * @param in Input stream
     * @return Instance of OFPacketOut
     * @throws IOException
     * @throws ClassNotFoundException
     * @see org.openflow.protocol.OFPacketOut
     */
    private static OFPacketOut readOFPacketOut(ObjectInput in) throws IOException, ClassNotFoundException {
        final OFPacketOut m = new OFPacketOut();
        DPlaneMsgExternalizable.readOFMessage(in, m);

        m.setBufferId(in.readInt());
        m.setInPort(in.readShort());
        m.setActionsLength(in.readShort());
        final int numActions = in.readInt();
        List<OFAction> actions = new ArrayList<OFAction>(numActions);
        for (int actId = 0; actId < numActions; actId++) {
            actions.add(DPlaneMsgExternalizable.readOFAction(in));
        }
        m.setActions(actions);
        final int sz = in.readInt();
        if (sz > 0) {
            final byte[] packetData = new byte[sz];
            for (int i = 0; i < sz; i++) {
                packetData[i] = in.readByte();
            }
            m.setPacketData(packetData);
        }

//        DPlaneMsgExternalizable.dumpPacketOut(m);
        return m;
    }

    /**
     * De-serialize an instance of OFFlowMod using data read from the input stream.
     *
     * @param in Input stream
     * @return Instance of OFFlowMod
     * @throws IOException
     * @throws ClassNotFoundException
     * @see org.openflow.protocol.OFFlowMod
     */
    private static OFFlowMod readOFFlowMod(ObjectInput in) throws IOException, ClassNotFoundException {
        final OFFlowMod m = new OFFlowMod();
        DPlaneMsgExternalizable.readOFMessage(in, m);

        // Match
        m.setMatch(DPlaneMsgExternalizable.readOFMatch(in));
        // Body
        m.setCookie(in.readLong());
        m.setCommand(in.readShort());
        m.setIdleTimeout(in.readShort());
        m.setHardTimeout(in.readShort());
        m.setPriority(in.readShort());
        m.setBufferId(in.readInt());
        m.setOutPort(in.readShort());
        m.setFlags(in.readShort());
        // Actions
        final int numActions = in.readInt();
        List<OFAction> actions = new ArrayList<OFAction>(numActions);
        for (int actId = 0; actId < numActions; actId++) {
            actions.add(DPlaneMsgExternalizable.readOFAction(in));
        }
        m.setActions(actions);

//        DPlaneMsgExternalizable.dumpFlowMod(m);
        return m;
    }

    /**
     * De-serialize an instance of SwitchNotification using data read from the input stream.
     *
     * @param in Input stream
     * @return Instance of SwitchNotification
     * @throws IOException
     * @throws ClassNotFoundException
     * @see edu.duke.cs.legosdn.core.appvisor.dplane.SwitchNotification
     */
    private static SwitchNotification readSwitchNotification(ObjectInput in)
            throws IOException, ClassNotFoundException {
        final SwitchNotification m = new SwitchNotification();
        m.readExternal(in);
        return m;
    }

    /**
     * De-serialize an instance of PortNotification using data read from the input stream.
     *
     * @param in Input stream
     * @return Instance of PortNotification
     * @throws IOException
     * @throws ClassNotFoundException
     * @see edu.duke.cs.legosdn.core.appvisor.dplane.PortNotification
     */
    private static PortNotification readPortNotification(ObjectInput in)
            throws IOException, ClassNotFoundException {
        final PortNotification m = new PortNotification();
        m.readExternal(in);
        return m;
    }

    /**
     * De-serialize an instance of LinkDiscoveryNotification using data read from the input stream.
     *
     * @param in Input stream
     * @return Instance of LinkDiscoveryNotification
     * @throws IOException
     * @throws ClassNotFoundException
     * @see edu.duke.cs.legosdn.core.appvisor.dplane.LinkDiscoveryNotification
     */
    private static LinkDiscoveryNotification readLinkNotification(ObjectInput in)
            throws IOException, ClassNotFoundException {
        final LinkDiscoveryNotification m = new LinkDiscoveryNotification();
        m.readExternal(in);
        return m;
    }

    /**
     * De-serialize an instance of OFStatisticsRequest using data read from the input stream.
     *
     * @param in Input stream
     * @return Instance of OFStatisticsRequest
     * @throws IOException
     * @throws ClassNotFoundException
     * @see org.openflow.protocol.OFStatisticsRequest
     */
    private static OFStatisticsRequest readOFStatisticsRequest(ObjectInput in) throws ClassNotFoundException, IOException{
    	OFStatisticsRequest m = new OFStatisticsRequest();
    	DPlaneMsgExternalizable.readOFStatisticsMessageBase(in, m, false);
    	return m;
    }
    
    /**
     * De-serialize an instance of OFStatisticsReply using data read from the input stream.
     *
     * @param in Input stream
     * @return Instance of OFStatisticsReply
     * @throws IOException
     * @throws ClassNotFoundException
     * @see org.openflow.protocol.OFStatisticsReply
     */
    private static OFStatisticsReply readOFStatisticsReply(ObjectInput in) throws ClassNotFoundException, IOException{
    	OFStatisticsReply m = new OFStatisticsReply();
    	DPlaneMsgExternalizable.readOFStatisticsMessageBase(in, m, true);
    	return m;
    }
    
    /**
     * De-serialize an instance of OFStatisticsBaseMessage using data read from the input stream.
     *
     * @param in Input stream
     * @param m openflow stats base message
     * @throws IOException
     * @throws ClassNotFoundException
     * @see org.openflow.protocol.OFStatisticsMessageBase
     */
    private static void readOFStatisticsMessageBase(ObjectInput in, OFStatisticsMessageBase m, boolean isReply) throws ClassNotFoundException, IOException{
    	DPlaneMsgExternalizable.readOFMessage(in, m);
    	m.setStatisticType( OFStatisticsType.valueOf(in.readShort(), m.getType() ) );
    	m.setFlags( in.readShort() );
    	
    	int numStats = in.readInt();
    	List<OFStatistics> stats = new ArrayList<OFStatistics>(numStats);
    	for(int statId = 0; statId < numStats; statId++){
    		stats.add( DPlaneMsgExternalizable.readOFStatistics(in, m.getStatisticType(), isReply) );
    	}
    	m.setStatistics( stats );

    }
    
    /**
     * De-serialize an OFStatistics message and returns the message.
     * 
     * @param in input stream
     * @param ofStatsType type of statistic expected
     * @param isReply is a statistics reply message
     * @return OFStatistics message
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static OFStatistics readOFStatistics(ObjectInput in, OFStatisticsType ofStatsType, boolean isReply) throws IOException, ClassNotFoundException{
    	OFStatistics stat = null;
    	switch(ofStatsType){
	    	case DESC:
	    		stat = DPlaneMsgExternalizable.readOFDescriptionStatistics(in);
	    		break;
	    	case FLOW:
	    		if(isReply){ // OFFlowStatisticsReply
	    			stat = readOFFlowStatisticsReply(in);
	    		} else{ // OFFlowStatisticsRequest
	    			stat = readOFFlowStatisticsRequest(in);
	    		}
	    		break;
	    	case AGGREGATE:
	    		if(isReply){ //OFAggregateStatisticsReply
	    			stat = readOFAggregateStatisticsReply(in);
	    		} else{ 
	    			stat = readOFAggregateStatisticsRequest(in);
	    		}
	    		break;
	    	case TABLE:
	    		stat = readOFTableStatistics(in);
	    		break;
	    	case PORT:
	    		if(isReply){ //OFPortStatisticsReply
	    			stat = readOFPortStatisticsReply(in);
	    		} else{ // OFPortStatisticsRequest
	    			stat = readOFPortStatisticsRequest(in);
	    		}
	    		break;
	    	case QUEUE:
	    		if(isReply){ //OFQueueStatisticsReply
	    			stat = readOFQueueStatisticsReply(in);
	    		} else{ //OFQueueStatisticsRequest
	    			stat = readOFQueueStatisticsRequest(in);
	    		}
	    		break;
	    	case VENDOR:
	    		OFVendorStatistics ofVendStat = new OFVendorStatistics();
	    		stat = ofVendStat;
	    		break;
	    	default:
	    		throw new UnknownMsgTypeException(String.format("Unsupported payload (type: %s)", ofStatsType));
    	}
    	return stat;
    }

    /**
	 * De-serialize an instance of OFQueueStatisticsRequest
	 * @param in
	 * @return
	 * @throws IOException
	 * @see org.openflow.protocol.statistics.OFQueueStatisticsRequest
	 */
	private static OFQueueStatisticsRequest readOFQueueStatisticsRequest(
			ObjectInput in) throws IOException {
		OFQueueStatisticsRequest ofQueueStat = new OFQueueStatisticsRequest();
		ofQueueStat.setPortNumber( in.readShort() );
		in.readShort(); // pad
		ofQueueStat.setQueueId( in.readInt() );
		return ofQueueStat;
	}

	/**
	 * De-serialize an instance of OFQueueStatisticsReply
	 * @param in
	 * @return
	 * @throws IOException
	 * @see org.openflow.protocol.statistics.OFQueueStatisticsReply
	 */
	private static OFQueueStatisticsReply readOFQueueStatisticsReply(
			ObjectInput in) throws IOException {
		OFQueueStatisticsReply ofQueueStat = new OFQueueStatisticsReply();
		ofQueueStat.setPortNumber( in.readShort() );
		in.readShort(); // pad
		ofQueueStat.setQueueId( in.readInt() );
		ofQueueStat.setTransmitBytes( in.readLong() );
		ofQueueStat.setTransmitPackets( in.readLong() );
		ofQueueStat.setTransmitErrors( in.readLong() );
		return ofQueueStat;
	}

	/**
	 * De-serialize an instance of OFPortStatisticsRequest
	 * @param in
	 * @return
	 * @throws IOException
	 * @see org.openflow.protocol.statistics.OFPortStatisticsRequest
	 */
	private static OFPortStatisticsRequest readOFPortStatisticsRequest(
			ObjectInput in) throws IOException {
		OFPortStatisticsRequest ofPortStat = new OFPortStatisticsRequest();
		ofPortStat.setPortNumber( in.readShort() );
		in.readShort(); // pad
		in.readInt(); // pad
		return ofPortStat;
	}

	/**
	 * De-serialize an instance of OFPortStatisticsReply
	 * @param in
	 * @return
	 * @throws IOException
	 * @see org.openflow.protocol.statistics.OFPortStatisticsReply
	 */
	private static OFPortStatisticsReply readOFPortStatisticsReply(
			ObjectInput in) throws IOException {
		OFPortStatisticsReply ofPortStat = new OFPortStatisticsReply();
		ofPortStat.setPortNumber( in.readShort() );
		in.readShort(); // pad
		in.readInt(); // pad
		ofPortStat.setreceivePackets ( in.readLong() );
		ofPortStat.setTransmitPackets ( in.readLong() );
		ofPortStat.setReceiveBytes ( in.readLong() );
		ofPortStat.setTransmitBytes ( in.readLong() );
		ofPortStat.setReceiveDropped ( in.readLong() );
		ofPortStat.setTransmitDropped ( in.readLong() );
		ofPortStat.setreceiveErrors ( in.readLong() );
		ofPortStat.setTransmitErrors ( in.readLong() );
		ofPortStat.setReceiveFrameErrors ( in.readLong() );
		ofPortStat.setReceiveOverrunErrors ( in.readLong() );
		ofPortStat.setReceiveCRCErrors ( in.readLong() );
		ofPortStat.setCollisions ( in.readLong() );
		return ofPortStat;
	}

	/**
	 * De-serialize an instance of OFTableStatistics
	 * @param in
	 * @return
	 * @throws IOException
	 * @see org.openflow.protocol.statistics.OFTableStatistics
	 */
	private static OFTableStatistics readOFTableStatistics(ObjectInput in)
			throws IOException {
		OFTableStatistics ofTableStat = new OFTableStatistics();
		ofTableStat.setTableId( in.readByte() );
		in.readByte(); // pad
		in.readByte(); // pad
		in.readByte(); // pad
		ofTableStat.setName( DPlaneMsgExternalizable.readString(in) );
		ofTableStat.setWildcards( in.readInt() );
		ofTableStat.setMaximumEntries( in.readInt() );
		ofTableStat.setActiveCount( in.readInt() );
		ofTableStat.setLookupCount( in.readLong() );
		ofTableStat.setMatchedCount( in.readLong() );
		return ofTableStat;
	}

	/**
	 * De-serialize an instance of OFAggregateStatisticsRequest
	 * @param in
	 * @return
	 * @throws IOException
	 * @see org.openflow.protocol.statistics.OFAggregateStatisticsRequest
	 */
	private static OFAggregateStatisticsRequest readOFAggregateStatisticsRequest(
			ObjectInput in) throws IOException {
		OFAggregateStatisticsRequest ofAggStat = new OFAggregateStatisticsRequest();
		ofAggStat.setMatch( DPlaneMsgExternalizable.readOFMatch(in) );
		ofAggStat.setTableId( in.readByte() );
		in.readByte();
		ofAggStat.setOutPort( in.readShort() );
		return ofAggStat;
	}

	/**
	 * De-serialize an instance of OFAggregateStatisticsReply
	 * @param in
	 * @return
	 * @throws IOException
	 * @see org.openflow.protocol.statistics.OFAggregateStatisticsReply
	 */
	private static OFAggregateStatisticsReply readOFAggregateStatisticsReply(
			ObjectInput in) throws IOException {
		OFAggregateStatisticsReply ofAggStat = new OFAggregateStatisticsReply();
		ofAggStat.setPacketCount( in.readLong() );
		ofAggStat.setByteCount( in.readLong() );
		ofAggStat.setFlowCount( in.readInt() );
		in.readInt(); // pad
		return ofAggStat;
	}

	/**
	 * De-serialize an instance of OFFlowStatisticsRequest
	 * @param in
	 * @return
	 * @throws IOException
	 * @see org.openflow.protocol.statistics.OFFlowStatisticsRequest
	 */
	private static OFFlowStatisticsRequest readOFFlowStatisticsRequest(
			ObjectInput in) throws IOException {
		OFFlowStatisticsRequest ofFlowStat = new OFFlowStatisticsRequest();
		ofFlowStat.setMatch( DPlaneMsgExternalizable.readOFMatch(in) );
		ofFlowStat.setTableId( in.readByte() );
		in.readByte(); // pad
		ofFlowStat.setOutPort( in.readShort() );
		return ofFlowStat;
	}

	/**
	 * De-serialize an instance of OFFlowStatisticsReply
	 * @param in
	 * @return
	 * @throws IOException
	 * @see org.openflow.protocol.statistics.OFFlowStatisticsReply
	 */
	private static OFFlowStatisticsReply readOFFlowStatisticsReply(
			ObjectInput in) throws IOException, ClassNotFoundException {
		OFFlowStatisticsReply ofFlowStat = new OFFlowStatisticsReply();
		ofFlowStat.setLength( in.readShort() );
		ofFlowStat.setTableId( in.readByte() );
		in.readByte(); // pad
		DPlaneMsgExternalizable.readOFMatch(in);
		ofFlowStat.setDurationSeconds( in.readInt() );
		ofFlowStat.setDurationNanoseconds( in.readInt() );
		ofFlowStat.setPriority( in.readShort() );
		ofFlowStat.setIdleTimeout( in.readShort() );
		ofFlowStat.setHardTimeout( in.readShort() );
		in.readInt(); // pad
		in.readShort(); // pad
		ofFlowStat.setCookie( in.readLong() );
		ofFlowStat.setPacketCount( in.readLong() );
		ofFlowStat.setByteCount( in.readLong() );
		int numActions = in.readInt();
		for( int i =0; i<numActions; i++ ){
			DPlaneMsgExternalizable.readOFAction(in);
		}
		return ofFlowStat;
	}

	/**
	 * De-serialize an instance of OFDescriptionStatistics
	 * @param in
	 * @return
	 * @throws IOException
	 * @see org.openflow.protocol.statistics.OFQueueStatisticsRequest
	 */
	private static OFDescriptionStatistics readOFDescriptionStatistics(
			ObjectInput in) throws IOException {
		OFDescriptionStatistics ofDescStat = new OFDescriptionStatistics();
		ofDescStat.setManufacturerDescription( DPlaneMsgExternalizable.readString(in) );
		ofDescStat.setHardwareDescription( DPlaneMsgExternalizable.readString(in) );
		ofDescStat.setSoftwareDescription( DPlaneMsgExternalizable.readString(in) );
		ofDescStat.setSerialNumber( DPlaneMsgExternalizable.readString(in) );
		ofDescStat.setDatapathDescription( DPlaneMsgExternalizable.readString(in) );
		return ofDescStat;
	}
    
    /**
     * Deserialize (UTF-8) strings.
     * @param in Object input stream
     * @return
     * @throws IOException
     */
    public static String readString(ObjectInput in) throws IOException{
    	int length = in.readInt();
    	byte[] string = new byte[length];
    	for(int i=0; i<length; i++){
    		string[i] = in.readByte();
    	}
    	return new String(string, Charset.forName("UTF-8"));
    }

    /**
     * Deserialize (UTF-8) strings.
     * @param in Netty channel buffer
     * @return UTF-8 string
     */
    public static String readString(ChannelBuffer in) {
        int length = in.readInt();
        byte[] string = new byte[length];
        for(int i=0; i<length; i++) {
            string[i] = in.readByte();
        }
        return new String(string, Charset.forName("UTF-8"));
    }

    /**
     * De-serialize an instance of IListener.Command using data read from the input stream.
     *
     * @param in Input stream
     * @return Instance of IListener.Command
     * @throws IOException
     * @throws ClassNotFoundException
     * @see net.floodlightcontroller.core.IListener.Command
     */
    private static IListener.Command readListenerCommand(ObjectInput in)
            throws IOException, ClassNotFoundException {
        return IListener.Command.values()[in.readInt()];
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        final int typeOrdinal = in.readInt();
        try {
            this.msgType = DPlaneMsgType.values()[typeOrdinal];
        } catch (ArrayIndexOutOfBoundsException e) {
            String errDesc = String.format("Unknown message type (ordinal: %d)", typeOrdinal);
            throw new UnknownMsgTypeException(errDesc, e);
        }
        // Application specific message ID
        this.appMsgId = in.readLong();
        // Switch to which message is written to
        this.switchID = in.readLong();
        // Flag indicating if message is part of a replay
        this.isReplay = in.readInt() == 1 ? true : false;
        switch (this.msgType) {
            case OFPACKET_IN:
                this.msgPayload = DPlaneMsgExternalizable.readOFPacketIn(in);
                break;
            case OFFLOW_REMOVED:
                this.msgPayload = DPlaneMsgExternalizable.readOFFlowRemoved(in);
                break;
            case OFPACKET_OUT:
                this.msgPayload = DPlaneMsgExternalizable.readOFPacketOut(in);
                break;
            case OFFLOW_MOD:
                this.msgPayload = DPlaneMsgExternalizable.readOFFlowMod(in);
                break;
            case OFSTATS_REQUEST:
            	this.msgPayload = DPlaneMsgExternalizable.readOFStatisticsRequest(in);
            	break;
            case OFSTATS_REPLY:
            	this.msgPayload = DPlaneMsgExternalizable.readOFStatisticsReply(in);
            	break;
            case SWITCH_NOTIF:
                this.msgPayload = DPlaneMsgExternalizable.readSwitchNotification(in);
                break;
            case PORT_NOTIF:
                this.msgPayload = DPlaneMsgExternalizable.readPortNotification(in);
                break;
            case LINK_NOTIF:
                this.msgPayload = DPlaneMsgExternalizable.readLinkNotification(in);
                break;
            case LISTENER_CMD:
                this.msgPayload = DPlaneMsgExternalizable.readListenerCommand(in);
                break;
            default:
                throw new UnknownMsgTypeException(String.format("Unsupported payload (type: %s)", this.msgType));
        }
    }

    /**
     * Dump the contents of a PACKET_OUT message.
     *
     * @param ofPacketOut PACKET_OUT message
     */
    public static void dumpPacketOut(OFPacketOut ofPacketOut) {
        System.out.println(">> " + OFType.PACKET_OUT.toString());
        System.out.println(">>          version: " + ofPacketOut.getVersion());
        System.out.println(">>           length: " + ofPacketOut.getLength());
        System.out.println(">>              xid: " + ofPacketOut.getXid());
        System.out.println(">>         bufferId: " + ofPacketOut.getBufferId());
        System.out.println(">>           inPort: " + ofPacketOut.getInPort());
        System.out.println(">>    actionsLength: " + ofPacketOut.getActionsLength());
        for (OFAction ofAction : ofPacketOut.getActions()) {
            System.out.println(">>      action.type: " + ofAction.getType().toString());
            System.out.println(">>    action.length: " + ofAction.getLength());
            final OFActionOutput ofActionOutput = (OFActionOutput) ofAction;
            System.out.println(">>      action.port: " + ofActionOutput.getPort());
            System.out.println(">> action.maxLength: " + ofActionOutput.getMaxLength());
        }
        System.out.println(">>       packetData: " + Arrays.toString(ofPacketOut.getPacketData()));
        System.out.println();
    }

    /**
     * Dump the contents of a FLOW_MOD message.
     *
     * @param ofFlowMod FLOW_MOD message
     */
    public static void dumpFlowMod(OFFlowMod ofFlowMod) {
        System.out.println(">> " + OFType.FLOW_MOD.toString());
        System.out.println(">>                                     version: " + ofFlowMod.getVersion());
        System.out.println(">>                                      length: " + ofFlowMod.getLength());
        System.out.println(">>                                         xid: " + ofFlowMod.getXid());
        System.out.println(">>                                    bufferId: " + ofFlowMod.getBufferId());
        final OFMatch ofMatch = ofFlowMod.getMatch();
        System.out.println(">>                             match.wildcards: " + ofMatch.getWildcards());
        System.out.println(">>                             match.inputPort: " + ofMatch.getInputPort());
        System.out.println(">>                       match.dataLayerSource: " +
                           Arrays.toString(ofMatch.getDataLayerSource()));
        System.out.println(">>                  match.dataLayerDestination: " +
                           Arrays.toString(ofMatch.getDataLayerDestination()));
        System.out.println(">>                   match.dataLayerVirtualLan: " + ofMatch.getDataLayerVirtualLan());
        System.out.println(">>  match.dataLayerVirtualLanPriorityCodePoint: " +
                           ofMatch.getDataLayerVirtualLanPriorityCodePoint());
        System.out.println(">>                         match.dataLayerType: " + ofMatch.getDataLayerType());
        System.out.println(">>                  match.networkTypeOfService: " +ofMatch.getNetworkTypeOfService());
        System.out.println(">>                       match.networkProtocol: " + ofMatch.getNetworkProtocol());
        System.out.println(">>                         match.networkSource: " + ofMatch.getNetworkSource());
        System.out.println(">>                    match.networkDestination: " + ofMatch.getNetworkDestination());
        System.out.println(">>                       match.transportSource: " + ofMatch.getTransportSource());
        System.out.println(">>                  match.transportDestination: " + ofMatch.getTransportDestination());
        System.out.println(">>                                      cookie: " + ofFlowMod.getCookie());
        System.out.println(">>                                     command: " + ofFlowMod.getCommand());
        System.out.println(">>                                 idleTimeout: " + ofFlowMod.getIdleTimeout());
        System.out.println(">>                                 hardTimeout: " + ofFlowMod.getHardTimeout());
        System.out.println(">>                                    priority: " + ofFlowMod.getPriority());
        System.out.println(">>                                    bufferId: " + ofFlowMod.getBufferId());
        System.out.println(">>                                     outPort: " + ofFlowMod.getOutPort());
        System.out.println(">>                                       flags: " + ofFlowMod.getFlags());
        for (OFAction ofAction : ofFlowMod.getActions()) {
            System.out.println(">>                                 action.type: " + ofAction.getType().toString());
            System.out.println(">>                                 action.length: " + ofAction.getLength());
            final OFActionOutput ofActionOutput = (OFActionOutput) ofAction;
            System.out.println(">>                                   action.port: " + ofActionOutput.getPort());
            System.out.println(">>                              action.maxLength: " + ofActionOutput.getMaxLength());
        }
        System.out.println();
    }

    /**
     * Write a byte array to an output stream.
     *
     * @param out Object output stream
     * @param byteArray Array of bytes
     * @return Number of bytes written
     * @throws IOException
     */
    public static int writeBytes(ObjectOutput out, byte[] byteArray) throws IOException {
        final int N = byteArray.length;
        for (int i = 0; i < N; i++) {
            out.writeByte(byteArray[i]);
        }
        return N;
    }

    /**
     * Write a byte array to an output stream.
     *
     * @param out Netty channel buffer
     * @param byteArray Array of bytes
     * @return Number of bytes written
     */
    public static int writeBytes(ChannelBuffer out, byte[] byteArray) {
        final int N = byteArray.length;
        for (int i = 0; i < N; i++) {
            out.writeByte(byteArray[i]);
        }
        return N;
    }

    /**
     * Read a byte array from an input stream.
     *
     * @param in Object input stream
     * @param byteArray Array of bytes
     * @return Number of bytes read
     * @throws IOException
     */
    public static int readBytes(ObjectInput in, byte[] byteArray) throws IOException {
        final int N = byteArray.length;
        for (int i = 0; i < N; i++) {
            byteArray[i] = in.readByte();
        }
        return N;
    }

    /**
     * Read a byte array from an input stream.
     *
     * @param in Netty channel buffer
     * @param byteArray Array of bytes
     * @return Number of bytes read
     */
    public static int readBytes(ChannelBuffer in, byte[] byteArray) {
        final int N = byteArray.length;
        for (int i = 0; i < N; i++) {
            byteArray[i] = in.readByte();
        }
        return N;
    }

}
