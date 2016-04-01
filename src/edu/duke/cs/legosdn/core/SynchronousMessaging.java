package edu.duke.cs.legosdn.core;

/**
 * SynchronousMessaging interface implies that the implementer supports old-style blocking receives.
 *
 * @param <T> Actual message type.
 */
public interface SynchronousMessaging<T> {

    /**
     * Wait while response arrives.
     *
     * @return Response message
     */
    public T waitForMessage();

}
