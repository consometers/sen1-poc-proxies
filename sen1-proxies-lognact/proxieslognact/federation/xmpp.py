'''
Created on 24 mai 2019

@author: Gregory Elléouet
'''

from sleekxmpp import ClientXMPP
from sleekxmpp import Message
from sleekxmpp.xmlstream import ElementBase, register_stanza_plugin

from proxieslognact.federation.protocol import FederationProtocol


_all__ = ['XmppFederationProtocol']


class XmppFederationProtocol(FederationProtocol):
    """
    Gestion des échanges sur le réseau fédéré avec le protocole XMPP
    
    Implémentation XMPP du FederationProtocol 
    """
    
    def __init__(self):
        """
        PostConstruct
        """
        self.configService = None
        self.appService = None
        self.xmpp = None
        
    
    def start(self):
        """
        Démarre le service
        """
        xmppDomain = self.configService.value("XMPP_DOMAIN_NAME")
        self.logger.info(f"Start XMPP protocol : try connecting {xmppDomain}...")
        self.xmpp = ClientXMPP(self.configService.value("XMPP_USERNAME"), self.configService.value("XMPP_PASSWORD"))
        
        # ajout des listeners
        self.xmpp.add_event_handler("session_start", self.session_start)
        self.xmpp.add_event_handler("message", self.receiveMessage)
        
        # ajout du plugin pour taiter les stanzas
        register_stanza_plugin(Message, XmppMessageStanza)
        
        if (not self.xmpp.connect(address = (xmppDomain, 5222))):
            raise Exception(f"Cannot connect to {xmppDomain}")
        
        self.xmpp.process()
    
    
    def stop(self):
        """
        Stoppe le service
        """
        self.logger.info(f"Stop XMPP protocol...")
        self.xmpp.disconnect()
        
    
    def session_start(self, event):
        """
        
        """
        self.logger.info("XMPP session is started")
        self.xmpp.send_presence()
        self.xmpp.get_roster()
        
    
    def receiveMessage(self, stanza):
        """
        Réception d'un message XMPP
        
        :param stanza: xmpp message
        """
        self.logger.debug(f"Receive stanza : {stanza['message']}")
        
        
    def sendMessage(self, message):
        """
        Envoi d'un message
        
        :param message: Message
        """
        
        # vérifie la conformité du message avant envoi
        message.asserts()
        
        # recherche du JID de l'application destinataire
        app = self.appService.findByName(message.applicationDst)
        
        self.logger.debug(f"Sending XMPP message to {app.jid}...")
        
        self.xmpp.send_message(app.jid, message)
        
        
        
class XmppMessage(object):
    
    def __init__(self, message):
        """
        Construction d'un XMPP message à partir d'un Message
        
        :param message: Message
        """
        self.
     
     
class XmppMessageStanza(ElementBase):
    #: The `name` field refers to the basic XML tag name of the stanza
    name = "Sen1Message"
    
    #: The namespace of the main XML tag.
    namespace = 'http://xmpp.rocks'
    
    #: The `plugin_attrib` value is the name that can be used
    #: with a parent stanza to access this stanza. For example
    #: from an Iq stanza object, accessing:
    #: 
    #:     iq['action']
    #: 
    #: would reference an Action object, and will even create
    #: an Action object and append it to the Iq stanza if
    #: one doesn't already exist.
    plugin_attrib = 'message'
    
    #: Stanza objects expose dictionary-like interfaces for
    #: accessing and manipulating substanzas and other values.
    #: The set of interfaces defined here are the names of
    #: these dictionary-like interfaces provided by this stanza
    #: type. For example, an Action stanza object can use:
    #:
    #:     action['method'] = 'foo'
    #:     print(action['param'])
    #:     del action['status']
    #:
    #: to set, get, or remove its values.
    interfaces = set(('username', 'name', 'metaname', 'unite', 'type', 'applicationSrc', 'applicationDst', 'Sen1Datas'))