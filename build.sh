#!/usr/bin/env bash
# -*- mode: sh; coding: utf-8; fill-column: 80; -*-
#
# build.sh
# Created by Balakrishnan Chandrasekaran on 2015-01-26 04:07 -0500.
#

if [ ${JAVA_HOME:-''} == '' ]; then
    echo 'Error: JAVA_HOME not set!' 1>&2
    exit 1
fi

if [ ${FLOODLIGHT_HOME:-''} == '' ]; then
    echo 'Error: FLOODLIGHT_HOME not set!' 1>&2
    exit 1
fi

if [ ${LEGOSDN_HOME:-''} == '' ]; then
    echo 'Error: LEGOSDN_HOME not set!' 1>&2
    exit 1
fi

JAVA="${JAVA_HOME}/bin/java"

ANT='/usr/bin/ant'
ANT_ARGS='-logger org.apache.tools.ant.listener.AnsiColorLogger'

cwd="$(pwd)"

cd ${LEGOSDN_HOME}		&& \
    ant ${ANT_ARGS}		&& \
    echo 'LEGOSDN built.'	&& \
    cd ${FLOODLIGHT_HOME}	&& \
    ant ${ANT_ARGS}		&& \
    echo 'FLOODLIGHT built.'

cd ${cwd}
