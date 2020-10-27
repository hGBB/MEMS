#!/usr/bin/env python3

import time
import requests
import names
import json
import argparse
import sys
import os
import getpass
import multiprocessing
from functools import partial
from plugins import cpu
from plugins import disk
from plugins import mem
from plugins import net
from plugins import power
from plugins import temp

#values will be replaced by the installation script
TOKEN='1064'
REG_URL='https://localhost:7331/api/nodes'
#REG_URL='https://132.231.1.229:8443/api/nodes'
CONF_URL='https://localhost:7331/api/nodes/configuration'
#URL='https://132.231.1.229:8443/api/monitoring/records'
URL='https://localhost:7331/api/monitoring/records'
#CONF_URL='https://132.231.1.229:8443/api/nodes/configuration'
NAME=''
INTERVAL=0
CA_PATH='ca.crt'
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
parser.add_argument('-d', '--debug',
                    type=str,
                    help='debug mode [True/False]')
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

#checks if energy is available
def energy_check():
    energy = power.get_power()
    if energy[0] == 'null':
        return True
    else:
        return False


#register to backend. Token and name required.
def register(TOKEN, NAME, REG_URL, CA_PATH):
    try:
        payload = {'key': TOKEN, 'name': NAME, 'estimated': energy_check()}
        r = requests.post(REG_URL, params=payload, verify=CA_PATH)
        key = json.loads(r.text)
        print(key)
        identity = {
                'uuid': key['uuid'],
                'name': NAME,
                'node_id' : key['id']
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
    disable_mc = 'systemctl --user disable mc_hamer.service'
    os.system(disable_mc)
    stop_mc = 'systemctl --user stop mc_hamer.service'
    os.system(stop_mc)

#def check_interval(interval):
#    old = interval.value
#    for i in range (0,(old // 2)):
#        time.sleep(2)
#        if old != interval.value:
#            break

def run(URL, INTERVAL, NAME, CA_PATH, uuid, headers):
    while True:
        try:
            network = net.get_net_io()
            energy = power.get_power()
            kpi_package['uuid'] = uuid
            kpi_package['node_name'] = NAME
            kpi_package['interval'] = INTERVAL
            kpi_package['kpis'][0]['value'] = cpu.get_cpu()
            kpi_package['kpis'][1]['value'] = disk.get_disk_usage()
            kpi_package['kpis'][2]['value'] = mem.get_virt_mem()
            kpi_package['kpis'][3]['value'] = network[0]
            kpi_package['kpis'][4]['value'] = network[1]
            kpi_package['kpis'][5]['value'] = energy[0]
            kpi_package['kpis'][6]['value'] = energy[1]
            kpi_package['kpis'][7]['value'] = energy[2]
            payload = json.dumps(kpi_package)
            r = requests.post(URL, data=payload, headers=headers, verify=CA_PATH)
            if DEBUG:
                print('this is the actual interval: {}'.format(INTERVAL))
                try:
                    print(payload)
                    print(r.json())
                except:
                    print(r)
        except Exception as e:
            if DEBUG:
                print(e)
                print('EXCEPTION IN COLLECTOR PROCESS')
            else:
                pass
#        check_interval(interval)
        time.sleep(INTERVAL)

def check_config(headers, url, CA_PATH):
    try:
        r = requests.get(url, headers=headers, verify=CA_PATH)
        config = r.json()
    except:
        config = 'null'
    return config

if __name__ == '__main__':
    if (args.name and NAME) or (args.name and not NAME):
        NAME = args.name
    elif not args.name and not NAME:
        NAME = names.get_first_name()
    if (args.interval and INTERVAL) or (args.interval and not INTERVAL):
        INTERVAL = args.interval
    elif not args.interval and not INTERVAL:
        INTERVAL = 5

    required = [[args.url, REG_URL, 'url'], [args.token, TOKEN, 'token']]
    for arg in required:
        if arg[0] and (arg[2] == 'url'):
            URL='https://{}/api/monitoring/records'.format(arg[0])
            CONF_URL='https://{}/api/nodes/configuration'.format(arg[0])
            REG_URL='https://{}/api/nodes'.format(arg[0])
        if arg[0] and (arg[2] == 'token'):
            TOKEN = arg[0]
        elif not arg[0] and not arg[1]:
            sys.exit('[-] you need to specify a valid {} for authentication.'.format(arg[2].upper()))

    if not os.path.isfile(token_path):
        register(TOKEN, NAME, REG_URL, CA_PATH)
        pass

    if args.debug:
        DEBUG = True
    else:
        DEBUG = False

    #needed for reboot   
    f = open(token_path)
    identity = json.loads(f.readline())
    uuid = identity['uuid']
    NAME = identity['name']
    node_id = identity['node_id']

    if DEBUG:
        print(URL, CONF_URL, REG_URL)
        print('Node name: {}'.format(NAME))
        print('Node interval: {}'.format(INTERVAL))
        print('Node token: {}'.format(TOKEN))

    headers = {'Node-Authorization' : 'Bearer {} {}'.format(node_id, uuid),
               'content-type': 'application/json'     
            }

    p = multiprocessing.Process(target=run, args=(URL, INTERVAL, NAME, CA_PATH, uuid, headers,))
    p.start()
    process_data['pid'] = p.pid
    process_data['process_instance'] = p
    while True:
        conf = check_config(headers, CONF_URL, CA_PATH)
        if conf != 'null':
            try:
                if conf['sendInterval']:
                    INTERVAL = conf['sendInterval']
                    process_data['process_instance'].terminate()
                    process_data['process_instance'].join()
                    p = multiprocessing.Process(target=run, args=(URL, INTERVAL, NAME, CA_PATH, uuid, headers,))
                    p.start()
                    process_data['pid'] = p.pid
                    process_data['process_instance'] = p
            except:
                pass
            try:
                if conf['error'] == 'Unauthorized':
                    de_register()
            except:
                pass
        time.sleep(5)
    
