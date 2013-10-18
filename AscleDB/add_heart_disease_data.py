# running this script will add massive data into database
# data source: http://archive.ics.uci.edu/ml/machine-learning-databases/

import json
import requests
import glob

#
url = 'http://localhost:5000/api'
headers = {'Content-Type': 'application/json'}

def send_to_db(data=[]):
    pass

def check_sensors():
    response = requests.get(url+'/sensortype', headers=headers)
    if not response.status_code == 200:
        raise Exception('Cannot connect w/ database :(')

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

data_loader()