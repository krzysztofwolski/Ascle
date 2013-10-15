import flask
import flask.ext.sqlalchemy
import flask.ext.restless
from sqlalchemy import Column, Integer, String, ForeignKey,\
    Date, DateTime, Boolean, Float
from sqlalchemy.orm import relationship, backref

app = flask.Flask(__name__)
app.config['DEBUG'] = True
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///test2.db'
db = flask.ext.sqlalchemy.SQLAlchemy(app)


## Models
class User(db.Model):
    __tablename__ = 'user'
    id = Column(Integer, primary_key=True)
    login = Column(String(50), unique=True, nullable=False)
    password = Column(String(50), nullable=False)
    last_name = Column(String(50), nullable=False)
    first_name = Column(String(120), nullable=False)
    type = Column(Integer, nullable=False)


class Measure(db.Model):
    __tablename__ = 'measure'
    id = Column(Integer, primary_key=True)
    sensor_id = Column(Integer, ForeignKey('sensor.id'),
                       nullable=False)
    value = Column(Float, nullable=False)
    timestamp = Column(DateTime)


class Drug(db.Model):
    __tablename__ = 'drug'
    id = Column(Integer, primary_key=True)
    user_id = Column(Integer,
                     ForeignKey('user.id'),
                     nullable=False)
    name = Column(String(50), unique=True, nullable=False)
    dose = Column(String(50))


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


class SensorType(db.Model):
    __tablename__ = 'sensortype'
    id = Column(Integer, primary_key=True)
    name = Column(String(50), unique=True, nullable=False)
    unit = Column(String(10))
    automatic = Column(Boolean, nullable=False)


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

##
db.create_all()
manager = flask.ext.restless.APIManager(app,
                                        flask_sqlalchemy_db=db)
manager.create_api(User, methods=['GET', 'POST'])
manager.create_api(Drug, methods=['GET', 'POST'])
manager.create_api(Message, methods=['GET', 'POST'])
manager.create_api(SensorType, methods=['GET', 'POST'])
manager.create_api(Sensor, methods=['GET', 'POST'])
manager.create_api(Measure, methods=['GET', 'POST'])
manager.create_api(Schedule, methods=['GET', 'POST'])

app.run()