#!/usr/bin/env bash
# Create the necessary mount points for LegoSDN to work properly.

[ $(whoami) != 'root' ] && \
    echo 'Error: please run the script as root!' >& 2 && \
    exit 1

LEGOSDN_MT_PT='/legosdn'
# Mount point size
MT_PT_SZ='8g'

mtpt=$(mount | grep ${LEGOSDN_MT_PT})
if [ $(mount | grep -c ${LEGOSDN_MT_PT}) -gt 0 ]; then
    echo "Mount point '${LEGOSDN_MT_PT}' already exists."
else
    echo "Using 'sudo' to mount tmpfs on '${LEGOSDN_MT_PT}'"

    sudo mount -t tmpfs -o size=${MT_PT_SZ} tmpfs /legosdn
    
    if [ $? -eq 0 ]; then
	echo "Mounted tmpfs on '${LEGOSDN_MT_PT}'."
    else
	echo "Failed to mount tmpfs on '${LEGOSDN_MT_PT}'." >&2
    fi
fi
