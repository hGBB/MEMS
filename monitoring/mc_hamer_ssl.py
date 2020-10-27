#!/usr/bin/env python3

import multiprocessing
import sys
import time
from multiprocessing import Value

import argparse
import getpass
import json
import names
import os
import requests
import ssl
import websocket
from functools import partial

from plugins import cpu
from plugins import disk
from plugins import mem
from plugins import net
from plugins import power

# values will be replaced by the installation script
TOKEN = 'asdf'
REG_URL = 'https://132.231.1.229:8443/api/nodes'
URL = 'wss://127.0.0.1:8443/mon'
# URL='wss://enpr01.fim.uni-passau.de/mon'
NAME = ''
INTERVAL = 5

token_path = '/home/{}/.hamer-token'.format(getpass.getuser())

parser = argparse.ArgumentParser(description='Monitoring Client for the HAMS EMS')
parser.add_argument('-u', '--url',
                    #                    required=True,
                    type=str,
                    help='Backend URL')
parser.add_argument('-t', '--token',
                    #                    required=True,
                    type=str,
                    help='very secret token to register on Backend')
parser.add_argument('-n', '--name',
                    type=str,
                    help='Name of the Node')
parser.add_argument('-i', '--interval',
                    type=int,
                    help='the sending interval of the KPIs')
args = parser.parse_args()

kpi_package = {
    'uuid': 'none',
    'node_name': 'none',
    'interval': 10,
    'kpis': [
        {'name': 'cpu', 'value': 'none'},
        {'name': 'disk_usage', 'value': 'none'},
        {'name': 'memory', 'value': 'none'},
        {'name': 'net_bytes_send', 'value': 'none'},
        {'name': 'net_bytes_recv', 'value': 'none'},
        {'name': 'power_current', 'value': 'none'},
        {'name': 'power_voltage', 'value': 'none'},
        {'name': 'power_usage', 'value': 'none'},
    ]
}

# dic to store relevant data of the collector process
process_data = {
    'pid': 'none',
    'process_instance': 'none',
    'interval': 'none',
}


# checks if energy is available
def energy_check():
    energy = power.get_power()
    if energy[0] == 'null':
        return True
    else:
        return False


# register to backend. Token and name required.
def register(args):
    try:
        payload = {'key': args.token, 'name': args.name, 'estimated': energy_check()}
        r = requests.post(args.url, params=payload, verify='ca.crt')
        key = json.loads(r.text)
        identity = {
            'uuid': key['uuid'],
            'name': args.name
        }
        with open(token_path, 'w+') as token:
            token.write(json.dumps(identity))
        token.closed
        os.chmod(token_path, 0o600)
    except Exception as e:
        print(e)
        sys.exit('[-] Please check if your URL is correct, or maybe your node name is already in use. For help use -h')


def de_register():
    rm_token = 'rm {}'.format(token_path)
    os.system(rm_token)
    disable_mc = 'systemctl --user disable mc_hammer.service'
    os.system(disable_mc)
    stop_mc = 'systemctl --user stop mc_hammer.service'
    os.system(stop_mc)


def on_message(ws, message):
    try:
        change_req = json.loads(message)
        interval.value = change_req['interval']
        if change_req['registered'] == 'False':
            de_register()
    except Exception as e:
        print(e)


def check_interval(interval):
    old = interval.value
    for i in range(0, old):
        time.sleep(2)
        if old != interval.value:
            break


def on_error(ws, error):
    print(error)


def on_close(ws):
    print("### closed ###")


def on_open(interval, uuid, name, ws):
    def run(*args):
        while True:
            try:
                network = net.get_net_io()
                energy = power.get_power()
                kpi_package['uuid'] = uuid
                kpi_package['node_name'] = name
                kpi_package['interval'] = interval.value
                kpi_package['kpis'][0]['value'] = cpu.get_cpu()
                kpi_package['kpis'][1]['value'] = disk.get_disk_usage()
                kpi_package['kpis'][2]['value'] = mem.get_virt_mem()
                kpi_package['kpis'][3]['value'] = network[0]
                kpi_package['kpis'][4]['value'] = network[1]
                kpi_package['kpis'][5]['value'] = energy[0]
                kpi_package['kpis'][6]['value'] = energy[1]
                kpi_package['kpis'][7]['value'] = energy[2]
                ws.send(json.dumps(kpi_package))
                print('this is the actual interval: {}'.format(interval.value))
            except Exception as e:
                print(e)
                print('EXCEPTION')
            check_interval(interval)

    p = multiprocessing.Process(target=run, args=())
    p.start()
    process_data['pid'] = p.pid
    process_data['process_instance'] = p
    process_data['interval'] = interval.value


if __name__ == '__main__':
    if not args.name and NAME:
        args.name = NAME
    elif not args.name and not NAME:
        args.name = names.get_first_name()
    if not args.interval and INTERVAL:
        args.interval = INTERVAL
    elif not args.interval and not INTERVAL:
        args.interval = 5

    required = [[args.url, REG_URL, 'url'], [args.token, TOKEN, 'token']]
    for arg in required:
        if arg[0] and (arg[2] == 'url'):
            URL = 'wss://{}/mon'.format(arg[0])
        if not arg[0] and arg[1]:
            setattr(args, arg[2], arg[1])
        elif not arg[0] and not arg[1]:
            sys.exit('[-] you need to specify a valid {} for authentication.'.format(arg[2].upper()))

    if not os.path.isfile(token_path):
        uuid = register(args)

    # needed for reboot
    f = open(token_path)
    identity = json.loads(f.readline())
    uuid = identity['uuid']
    args.name = identity['name']

    interval = Value('i', args.interval)  # shared memory map

    while True:
        # header = 'Authorization: Bearer {}'.format(uuid)
        # websocket.enableTrace(True)
        ws = websocket.WebSocketApp(URL,
                                    on_message=on_message,
                                    on_error=on_error,
                                    on_close=on_close)
        # header = [header])
        func = partial(on_open, interval, uuid, args.name)
        ws.on_open = func
        ws.run_forever(sslopt={"cert_reqs": ssl.CERT_REQUIRED,
                               "ca_certs": 'ca.crt',
                               "ssl_version": ssl.PROTOCOL_TLSv1_2})
        try:
            process_data['process_instance'].terminate()
            process_data['process_instance'].join()
        except Exception as e:
            print(e)
            print('Plz check if a collector instance is running.')

        time.sleep(10)
