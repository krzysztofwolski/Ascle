import flask
import flask.ext.sqlalchemy
import flask.ext.restless
from sqlalchemy import Column, Integer, String, ForeignKey, DateTime, Boolean

app = flask.Flask(__name__)
app.config['DEBUG'] = True
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///test.db'
db = flask.ext.sqlalchemy.SQLAlchemy(app)


## Models
class Member(db.Model):
    __tablename__ = 'member'
    id = db.Column(Integer, primary_key=True)
    last_name = db.Column(String(50))
    first_name = db.Column(String(120))
    email = db.Column(String(120), unique=True)

class User(db.Model):
    __tablename__ = 'user'
    id = db.Column(Integer, primary_key=True)
    login = db.Column(String(50), unique=True)
    password = db.Column(String(50))
    last_name = db.Column(String(50))
    first_name = db.Column(String(120))
    email = db.Column(String(120), unique=True)
    type = db.Column(Integer)

class Measure(db.Model):
    __tablename__ = 'measure'
    id = db.Column(Integer, primary_key=True)
    sensor_id = db.Column(Integer, ForeignKey('sensor.id'))
    value = db.Column(Integer)
    timestamp = db.Column(DateTime)

class Drug(db.Model):
    __tablename__ = 'drug'
    id = db.Column(Integer, primary_key=True)
    user_id = db.Column(Integer, ForeignKey('user.id'))
    name = db.Column(String(50))
    dose = db.Column(String(50))

class Sensor(db.Model):
    __tablename__ = 'sensor'
    id = db.Column(Integer, primary_key=True)
    name = db.Column(String(50), unique=True)
    sensor_type_id = db.Column(Integer, ForeignKey('sensortype.id'))
    alert_min = db.Column(Integer)
    alert_max = db.Column(Integer)
    warning_min = db.Column(Integer)
    warning_max = db.Column(Integer)
    user_id = db.Column(Integer, ForeignKey('user.id'))

class SensorType(db.Model):
    __tablename__ = 'sensortype'
    id = db.Column(Integer, primary_key=True)
    name = db.Column(String(50))
    unit = db.Column(String(10))
    automatic = db.Column(Boolean)


class Messages(db.Model):
    __tablename__ = 'messages'
    id = db.Column(Integer, primary_key=True)
    sender_user_id = db.Column(Integer, ForeignKey('user.id'))
    receiver_user_id = db.Column(Integer, ForeignKey('user.id'))
    text = db.Column(String(500))
    new = db.Column(Boolean)


class Schedule(db.Model):
    __tablename__ = 'schedule'
    id = db.Column(Integer, primary_key=True)
    user_id = db.Column(Integer, ForeignKey('user.id'))
    morning_dose = db.Column(Boolean)
    afternoon_dose = db.Column(Boolean)
    midnight_dose = db.Column(Boolean)
    time_interval = db.Column(Integer)
    date_start = db.Column(DateTime)
    date_stop = db.Column(DateTime)
    sensor_id = db.Column(Integer, ForeignKey('sensor.id'))
    drug_id = db.Column(Integer, ForeignKey('drug.id'))

##
db.create_all()
manager = flask.ext.restless.APIManager(app, flask_sqlalchemy_db=db)
manager.create_api(Member, methods=['GET', 'POST'])
app.run()