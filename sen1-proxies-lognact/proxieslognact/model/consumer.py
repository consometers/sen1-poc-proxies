'''
Module consumer

@author: Gregory Elléouet
'''

from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String, Sequence, DateTime, ForeignKey
from sqlalchemy.orm import relationship

from proxieslognact.model.user_app import UserApp 
from proxieslognact.model.app import App 


Base = declarative_base()


class Consumer(Base):
    """
    Model Consumer
    """
    __tablename__ = 'consumer'
    __table_args__ = {"schema": "proxy"}

    id = Column(Integer, Sequence("sqlachemy_sequence"), primary_key=True)
    name = Column(String)
    unite = Column(String)
    metaname = Column(String)
    metavalue = Column(String)
    type = Column(String)
    date_last_value = Column(DateTime)
    user_app_id = Column(Integer, ForeignKey(UserApp.id), nullable = False)
    consumer_app_id = Column(Integer, ForeignKey(App.id), nullable = False)

    # https://docs.sqlalchemy.org/en/13/orm/loading_relationships.html#joined-eager-loading
    userApp = relationship(UserApp, innerjoin=True) # innerjoin car property non nullable
    consumerApp = relationship(App, innerjoin=True) # innerjoin car property non nullable