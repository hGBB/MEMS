import asyncio
import json
import websockets

kpi = {
        'uuid' : 'lol',
        'node_name' : 'Node1',
        'interval' : 10,
        'kpis' : [
            {'name' : 'cpu', 'value' : 12}
            ]
        }

async def test():
    async with websockets.connect('ws://localhost:8080/mon') as ws:
        await ws.send(json.dumps(kpi))
        res = await ws.recv()


asyncio.get_event_loop().run_until_complete(test())
