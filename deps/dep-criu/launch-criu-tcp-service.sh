#!/usr/bin/env bash
# -*- mode: sh; coding: utf-8; fill-column: 80; -*-
#
# launch-criu-tcp-service.sh
# Created by Balakrishnan Chandrasekaran on 2015-01-26 21:29 -0400.
#

CHKPTS_BASE_DIR='/legosdn/checkpoints'
CRIU_LOGS_DIR="${CHKPTS_BASE_DIR}/criulogs"
CRIU_LOG="${CRIU_LOGS_DIR}/criu-log.txt"

if [ ${LEGOSDN_HOME:-''} == '' ]; then
    echo 'Error: LEGOSDN_HOME not set!' >& 2
    exit 1
fi

if [ -d ${CHKPTS_BASE_DIR} ]; then
    sudo rm -rf ${CHKPTS_BASE_DIR}
    [ $? -eq 0 ]                                             && \
	echo "> removed old checkpoints in '${CHKPTS_BASE_DIR}'" && \
	sudo mkdir -p ${CHKPTS_BASE_DIR}                         && \
    sudo mkdir -p ${CRIU_LOGS_DIR}
else
    echo "> path '${CHKPTS_BASE_DIR}' missing!" >& 2
    sudo mkdir -p ${CHKPTS_BASE_DIR}                         && \
    sudo mkdir -p ${CRIU_LOGS_DIR}                           && \
	echo "> path '${CHKPTS_BASE_DIR}' created"
    [ $? -ne 0 ]                                             && \
	echo "> failed to create path '${CHKPTS_BASE_DIR}'" >& 2 && \
	exit 1
fi

CRIU_BASE_DIR="${LEGOSDN_HOME}/criu"
CRTOOLS="${CRIU_BASE_DIR}/crtools"

for p in $(sudo ps -elf | grep ${CRTOOLS} | grep -v grep | awk '{print $4}'); do
    sudo kill -9 $p
    if [ $? -eq 0 ]; then
        echo "> kill stale proc '${p}'  .... OK"
    else
        echo "> kill stale proc '${p}'  .... FAIL"
    fi
done

rm -f ${CRIU_LOG}
sudo ${CRTOOLS} tcp-service -v4 --log-file ${CRIU_LOG}
