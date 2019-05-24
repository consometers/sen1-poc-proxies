"""
Module consumer

@author: Gregory Elléouet
"""

from sqlalchemy import func
from sqlalchemy.orm import joinedload
from sqlalchemy.orm import aliased

from proxieslognact.model.consumer import Consumer
from proxieslognact.model.user_app import UserApp
from proxieslognact.persistance.datasource import transactional


class ConsumerService(object):
    
    @transactional()
    def count(self, session=None):
        """
        Compte le nombre total de consumers
        
        :param session: auto injecté par le decorator transactionel
        
        :return number
        """
        return session.query(func.count(Consumer.id)).scalar()
    
    
    @transactional(readonly = False)
    def save(self, consumer, session=None):
        """
        Enregistre un consumer. Il est créé ou modifié en fonction de son état
        
        :param consumer:
        :param session: auto injecté par le decorator transactionel
        
        :return consumer
        """
        session.add(consumer)
        return consumer
        
    
    @transactional()
    def listIds(self, pagination, session=None):
        """
        Liste les consumers triés par id. La pagination est activée pour ne pas
        tout chargé d'un coup
        Seul l'id de l'objet est chargé
        
        :param pagination: dict avec offset et max
        :param session: auto injecté par le decorator transactionel
        
        :return list
        """
        return session.query(Consumer.id) \
            .order_by(Consumer.id) \
            .limit(pagination["max"]) \
            .offset(pagination["offset"]) \
            .all()
            
    
    @transactional()
    def fetchId(self, consumerId, session=None):
        """
        Charge un consumer par son id avec ses associations
        
        :param consumerId: id du consumer
        :param session: auto injecté par le decorator transactionel
        
        :return Consumer
        """
        
        userApp = joinedload(Consumer.userApp)
        userApp.joinedload(UserApp.app)
        userApp.joinedload(UserApp.user)
        
        return session.query(Consumer) \
            .options(joinedload(Consumer.consumerApp)) \
            .options(userApp) \
            .filter(Consumer.id == consumerId) \
            .one()
            
            
