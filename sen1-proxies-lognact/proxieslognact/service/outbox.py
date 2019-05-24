"""
Module consumer

@author: Gregory Elléouet
"""

from proxieslognact.model.outbox import Outbox
from proxieslognact.persistance.datasource import transactional


class OutboxService(object):
    
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
        
    
