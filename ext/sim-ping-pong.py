#!/usr/bin/env python
# -*- mode: python; coding: utf-8; fill-column: 80; -*-
#
# sim-ping-pong.py
# Created by Balakrishnan Chandrasekaran on 2015-02-12 17:55 -0500.
#

"""
sim-ping-pong.py
Run pong on a host and ping on another and collect measurements.
"""

__author__  = 'Balakrishnan Chandrasekaran <balac@cs.duke.edu>'
__version__ = '1.0'
__license__ = 'MIT'

from mininet.log import setLogLevel
from mininet.net import Mininet
from mininet.node import RemoteController
from mininet.topo import LinearTopo
import sys

# Number of switches
# NOTE: Assuming a linear topology with each switch attached to one host
N = 4

# Controller name and IP
CTRLR_NAME = 'galvatron.cs.duke.edu'
CTRLR_IP = '152.3.144.152'

def setup(num_switches, hosts_per_switch, ctrlr_name, ctrlr_ip):
    """Setup the topology and configuration.
    """
    topo = LinearTopo(num_switches, hosts_per_switch)
    net = Mininet(topo, controller=RemoteController,
                  autoSetMacs=True, autoStaticArp=True, build=False)
    remote = RemoteController(ctrlr_name, ip=ctrlr_ip)
    net.addController(remote)
    net.build()
    return net

def run_exp(net, out_file):
    """Run experiment to generate link up/down events.
    """
    dst = net.get("h%d" % (N))
    src = net.get('h1')
    try:
        # Run pong in the background
        dst.cmd("./ext/pong.py -m %s &" % (dst.IP()))
        
        # Run ping
        src.cmd("./ext/ping.py -m %s %s" % (dst.IP(), out_file))
    except Exception as e:
        sys.stderr.write("Error: %s\n" % (e))
    finally:
        dst.cmd('kill %./ext/pong.py')
        src.cmd('kill %./ext/ping.py')

if __name__ == '__main__':
    if len(sys.argv) != 2:
        sys.stderr.write("usage: %s <out-file>\n" % (sys.argv[0]))
        sys.exit(1)

    out_file = sys.argv[1]
    setLogLevel('info')
    n = None
    try:
        n = setup(N, 1, CTRLR_NAME, CTRLR_IP)
        n.start()
        run_exp(n, out_file)
        n.stop()
    except Exception as e:
        import sys
        sys.stderr.write("Error: %s\n" % str(e))
    finally:
        if n:
            n.stop()
