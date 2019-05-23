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

    id = Column(Integer, primary_key=True, Sequence("sqlachemy_sequence"))
    data = Column(LargeBinary)
    received_date = Column(DateTime)
