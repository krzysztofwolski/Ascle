# -*- coding: utf-8 -*-
# running this script will add massive data into database
# data source: http://archive.ics.uci.edu/ml/machine-learning-databases/

import json
import requests
import glob
import random

#
url = 'http://localhost:5000/api'
headers = {'Content-Type': 'application/json'}

f_names = []
m_names = []
l_names = []

wanted_sensor_types =  {u"Ból klatki piersiowej": {"unit": "Id", "index_db": -1, "index": 9-1},
                        u"Ciśnienie krwi - spoczynek": {"unit": "mm/Hg", "index_db": -1, "index": 10-1},
                        u"Cholesterol": {"unit": "mg/dl", "index_db": -1, "index": 12-1},
                        u"Wysoki poziom cukru": {"unit": "Boolean", "index_db": -1, "index": 16-1},
                        u"Elektrokardiograf": {"unit": "Id", "index_db": -1, "index": 19-1},
                        u"Max. puls": {"unit": "unit", "index_db": -1, "index": 32-1},
                        u"Przebyta angina": {"unit": "Boolean", "index_db": -1, "index": 38-1},
                        u"Obniżenie ST": {"unit": "unit", "index_db": -1, "index": 40-1},
                        u"Opad ST": {"unit": "Id", "index_db": -1, "index": 41-1},
                        u"Naczynek na fluorosopii": {"unit": "Szt.", "index_db": -1, "index": 44-1},
                        u"Thal": {"unit": "Id", "index_db": -1, "index": 51-1}}


def check_sensors():
    # method checks if all needed sensor types are in database. If not, they're added

    response = requests.get(url+'/sensortype?results_per_page=999', headers=headers)
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
    # input - data row from file

    # let's make new user!
    sex = False
    if data_tab[3] == "1":
        sex = True

    birth_date = "%d-%d-%dT01:30:55.752000" % (2013-int(data_tab[2]),
                                              random.randint(1, 12),
                                              random.randint(1, 25))

    user_dict = generate_user(sex, birth_date)
    # user_dict =  dict(login='HD'+str(random.randint(11, 99)),
    #                   password="potato",
    #                   last_name="Nazwisko",
    #                   first_name="Imie",
    #                   type=3,
    #                   sex=sex,
    #                   birth_date=birth_date)

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

def load_names():
    f_names = []
    m_names = []
    l_names = []
    with open('./data/imionakobiece.txt', 'r') as f:
        f_names = f.readlines()

    with open('./data/imionameskie.txt', 'r') as f:
        m_names = f.readlines()

    with open('./data/nazwiska.txt', 'r') as f:
        l_names = f.readlines()

    return f_names, m_names, l_names


def generate_user(sex=None, birth_date=None, login=None, password = "potato", type=3):
    if sex is None:
        sex = random.choice([True, False])
    name = ""

    if sex: # if male...
        name = random.choice(m_names).strip()
    else:
        name = random.choice(f_names).strip()

    last_name = random.choice(l_names).strip()

    if birth_date is None:
        birth_date = "%d-0%d-%dT%d:%d:%d.000000" %\
                     (random.randint(11, 20),
                      random.randint(2, 9),
                      random.randint(1910, 2000),
                      random.randint(11, 20),
                      random.randint(11, 50),
                      random.randint(11, 59))

    if login is None:
        login = name+last_name+str(random.randint(1,9))

    return dict(login=login,
                password=password,
                type=type,
                sex=sex,
                first_name=name,
                last_name=last_name,
                birth_date=birth_date)


def generate_users():
    # lets generate 1 admin & 5 doctors & 1 patient
    users = []#generate_user(type=1, login="doc%d"%i, password="doc") for i in xrange(5)]
    # users.append(generate_user(type =0, login="admin", password="admin"))
    users.append(generate_user(type =3, login="patient", password="patient"))

    for u in users:
        response = requests.post(url+'/user', data=json.dumps(u), headers=headers)
        assert response.status_code == 201, response # chceck if created

# here we run magic
f_names, m_names, l_names = load_names()

# check_sensors()
# data_loader()
generate_users()