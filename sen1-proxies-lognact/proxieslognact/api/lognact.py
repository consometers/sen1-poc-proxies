"""
Module lognact

@author: Gregory Elleouet
"""

from datetime import datetime
from datetime import timedelta
from abc import ABC, abstractmethod

from proxieslognact.util.builder import Builder
from proxieslognact import settings


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
        self.consumerService = None
        self.configService = None
    
    
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
    
    
    def fetch_consumer_data(self, consumer):
        """
        Méthode au niveau pour récupérer les données d"un consumer.
        La facon de faire ne dépend pas d'une implémentation mais est justement
        commune à chaque impl.
        
        L'extraction finale des datas sera surchargée par chaque impl
        """
        # la connexion au system se fait via une paire login/password
        # c'est enregistré sous la forme user:password dans le champ token
        tokens = consumer.userApp.token.split(":")

        # construit un objet pour requêter les données
        command = Builder(FetchData) \
            .serverUrl(self.configService.value("LOGNACT_URL")) \
            .user(tokens[0]) \
            .password(tokens[1]) \
            .itemIds(consumer.name) \
            .dateEnd(datetime.now()) \
            .build()
            
        # aucune extraction n'a été faite pour l'instant, on récupère un nombre de values
        if (consumer.date_last_value == None):
            command.limit = settings["proxy"]["firstMaxValue"]
        else:
            #  dateStart est inclusif, il faut au moins incrémenter d'une seconde
            # pour ne pas charger des données en doublon
            command.dateStart = consumer.date_last_value + timedelta(seconds = 1)

        return self.fetch_data(command)



class PushData(object):
    """
    Paramètres de commande pour pusher des données
    """
    def __init__(self):
        pass
    
    
    
class FetchData(object):
    """
    Paramètres de commande pour fetcher des données
    Le fetch peut se faire de 2 manières :
     * soit entre 2 dates avec dateStart et dateEnd
     * soit les "limit" dernières valeurs
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
        self.limit = None
        
        
    def asserts(self):
        """
        Vérifie que l'objet est valide pour l'appel à l'API
        
        :throw RuntimeError
        """
        assert self.serverUrl, "FetchData : serverUrl is required !"
        assert self.user, "FetchData : user is required !"
        assert self.itemIds, "FetchData : itemIds is required !"
        assert (self.dateStart and self.dateEnd) or self.limit, \
            "FetchData :  dateStart+dateEnd or limit is required !"
