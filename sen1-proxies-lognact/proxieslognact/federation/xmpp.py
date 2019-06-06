'''
Created on 24 mai 2019

@author: Gregory Elléouet
'''

from datetime import datetime
from backports.datetime_fromisoformat import MonkeyPatch

from sleekxmpp import ClientXMPP
from sleekxmpp import Message
from sleekxmpp.plugins.base import base_plugin
from sleekxmpp.xmlstream import ElementBase, ET, register_stanza_plugin
from sleekxmpp.xmlstream.handler import Callback
from sleekxmpp.xmlstream.matcher import MatchXPath

from proxieslognact.federation.protocol import FederationProtocol
from proxieslognact.federation.message import Message as FederationMessage
from proxieslognact.federation import xmpp


_all__ = ['XmppFederationProtocol', 'XmppMessagePlugin']


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
        self.messageHandler = None
        MonkeyPatch().patch_fromisoformat()
        
    
    def start(self):
        """
        Démarre le service
        """
        xmppDomain = self.configService.value("XMPP_DOMAIN_NAME")
        xmppUser = self.configService.value("XMPP_USERNAME")
        self.xmpp = ClientXMPP(xmppUser, self.configService.value("XMPP_PASSWORD"))
        
        # ajout des listeners et plugins
        self.xmpp.add_event_handler("session_start", self.session_start)
        self.xmpp.register_plugin('XmppMessagePlugin', module=xmpp)
        self.xmpp.register_handler(Callback('SEN1 Message',
                                            MatchXPath('{%s}message/{http://xmpp.rocks}Sen1Message' % self.xmpp.default_ns),
                                            self.receiveMessage))
        register_stanza_plugin(Message, XmppMessageStanza)
        
        if (not self.xmpp.connect(address = (xmppDomain, 5222))):
            raise Exception("Cannot bind XMPP session to {}".format(xmppUser))
        
        self.logger.info("Start XMPP protocol : bind session {}...".format(xmppUser))
        
        self.xmpp.process()
    
    
    def stop(self):
        """
        Stoppe le service
        """
        self.logger.info("Stop XMPP protocol...")
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
        self.logger.info("Receive SEN1 message from {}...".format(stanza['from']))
        
        try:
            message = XmppMessageStanza.parse_xmpp_message(stanza)
            self.messageHandler.handle(message)
        except Exception as ex:
            self.logger.error("Receive SEN1 message : {}".format(ex))
        
        
    def sendMessage(self, message):
        """
        Envoi d'un message
        
        :param message: Message
        """
        
        # vérifie la conformité du message avant envoi
        message.asserts()
        
        # recherche du JID de l'application destinataire
        app = self.appService.findByName(message.applicationDst)
        self.logger.info("Sending SEN1 message to {}...".format(app.jid))

        # construction d'un message
        xmppMessage = self.xmpp.make_message(app.jid)
        XmppMessageStanza.build_xmpp_message(xmppMessage, message).send()  
        

class XmppMessagePlugin(base_plugin):
    
    def plugin_init(self):
        self.description = "SEN1 Message Plugin"
    
   
        
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
            "xmlns": XmppMessageStanza.namespace,
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
    
    
    @staticmethod
    def parse_xmpp_message(stanza):
        """
        Construction d'un objet Message à partir d'un message XMPP
        
        :param xmppMessage:
        :return Message
        """
        xmppMessage = stanza['message']
        message = FederationMessage()
        
        message.username = xmppMessage.xml.findtext("{%s}username" % XmppMessageStanza.namespace)
        message.name = xmppMessage.xml.findtext("{%s}name" % XmppMessageStanza.namespace)
        message.metaname = xmppMessage.xml.findtext("{%s}metaname" % XmppMessageStanza.namespace)
        message.metavalue = xmppMessage.xml.findtext("{%s}metavalue" % XmppMessageStanza.namespace)
        message.unite = xmppMessage.xml.findtext("{%s}unite" % XmppMessageStanza.namespace)
        message.type = xmppMessage.xml.findtext("{%s}type" % XmppMessageStanza.namespace)
        message.applicationSrc = xmppMessage.xml.findtext("{%s}applicationSrc" % XmppMessageStanza.namespace)
        message.applicationDst = xmppMessage.xml.findtext("{%s}applicationDst" % XmppMessageStanza.namespace)
        
        for xmlData in xmppMessage.xml.findall("{%s}Sen1Datas/{%s}Sen1Data" % (XmppMessageStanza.namespace,
                                                                            XmppMessageStanza.namespace)):
            message.add_data(xmlData.findtext("{%s}value" % XmppMessageStanza.namespace),
                             datetime.fromisoformat(xmlData.findtext("{%s}timestamp" % XmppMessageStanza.namespace)))
        
        # validation du message avant delegation
        message.asserts()
        return message