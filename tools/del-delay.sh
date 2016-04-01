#!/usr/bin/env bash
# -*- mode: sh; coding: utf-8; fill-column: 80; -*-
#
# del-delay.sh
# Created by Balakrishnan Chandrasekaran on 2015-08-05 21:02 -0500.
#

if [ $# -ne 2 ]; then
    echo "Usage: $0 <dev> <delay (in ms)>" >& 2
    exit 1
fi


# Network interface
iface=$1

# Link delay (in ms)
msdelay=$2

sudo tc qdisc del dev ${iface} root netem delay ${msdelay}ms
