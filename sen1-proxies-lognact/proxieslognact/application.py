# -*- coding: utf-8 -*-

'''
Module application

Contexte applicatif
Déclare les objets de l'application et les créé dynamiquement à la demande
Gère aussi la configuration globale du projet
Ceci permet aussi d'éviter des dépendances cycliques entre module car les modules sont
importées dynamiquement

@author: Gregory Elléouet
'''

import os
import logging
from importlib import import_module


__all__ = ['ApplicationContext', 'applicationContext']


class ApplicationContext(object):
    """
    Contexte applicatif
    Instancie les beans à la demande à partir du dictionnaire beans
    """
    def __init__(self):
        """
        Constructor
        """
        self.instances = {}
        
        if "PROXY_ENV" in os.environ:
            self.environnement = os.environ["PROXY_ENV"]
            assert self.environnement in ["dev", "prod"], f"ApplicationContext : '{self.environnement}' environnement not recognized !"
        else:
            self.environnement = "prod"
        
    
    def _set_attrs(self, bean, attrs):
        """
        Injecte les propriétés d'un bean
        Une propriété peut être une référence vers un autre bean
        :param bean
        :param attrs
        """
        if attrs != None:
            for key, value in attrs.items():
                if key != "class":
                    # injection en cascade d'un autre bean
                    if value.startswith("bean:"):
                        setattr(bean, key, self.bean(value.replace("bean:", "")))
                    # injection d'une valeur simple
                    else:
                        setattr(bean, key, value)
            
        
        
    def bean(self, name):
        """
        Récupère un bean dans le contexte
        Crée une instance si l'objet n'existe pas mais qu'il est référencé dans
        le dictionnaire beans
        """
        bean = None

        # l'objet n'a pas encore été instancié, on le créé à la demande   
        # s'il existe dans le dictionnaire     
        if not name in self.instances:
            assert name in beans, f"ApplicationContext : bean {name} is not declared !"
            beanDict = beans[name]
            
            module_path, class_name = beanDict["class"].rsplit('.', 1)
            module = import_module(module_path)
            beanClass = getattr(module, class_name)
            
            # instancie le bean avec la classe trouvée
            # et le conserve dans la map des instances
            # inject aussi auto un logger configuré sur le nom du module
            bean = beanClass()
            self._set_attrs(bean, beanDict)
            self.instances[name] = bean
            bean.logger = logging.getLogger(module.__name__)
        else:
            bean = self.instances[name]
        
        return bean
    


# ------------------------------------------------------------------------------
# beans : dictionnaire des beans de l'application    
# ------------------------------------------------------------------------------

beans = {
    "proxy": {
        "class": "proxieslognact.proxy.Proxy",
        "pushoutboxjob": "bean:pushoutboxjob",
        "fetchoutboxjob": "bean:fetchoutboxjob",
        "federationProtocol": "bean:federationProtocol"
    },
    "lognact": {
        "class": "proxieslognact.api.zabbix.Zabbix",
        "consumerService": "bean:consumerService",
        "configService": "bean:configService"
    },
    "pushoutboxjob": {
        "class": "proxieslognact.job.pushoutbox.PushOutboxJob",
        "proxyService": "bean:proxyService",
        "consumerService": "bean:consumerService",
        "messageHandler": "bean:messageHandler"
    },
    "fetchoutboxjob": {
        "class": "proxieslognact.job.fetchoutbox.FetchOutboxJob",
        "proxyService": "bean:proxyService",
        "outboxService": "bean:outboxService"
    },
    "messageHandler": {
        "class": "proxieslognact.federation.handler.outbox.OutboxMessageHandler",
        "outboxService": "bean:outboxService",
        "messageSerializer": "bean:messageSerializer"
    },
    "messageSerializer": {
        "class": "proxieslognact.federation.serializer.bytearray.ByteArrayMessageSerializer",
    },
    "proxyService": {
        "class": "proxieslognact.service.proxy.ProxyService",
        "lognact": "bean:lognact",
        "consumerService": "bean:consumerService",
        "messageSerializer": "bean:messageSerializer",
        "outboxService": "bean:outboxService",
        "federationProtocol": "bean:federationProtocol"
    },
    "federationProtocol": {
        "class": "proxieslognact.federation.xmpp.XmppFederationProtocol",
        "configService": "bean:configService",
        "appService": "bean:appService"
    },
    "configService": {
        "class": "proxieslognact.service.config.ConfigService"
    },
    "consumerService": {
        "class": "proxieslognact.service.consumer.ConsumerService"
    },
    "outboxService": {
        "class": "proxieslognact.service.outbox.OutboxService"
    },
    "appService": {
        "class": "proxieslognact.service.app.AppService"
    },
    "datasource": {
        "class": "proxieslognact.persistance.datasource.Datasource"
    }
}



# le conteneur principal de l'application
applicationContext = ApplicationContext()