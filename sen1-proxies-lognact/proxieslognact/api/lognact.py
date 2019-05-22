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
    Paramètres de commande pour l'appel push_data
    """
    
    def __init__(self):
        pass
    
    
    
class GetData(object):
    """
    Paramètres de commande pour l'appel get_data
    Api Fluent
    """
    
    def __init__(self):
        self._serverUrl = None
        self._user = None
        self._password = None
        self._dateStart = None
        self._dateEnd = None
        self._itemIds = None
    
    
    def serverUrl(self, serverUrl):
        """
        Injecte l'URL du serveur
        """
        self._serverUrl = serverUrl
        return self
    
    
    def user(self, user):
        """
        Injecte l'identifiant de connexion
        """
        self._user = user
        return self
    
    
    def password(self, password):
        """
        Injecte le mot de passe de connexion
        """
        self._password = password
        return self
    
    
    def dateStart(self, dateStart):
        """
        Date début historique (inclusif)
        """
        self._dateStart = dateStart
        return self
    
    
    def dateEnd(self, dateEnd):
        """
        Date fin historique (inclusif)
        """
        self._dateEnd = dateEnd
        return self
    
    
    def itemIds(self, itemIds):
        """
        Les items à recherche.
        Peut-être une simple valeur ou un tableau de valeur
        """
        self._itemIds = itemIds
        return self
    