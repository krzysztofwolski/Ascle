import json
import datetime
import flask
import flask.ext.sqlalchemy
import flask.ext.restless
from flask.ext.login import current_user, login_user, LoginManager, UserMixin


def prettyJson(data={}):
    return json.dumps(data, sort_keys=True,indent=2, separators=(',', ': '))


def now():
    return datetime.datetime.now()


def auth_func(**kw):    # easy auth function.
    if not current_user.is_authenticated():
        raise flask.ext.restless.ProcessingException(message='Not authenticated!')