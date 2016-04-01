#!/usr/bin/env bash
# -*- mode: sh; coding: utf-8; fill-column: 80; -*-
#
# gen-rt-deps.sh
# Created by Balakrishnan Chandrasekaran on 2015-01-22 00:21 -0500.
# Copyright (c) 2014 Balakrishnan Chandrasekaran <balakrishnan.c@gmail.com>.
#

# NOTE: Run this script as a *regular* user, and *NOT AS root*
[ $(whoami) == 'root' ] && \
    echo 'Error: please run the script as a regular user and not root!' >& 2 && \
    exit 1

if [ ${LEGOSDN_HOME:-''} == '' ]; then
    echo 'Error: LEGOSDN_HOME not set!' 1>&2
    exit 1
fi

LEGOSDN_MT_PT='/legosdn'
LEGOSDN_RT="${LEGOSDN_MT_PT}/runtime"
LEGOSDN_TOOLS="${LEGOSDN_HOME}/tools"

function alert_missing_mt_pt() {
    echo "Mount point '${LEGOSDN_MT_PT}' does not exist!" >&2
    exit 1
}

[ -d ${LEGOSDN_MT_PT} ] || alert_missing_mt_pt

mkdir -p ${LEGOSDN_RT}/{stubs,tools,conf} &&                                \
    mkdir -p ${LEGOSDN_RT}/{crash-indicators,crash-flags} &&                \
    mkdir -p ${LEGOSDN_RT}/{counters,timers} &&                             \
    mkdir -p ${LEGOSDN_RT}/app/{resources,logs} &&                          \
    mkdir -p ${LEGOSDN_MT_PT}/checkpoints &&                                \
    mkdir -p ${LEGOSDN_MT_PT}/checkpoints/criulogs &&                       \
    cp -v ${LEGOSDN_HOME}/conf/logback.xml ${LEGOSDN_RT}/conf/ &&           \
    cp -v ${LEGOSDN_HOME}/conf/ctrlr-appv.properties ${LEGOSDN_RT}/conf/ && \
    cp -v ${LEGOSDN_TOOLS}/daemonize.sh ${LEGOSDN_RT}/tools/ &&             \
    cp -v ${LEGOSDN_HOME}/apploader-lomem.sh ${LEGOSDN_RT}/tools/ &&        \
    cp -v ${LEGOSDN_HOME}/apploader.sh ${LEGOSDN_RT}/tools/
res=$?
status="OK"
[ ${res} -ne 0 ] && status="FAIL"
echo "Generate runtime filesystem dependencies ...   ${status}"
exit ${res}
