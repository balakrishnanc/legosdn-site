package edu.duke.cs.legosdn.core.appvisor.stub;

import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsgGrpDecoder;
import edu.duke.cs.legosdn.core.appvisor.dplane.DPlaneMsgGrpEncoder;
import net.floodlightcontroller.core.IOFMessageListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class DPlanePipelineFactory implements ChannelPipelineFactory {

    private static final DPlaneMsgGrpEncoder DPLANE_MSG_GRP_ENCODER = new DPlaneMsgGrpEncoder();
    private static final DPlaneMsgGrpDecoder DPLANE_MSG_GRP_DECODER = new DPlaneMsgGrpDecoder();

    private final IOFMessageListener                      app;
    private final ConcurrentHashMap<String, OFSwitchStub> switches;
    private final OrderedMemoryAwareThreadPoolExecutor    eventExecutor;

    /**
     * Initialize DPlanePipelineFactory.
     *
     * @param app Application to sandbox.
     * @param switches Set of switches.
     * @param eventExecutor Thread pool for running computations.
     */
    public DPlanePipelineFactory(IOFMessageListener app,
                                 ConcurrentHashMap<String, OFSwitchStub> switches,
                                 OrderedMemoryAwareThreadPoolExecutor eventExecutor) {
        this.app = app;
        this.switches = switches;
        this.eventExecutor = eventExecutor;
    }

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();

        pipeline.addLast("decoder", DPLANE_MSG_GRP_DECODER);
        pipeline.addLast("encoder", DPLANE_MSG_GRP_ENCODER);
        pipeline.addLast("pipeLineExecutor", new ExecutionHandler(this.eventExecutor));
//        pipeline.addLast("handler", new DPlaneHandler<DPlaneMsg>(app, switches));

        return pipeline;
    }

}
