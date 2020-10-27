import requests

BASE = 'https://localhost:8443'
HEADERS = {'Node-Authorization': 'asdf'}

req = requests.get(BASE + '/api/nodes/configuration', headers=HEADERS, verify='ca.crt')
print(req.json())
