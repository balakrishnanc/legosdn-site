#!/usr/bin/env bash
# -*- mode: sh; coding: utf-8; fill-column: 80; -*-
#
# crservice.sh
# Created by Balakrishnan Chandrasekaran on 2014-09-21 20:18 -0400.
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

if [ ${LEGOSDN_RT:-''} == '' ]; then
    echo 'Error: LEGOSDN_RT not set!' 1>&2
    exit 1
fi

JAVA="${JAVA_HOME}/bin/java"

JVM_OPTS="-server -d64"
JVM_OPTS="${JVM_OPTS} -Xmx64m -Xms64m -Xmn32m"
# Turn off jvmstat instrumentation
JVM_OPTS="${JVM_OPTS} -XX:-UsePerfData"
JVM_OPTS="${JVM_OPTS} -XX:+UseParallelGC -XX:+AggressiveOpts -XX:+UseFastAccessorMethods"
JVM_OPTS="$JVM_OPTS -XX:MaxInlineSize=8192 -XX:FreqInlineSize=8192"
JVM_OPTS="$JVM_OPTS -XX:CompileThreshold=1500"

LOG_CONFIG="${LEGOSDN_RT}/conf/logback.xml"
if [ ! -f ${LOG_CONFIG} ]; then
    echo "Error: Cannot find '${LOG_CONFIG}'" 1>&2
    exit 1
fi
LOG_OPTS="-Dlogback.configurationFile=${LOG_CONFIG}"

LEGOSDN_CORE='dist/legosdn-core.jar'
LEGOSDN_TOOLS='dist/legosdn-tools.jar'
LEGOSDN_JARS="${LEGOSDN_CORE}:${LEGOSDN_TOOLS}"

CLASSPATH="${LEGOSDN_JARS}:${FLOODLIGHT_HOME}/target/floodlight.jar"

CR_SERVICE='edu.duke.cs.legosdn.tools.cr.srv.CRService'
PROPS_FILE="${LEGOSDN_RT}/conf/crservice.properties"
if [ ! -f ${PROPS_FILE} ]; then
    echo "Error: Cannot find '${PROPS_FILE}'" 1>&2
    exit 1
fi

${JAVA} ${JVM_OPTS} ${LOG_OPTS} -cp ${CLASSPATH} ${CR_SERVICE} ${PROPS_FILE}
