'''
Created on 26 mai 2019

@author: Gregory Elléouet
'''

from abc import ABC, abstractmethod


class FederationProtocol(ABC):
    """
    Contrat pour la gestion du protocole de la fédération
    """
    
    @abstractmethod
    def start(self):
        """
        Init du protocole
        """
        pass
    
    @abstractmethod
    def stop(self):
        """
        Arrêt du protocole
        """
        pass
    
    
    @abstractmethod
    def sendMessage(self, message):
        """
        Envoit d'un message sur le réseau
        
        :param message: Message
        """
        pass