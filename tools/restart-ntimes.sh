#!/usr/bin/env bash
# -*- mode: sh; coding: utf-8; fill-column: 80; -*-
#
# restart-ntimes.sh
# Created by Balakrishnan Chandrasekaran on 2015-02-12 17:35 -0500.
#

if [ $# -ne 2 ]; then
    echo "Usage: $0 <N> <launcher-script>" >& 2
    exit 1
fi

NUM_RESTARTS=$1
LAUNCHER_SCRIPT="$2"
for r in $(seq ${NUM_RESTARTS}); do
    ${LAUNCHER_SCRIPT}
    echo 'Crashed.' >& 2
done
