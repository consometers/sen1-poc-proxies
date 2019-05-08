"""
Module lognact

@author: Gregory Elleouet
"""

from abc import ABC, abstractmethod


__all__ = ['LogNAct', 'PushData', 'GetData']


class LogNAct(ABC):
    """
    API LogNAct
    """


    @abstractmethod
    def __init__(self):
        '''
        Constructor
        '''
        self.host = None
    
    
    def host(self, host):
        """
        Associe le host du serveur et renvoit une référence à l'instance
        pour une utilisation Fluent
        :param host
        :return self
        """
        self.host = host
        return self
      
        
    @abstractmethod
    def push_data(self, command):
        """
        Envoit des données vers le serveur
        :param command: objet de type PushData
        :return
        """
        pass
    
    
    @abstractmethod
    def get_data(self, command):
        """
        Récupère des données depuis le serveur
        :param command: objet de type GetData
        :return
        """
        pass



class PushData(object):
    """
    Paramètres de commande pour l'appel push_data
    """
    
    def __init__(self, params):
        pass
    
    
    
class GetData(object):
    """
    Paramètres de commande pour l'appel get_data
    """
    
    def __init__(self, params):
        pass