'''
Module user_app

@author: Gregory Elléouet
'''

from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String, ForeignKey, Sequence
from sqlalchemy.orm import relationship

from proxieslognact.model.user import User 
from proxieslognact.model.app import App 

Base = declarative_base()


class UserApp(Base):
    """
    Model UserApp
    """
    __tablename__ = 'user_app'
    __table_args__ = {"schema": "proxy"}

    id = Column(Integer, Sequence("sqlachemy_sequence"), primary_key=True)
    token = Column(String)
    user_id = Column(Integer, ForeignKey(User.id), nullable = False)
    app_id = Column(Integer, ForeignKey(App.id), nullable = False)

    # https://docs.sqlalchemy.org/en/13/orm/loading_relationships.html#joined-eager-loading
    user = relationship(User, innerjoin=True) # innerjoin car property non nullable
    app = relationship(App, innerjoin=True) # innerjoin car property non nullable