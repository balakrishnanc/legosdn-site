#!/usr/bin/env python
# -*- mode: python; coding: utf-8; fill-column: 80; -*-
#
# pong.py
# Created by Balakrishnan Chandrasekaran on 2015-12-29 03:12 -0500.
# Copyright (c) 2014 Balakrishnan Chandrasekaran <balac@cs.duke.edu>.
#

"""
pong.py
Starts a UDP echo server listening at a given address over port 9999.
"""

__author__  = 'Balakrishnan Chandrasekaran <balac@cs.duke.edu>'
__version__ = '1.0'
__license__ = 'MIT'

import argparse
import socket

BUF_SZ = 1024

def run_pong(host, port):
    server = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        server.bind((host, port))
        while True:
            data, client = server.recvfrom(BUF_SZ)
            server.sendto(data, client)
    except KeyboardInterrupt as e:
        pass
    finally:
        server.close()

def main(args):
    run_pong(args.host, args.port)

def __get_arg_parser():
    """__get_arg_parser() - configures and return a parser for parsing command
    line arguments.
    """
    desc = ("'pong' UDP echo server")
    parser = argparse.ArgumentParser(description = desc)
    parser.add_argument('--version',
                        action = 'version',
                        version = '%(prog)s ' + "%s" % (__version__))
    parser.add_argument('-m', '--host',
                        dest = 'host',
                        default = '127.0.0.1',
                        help = 'Hostname.')
    parser.add_argument('-p', '--port',
                        type = int,
                        dest = 'port',
                        default = 9999,
                        help = 'Port.')
    parser.add_argument('-v', '--verbose',
                        action = 'store_true',
                        help = 'Enable verbose output.')
    return parser

if __name__ == '__main__':
    args = __get_arg_parser().parse_args()
    main(args)
