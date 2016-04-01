#!/usr/bin/env bash
# -*- mode: sh; coding: utf-8; fill-column: 80; -*-
#
# copy-conf.sh
# Created by Balakrishnan Chandrasekaran on 2015-09-20 16:50 -0400.
#

if [ ${FLOODLIGHT_HOME:-''} == '' ]; then
    echo 'Error: FLOODLIGHT_HOME not set!' 1>&2
    exit 1
fi

if [ ${LEGOSDN_HOME:-''} == '' ]; then
    echo 'Error: LEGOSDN_HOME not set!' 1>&2
    exit 1
fi

if [ ${LEGOSDN_RT:-''} == '' ]; then
    echo 'Error: LEGOSDN_RT not set!' 1>&2
    exit 1
fi

if [ $# -ne 1 ]; then
    echo "Usage: $0 <out-path>" >& 2
    exit 1
fi

OUT_PATH="${1}"

FL_MOD="${FLOODLIGHT_HOME}/src/main/resources/META-INF/services/net.floodlightcontroller.core.module.IFloodlightModule"
cp -v ${FL_MOD} ${OUT_PATH}

FL_PROPS="${FLOODLIGHT_HOME}/src/main/resources/floodlightdefault.properties"
cp -v ${FL_PROPS} ${OUT_PATH}

CTRLR_PROPS="${LEGOSDN_RT}/conf/ctrlr-appv.properties"
cp -v ${CTRLR_PROPS} ${OUT_PATH}
