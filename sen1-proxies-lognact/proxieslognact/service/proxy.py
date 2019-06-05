"""
Module proxy

@author: Gregory Elléouet
"""

from datetime import datetime
from datetime import timedelta

from proxieslognact import settings
from proxieslognact.api.lognact import FetchData, PushData
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
        self.configService = None
        self.userAppService = None
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
        
        # la connexion au system se fait via une paire login/password
        # c'est enregistré sous la forme user:password dans le champ token
        tokens = consumer.userApp.token.split(":")

        # construit un objet pour requêter les données
        command = Builder(FetchData) \
            .user(tokens[0]) \
            .password(tokens[1]) \
            .itemIds(consumer.metaname) \
            .dateEnd(datetime.now()) \
            .build()
            
        # aucune extraction n'a été faite pour l'instant, on récupère un nombre de values
        if (consumer.date_last_value == None):
            command.limit = settings["proxy"]["firstMaxValue"]
        else:
            #  dateStart est inclusif, il faut au moins incrémenter d'une seconde
            # pour ne pas charger des données en doublon
            command.dateStart = consumer.date_last_value + timedelta(seconds = 1)

        datas = self.lognact.fetch_data(command)
        
        if (datas and len(datas)):
            # construction du message au format fédération
            # !! certaines données changent de noms entre la source et la dest
            message = Builder(Message) \
                .username(consumer.userApp.user.username) \
                .applicationDst(consumer.consumerApp.name) \
                .applicationSrc(consumer.userApp.app.name) \
                .name(consumer.consumer_name) \
                .metaname(consumer.consumer_metaname) \
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
        
        
    @transactional(readonly = False)
    def push_inbox_data(self, inboxId, session=None):
        """
        Charge les datas d'une inbox et les envoit sur le système local
        Les datas sont supprimées si l'envoit s'est bien passé
        
        :param inboxId: id inbox
        :param session: auto injecté par le decorator transactional
        """
        inbox = self.inboxService.fetchId(inboxId)
        
        # deserialise le message depuis les datas de la inbox
        # et vérifie sa conformité
        message = self.messageSerializer.read(inbox.data)
        message.asserts()
        
        # recherche des infos UserApp en fonction application et user
        userApp = self.userAppService.findByUserAndApplication(message.username, message.applicationSrc)
        
        # la connexion au system se fait via une paire login/password
        # c'est enregistré sous la forme user:password dans le champ token
        tokens = userApp.token.split(":")
        
        # construit un objet pour requêter les données
        command = Builder(PushData) \
            .user(tokens[0]) \
            .password(tokens[1]) \
            .hostname(message.name) \
            .itemKey(message.metaname) \
            .datas(message.datas) \
            .build()
            
        self.lognact.push_data(command)
        
        # si aucune erreur, la inbox est supprimée
        self.inboxService.delete(inbox)
        
