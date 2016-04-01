#!/usr/bin/env bash
# -*- mode: sh; coding: utf-8; fill-column: 80; -*-
#
# daemonize.sh
# Created by Balakrishnan Chandrasekaran on 2015-01-22 00:13 -0500.
#

if [ $# -ne 2 ]; then
    echo "Usage: $0 <launcher-path> <app-name>" >&2
fi

LAUNCHER="$1"
APP_NAME="$2"
${LAUNCHER} ${APP_NAME} &

exit $?
