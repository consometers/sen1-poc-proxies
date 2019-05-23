'''
Module app

@author: Gregory Elléouet
'''

from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String


Base = declarative_base()


class App(Base):
    """
    Model App
    """
    __tablename__ = 'app'
    __table_args__ = {"schema": "proxy"}

    id = Column(Integer, primary_key=True)
    name = Column(String)
    jid = Column(String)
