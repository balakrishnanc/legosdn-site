#!/usr/bin/env python
# -*- mode: python; coding: utf-8; fill-column: 80; -*-
#
# tcppong.py
# Created by Balakrishnan Chandrasekaran on 2015-12-29 03:12 -0500.
# Copyright (c) 2014 Balakrishnan Chandrasekaran <balac@cs.duke.edu>.
#

"""
tcppong.py
Starts a TCP echo server listening at a given address over port 2222.
"""

__author__  = 'Balakrishnan Chandrasekaran <balac@cs.duke.edu>'
__version__ = '1.0'
__license__ = 'MIT'

import argparse
import socket
import thread

BUF_SZ = 1024

def handle_client(client):
    try:
        while True:
            data = client.recv(BUF_SZ)
            if not data:
                break
            client.send(data)
    except:
        pass
    finally:
        client.close()


def run_pong(host, port):
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.setblocking(True)
    server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    try:
        server.bind((host, port))
        server.listen(1)
        while True:
            client, addrinfo = server.accept()
            thread.start_new_thread(handle_client, (client, ))
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
    desc = ("'pong' TCP echo server")
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
                        default = 2222,
                        help = 'Port.')
    parser.add_argument('-v', '--verbose',
                        action = 'store_true',
                        help = 'Enable verbose output.')
    return parser

if __name__ == '__main__':
    args = __get_arg_parser().parse_args()
    main(args)
