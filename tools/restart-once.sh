#!/usr/bin/env bash
# -*- mode: sh; coding: utf-8; fill-column: 80; -*-
#
# restart-once.sh
# Created by Balakrishnan Chandrasekaran on 2015-01-28 23:40 -0500.
#

if [ $# -ne 1 ]; then
    echo "Usage: $0 <launcher-script>" >& 2
    exit 1
fi

LAUNCHER_SCRIPT="$1"
for r in $(seq 2); do
    ${LAUNCHER_SCRIPT}
done
