from sqlalchemy import Column, Integer, String

class Member(db.Model):
   __tablename__ = 'member'
   id = Column(Integer, primary_key=True)
   last_name = Column(String(50))
   first_name = Column(String(120))
   email = Column(String(120), unique=True)

   def __repr__(self):
      return '%s' % (self.last_name)