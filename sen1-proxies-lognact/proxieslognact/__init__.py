"""
Module log

@author: Gregory Elléouet
"""

import logging
from logging.config import dictConfig

from proxieslognact.application import applicationContext


# logger personnalisé en fonction environnement
envLoggers = {
    "dev": {
        "root": logging.INFO,
        "loggers": {
            # insert here config logger
            "pyzabbix": { "level": logging.DEBUG},
            "sqlachemy.sql": { "level": logging.DEBUG}
        }
    },
    "prod": {
        "root": logging.INFO,
        "loggers": {
            # insert here config logger
        }
    }
}

# init du logger
dictConfig(dict(
    version = 1,
    formatters = {
        "default_formatter" : {
            "format": "%(asctime)s %(levelname)s [%(threadName)s] %(module)s - %(message)s"
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



        
        
