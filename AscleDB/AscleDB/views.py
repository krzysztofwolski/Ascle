import datetime
import json
import flask
import flask.ext.sqlalchemy
import flask.ext.restless
from flask.ext.login import current_user, login_user, LoginManager, UserMixin

from . import app, db, login_manager
from models import *

## routes


@login_manager.user_loader
def load_user(id):
    return User.query.get(int(id))


@app.route('/login', methods=['POST'])
def login():
    username = flask.request.form['login']
    password = flask.request.form['password']

    # search for users w/ submitted login/pass
    matches = User.query.filter_by(login=username,
                                   password=password).all()
    if len(matches) > 0:
        login_user(matches[0])
        return "Gratz! You're logged in!"
    else:
        return "Nope nope nope. Bad login/pass."


@app.route('/api/stats', methods=['GET'])
def stats():
    return json.dumps(dict(online=True))
