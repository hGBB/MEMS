#!/usr/bin/env python3

import multiprocessing
import time
import json
import ssl
from stompest.config import StompConfig
from stompest.sync import Stomp
from stompest.protocol import StompSpec
from plugins import cpu
from plugins import disk
from plugins import io
from plugins import mem
from plugins import net
from plugins import temp
from plugins import power

kpi_package = {
   'node':'none',
   'node_name':'none',
   'interval':10,
   'kpis':{
      'cpu':'none',
      'disk_usage':'none',
      'disk_part':'none',
      'io':'none',
      'mem_virt':'none',
      'mem_swap':'none',
      'net_io':'none',
      'net_con':'none',
      'temp':'none',
      'power':'none'
   }
}

#async def collector(interval):
#    ssl_context = ssl.SSLContext(ssl.PROTOCOL_TLS_CLIENT)
#    ssl_context.load_verify_locations('localhost.pem', 'key.pem')
#    while True:
#        async with websockets.connect('wss://localhost:8765', ssl=ssl_context) as websocket:
#            kpi_package['node'] = 1
#            kpi_package['node_name'] = 'bacon'
#            kpi_package['kpis']['cpu'] = cpu.get_cpu()
#            kpi_package['kpis']['disk_usage'] = disk.get_disk_usage()
#            #kpi_package['kpis']['disk_part'] = disk.get_disk_part()
#            #kpi_package['kpis']['io'] = io.get_io_counters()
#            kpi_package['kpis']['mem_virt'] = mem.get_virt_mem()
#            kpi_package['kpis']['mem_swap'] = mem.get_swap_mem()
#            kpi_package['kpis']['net_io'] = net.get_net_io()
#            kpi_package['kpis']['net_con'] = net.get_net_con()
#            kpi_package['kpis']['temp'] = temp.get_temp()
#            kpi_package['kpis']['cpu'] = cpu.get_cpu()
#            kpi_package['kpis']['power'] = power.get_power()
#            await websocket.send(json.dumps(kpi_package))
#            #print(f"> {kpi_package}")
#            ack = await websocket.recv()
#            print(f"> {ack}")
#            print('\n \n')
#
#        time.sleep(interval)

def collector(interval):
    CONFIG = StompConfig('tcp://172.17.0.2:61613')
    QUEUE = '/queue/test'
    while True:
        kpi_package['node'] = 1
        kpi_package['node_name'] = 'bacon'
        kpi_package['kpis']['cpu'] = cpu.get_cpu()
        kpi_package['kpis']['disk_usage'] = disk.get_disk_usage()
        #kpi_package['kpis']['disk_part'] = disk.get_disk_part()
        #kpi_package['kpis']['io'] = io.get_io_counters()
        kpi_package['kpis']['mem_virt'] = mem.get_virt_mem()
        kpi_package['kpis']['mem_swap'] = mem.get_swap_mem()
        kpi_package['kpis']['net_io'] = net.get_net_io()
        kpi_package['kpis']['net_con'] = net.get_net_con()
        kpi_package['kpis']['temp'] = temp.get_temp()
        kpi_package['kpis']['cpu'] = cpu.get_cpu()
        kpi_package['kpis']['power'] = power.get_power()

        client = Stomp(CONFIG)
        client.connect()
        client.send(QUEUE, json.dumps(kpi_package).encode())
        #client.send(QUEUE, 'test message 2'.encode())
        client.disconnect()

        time.sleep(interval)


if __name__=="__main__":
    interval = 10
    data = {
            'pid' : 'none', 
            'process_instance' : 'none', 
            'interval' : 'none', 
            'start' : 'none'
            }


    p = multiprocessing.Process(target=collector, args=(interval,))
    p.start()
    start_time = time.time()
    data['pid'] = p.pid 
    data['process_instance'] = p 
    data['interval'] = interval
    data['start'] = start_time

    CONFIG = StompConfig('tcp://172.17.0.2:61613')
    QUEUE = '/queue/interval'
    client = Stomp(CONFIG)  
    client.connect()
    client.subscribe(QUEUE, {StompSpec.ACK_HEADER: StompSpec.ACK_CLIENT_INDIVIDUAL})
    
    while True:
#        print(p.is_alive())
#        try:
#            change = int(input("new interval: "))
#        except:
#            print('only integers plz')
        frame = client.receiveFrame()
        sample = frame.body.decode("utf-8")
        j = json.loads(sample)
        change = j['interval']
        if change != data['interval']:
            data['process_instance'].terminate()
            data['process_instance'].join()
            p = multiprocessing.Process(target=collector, args=(change,))
            p.start()
            start_time = time.time()
            data['pid'] = p.pid 
            data['process_instance'] = p 
            data['interval'] = change
            data['start'] = start_time

        time.sleep(5)
        
















