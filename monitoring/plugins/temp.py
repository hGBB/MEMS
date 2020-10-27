#!/usr/bin/env python3

import psutil

def get_temp():
    try:
        temp = psutil.sensors_temperatures()
        return temp
    except Exception as e:
        return e
