'''
Module config

@author: Gregory Elléouet
'''

from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String, Sequence


Base = declarative_base()


class Config(Base):
    """
    Model Config
    """
    __tablename__ = 'config'
    __table_args__ = {"schema": "proxy"}

    id = Column(Integer, Sequence("sqlachemy_sequence"), primary_key=True)
    name = Column(String)
    value = Column(String)
