import json
import requests

url = 'http://127.0.0.1:5000/api'
headers = {'Content-Type': 'application/json'}

placeholder_users = [dict(login='Zeus', type=0),
                     dict(login='Hera', type=0),
                     dict(login='Dionizes', type=1),
                     dict(login='Atena', type=1)]

placeholder_drugs = [dict(name="Aspiryna", dose="5mg", user_id=3),
                     dict(name="Renigast", dose="1mg", user_id=3),
                     dict(name="Cerutin", dose="3mg", user_id=3),
                     dict(name="Vodka", dose="100ml", user_id=4),
                     dict(name="Sleep", dose="8h", user_id=4)]

#users in database?
response = requests.get(url+'/user', headers=headers)
assert response.status_code == 200
if response.json()['num_results'] == 0:
    # let's add some users!
    for data in placeholder_users:
        response = requests.post(url+'/user', data=json.dumps(data), headers=headers)
        assert response.status_code == 201

    # chceck if all added
    response = requests.get(url+'/user', headers=headers)
    assert response.status_code == 200
    assert response.json()['num_results'] == len(placeholder_users)

# drugs in database?
response = requests.get(url+'/drug', headers=headers)
assert response.status_code == 200
if response.json()['num_results'] == 0:
    # let's add some drugs!
    for data in placeholder_drugs:
        response = requests.post(url+'/drug', data=json.dumps(data), headers=headers)
        assert response.status_code == 201

    # chceck if all added
    response = requests.get(url+'/drug', headers=headers)
    assert response.status_code == 200
    assert response.json()['num_results'] == len(placeholder_drugs)


## Make a GET request for the entire collection.
#response = requests.get(url+'/user', headers=headers)
#assert response.status_code == 200
#print(json.dumps(response.json(), sort_keys=True, indent=2, separators=(',', ': ')))
#
## Make a GET request for an individual instance of the model.
#response = requests.get(url + '/1', headers=headers)
#assert response.status_code == 200
#print(response.json())
#
## Use query parameters to make a search. Make sure to convert the value of `q`
## to a string.
#filters = [dict(name='name', op='like', val='%y%')]
#params = dict(q=json.dumps(dict(filters=filters)))
#response = requests.get(url, params=params, headers=headers)
#assert response.status_code == 200
#print(response.json())
