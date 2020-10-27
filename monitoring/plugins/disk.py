#!/usr/bin/env python3

import psutil

def get_disk_usage():
    try:
        use = psutil.disk_usage('/')
        return use.percent
    except Exception as e:
        return e

def get_disk_part():
    try:
        use = psutil.disk_partitions()
        return use
    except Exception as e:
        return e
