#!/usr/bin/env bash
# -*- mode: sh; coding: utf-8; fill-column: 80; -*-
#
# netlog.sh
# Created by Balakrishnan Chandrasekaran on 2014-11-30 03:52 -0500.
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

JVM_OPTS="-server -d64"
JVM_OPTS="${JVM_OPTS} -Xmx512m -Xms512m -Xmn128m"
# Turn off jvmstat instrumentation
# JVM_OPTS="${JVM_OPTS} -XX:-UsePerfData"
JVM_OPTS="${JVM_OPTS} -XX:+UseParallelGC -XX:+AggressiveOpts -XX:+UseFastAccessorMethods"
JVM_OPTS="$JVM_OPTS -XX:MaxInlineSize=8192 -XX:FreqInlineSize=8192"
JVM_OPTS="$JVM_OPTS -XX:CompileThreshold=1500"

LOG_CONFIG='logback.xml'
LOG_OPTS="-Dlogback.configurationFile=${LOG_CONFIG}"

LEGOSDN_CORE='dist/legosdn-core.jar'
LEGOSDN_TOOLS='dist/legosdn-tools.jar'
LEGOSDN_JARS="${LEGOSDN_CORE}:${LEGOSDN_TOOLS}"

CLASSPATH="${LEGOSDN_JARS}:${FLOODLIGHT_HOME}/target/floodlight.jar"

NETLOG='edu.duke.cs.legosdn.core.netlog.NetLog'

${JAVA} ${JVM_OPTS} ${LOG_OPTS} -cp ${CLASSPATH} ${NETLOG}
