'''
Module outbox

@author: Gregory Elléouet
'''

from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, DateTime, LargeBinary, Sequence


Base = declarative_base()


class Outbox(Base):
    """
    Model Outbox
    """
    __tablename__ = 'outbox'
    __table_args__ = {"schema": "proxy"}

    id = Column(Integer, Sequence("sqlachemy_sequence"), primary_key=True)
    data = Column(LargeBinary)
    received_date = Column(DateTime)
