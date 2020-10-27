#!/usr/bin/env python3

import psutil

def get_io_counters():
    try:
        io = psutil.disk_io_counters()
        return io
    except Exception as e:
        return e
