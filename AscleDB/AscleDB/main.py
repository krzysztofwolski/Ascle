import os
import datetime
import json
import flask
import flask.ext.sqlalchemy
import flask.ext.restless
from flask.ext.login import current_user, login_user, LoginManager, UserMixin
from sqlalchemy import Column, Integer, String, ForeignKey,\
    Date, DateTime, Boolean, Float
from sqlalchemy.orm import relationship, backref

app = flask.Flask(__name__)
app.config['DEBUG'] = True
app.config['TESTING'] = True
app.config['SECRET_KEY'] = os.urandom(24)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///test.db'
db = flask.ext.sqlalchemy.SQLAlchemy(app)

login_manager = LoginManager()
login_manager.setup_app(app)

## Models
class User(db.Model):
    __tablename__ = 'user'
    id = Column(Integer, primary_key=True)
    login = Column(String(50), unique=True, nullable=False)
    password = Column(String(50), nullable=False)
    last_name = Column(String(50), nullable=False)
    first_name = Column(String(120), nullable=False)
    sex = Column(Boolean) #
    birth_date = Column(DateTime, default=datetime.datetime.utcnow)
    type = Column(Integer, nullable=False)

    def is_authenticated(self):
        return True

    def is_active(self):
        return True

    def is_anonymous(self):
        return False

    def get_id(self):
        return unicode(self.id)

    def __repr__(self):
        return '<User %s, %s %s>' % (self.login, self.last_name, self.first_name)


class Measure(db.Model):
    __tablename__ = 'measure'
    id = Column(Integer, primary_key=True)
    sensor_id = Column(Integer, ForeignKey('sensor.id'),
                       nullable=False)
    value = Column(Float, nullable=False)
    timestamp = Column(DateTime, default=datetime.datetime.utcnow)

    def __repr__(self):
        return '<Measure from id:%s: %s at %s>' % (str(self.sensor_id),
                                                   str(self.value),
                                                   str(self.timestamp))


class Drug(db.Model):
    __tablename__ = 'drug'
    id = Column(Integer, primary_key=True)
    user_id = Column(Integer,
                     ForeignKey('user.id'),
                     nullable=False)
    name = Column(String(50), unique=True, nullable=False)
    dose = Column(String(50))

    def __repr__(self):
        return '<Drug: %s>' % (self.name)


class Sensor(db.Model):
    __tablename__ = 'sensor'
    id = Column(Integer, primary_key=True)
    #name = Column(String(50), unique=True, nullable=False)
    sensor_type_id = Column(Integer,
                            ForeignKey('sensortype.id'),
                            nullable=False)
    alert_min = Column(Integer)
    alert_max = Column(Integer)
    warning_min = Column(Integer)
    warning_max = Column(Integer)
    user_id = Column(Integer,
                     ForeignKey('user.id'),
                     nullable=False)

    def __repr__(self):
        return '<Sensor: type_id:%d user_id:%d>' % (self.sensor_type_id, self.user_id)


class SensorType(db.Model):
    __tablename__ = 'sensortype'
    id = Column(Integer, primary_key=True)
    name = Column(String(50), unique=True, nullable=False)
    unit = Column(String(10))
    automatic = Column(Boolean, nullable=False)

    def __repr__(self):
        return '<SensorType: %s %s>' % (self.name)


class Message(db.Model):
    __tablename__ = 'message'
    id = Column(Integer, primary_key=True)
    sender_user_id = Column(Integer,
                            ForeignKey('user.id'),
                            nullable=False)
    receiver_user_id = Column(Integer,
                              ForeignKey('user.id'),
                              nullable=False)
    text = Column(String(500), nullable=False)
    new = Column(Boolean, nullable=False, default=False)

    def __repr__(self):
        return '<Message: from:%d to:%d text:%s>' % (self.sender_user_id,
                                                     self.receiver_user_id,
                                                     self.text[:10])


class Schedule(db.Model):
    __tablename__ = 'schedule'
    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey('user.id'))
    morning_dose = Column(Boolean)
    afternoon_dose = Column(Boolean)
    midnight_dose = Column(Boolean)
    time_interval = Column(Integer)
    date_start = Column(Date, nullable=False)
    date_stop = Column(Date)
    sensor_id = Column(Integer, ForeignKey('sensor.id'))
    drug_id = Column(Integer, ForeignKey('drug.id'))

    def __repr__(self):
        return '<Schedule id:%d>' % (self.id)
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

## things and stuff
def auth_func(**kw):    # easy auth function.
    pass
    if not current_user.is_authenticated():
        raise flask.ext.restless.ProcessingException(message='Not authenticated!')

def now():
    return datetime.datetime.now()
##
db.create_all()
manager_api = flask.ext.restless.APIManager(app,
                                        flask_sqlalchemy_db=db)
manager_api.create_api(User, methods=['GET', 'POST'])
manager_api.create_api(Drug, methods=['GET', 'POST'])
manager_api.create_api(Message, methods=['GET', 'POST'],
                       preprocessors=dict(GET_SINGLE=[auth_func],
                                          GET_MANY=[auth_func]))
manager_api.create_api(SensorType, methods=['GET', 'POST'])
manager_api.create_api(Sensor, methods=['GET', 'POST'])
manager_api.create_api(Measure, methods=['GET', 'POST'])
manager_api.create_api(Schedule, methods=['GET', 'POST'])

app.run()
