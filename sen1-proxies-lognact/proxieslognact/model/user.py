'''
Module user

@author: Gregory Elléouet
'''

from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String, Sequence


Base = declarative_base()


class User(Base):
    """
    Model User
    """
    __tablename__ = 'user'
    __table_args__ = {"schema": "proxy"}

    id = Column(Integer, Sequence("sqlachemy_sequence"), primary_key=True)
    username = Column(String)
