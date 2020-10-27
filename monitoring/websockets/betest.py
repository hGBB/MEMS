import asyncio

import websockets


async def test():
    async with websockets.connect('ws://localhost:8080/mon') as ws:
        await ws.send('{"uuid": "lol", "node_name": "test"}')
        res = await ws.recv()


asyncio.get_event_loop().run_until_complete(test())
