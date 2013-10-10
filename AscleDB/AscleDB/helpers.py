import json

def prettyJson(data={}):
    return json.dumps(data, sort_keys=True,indent=2, separators=(',', ': '))