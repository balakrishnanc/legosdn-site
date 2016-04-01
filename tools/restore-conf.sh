#!/usr/bin/env bash
# -*- mode: sh; coding: utf-8; fill-column: 80; -*-
#
# restore-conf.sh
# Created by Balakrishnan Chandrasekaran on 2015-09-20 18:50 -0400.
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
    echo "Usage: $0 <in-path>" >& 2
    exit 1
fi

IN_PATH="${1}"

FL_MOD_SRC="${IN_PATH}/net.floodlightcontroller.core.module.IFloodlightModule"
FL_MOD_DST="${FLOODLIGHT_HOME}/src/main/resources/META-INF/services/net.floodlightcontroller.core.module.IFloodlightModule"
[ -f ${FL_MOD_SRC} ] && cp -v ${FL_MOD_SRC} ${FL_MOD_DST}

FL_PROPS_SRC="${IN_PATH}/floodlightdefault.properties"
FL_PROPS_DST="${FLOODLIGHT_HOME}/src/main/resources/floodlightdefault.properties"
[ -f ${FL_PROPS_SRC} ] && cp -v ${FL_PROPS_SRC} ${FL_PROPS_DST}

CTRLR_PROPS_SRC="${IN_PATH}/ctrlr-appv.properties"
CTRLR_PROPS_DST="${LEGOSDN_RT}/conf/ctrlr-appv.properties"
[ -f ${CTRLR_PROPS_SRC} ] && cp -v ${CTRLR_PROPS_SRC} ${CTRLR_PROPS_DST}
