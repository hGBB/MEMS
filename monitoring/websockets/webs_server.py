#!/usr/bin/python3
# WSS (WS over TLS) server example, with a self-signed certificate

import asyncio
import pathlib
import ssl
import websockets

async def hello(websocket, path):
    name = await websocket.recv()
    print(f"< {name}")
    greeting = f"ACK! I GOT YOUR JSON WITH KPIs"
    await websocket.send(greeting)
    #print(f"> {greeting}")
    print('\n \n')

ssl_context = ssl.SSLContext(ssl.PROTOCOL_TLS_SERVER)
ssl_context.load_cert_chain('localhost.pem', 'key.pem')

start_server = websockets.serve(
    hello, 'localhost', 8765, ssl=ssl_context)

asyncio.get_event_loop().run_until_complete(start_server)
asyncio.get_event_loop().run_forever()


