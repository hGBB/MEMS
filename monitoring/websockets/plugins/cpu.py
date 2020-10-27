#!/usr/bin/env python3

import psutil


def get_cpu():
    try:
        return psutil.cpu_percent(interval=1)
    except Exception as e:
        return e

#print(get_cpu())
