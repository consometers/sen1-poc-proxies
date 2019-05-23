'''
Module consumer

@author: Gregory Elléouet
'''

from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String


Base = declarative_base()


class Consumer(Base):
    """
    Model Consumer
    """
    __tablename__ = 'consumer'
    __table_args__ = {"schema": "proxy"}

    id = Column(Integer, primary_key=True)
    name = Column(String)
    value = Column(String)
