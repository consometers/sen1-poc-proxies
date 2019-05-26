"""
Module proxy

@author: Gregory Elléouet
"""

from proxieslognact.persistance.datasource import transactional
from proxieslognact.util.builder import Builder
from proxieslognact.federation.message import Message


class ProxyService(object):
    
    def __init__(self):
        """
        PostConstruct
        """
        self.lognact = None
        self.consumerService = None
        self.messageSerializer = None
        self.outboxService = None
        self.federationProtocol = None
    
    
    @transactional(readonly = False)
    def handle_consumer_data(self, consumerId, messageHandler, session=None):
        """
        Fetch les dernières données d'un consumer et les transforme en message
        au format du réseau fédéré
        ce message est ensuite délégué à un handler pour sa prise en charge (le
        handler peut faire un envoi direct, ou un stockage intermédiaire en base, etc)
        
        :param consumerId: l'id du consumer
        :param messageHandler: le gestionnaire de message
        :param session: auto injecté par le decorator transactional
        """
        consumer = self.consumerService.fetchId(consumerId)
        
        datas = self.lognact.fetch_consumer_data(consumer)
        
        if (datas and len(datas)):
            # construction du message au format fédération
            message = Builder(Message) \
                .username(consumer.userApp.user.username) \
                .applicationDst(consumer.consumerApp.name) \
                .applicationSrc(consumer.userApp.app.name) \
                .name(consumer.name) \
                .metaname(consumer.metaname) \
                .metavalue(consumer.metavalue) \
                .unite(consumer.unite) \
                .type(consumer.type) \
                .build()
            
            for data in datas:
                message.add_data(self.lognact.datapoint_value(data),
                                 self.lognact.datapoint_timestamp(data))
                
            # verifie que le message est conforme avant de continuer
            message.asserts()
            
            # le handler prend le relai pour traiter le message
            messageHandler.handle(message)
            
            # si aucune erreur pendant tout le process, on peut flagguer le consumer
            # avec la date de la dernière valeur extraite
            consumer.date_last_value = message.dateLastValue()
            self.consumerService.save(consumer)
        
    
    @transactional(readonly = False)
    def federate_outbox_data(self, outboxId, session=None):
        """
        Charge les datas d'une outbox et les envoit sur le réseau fédéré
        Les datas sont supprimées si l'envoit s'est bien passé
        
        :param outboxId: id oubox
        :param session: auto injecté par le decorator transactional
        """
        outbox = self.outboxService.fetchId(outboxId)
        
        # deserialise le message depuis les datas de la outbox
        # et vérifie sa conformité
        message = self.messageSerializer.read(outbox.data)
        
        # envoi du message
        self.federationProtocol.sendMessage(message)
        
        # si aucune erreur, la outbox est supprimée
        self.outboxService.delete(outbox)