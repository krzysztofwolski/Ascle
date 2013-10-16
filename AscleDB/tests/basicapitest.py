import json
import requests
import logging

url = 'http://localhost:5000/api'
headers = {'Content-Type': 'application/json'}
logging.basicConfig(level=logging.WARNING)

placeholder_users = [dict(login='Zeus',
                          password="potato",
                          last_name="Greek",
                          first_name="Zeus",
                          type=0),
                     dict(login='Hera',
                          password="potato",
                          last_name="Greek",
                          first_name="Hera",
                          type=0),
                     dict(login='Dionizos',
                          password="potato",
                          last_name="Greek",
                          first_name="Dionizos",
                          type=1),
                     dict(login='Athena',
                          password="potato",
                          last_name="Greek",
                          first_name="Athena",
                          type=1)]

placeholder_drugs = [dict(name="Aspiryna", dose="5mg", user_id=1),
                     dict(name="Renigast", dose="1mg", user_id=3),
                     dict(name="Cerutin", dose="3mg", user_id=3),
                     dict(name="Vodka", dose="100ml", user_id=4),
                     dict(name="Sleep", user_id=4)]

placeholder_sensortypes = [dict(name="Termometr",
                                unit="C",
                                automatic=False),
                           dict(name="Pressure",
                                automatic=True),
                           dict(name="Weight",
                                unit="kg",
                                automatic=False)]

placeholder_sensors = [dict(sensor_type_id=0,
                            user_id=1),
                       dict(sensor_type_id=1,
                            user_id=1),
                       dict(sensor_type_id=0,
                            user_id=4),
                       dict(sensor_type_id=3,
                            user_id=4),]

placeholder_messages = [dict(sender_user_id=1,
                             receiver_user_id=2,
                             text="I just call, to say, I love youuuu!"),
                        dict(sender_user_id=3,
                             receiver_user_id=4,
                             text="Are you all right?")]

# FIXME: no idea how to deal w/ timedata. Needed in measures

# build testing set and run it on database
testing_set = [('user', placeholder_users),
               ('drug', placeholder_drugs),
               ('message', placeholder_messages),
               ('sensortype', placeholder_sensortypes),
               ('sensor', placeholder_sensors)]

for name, data in testing_set:
    # is database table empty?
    response = requests.get(url+'/'+name, headers=headers)
    assert response.status_code == 200, response
    if response.json()['num_results'] == 0:
        # let's add some!
        logging.warning("I'm addin': "+name)
        for d in data:
            response = requests.post(url+'/'+name, data=json.dumps(d), headers=headers)
            assert response.status_code == 201, response

        # chceck if all added
        response = requests.get(url+'/'+name, headers=headers)
        assert response.status_code == 200, response
        assert response.json()['num_results'] == len(data)
    else:
        logging.warning('Already in database: '+name)

# print it out~!
for name, data in testing_set:
    # Make a GET request for the entire collection.
    response = requests.get(url+'/'+name, headers=headers)
    assert response.status_code == 200
    print(json.dumps(response.json(), sort_keys=True, indent=2, separators=(',', ': ')))

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
