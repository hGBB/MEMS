#!/usr/bin/env python3

import psutil

def get_virt_mem():
    try:
        mem = psutil.virtual_memory()
        return mem.percent
    except Exception as e:
        return e

def get_swap_mem():
    try:
        mem = psutil.swap_memory()
        return mem
    except Exception as e:
        return e
