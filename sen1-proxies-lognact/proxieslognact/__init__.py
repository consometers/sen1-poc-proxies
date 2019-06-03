"""
Configuration du projet
* logger
* settings

@author: Gregory Elléouet
"""

import logging
from logging.config import dictConfig

from proxieslognact.application import ApplicationContext


# ------------------------------------------------------------------------------
# beans : dictionnaire des beans de l'application    
# ------------------------------------------------------------------------------

beans = {
    "proxy": {
        "class": "proxieslognact.proxy.Proxy",
        "pushoutboxjob": "bean:pushoutboxjob",
        "fetchoutboxjob": "bean:fetchoutboxjob",
        "fetchinboxjob": "bean:fetchinboxjob",
        "federationProtocol": "bean:federationProtocol"
    },
    "lognact": {
        "class": "proxieslognact.api.zabbix.Zabbix",
        "configService": "bean:configService"
    },
    "pushoutboxjob": {
        "class": "proxieslognact.job.pushoutbox.PushOutboxJob",
        "proxyService": "bean:proxyService",
        "consumerService": "bean:consumerService",
        "messageHandler": "bean:outboxMessageHandler"
    },
    "fetchoutboxjob": {
        "class": "proxieslognact.job.fetchoutbox.FetchOutboxJob",
        "proxyService": "bean:proxyService",
        "outboxService": "bean:outboxService"
    },
    "fetchinboxjob": {
        "class": "proxieslognact.job.fetchinbox.FetchInboxJob",
        "proxyService": "bean:proxyService",
        "inboxService": "bean:inboxService"
    },
    "outboxMessageHandler": {
        "class": "proxieslognact.federation.handler.outbox.OutboxMessageHandler",
        "outboxService": "bean:outboxService",
        "messageSerializer": "bean:messageSerializer"
    },
    "inboxMessageHandler": {
        "class": "proxieslognact.federation.handler.inbox.InboxMessageHandler",
        "inboxService": "bean:inboxService",
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
        "inboxService": "bean:inboxService",
        "configService": "bean:configService",
        "userAppService": "bean:userAppService",
        "federationProtocol": "bean:federationProtocol"
    },
    "federationProtocol": {
        "class": "proxieslognact.federation.xmpp.XmppFederationProtocol",
        "configService": "bean:configService",
        "appService": "bean:appService",
        "messageHandler": "bean:inboxMessageHandler"
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
    "inboxService": {
        "class": "proxieslognact.service.inbox.InboxService"
    },
    "appService": {
        "class": "proxieslognact.service.app.AppService"
    },
    "userAppService": {
        "class": "proxieslognact.service.user_app.UserAppService"
    },
    "datasource": {
        "class": "proxieslognact.persistance.datasource.Datasource"
    }
}

# le conteneur principal de l'application
# a creer le plus tot dans le code car il gère notemment l'environnement
# qui peut servir au système de logging
applicationContext = ApplicationContext(beans)


# ------------------------------------------------------------------------------
# logger    
# ------------------------------------------------------------------------------

# logger personnalisé en fonction environnement
envLoggers = {
    "dev": {
        "root": logging.INFO,
        "loggers": {
            # insert here config logger
            "pyzabbix": { "level": logging.INFO},
            "proxieslognact.federation.xmpp": { "level": logging.DEBUG},
            "sqlalchemy.engine.base.Engine": { "level": logging.INFO}
        }
    },
    "prod": {
        "root": logging.INFO,
        "loggers": {
            # insert here config logger
            "sqlalchemy.engine.base.Engine": { "level": logging.WARN}
        }
    }
}

# init du logger
dictConfig(dict(
    version = 1,
    formatters = {
        "default_formatter" : {
            "format": "%(asctime)s %(levelname)s [%(threadName)s] %(name)s - %(message)s"
        }
    },
    handlers = {
        'console': {
            'class': 'logging.StreamHandler',
            'formatter': 'default_formatter',
        }
    },
    loggers = envLoggers[applicationContext.environnement]["loggers"],
    root = {
        'handlers': ['console'],
        'level': envLoggers[applicationContext.environnement]["root"]
    }
))


# ------------------------------------------------------------------------------
# settings    
# ------------------------------------------------------------------------------

settings = {
    "proxy": {
        "firstMaxValue": 500
    },
    "persistance": {
        "paginationBackendMax": 100, 
        "paginationFrontendMax": 25 
    },
    "jobs": {
        "pushoutboxjob": {
            "interval": 5
        },
        "fetchoutboxjob": {
            "interval": 5
        },
        "fetchinboxjob": {
            "interval": 1
        }
    }
}


        
        
