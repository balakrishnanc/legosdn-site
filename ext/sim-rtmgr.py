#!/usr/bin/env python
# -*- mode: python; coding: utf-8; fill-column: 80; -*-
#
# sim-lb.py
# Created by Balakrishnan Chandrasekaran on 2015-02-12 17:55 -0500.
#

"""
sim-rtmgr.py
Run pong on few hosts and ping on a few other hosts and test a simple rt mgr 
application.
"""

__author__  = 'Balakrishnan Chandrasekaran <balac@cs.duke.edu>'
__version__ = '1.0'
__license__ = 'MIT'

from mininet.cli import CLI
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
E = 300

# Time in seconds between the events
T = 0.01

# Controller name and IP
CTRLR_NAME = 'galvatron.cs.duke.edu'
CTRLR_IP = '152.3.144.152'

class CustomTopo(Topo):
    """Custom topology for testing route managager. Desribed below:

        h1 h2       h7 h8
        |  |        |  |
	s1-s2-s3-s4-s5-s6
            \       / 
             s7-s8-s9

	9 inter-switch links
	9 switches
	4 hosts
    """
    def __init__(self):
        Topo.__init__(self)

        # Add hosts/switches
        h1 = self.addHost('h1')
        h2 = self.addHost('h2')
        h7 = self.addHost('h7')
        h8 = self.addHost('h8')
	sw1 = self.addSwitch('s1')
	sw2 = self.addSwitch('s2')
	sw3 = self.addSwitch('s3')
	sw4 = self.addSwitch('s4')
	sw5 = self.addSwitch('s5')
	sw6 = self.addSwitch('s6')
	sw7 = self.addSwitch('s7')
	sw8 = self.addSwitch('s8')
	sw9 = self.addSwitch('s9')

        # Add links
        self.addLink(h1, sw1)
	self.addLink(h2, sw2)
	self.addLink(h7, sw5)
	self.addLink(h8, sw6)
        self.addLink(sw1, sw2)
        self.addLink(sw2, sw3)
        self.addLink(sw3, sw4)
        self.addLink(sw4, sw5)
        self.addLink(sw5, sw6)

	# Add 'backup' path
	self.addLink(sw2, sw7)
	self.addLink(sw7, sw8)
	self.addLink(sw8, sw9)
	self.addLink(sw9, sw5)

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

    # Destinations (pong)
    # NOTE: Each destination is paired with a source in the sources list read in reverse.
    # In other words, first destination is mapped to last source, and
    #  last destination is mapped to the first source.
    dst_ids = (8, 7)
    dsts = map(lambda h: net.get("h%d" % (h)), dst_ids)
    for dst in dsts: print("#> pong @ %s" % (dst.IP()))
    # Sources (ping)
    src_ids = (1, 2)
    srcs = map(lambda h: net.get("h%d" % (h)), src_ids)
    for src in srcs: print("#> ping @ %s" % (src.IP()))

    # Random event generator can take down any switch-to-switch link
    #  on the main path: s1-s2-s3-s4-s5-s6

    # Links (Sx <-> Sx) that are up
    up_links = []
    for i in range(1, 6): up_links.append((get_sname(i), get_sname(i+1)))

    # Total number of links to work with
    num_links = len(up_links)

    # Links (Sx <-> Hx) that are down
    dn_links = []

    rng = random.Random(RNG_SEED)

    try:
        for dst in dsts:
            # Run pong in the background
            pong_cmd = "./ext/pong.py -m %s &" % (dst.IP())
            dst.cmd(pong_cmd)
            print("#> " + pong_cmd)
        
        i = 0
        for src, dst in zip(srcs, dsts):
            i += 1
            out_file = os.path.sep.join((out_path, "ping-pong-h%d-log.txt" % (i)))
            # Run ping
            ping_cmd = "./ext/ping.py -m %s %s &" % (dst.IP(), out_file)
            src.cmd(ping_cmd)
            print("#> " + ping_cmd)

        # While the ping-pongs are happening, go ahead and bring links up and down.
        for e in range(E):
            if len(dn_links) == num_links or (dn_links and rng.random() <= 0.5):
                r = rng.randrange(len(dn_links))
                link = dn_links[r]
                dn_links.remove(link)
                up_links.append(link)
                
                end_a, end_b = link
                net.configLinkStatus(end_a, end_b, 'up')
            else:
                r = rng.randrange(len(up_links))
                link = up_links[r]
                up_links.remove(link)
                dn_links.append(link)
                
                end_a, end_b = link
                net.configLinkStatus(end_a, end_b, 'down')

            try:
                time.sleep(pause_time)
            except KeyboardInterrupt:
                break

        # CLI(net)
        # Should we give time after all events are done?
        time.sleep(10)
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
        if n:
            n.stop()
