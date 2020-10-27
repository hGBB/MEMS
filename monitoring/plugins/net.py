#!/usr/bin/env python3

import psutil

def get_net_io():
    try:
        net = psutil.net_io_counters()
        tp = [net.bytes_sent, net.bytes_recv]
        return tp
    except Exception as e:
        return e

def get_net_con():
    try:
        net = psutil.net_connections()
        return net
    except Exception as e:
        return e
