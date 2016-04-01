#!/usr/bin/env python
# -*- mode: python; coding: utf-8; fill-column: 80; -*-
#
# sim-strec.py
# Created by Balakrishnan Chandrasekaran on 2015-08-01 00:55 -0500.
#

"""
sim-strec.py
Setup a simple topology with two paths between a source and destination. Setup a
flow, watch it going over one of the paths, bring down a link on that path and
test what the application does when it recovers.
"""

__author__  = 'Balakrishnan Chandrasekaran <balac@cs.duke.edu>'
__version__ = '1.0'
__license__ = 'MIT'

from mininet.cli import CLI
from mininet.link import TCLink
from mininet.log import setLogLevel
from mininet.net import Mininet
from mininet.node import RemoteController
from mininet.topo import LinearTopo, Topo
import os
import random
import sys
import time

# Random number generator seed
# For repeatable random sequences
RNG_SEED = 3262015
# For truly random sequences
# RNG_SEED = long(time.time())

# Number of events to generate
E = 30

# Time in seconds between the events
T = 0.01

# Controller name and IP
CTRLR_NAME = 'galvatron.cs.duke.edu'
CTRLR_IP = '152.3.144.152'

class CustomTopo(Topo):
    """Custom topology for testing route manager application.

        h1 h2 h3 h4 h5 h6
        |  |  |   |  |  |
	s1-s2-s3-s4-s5-s6
         \             / 
          s7---------s8
          |           |
          h7         h8
    """
    def __init__(self):
        Topo.__init__(self)

        # Add hosts/switches
        h1 = self.addHost('h1')
        h2 = self.addHost('h2')
        h3 = self.addHost('h3')
        h4 = self.addHost('h4')
        h5 = self.addHost('h5')
        h6 = self.addHost('h6')
        h7 = self.addHost('h7')
        h8 = self.addHost('h8')
	s1 = self.addSwitch('s1')
	s2 = self.addSwitch('s2')
	s3 = self.addSwitch('s3')
	s4 = self.addSwitch('s4')
	s5 = self.addSwitch('s5')
	s6 = self.addSwitch('s6')
	s7 = self.addSwitch('s7')
	s8 = self.addSwitch('s8')

        # Connect hosts to switches
        self.addLink(h1, s1)
	self.addLink(h2, s2)
        self.addLink(h3, s3)
	self.addLink(h4, s4)
	self.addLink(h5, s5)
	self.addLink(h6, s6)
	self.addLink(h7, s7)
	self.addLink(h8, s8)

        # Connect switches
	self.addLink(s1, s7, delay='1ms')
        self.addLink(s1, s2, delay='1ms')
        self.addLink(s2, s3, delay='1ms')
        self.addLink(s3, s4, delay='1ms')
        self.addLink(s4, s5, delay='1ms')
        self.addLink(s5, s6, delay='1ms')
	self.addLink(s7, s8, delay='1ms')
	self.addLink(s8, s6, delay='1ms')


def setup(ctrlr_name, ctrlr_ip):
    """Setup the topology and configuration.
    """
    topo = CustomTopo()
    net = Mininet(topo,
                  controller=RemoteController,
                  link=TCLink,
                  autoSetMacs=True,
                  autoStaticArp=True,
                  build=False)
    remote = RemoteController(ctrlr_name, ip=ctrlr_ip)
    net.addController(remote)
    net.build()
    return net

def get_sname(i):
    """Get name of switch.
    """
    return "s%d" % (i)

def get_hname(i):
    """Get name of host.
    """
    return "h%d" % (i)

def run_exp(net, num_events, pause_time):
    """Run experiment to generate link up/down events.
    """
    out_path = '/tmp'
    # CLI(net)

    # Wait till routes are populated.
    try:
        print('#> waiting till network is setup ...')
        time.sleep(120)
        print('#> network is setup')
    except KeyboardInterrupt as e:
        raise e

    # Destinations (pong)
    dst_ids = (8, )
    dsts = map(lambda h: net.get("h%d" % (h)), dst_ids)
    for dst in dsts:
        print("#> pong @ %s" % (dst.IP()))

    # Sources (ping)
    src_ids = (2, )
    srcs = map(lambda h: net.get("h%d" % (h)), src_ids)
    for src in srcs:
        print("#> ping @ %s" % (src.IP()))

    # CLI(net)
    try:
        for dst in dsts:
            # Run pong in the background
            pong_cmd = "./ext/pong.py -m %s &" % (dst.IP())
            dst.cmd(pong_cmd)
            print("#> " + pong_cmd)
        
        i = 0
        for src, dst in zip(srcs, dsts):
            out_file = os.path.sep.join((out_path,
                                         "h%d-to-h%d-log.txt" % (
                                             src_ids[i], dst_ids[i])))
            # Run ping
            ping_cmd = "./ext/ping.py -m %s %s &" % (dst.IP(), out_file)
            src.cmd(ping_cmd)
            print("#> " + ping_cmd)
            i += 1

        # While the ping-pongs are happening, go ahead and bring one of the links down
        try:
            time.sleep(5)
        except KeyboardInterrupt as e:
            raise e

        i = 0
        while i < E:
            i += 1
            
            net.configLinkStatus('s1', 's7', 'down')
            # CLI(net)
            
            # Give sufficient time for the rules to propagate
            time.sleep(1)

            # CLI(net)
            net.configLinkStatus('s1', 's7', 'up')
        
            # Give sufficient time for the rules to propagate
            time.sleep(1)

        # CLI(net)
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
        if out_file:
            print("#> check ping-pong times in %s" % (out_file))

if __name__ == '__main__':
    setLogLevel('info')
    n = None
    try:
        n = setup(CTRLR_NAME, CTRLR_IP)
        n.start()
        run_exp(n, E, T)
    except Exception as e:
        import sys
        sys.stderr.write("Error: %s\n" % str(e))
    finally:
        raw_input('> press any key to quit!')
        if n:
            n.stop()
