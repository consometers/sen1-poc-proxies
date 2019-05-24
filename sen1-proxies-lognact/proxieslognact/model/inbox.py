'''
Module inbox

@author: Gregory Elléouet
'''

from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, DateTime, LargeBinary, Sequence


Base = declarative_base()


class Inbox(Base):
    """
    Model Inbox
    """
    __tablename__ = 'inbox'
    __table_args__ = {"schema": "proxy"}

    id = Column(Integer, Sequence("sqlachemy_sequence"), primary_key=True)
    data = Column(LargeBinary)
    received_date = Column(DateTime)
