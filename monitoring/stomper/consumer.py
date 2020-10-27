from stompest.config import StompConfig
from stompest.protocol import StompSpec
from stompest.sync import Stomp
import json

CONFIG = StompConfig('tcp://172.17.0.2:61613')
QUEUE = '/queue/test'

if __name__ == '__main__':
    client = Stomp(CONFIG)
    client.connect()
    client.subscribe(QUEUE, {StompSpec.ACK_HEADER: StompSpec.ACK_CLIENT_INDIVIDUAL})
    while True:
        frame = client.receiveFrame()
        #print('Got %s' % frame.info())
        sample = frame.body.decode("utf-8")
        j = json.loads(sample)
        print(j["kpis"])
        print("\n \n")
        client.ack(frame)
    client.disconnect()
