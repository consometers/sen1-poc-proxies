"""
Module consumer

@author: Gregory Elléouet
"""

from sqlalchemy import func

from proxieslognact.model.inbox import Inbox
from proxieslognact.persistance.datasource import transactional


class InboxService(object):
    
    @transactional()
    def count(self, session=None):
        """
        Compte le nombre total
        
        :param session: auto injecté par le decorator transactionel
        
        :return number
        """
        return session.query(func.count(Inbox.id)).scalar()
    
    
    @transactional(readonly = False)
    def save(self, inbox, session=None):
        """
        Enregistre un inbox. Il est créé ou modifié en fonction de son état
        
        :param inbox:
        :param session: auto injecté par le decorator transactionel
        
        :return inbox
        """
        session.add(inbox)
        return inbox
    
    
    @transactional(readonly = False)
    def delete(self, inbox, session=None):
        """
        Suppression d'une inbox
        
        :param inbox:
        :param session: auto injecté par le decorator transactionel
        
        :return inbox
        """
        session.delete(inbox)
        return inbox
    
    
    @transactional()
    def listIds(self, pagination, session=None):
        """
        Liste les inbox triées par id. La pagination est activée pour ne pas
        tout chargé d'un coup
        Seul l'id de l'objet est chargé
        
        :param pagination: dict avec offset et max
        :param session: auto injecté par le decorator transactionel
        
        :return list
        """
        return session.query(Inbox.id) \
            .order_by(Inbox.id) \
            .limit(pagination["max"]) \
            .offset(pagination["offset"]) \
            .all()
        
    
    @transactional()
    def fetchId(self, inboxId, session=None):
        """
        Charge une inbox par son id
        
        :param inboxId: id outbox
        :param session: auto injecté par le decorator transactionel
        
        :return Consumer
        """
        return session.query(Inbox) \
            .filter(Inbox.id == inboxId) \
            .one()
