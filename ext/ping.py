#!/usr/bin/env python
# -*- mode: python; coding: utf-8; fill-column: 80; -*-
#
# ping.py
# Created by Balakrishnan Chandrasekaran on 2015-12-29 03:02 -0500.
# Copyright (c) 2014 Balakrishnan Chandrasekaran <balac@cs.duke.edu>.
#

"""
ping.py
Starts a UDP echo client that sends a ping to a UDP server running at a given
address and listening over port 9999, and prints the response back on STDOUT.
"""

__author__  = 'Balakrishnan Chandrasekaran <balac@cs.duke.edu>'
__version__ = '1.0'
__license__ = 'MIT'

import argparse
import Queue
from datetime import datetime as dt
import io
import socket
import sys
import time
import thread

# Reference for converting current time to milliseconds since the reference
EPOCH = dt(1970, 1, 1)

# Time between pings
SNOOZE_TIME = 0.01
# Total number of pings to send
MAX_PINGS   = 1200
# MAX_PINGS   = 30000
# MAX_PINGS   = 90000
# Size of buffer for receiving pongs
BUF_SZ      = 1024

# Maximum number of unanswered pings in queue
MAX_QSZ   = MAX_PINGS
# Time to wait blocking on queue (put/get)
WAIT_TIME = 30

def drain_queue(q, rcvd, now, out):
    n = 0
    while not q.empty():
        ts = q.get(True, WAIT_TIME)
        if not ts:
            break
        n += 1
        if ts < rcvd:
            out.write(u"%.6f 0 -1\n" % (ts))
            out.flush()
        elif ts >= rcvd:
            delta = (now - ts) * 1000.0
            out.write(u"%.6f 1 %.3f\n" % (ts, delta))
            out.flush()
            break
    return n

def process_pong(client, q, out):
    try:
        n = 0
        while n < MAX_PINGS:
            data, server = client.recvfrom(BUF_SZ)
            rcvd = float(data)
            now = (dt.now() - EPOCH).total_seconds()
            n += drain_queue(q, rcvd, now, out)
    except KeyboardInterrupt as e:
        pass

def run_ping(host, port, out):
    client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    server = (args.host, args.port)
    q = Queue.Queue(MAX_QSZ)
    try:
        t = thread.start_new_thread(process_pong, (client, q, out))
        n = 0
        while not q.full() or n < MAX_PINGS:
            n += 1
            time.sleep(SNOOZE_TIME)
            now = (dt.now() - EPOCH).total_seconds()
            q.put(now, True, WAIT_TIME)
            data = "%.6f" % (now)
            client.sendto(data, server)
    except KeyboardInterrupt as e:
        pass
    except Queue.Full as e:
        pass
    finally:
        sys.stderr.write('closed.\n')
        client.close()

def main(args):
    with io.open(args.out_fpath, 'w', encoding='utf-8') as out:
        run_ping(args.host, args.port, out)

def __get_arg_parser():
    """__get_arg_parser() - configures and return a parser for parsing command
    line arguments.
    """
    desc = ("'pong' UDP echo server")
    parser = argparse.ArgumentParser(description = desc)
    parser.add_argument('--version',
                        action = 'version',
                        version = '%(prog)s ' + "%s" % (__version__))
    parser.add_argument('out_fpath',
                        type = str,
                        help = 'Absolute/relative path to output file.')
    parser.add_argument('-m', '--host',
                        dest = 'host',
                        default = '127.0.0.1',
                        help = 'Server hostname.')
    parser.add_argument('-p', '--port',
                        type = int,
                        dest = 'port',
                        default = 9999,
                        help = 'Server port.')
    parser.add_argument('-v', '--verbose',
                        action = 'store_true',
                        help = 'Enable verbose output.')
    return parser

if __name__ == '__main__':
    args = __get_arg_parser().parse_args()
    main(args)
