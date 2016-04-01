#!/usr/bin/env python
# -*- mode: python; coding: utf-8; fill-column: 80; -*-
#
# sim-lb.py
# Created by Balakrishnan Chandrasekaran on 2015-02-12 17:55 -0500.
#

"""
sim-lb.py
Run pong on few hosts and ping on a few other hosts and test a simple load
balancing application.
"""

__author__  = 'Balakrishnan Chandrasekaran <balac@cs.duke.edu>'
__version__ = '1.0'
__license__ = 'MIT'

from mininet.log import setLogLevel
from mininet.net import Mininet
from mininet.node import RemoteController
from mininet.topo import LinearTopo, Topo
import os
import sys
import time

# Experiment duration
EXP_TIME = 800

# Controller name and IP
CTRLR_NAME = 'galvatron.cs.duke.edu'
CTRLR_IP = '152.3.144.152'

class CustomTopo(Topo):
    """Custom topology for testing load balancer.
    """
    def __init__(self):
        Topo.__init__(self)

        # Add hosts/switches
        leftHost1    = self.addHost('h1')
        leftHost2    = self.addHost('h2')
        rightHost1   = self.addHost('h3')
        rightHost2   = self.addHost('h4')
        rightHost3   = self.addHost('h5')
        leftSwitch   = self.addSwitch('s6')
        topSwitch    = self.addSwitch('s7')
        midSwitch    = self.addSwitch('s8')
        bottomSwitch = self.addSwitch('s9')
        rightSwitch  = self.addSwitch('s10')

        # Add links
        self.addLink(leftHost1, leftSwitch)
        self.addLink(leftHost2, leftSwitch)
        self.addLink(leftSwitch, topSwitch)
        self.addLink(leftSwitch, midSwitch)
        self.addLink(leftSwitch, bottomSwitch)
        self.addLink(topSwitch, rightSwitch)
        self.addLink(midSwitch, rightSwitch)
        self.addLink(bottomSwitch, rightSwitch)
        self.addLink(rightSwitch, rightHost1)
        self.addLink(rightSwitch, rightHost2)
        self.addLink(rightSwitch, rightHost3)

def setup(ctrlr_name, ctrlr_ip):
    """Setup the topology and configuration.
    """
    topo = CustomTopo()
    net = Mininet(topo, controller=RemoteController,
                  autoSetMacs=True, autoStaticArp=True, build=False)
    remote = RemoteController(ctrlr_name, ip=ctrlr_ip)
    net.addController(remote)
    net.build()
    return net

def run_exp(net, out_path):
    """Run experiment to generate link up/down events.
    """
    # NOTE: Each destination is paired with a source in the sources list read in reverse.
    # In other words, first destination is mapped to last source, and
    #  last destination is mapped to the first source.
    # Destinations (pong)
    dsts = map(lambda h: net.get("h%d" % (h)), (5, 4))
    # Sources (ping)
    srcs = map(lambda h: net.get("h%d" % (h)), (1, 2))
    try:
        for dst in dsts:
            # Run pong in the background
            dst.cmd("./ext/pong.py -m %s &" % (dst.IP()))
        
        i = 0
        for src, dst in zip(srcs, dsts):
            i += 1
            out_file = os.path.sep.join((out_path, "ping-pong-h%d-log.txt" % (i)))
            # Run ping
            src.cmd("./ext/ping.py -m %s %s &" % (dst.IP(), out_file))

        time.sleep(EXP_TIME)
    except Exception as e:
        sys.stderr.write("Error: %s\n" % (e))
    finally:
        for src in srcs:
            try:
                src.cmd('kill %./ext/ping.py')
            except:
                continue
        for dst in dsts:
            try:
                dst.cmd('kill %./ext/pong.py')
            except:
                continue

if __name__ == '__main__':
    if len(sys.argv) != 2:
        sys.stderr.write("usage: %s <out-path>\n" % (sys.argv[0]))
        sys.exit(1)

    out_path = sys.argv[1]
    setLogLevel('info')
    n = None
    try:
        n = setup(CTRLR_NAME, CTRLR_IP)
        n.start()
        run_exp(n, out_path)
        n.stop()
    except Exception as e:
        import sys
        sys.stderr.write("Error: %s\n" % str(e))
    finally:
        if n:
            n.stop()
