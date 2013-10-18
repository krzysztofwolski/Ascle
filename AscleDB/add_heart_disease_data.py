# running this script will add massive data into database
# data source: http://archive.ics.uci.edu/ml/machine-learning-databases/

import json
import requests
import glob
import datetime
import random

#
url = 'http://localhost:5000/api'
headers = {'Content-Type': 'application/json'}

wanted_sensor_types =  {"cp": {"unit": "unit", "index_db": -1, "index": 9-1},
                        "trestbps": {"unit": "unit", "index_db": -1, "index": 10-1},
                        "chol": {"unit": "unit", "index_db": -1, "index": 12-1},
                        "fbs": {"unit": "unit", "index_db": -1, "index": 16-1},
                        "restecg": {"unit": "unit", "index_db": -1, "index": 19-1},
                        "thalach": {"unit": "unit", "index_db": -1, "index": 32-1},
                        "exang": {"unit": "unit", "index_db": -1, "index": 38-1},
                        "oldpeak": {"unit": "unit", "index_db": -1, "index": 40-1},
                        "slope": {"unit": "unit", "index_db": -1, "index": 41-1},
                        "ca": {"unit": "unit", "index_db": -1, "index": 44-1},
                        "thal": {"unit": "unit", "index_db": -1, "index": 51-1}}


def check_sensors():
    # method chcecks if all needed sensor typer are in database. If not, the're added

    response = requests.get(url+'/sensortype?results_per_page=99', headers=headers)
    # fixme: we need better pagination query
    if not response.status_code == 200:
        raise Exception('Cannot connect w/ database :(')
    sensor_types_database = response.json()['objects']

    # for every type...
    for stype in wanted_sensor_types:
        name = stype
        unit = wanted_sensor_types[stype]['unit']
        found = None

        # is that sensor in database?
        for type in sensor_types_database:
            if type['name'] == name:
                found = type
                break

        if found is None:
            # if not, we must make it!
            new_sensor = dict(name=name, unit=unit, automatic=False)
            response = requests.post(url+'/sensortype', data=json.dumps(new_sensor), headers=headers)
            assert response.status_code == 201, response
            found = response.json()

    wanted_sensor_types[stype]['index_db'] = found['id']


def upload_data(data_tab):
    # input - data list from file

    # let's make new user!
    sex = False
    if data_tab[3] == "0":  # fixme: probably bad index
        sex = True

    # fixme: date of birth not included!
    user_dict = dict(login='Zeus'+str(random.randint(1,999999)),
                          password="potato",
                          last_name="Greek",
                          first_name="Zeus",
                          type=3)

    response = requests.post(url+'/user', data=json.dumps(user_dict), headers=headers)
    assert response.status_code == 201, response # chceck if created
    user = response.json()

    # user need sensors
    for sensortype in wanted_sensor_types:
        sensor_dict = dict(
            sensor_type_id = wanted_sensor_types[sensortype]['index_db'],
            user_id = user['id']
            )

        response = requests.post(url+'/sensor', data=json.dumps(sensor_dict), headers=headers)
        assert response.status_code == 201, response # chceck if created
        sensor_id = response.json()['id']

        # ok, now we can add measure
        measure_dict = dict(sensor_id=sensor_id,
                            value=float(data_tab[wanted_sensor_types[sensortype]['index']-1])
                           )

        response = requests.post(url+'/measure', data=json.dumps(measure_dict), headers=headers)
        assert response.status_code == 201, response # chceck if created


def data_loader():
    # test if database is running

    response = requests.get(url+'/stats', headers=headers)
    if not response.status_code == 200:
       raise Exception("Cannot connect w/ api!\nResponse code: " +
                       str(response.status_code))

    # get data file names
    data_files = glob.glob("./data/*.data")

    if len(data_files) < 1:
        raise Exception("Not a single datafile found!")

    # here magic starts
    for file_path in data_files:
        with open(file_path, 'r') as file:
            row = ""
            for i, line in enumerate(file):
                row += line
                if i > 0 and (i+1) % 10 == 0:
                    upload_data(row.split())
                    row = ""

# here we run magic
check_sensors()
data_loader()
