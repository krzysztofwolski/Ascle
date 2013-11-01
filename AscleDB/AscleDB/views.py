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



class LoginForm(Form):
    username = TextField('username', validators = [validators.Required()])
    password = TextField('password', validators = [validators.Required()])



@login_manager.user_loader
def load_user(id):
    return User.query.get(int(id))


@app.route('/login', methods=['POST'])
def login():

    form = LoginForm(flask.request.form)
    if form.validate():

        username = form.username.data
        password = form.password.data
        print username, password
        # search for users w/ submitted login/pass
        matches = User.query.filter_by(login=username,
                                       password=password).all()
        if len(matches) > 0:
            login_user(matches[0])
            return json.dumps(dict(message="Success! Logged in."))
        else:
            return json.dumps(dict(message="Wrong username or password."))
    else:
        return json.dumps(dict(message="Enter both username and password!"))


@app.route("/logout", methods=['GET'])
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
                               type=current_user.type,
                               user_id=current_user.id))
    else:
        return json.dumps(dict(online=True,
                               logged_in=False))
