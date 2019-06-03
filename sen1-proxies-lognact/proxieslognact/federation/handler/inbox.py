'''
Created on 24 mai 2019

@author: Gregory Elléouet
'''

from datetime import datetime

from proxieslognact.federation.message import MessageHandler
from proxieslognact.model.inbox import Inbox


class InboxMessageHandler(MessageHandler):
    """
    Handler pour stocker les messages dans la inbox
    """
    
    def __init__(self):
        self.inboxService = None
        self.messageSerializer = None
        
    
    def handle(self, message):
        inbox = Inbox()
        inbox.received_date = datetime.now()
        inbox.data = self.messageSerializer.write(message)
        self.inboxService.save(inbox)
    
