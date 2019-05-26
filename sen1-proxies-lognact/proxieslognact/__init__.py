"""
Configuration du projet
* logger
* settings

@author: Gregory Elléouet
"""

import logging
from logging.config import dictConfig

from proxieslognact.application import applicationContext


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
        }
    }
}


        
        
