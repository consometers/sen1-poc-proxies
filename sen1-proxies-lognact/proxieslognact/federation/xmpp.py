'''
Created on 24 mai 2019

@author: Gregory Elléouet
'''

from sleekxmpp import ClientXMPP


class XmppFederationProtocol(object):
    """
    Gestion des échanges sur le réseau fédéré avec le protocole XMPP
    
    Ecoute et envoit  les messages 
    """
    
    def __init__(self):
        """
        PostConstruct
        """
        self.configService = None
        self.xmpp = None
        
    
    def start(self):
        """
        Démarre le service
        """
        self.xmpp = ClientXMPP(self.configService.value("XMPP_USERNAME"),
                               self.configService.value("XMPP_PASSWORD"))
        self.xmpp.connect()
    
    
    def stop(self):
        """
        Stoppe le service
        """
        pass