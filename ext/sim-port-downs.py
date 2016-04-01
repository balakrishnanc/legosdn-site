#!/usr/bin/env python
# -*- mode: python; coding: utf-8; fill-column: 80; -*-
#
# sim-port-downs.py
# Created by Balakrishnan Chandrasekaran on 2015-01-28 00:26 -0500.
#

"""
sim-port-downs.py
Simulate port downs (and ups).
"""

__author__  = 'Balakrishnan Chandrasekaran <balac@cs.duke.edu>'
__version__ = '1.0'
__license__ = 'MIT'

import math
from mininet.cli import CLI
from mininet.log import setLogLevel
from mininet.net import Mininet
from mininet.node import RemoteController
from mininet.topo import LinearTopo
import random
import time

# Number of switches
# NOTE: Assuming a linear topology with each switch attached to one host
N = 8

# Number of events to generate
E = 3000

# Time in seconds between the events
T = 0.01

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

def run_exp(net, num_events, pause_time):
    """Run experiment to generate link up/down events.
    """
    # Links (Sx <-> Hx) that are up
    up_links = [(s, s) for s in range(1, N+1)]
    num_links = len(up_links)

    # Links (Sx <-> Hx) that are down
    dn_links = []

    rng = random.Random(long(time.time()))
    
    # net.pingAll()

    for e in range(E):
        if len(dn_links) == num_links or (dn_links and rng.random() <= 0.5):
            r = rng.randrange(len(dn_links))
            link = dn_links[r]
            dn_links.remove(link)
            up_links.append(link)

            s, h = link
            switch_name = "s%d" % (s)
            host_name = "h%d" % (h)
            net.configLinkStatus(switch_name, host_name, 'up')
        else:
            r = rng.randrange(len(up_links))
            link = up_links[r]
            up_links.remove(link)
            dn_links.append(link)

            s, h = link
            switch_name = "s%d" % (s)
            host_name = "h%d" % (h)
            net.configLinkStatus(switch_name, host_name, 'down')
        try:
            time.sleep(pause_time)

            if rng.random() < 0.01:
                net.pingPair()
        except:
            break

if __name__ == '__main__':
    setLogLevel('info')
    n = None
    try:
        n = setup(N, 1, CTRLR_NAME, CTRLR_IP)
        n.start()
        run_exp(n, E, T)
    except Exception as e:
        import sys
        sys.stderr.write("Error: %s\n" % str(e))
    finally:
        if n:
            n.stop()
