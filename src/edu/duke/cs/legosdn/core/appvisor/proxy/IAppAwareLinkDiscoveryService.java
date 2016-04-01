package edu.duke.cs.legosdn.core.appvisor.proxy;

import net.floodlightcontroller.linkdiscovery.ILinkDiscoveryService;
import net.floodlightcontroller.linkdiscovery.LinkInfo;
import net.floodlightcontroller.routing.Link;

import java.util.Map;
import java.util.Set;

public interface IAppAwareLinkDiscoveryService extends ILinkDiscoveryService {

    /**
     * Retrieves a map of all known link connections between OpenFlow switches
     * and the associated info (valid time, port states) for the link, based
     * on the network state exposed to the application.
     */
    public Map<Link, LinkInfo> getLinks(String appId);

    /**
     * Update the per-application state by adding a new link.
     *
     * @param link Link
     * @param linkInfo Link details
     * @param appId Application identifier
     */
    public void addLink(Link link, LinkInfo linkInfo, String appId);

    /**
     * Update the per-application state by deleting an existing link.
     *
     * @param link Link
     * @param linkInfo Link details
     * @param appId Application identifier
     */
    public void delLink(Link link, LinkInfo linkInfo, String appId);

    /**
     * Retrieves a map of all known but ignored link connections between OpenFlow switches
     * and the associated info (valid time, port states) for the link, based on the network
     * state exposed to the application.
     *
     * @param appId Application identifier
     * @returns Map of link-connections and associated info
     */
    public Map<Link, LinkInfo> getDelLinks(String appId);

    /**
     * Retrieves a set of switch IDs that are kept hidden from a given application.
     *
     * @param appId Application identifier
     * @return Set of switch identifiers
     */
    public Set<Long> getSwitchesDel(String appId);

    /**
     * Ignores updates associated with a given switch for the given application.
     *
     * @param swID Switch identifier
     * @param appId Application identifier
     */
    public void setSwitchUp(Long swID, String appId);

    /**
     * Stops ignoring updates associated with a given switch for the given application.
     *
     * @param swID Switch identifier
     * @param appId Application identifier
     */
    public void setSwitchDown(Long swID, String appId);

    /**
     * Register endpoint.
     *
     * @param appId Application identifier
     */
    public void registerEndpt(String appId);

    /**
     * Ignore per-app state data.
     */
    public void disableAppAwareness();

}
