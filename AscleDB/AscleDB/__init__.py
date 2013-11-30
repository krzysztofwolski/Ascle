"""
    AscleDB
    ~~~~~~~~

    AscleDB application package
"""

import os
import datetime
import json
import flask
import flask.ext.sqlalchemy
import flask.ext.restless
from flask.ext.login import current_user, login_user, LoginManager, UserMixin
from sqlalchemy import Column, Integer, String, ForeignKey,\
    Date, DateTime, Boolean, Float

import settings
from helpers import *

app = flask.Flask(__name__)
app.config.update(settings.APP)
db = flask.ext.sqlalchemy.SQLAlchemy(app)

login_manager = LoginManager()
login_manager.setup_app(app)

import views, models

db.create_all()

methods = ['GET', 'POST', 'PUT', "DELETE"]

# in case of auth: preprocessors=dict(GET_SINGLE=[auth_func],
#                                     GET_MANY=[auth_func]))

manager_api = flask.ext.restless.APIManager(app,
                                        flask_sqlalchemy_db=db)

manager_api.create_api(models.User, methods=methods,
                       preprocessors=dict(GET_SINGLE=[auth_func, get_single_chceck_user],
                                          GET_MANY=[auth_func, get_many_chceck_user],
                                          DELETE=[auth_func]))
manager_api.create_api(models.Drug, methods=methods,
                       preprocessors=dict(GET_SINGLE=[auth_func],
                                          GET_MANY=[auth_func],
                                          POST=[auth_func],
                                          DELETE=[auth_func]))
manager_api.create_api(models.Message, methods=methods,
                       preprocessors=dict(GET_SINGLE=[auth_func],
                                          GET_MANY=[auth_func],
                                          POST=[auth_func],
                                          DELETE=[auth_func]))
manager_api.create_api(models.SensorType, methods=methods,
                       preprocessors=dict(GET_SINGLE=[auth_func],
                                          GET_MANY=[auth_func],
                                          POST=[auth_func],
                                          DELETE=[auth_func]))
manager_api.create_api(models.Sensor, methods=methods,
                       preprocessors=dict(GET_SINGLE=[auth_func],
                                          GET_MANY=[auth_func],
                                          POST=[auth_func],
                                          DELETE=[auth_func]))
manager_api.create_api(models.Measure, methods=methods,
                       preprocessors=dict(GET_SINGLE=[auth_func],
                                          GET_MANY=[auth_func],
                                          POST=[auth_func],
                                          DELETE=[auth_func]),
                       postprocessors=dict(GET_MANY=[calculate_avg,
                                                     calculate_max,
                                                     calculate_med,
                                                     calculate_min]))
manager_api.create_api(models.Schedule, methods=methods,
                       preprocessors=dict(GET_SINGLE=[auth_func],
                                          GET_MANY=[auth_func],
                                          POST=[auth_func],
                                          DELETE=[auth_func]))