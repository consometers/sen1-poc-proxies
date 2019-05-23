"""
Module lognact

@author: Gregory Elleouet
"""

from abc import ABC, abstractmethod


__all__ = ['LogNAct', 'PushData', 'FetchData']


class LogNAct(ABC):
    """
    API LogNAct
    """


    @abstractmethod
    def __init__(self):
        '''
        PostConstruct
        '''
        pass
    
    
    
    @abstractmethod
    def push_data(self, command):
        """
        Envoit des données vers le serveur
        :param command: objet de type PushData
        :return
        """
        pass
    
    
    
    @abstractmethod
    def fetch_data(self, command):
        """
        Récupère des données depuis le serveur
        :param command: objet de type GetData
        :return
        """
        pass



class PushData(object):
    """
    Paramètres de commande pour pusher des données
    """
    def __init__(self):
        pass
    
    
    
class FetchData(object):
    """
    Paramètres de commande pour fetcher des données
    """
    def __init__(self):
        self.serverUrl = None
        self.user = None
        self.password = None
        # Date début historique (inclusif)
        self.dateStart = None
        # Date fin historique (inclusif)
        self.dateEnd = None
        self.itemIds = None
    