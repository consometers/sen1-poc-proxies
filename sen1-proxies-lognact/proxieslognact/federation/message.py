"""
Module message
Created on 24 mai 2019

@author: Gregory Elléouet
"""

from abc import ABC, abstractmethod


_all__ = ['Message', 'MessageData', 'MessageHandler', 'MessageSerializer']


class MessageSerializer(ABC):
    
    @abstractmethod
    def read(self, buffer):
        """
        Deserialize un buffer pour construire un message
        
        :param buffer: bytearray
        :return Message
        """
        pass
    
    
    @abstractmethod
    def write(self, message):
        """
        Serialize un objet message
        
        :param message: Message
        :return bytearray
        """
        pass



class MessageHandler(ABC):
    
    @abstractmethod
    def handle(self, message):
        """
        Traite un message
        """
        pass



class Message(object):
    """
    Message transitant sur le réseau fédéré
    """
    
    def __init__(self):
        """
        PostConstruct
        """
        self.username = None
        self.name = None
        self.metaname = None
        self.metavalue = None
        self.unite = None
        self.type = None
        self.applicationSrc = None
        self.applicationDst = None
        self.datas = []
        self._dateLastValue = None
        
        
    def dateLastValue(self):
        """
        Retourne la date de la data la plus récente
        
        :return datetime
        """    
        return self._dateLastValue
    
        
    def add_data(self, value, timestamp):
        """
        Ajout d'une nouvelle donnée dans le message
        
        :param value:
        :param timestamp: datetime
        :return MessageData
        """
        assert value, "Message.add_data : value is required !"
        assert timestamp, "Message.add_data : timestamp is required !"
        
        data = MessageData(value, timestamp)
        self.datas.append(data)
        
        # on en profite pour calculer la date de la donnée la plus récente
        if (not self._dateLastValue or timestamp > self._dateLastValue):
            self._dateLastValue = timestamp 
        
        return data
    
    
    def asserts(self):
        """
        Vérifie que le message est conforme
        """
        assert self.username, "Message : username is required !"
        assert self.applicationSrc, "Message : applicationSrc is required !"
        assert self.applicationDst, "Message : applicationDst is required !"
        assert self.type, "Message : type is required !"
        assert self.name, "Message : name is required !"
        assert len(self.datas), "Message : datas is empty !"
    
    
    
class MessageData(object):
    """
    Data d'un message
    """
    
    def __init__(self, value, timestamp):
        """
        PostConstruct
        """
        self.value = value
        self.timestamp = timestamp