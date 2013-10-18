# running this script will add massive data into database
# data source: http://archive.ics.uci.edu/ml/machine-learning-databases/

import json
import requests
import glob

#
url = 'http://localhost:5000/api'
headers = {'Content-Type': 'application/json'}



wanted_sensor_types =  {"cp": {"unit": "unit", "index_db": -1, "index": 9},
                        "trestbps": {"unit": "unit", "index_db": -1, "index": 10},
                        "chol": {"unit": "unit", "index_db": -1, "index": 12},
                        "fbs": {"unit": "unit", "index_db": -1, "index": 16},
                        "restecg": {"unit": "unit", "index_db": -1, "index": 19},
                        "thalach": {"unit": "unit", "index_db": -1, "index": 32},
                        "exang": {"unit": "unit", "index_db": -1, "index": 38},
                        "oldpeak": {"unit": "unit", "index_db": -1, "index": 40},
                        "slope": {"unit": "unit", "index_db": -1, "index": 41},
                        "ca": {"unit": "unit", "index_db": -1, "index": 44},
                        "thal": {"unit": "unit", "index_db": -1, "index": 51}}


def send_to_db(data=[]):
    pass

def check_sensors():
    response = requests.get(url+'/sensortype?results_per_page=99', headers=headers)
    if not response.status_code == 200:
        raise Exception('Cannot connect w/ database :(')

    sensor_types_database = response.json()['objects']

    for i, stype in enumerate(wanted_sensor_types):
        name, unit, index = stype
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
            assert response.status_code == 201, name
            found = response.json()

        wanted_sensor_types[i][2] = found['id']

def upload_data(data_tab):
    # let's make new user!


def data_loader():
    #
    ## test if database is running
    #response = requests.get(url+'/stats', headers=headers)
    #if not response.status_code == 200:
    #    raise Exception("Cannot connect w/ api!\nResponse code: " +
    #                    str(response.status_code))

    # get data file names
    data_files = glob.glob("./data/*.data")

    if len(data_files) < 1:
        raise Exception("Not a single datafile found!")

    # here magic starts
    for file_path in data_files:
        print file_path
        with open(file_path, 'r') as file:
            row = ""
            for i, line in enumerate(file):
                row += line
                if i > 0 and (i+1) % 10 == 0:
                    print len(row.split())
                    row = ""

check_sensors()
# data_loader()
print wanted_sensor_types