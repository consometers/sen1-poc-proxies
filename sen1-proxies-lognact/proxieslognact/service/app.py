"""
Module config

@author: Gregory Elléouet
"""

from proxieslognact.model.app import App
from proxieslognact.persistance.datasource import transactional


class AppService(object):
    
    @transactional()
    def findByName(self, name, session=None):
        """
        Recherche d'une application par son nom
        
        :param name: le nom de l'application
        :param session: auto injecté par le decorator transactionel
        
        :return App
        """
        return session.query(App) \
                .filter(App.name == name) \
                .one()
        
        