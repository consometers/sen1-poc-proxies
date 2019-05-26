"""
Module consumer

@author: Gregory Elléouet
"""

from sqlalchemy import func

from proxieslognact.model.outbox import Outbox
from proxieslognact.persistance.datasource import transactional


class OutboxService(object):
    
    @transactional()
    def count(self, session=None):
        """
        Compte le nombre total de outbox
        
        :param session: auto injecté par le decorator transactionel
        
        :return number
        """
        return session.query(func.count(Outbox.id)).scalar()
    
    
    @transactional(readonly = False)
    def save(self, outbox, session=None):
        """
        Enregistre un outbox. Il est créé ou modifié en fonction de son état
        
        :param outbox:
        :param session: auto injecté par le decorator transactionel
        
        :return outbox
        """
        session.add(outbox)
        return outbox
    
    
    @transactional(readonly = False)
    def delete(self, outbox, session=None):
        """
        Suppression d'une outbox
        
        :param outbox:
        :param session: auto injecté par le decorator transactionel
        
        :return outbox
        """
        session.delete(outbox)
        return outbox
    
    
    @transactional()
    def listIds(self, pagination, session=None):
        """
        Liste les outobx triées par id. La pagination est activée pour ne pas
        tout chargé d'un coup
        Seul l'id de l'objet est chargé
        
        :param pagination: dict avec offset et max
        :param session: auto injecté par le decorator transactionel
        
        :return list
        """
        return session.query(Outbox.id) \
            .order_by(Outbox.id) \
            .limit(pagination["max"]) \
            .offset(pagination["offset"]) \
            .all()
        
    
    @transactional()
    def fetchId(self, outboxId, session=None):
        """
        Charge une outbox par son id
        
        :param outboxId: id outbox
        :param session: auto injecté par le decorator transactionel
        
        :return Consumer
        """
        return session.query(Outbox) \
            .filter(Outbox.id == outboxId) \
            .one()
