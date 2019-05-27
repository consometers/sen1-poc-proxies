'''
Created on 24 mai 2019

@author: Gregory Elléouet
'''

from sleekxmpp import ClientXMPP
from sleekxmpp import Message
from sleekxmpp.xmlstream import ElementBase, ET, register_stanza_plugin

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
        
        # ajout du plugin pour traiter les stanzas
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
        self.logger.info(f"Sending XMPP message to {app.jid}...")

        # construction d'un message
        xmppMessage = self.xmpp.make_message(app.jid)
        XmppMessageStanza.build_xmpp_message(xmppMessage, message).send()  
        
        
        
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
    
    
    @staticmethod
    def build_xmpp_message(xmppMessage, message):
        """
        Construction d'un stanza à partir d'un objet Message
        
        :param xmppMessage: Message
        :param message: Message
        """
        xmlMessage = ET.Element("Sen1Message", {
            "xmlns": "http://xmpp.rocks",
            "xmlns:xsi": "http://www.w3.org/2001/XMLSchema-instance",
            "xmlns:xs": "http://www.w3.org/2001/XMLSchema"
        })
        xmppMessage.append(xmlMessage)
        
        ET.SubElement(xmlMessage, "username").text = message.username
        ET.SubElement(xmlMessage, "name").text = message.name
        ET.SubElement(xmlMessage, "metaname").text = message.metaname
        ET.SubElement(xmlMessage, "metavalue").text = message.metavalue
        ET.SubElement(xmlMessage, "unite").text = message.unite
        ET.SubElement(xmlMessage, "type").text = message.type
        ET.SubElement(xmlMessage, "applicationSrc").text = message.applicationSrc
        ET.SubElement(xmlMessage, "applicationDst").text = message.applicationDst
        
        # ajout des datas
        xmlDatas = ET.SubElement(xmlMessage, "Sen1Datas")
        
        for data in message.datas:
            xmlData = ET.SubElement(xmlDatas, "Sen1Data")
            ET.SubElement(xmlData, "value", {"xsi:type": "xs:string"}).text = data.value
            ET.SubElement(xmlData, "timestamp").text = data.timestamp.strftime("%Y-%m-%dT%H:%M:%S") 
        
        return xmppMessage