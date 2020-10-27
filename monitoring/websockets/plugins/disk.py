#!/usr/bin/env python3

import psutil

def get_disk_usage():
    try:
        use = psutil.disk_usage('/')
    #    return use
        return use.percent #just return the used in %
    except Exception as e:
        return e

def get_disk_part():
    try:
        use = psutil.disk_partitions()
        return use
    except Exception as e:
        return e

#print(get_disk_usage())
