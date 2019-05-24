'''
Created on 24 mai 2019

@author: Gregory Elléouet
'''

from datetime import datetime

from proxieslognact.federation.message import MessageHandler
from proxieslognact.model.outbox import Outbox


class OutboxMessageHandler(MessageHandler):
    """
    Handler pour stocker les messages dans la outbox
    """
    
    def __init__(self):
        self.outboxService = None
        self.messageSerializer = None
        
    
    def handle(self, message):
        outbox = Outbox()
        outbox.received_date = datetime.now()
        outbox.data = self.messageSerializer.write(message)
        self.outboxService.save(outbox)
    
