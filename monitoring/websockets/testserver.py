#!/usr/bin/python3
# WSS (WS over TLS) server example, with a self-signed certificate

import asyncio
import pathlib
import ssl
import websockets
import time
import json

async def hello(websocket, path):
#    name = await websocket.recv()
#    print(f"< {name}")
#    greeting = f"ACK! I GOT YOUR JSON WITH KPIs"
#    await websocket.send(greeting)
#    #print(f"> {greeting}")
#    print('\n \n')
#    time.sleep(5)
    while True:
        #name = await websocket.recv()
        #print(f"< {name}")
        change = input('new interval: ')
        reg = input('registered?: ')
        send = {
                "interval": int(change),
                "registered": reg
                }
        print(send)
        #greeting = f"ACK! I GOT YOUR JSON WITH KPIs"
        await websocket.send(json.dumps(send))
        #print(f"> {greeting}")
        print('\n \n')


start_server = websockets.serve(hello, 'localhost', 8080)

asyncio.get_event_loop().run_until_complete(start_server)
asyncio.get_event_loop().run_forever()


