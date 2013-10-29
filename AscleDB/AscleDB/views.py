import datetime
import json
import flask
import flask.ext.sqlalchemy
import flask.ext.restless
from flask.ext.login import current_user, login_user, logout_user, \
    LoginManager, UserMixin, login_required

from . import app, db, login_manager
from models import *

from wtforms import Form, BooleanField, TextField, PasswordField, validators


class RegistrationForm(Form):
    username = TextField('Username', [validators.Length(min=4, max=25)])
    password = PasswordField('New Password', [
        validators.Required(),
        validators.EqualTo('confirm', message='Passwords must match')
    ])

## routes



@login_manager.user_loader
def load_user(id):
    return User.query.get(int(id))


@app.route('/login', methods=['POST'])
def login():
    form = RegistrationForm(flask.request.form)
    if form.validate():
        username = form.username.data
        password = form.password.data
        # search for users w/ submitted login/pass
        matches = User.query.filter_by(login=username,
                                       password=password).all()
        if len(matches) > 0:
            login_user(matches[0])
            return json.dumps(dict(message="Success! Logged in."))
        else:
            return json.dumps(dict(message="Wrong username or password."))


@app.route("/logout")
@login_required
def logout():
    logout_user()
    return json.dumps(dict(message="Logged out."))


@app.route('/api/stats', methods=['GET'])
def stats():
    if current_user.is_authenticated():
        return json.dumps(dict(online=True,
                               logged_in=True,
                               is_authenticated=current_user.is_authenticated(),
                               username=current_user.login,
                               type=current_user.type))
    else:
        return json.dumps(dict(online=True,
                               logged_in=False))
