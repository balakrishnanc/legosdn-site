#!/usr/bin/env python
# -*- mode: python; coding: utf-8; fill-column: 80; -*-
#
# tcpping.py
# Created by Balakrishnan Chandrasekaran on 2015-12-29 03:02 -0500.
# Copyright (c) 2014 Balakrishnan Chandrasekaran <balac@cs.duke.edu>.
#

"""
tcpping.py
Starts a TCP echo client that sends a ping to a TCP server running at a given
address and listening over port 2222, and prints the response back on STDOUT.
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
import threading

# Reference for converting current time to milliseconds since the reference
EPOCH = dt(1970, 1, 1)

# Time between pings
SNOOZE_TIME = 0.01
# Total number of pings to send
MAX_PINGS   = 300
# MAX_PINGS   = 30000
# MAX_PINGS   = 90000
# Size of buffer for receiving pongs
BUF_SZ      = 1024

# Maximum number of unanswered pings in queue
MAX_QSZ   = MAX_PINGS
# Time to wait blocking on queue (put/get)
WAIT_TIME = 30

PER_CONN_LIMIT = 1

def run_ping(host, port, wait_time, out):
    n = 0
    c = 0
    client = None
    try:
        if wait_time:
            time.sleep(wait_time)

        while n < MAX_PINGS:
            if not c:
                # New connection
                client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                client.setblocking(True)
                server = (args.host, args.port)
                client.connect(server)

            n += 1
            c += 1

            time.sleep(SNOOZE_TIME)
            now = (dt.now() - EPOCH).total_seconds()
            snd_data = "%.6f" % (now)
            client.send(snd_data)

            rcv_data = None
            try:
                rcv_data = client.recv(BUF_SZ)
            except:
                pass

            now = (dt.now() - EPOCH).total_seconds()
            if rcv_data:
                rcvd = float(rcv_data)
                delta = (now - rcvd) * 1000.0

                out.write(u"%.6f 1 %.3f\n" % (now, delta))
            else:
                out.write(u"%.6f 0 -1\n" % (now))

            out.flush()

            if c >= PER_CONN_LIMIT:
                c = 0
                client.close()
    except KeyboardInterrupt as e:
        pass
    except Queue.Full as e:
        pass
    finally:
        sys.stderr.write('closed.\n')
        if client is not None:
            client.close()

def main(args):
    with io.open(args.out_fpath, 'w', encoding='utf-8') as out:
        run_ping(args.host, args.port, args.wait, out)

def __get_arg_parser():
    """__get_arg_parser() - configures and return a parser for parsing command
    line arguments.
    """
    desc = ("'pong' TCP echo server")
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
                        default = 2222,
                        help = 'Server port.')
    parser.add_argument('-w', '--wait',
                        type = int,
                        dest = 'wait',
                        default = 0,
                        help = 'Time to wait before starting pings.')
    parser.add_argument('-v', '--verbose',
                        action = 'store_true',
                        help = 'Enable verbose output.')
    return parser

if __name__ == '__main__':
    args = __get_arg_parser().parse_args()
    main(args)
