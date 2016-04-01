/** -*- mode: java; coding: utf-8; fill-column: 80; -*-
 * Created by Balakrishnan Chandrasekaran on 2014-09-22 02:41
 * Copyright (c) 2014 Balakrishnan Chandrasekaran <balakrishnan.c@gmail.com>.
 */
package edu.duke.cs.legosdn.tools.cr;

/**
 * Checkpoint and restore service provider.
 *
 * @param <T> Actual message type accepted by the service provider.
 */
public interface CRServiceProvider<T> {

    /**
     * Check service and report its status.
     *
     * @param req Service check request message
     * @return Service status response
     */
    public T checkServiceStatus(T req);

    /**
     * Register an application with the C/R service.
     *
     * @param req Register process request
     * @return Register operation response
     */
    public T registerProcess(T req);

    /**
     * Checkpoint an application process.
     *
     * @param req Checkpoint process request
     * @return Checkpoint operation response
     */
    public T checkpointProcess(T req);

    /**
     * Restore an application process using a previous checkpoint.
     *
     * @param req Restore process request
     * @return Restore operation response
     */
    public T restoreProcess(T req);

}
