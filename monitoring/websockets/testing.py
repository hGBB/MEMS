#!/usr/bin/env python3

import multiprocessing
import time
import requests
import names
import json
import argparse
import sys
import os
import getpass
import websockets
import asyncio
import ssl
from plugins import cpu
from plugins import disk
from plugins import mem
from plugins import net
from plugins import power
from plugins import temp

#values will be replaced by the installation script
TOKEN='asdf'
URL='http://www.localhost:8080/api/nodes'
NAME=''
INTERVAL=0

token_path = '/home/{}/.hammer-token'.format(getpass.getuser())


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
        {'name' : 'cpu', 'value' : 'none'},
        {'name' : 'disk_usage', 'value' : 'none'},
        {'name' : 'memory', 'value' : 'none'},
        {'name' : 'net_bytes_send', 'value' : 'none'},
        {'name' : 'net_bytes_recv', 'value' : 'none'},
        {'name' : 'power_current', 'value' : 'none'},
        {'name' : 'power_voltage', 'value' : 'none'},
        {'name' : 'power_usage', 'value' : 'none'},
   ]
}

#dic to store relevant data of the collector process
process_data = {
    'pid': 'none',
    'process_instance': 'none',
    'interval': 'none',
}

#register to backend. Token and name required.
def register(args):
    try:
        payload = {'key':args.token, 'name':args.name}
        r = requests.post(args.url, params=payload)
        key = json.loads(r.text)
        print('uuid = {}'.format(key['uuid']))
        with open(token_path, 'w+') as token:
            token.write(key['uuid'])
        token.closed
        os.chmod(token_path, 0o600)
#        return key['uuid']
    except:
        sys.exit('[-] Please check if your URL is correct, or maybe your node name is already in use.')


async def collector(interval, uuid, name):
    #ssl_context = ssl.SSLContext(ssl.PROTOCOL_TLS_CLIENT)
    #ssl_context.load_verify_locations('localhost.pem', 'key.pem')
    while True:
        print('connection loop')
        #async with websockets.connect('wss://localhost:8765', ssl=ssl_context) as websocket:
        try:
            async with websockets.connect('ws://localhost:8088') as websocket:
                while True:
                    print('sending loop')
                    network = net.get_net_io()
                    energy = power.get_power()
                    kpi_package['uuid'] = uuid
                    kpi_package['node_name'] = name
                    kpi_package['interval'] = interval
                    kpi_package['kpis'][0]['value'] = cpu.get_cpu()
                    kpi_package['kpis'][1]['value'] = disk.get_disk_usage()
                    kpi_package['kpis'][2]['value'] = mem.get_virt_mem()
                    kpi_package['kpis'][3]['value'] = network[0]
                    kpi_package['kpis'][4]['value'] = network[1]
                    kpi_package['kpis'][5]['value'] = energy[0]
                    kpi_package['kpis'][6]['value'] = energy[1]
                    kpi_package['kpis'][7]['value'] = energy[2]
                    try:
                        await websocket.send(json.dumps(kpi_package))
                        ack = await websocket.recv()
                        print(ack)
                        print('------------------------')
                        await websocket.send(json.dumps(kpi_package))
                        ack = await websocket.recv()
                        print(ack)
                    except:
                        print('whhhhaaaat?')
                        break
        except Exception as e:
            print(e)
            print('EXCEPTION')
        time.sleep(interval)
        
def start(interval, uuid, name):
    asyncio.get_event_loop().run_until_complete(collector(interval, uuid, name))

if __name__ == '__main__':
    if not args.name and NAME:
        args.name = NAME
    elif not args.name and not NAME:
        args.name = names.get_first_name()
    if not args.interval and INTERVAL:
        args.interval = INTERVAL
    elif not args.interval and not INTERVAL:
        args.interval = 5

    required = [[args.url, URL, 'url'], [args.token, TOKEN, 'token']]
    for arg in required:
        if not arg[0] and arg[1]:
            setattr(args, arg[2], arg[1])
        elif not arg[0] and not arg[1]:
            sys.exit('[-] you need to specify a valid {} for authentication.'.format(arg[2].upper()))

    if not os.path.isfile(token_path):
        uuid = register(args)

    #needed for reboot   
    f = open(token_path)
    uuid = f.readline() 

    p = multiprocessing.Process(target=start, args=(args.interval, uuid, args.name,))
    p.name = 'collector'
    p.start()
    process_data['pid'] = p.pid
    process_data['process_instance'] = p
    process_data['interval'] = args.interval

    while True:
        try:
            change = int(input('new interval: '))
            if change != process_data['interval']:
                process_data['process_instance'].terminate()
                process_data['process_instance'].join()
                p = multiprocessing.Process(target=start, args=(change, uuid, args.name))
                p.start()
                process_data['pid'] = p.pid
                process_data['process_instance'] = p
                process_data['interval'] = change
        except:
            print('only integers plz')
        time.sleep(5)
