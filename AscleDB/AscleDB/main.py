import flask
import flask.ext.sqlalchemy
import flask.ext.restless
from sqlalchemy import Column, Integer, String, ForeignKey, DateTime, Boolean
from sqlalchemy.orm import relationship, backref

app = flask.Flask(__name__)
app.config['DEBUG'] = True
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///test2.db'
db = flask.ext.sqlalchemy.SQLAlchemy(app)


## Models
class User(db.Model):
    __tablename__ = 'user'
    id = Column(Integer, primary_key=True)
    login = Column(String(50), unique=True)
    password = Column(String(50))
    last_name = Column(String(50))
    first_name = Column(String(120))
    email = Column(String(120), unique=True)
    type = Column(Integer)

class Measure(db.Model):
    __tablename__ = 'measure'
    id = Column(Integer, primary_key=True)
    sensor_id = Column(Integer, ForeignKey('sensor.id'))
    value = Column(Integer)
    timestamp = Column(DateTime)

class Drug(db.Model):
    __tablename__ = 'drug'
    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey('user.id'))
    name = Column(String(50))
    dose = Column(String(50))

class Sensor(db.Model):
    __tablename__ = 'sensor'
    id = Column(Integer, primary_key=True)
    name = Column(String(50), unique=True)
    sensor_type_id = Column(Integer, ForeignKey('sensortype.id'))
    alert_min = Column(Integer)
    alert_max = Column(Integer)
    warning_min = Column(Integer)
    warning_max = Column(Integer)
    user_id = Column(Integer, ForeignKey('user.id'))

class SensorType(db.Model):
    __tablename__ = 'sensortype'
    id = Column(Integer, primary_key=True)
    name = Column(String(50))
    unit = Column(String(10))
    automatic = Column(Boolean)


class Messages(db.Model):
    __tablename__ = 'messages'
    id = Column(Integer, primary_key=True)
    sender_user_id = Column(Integer, ForeignKey('user.id'))
    receiver_user_id = Column(Integer, ForeignKey('user.id'))
    text = Column(String(500))
    new = Column(Boolean)


class Schedule(db.Model):
    __tablename__ = 'schedule'
    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey('user.id'))
    morning_dose = Column(Boolean)
    afternoon_dose = Column(Boolean)
    midnight_dose = Column(Boolean)
    time_interval = Column(Integer)
    date_start = Column(DateTime)
    date_stop = Column(DateTime)
    sensor_id = Column(Integer, ForeignKey('sensor.id'))
    drug_id = Column(Integer, ForeignKey('drug.id'))

##
db.create_all()
manager = flask.ext.restless.APIManager(app, flask_sqlalchemy_db=db)
manager.create_api(User, methods=['GET', 'POST'])
manager.create_api(Drug, methods=['GET', 'POST'])
app.run()