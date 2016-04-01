package edu.duke.cs.legosdn.tools.cr.srv;

/**
 * CRServiceConfigException captures exceptions encountered while configurint the checkpoint and restore service.
 */
public class CRServiceConfigException extends Exception {

    public CRServiceConfigException(String message, Throwable cause) {
        super(message, cause);
    }

}
