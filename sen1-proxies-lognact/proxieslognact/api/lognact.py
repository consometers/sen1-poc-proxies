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
        self.serverUrl = None
        self.configService = None
        
        
    def _load_config(self):
        """
        charge la config avant appel API
        Ne doit ëtre fait qu'une fois
        """
        if (self.serverUrl == None):
            self.serverUrl = self.configService.value("LOGNACT_URL")
    
    
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
    
    
    @abstractmethod
    def datapoint_value(self, data):
        """
        Extrait la valeur d'une data d'un fetch
        
        :param data: 
        :return value dans son type d'origine
        """
        pass
    
    
    @abstractmethod
    def datapoint_timestamp(self, data):
        """
        Extrait le timestamp d'une data d'un fetch
        
        :param data: 
        :return datetime
        """
        pass
    


class PushData(object):
    """
    Paramètres de commande pour pusher des données
    """
    def __init__(self):
        self.user = None
        self.password = None
        self.itemIds = None
        
        
    def asserts(self):
        """
        Vérifie que l'objet est valide pour l'appel à l'API
        
        :throw RuntimeError
        """
        assert self.user, "PushData : user is required !"
        assert self.itemIds, "PushData : itemIds is required !"
    
    
    
class FetchData(object):
    """
    Paramètres de commande pour fetcher des données
    Le fetch peut se faire de 2 manières :
     * soit entre 2 dates avec dateStart et dateEnd
     * soit les "limit" dernières valeurs
    """
    def __init__(self):
        self.user = None
        self.password = None
        # Date début historique (inclusif)
        self.dateStart = None
        # Date fin historique (inclusif)
        self.dateEnd = None
        self.itemIds = None
        self.limit = None
        
        
    def asserts(self):
        """
        Vérifie que l'objet est valide pour l'appel à l'API
        
        :throw RuntimeError
        """
        assert self.user, "FetchData : user is required !"
        assert self.itemIds, "FetchData : itemIds is required !"
        assert (self.dateStart and self.dateEnd) or self.limit, \
            "FetchData :  dateStart+dateEnd or limit is required !"
