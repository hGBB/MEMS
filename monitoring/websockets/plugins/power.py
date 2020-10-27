#!/usr/bin/env python3

import subprocess

def get_power():
    try:
        process = subprocess.Popen(['powerinfo'], stdout=subprocess.PIPE)
        out, err = process.communicate()
        pow = out.decode('utf-8').splitlines()
        kpi = [pow[0], pow[1], pow[2]]
        return kpi
    except Exception as e:
        expt = ['null', 'null', 'null']
        return expt

#print(get_power())
