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


def get_single_chceck_user(instance_id=None, **kw):
    if instance_id is None or not current_user.is_authenticated():
        raise flask.ext.restless.ProcessingException(message='Not authenticated!')
    elif int(instance_id) == int(current_user.id):
        pass # all is k!
    elif current_user.type < 3:
        # if admin or doctor
        pass # all is still ok!
    else:
        raise flask.ext.restless.ProcessingException(message='You can only get your own data!')

def get_many_chceck_user(instance_id=None, **kw):
    if not current_user.is_authenticated():
        raise flask.ext.restless.ProcessingException(message='Not authenticated!')
    elif int(current_user.type) > 2:
        raise flask.ext.restless.ProcessingException(message='You can only get your own data!')


def calculate_avg(result=None,**kw):
    if result['num_results'] > 0:
        results = [obj['value'] for obj in result['objects']]
        result['average'] = sum(results) / len(results)

def calculate_med(result=None,**kw):
    if result['num_results'] > 0:
        results = [obj['value'] for obj in result['objects']]
        sorts = sorted(results)
        length = len(sorts)
        if not length % 2:
            result['median'] = (sorts[length / 2] + sorts[length / 2 - 1]) / 2.0
            return
        result['median'] = sorts[length / 2]
        return

def calculate_min(result=None,**kw):
    if result['num_results'] > 0:
        results = [obj['value'] for obj in result['objects']]
        result['minimum'] = min(results)

def calculate_max(result=None,**kw):
    if result['num_results'] > 0:
        results = [obj['value'] for obj in result['objects']]
        result['maximum'] = max(results)