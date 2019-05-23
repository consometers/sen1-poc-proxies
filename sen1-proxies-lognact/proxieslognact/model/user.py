'''
Module user

@author: Gregory Elléouet
'''

from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String


Base = declarative_base()


class User(Base):
    """
    Model User
    """
    __tablename__ = 'user'
    __table_args__ = {"schema": "proxy"}

    id = Column(Integer, primary_key=True)
    username = Column(String)
