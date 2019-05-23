'''
Module config

@author: Gregory Elléouet
'''

from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String


Base = declarative_base()


class Config(Base):
    """
    Model Config
    """
    __tablename__ = 'config'
    __table_args__ = {"schema": "proxy"}

    id = Column(Integer, primary_key=True)
    name = Column(String)
    value = Column(String)
