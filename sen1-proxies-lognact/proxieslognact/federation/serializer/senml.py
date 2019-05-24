'''
Created on 24 mai 2019

@author: Gregory
'''

import pickle

from proxieslognact.federation.message import MessageSerializer


class SenMLMessageSerializer(MessageSerializer):
    """
    Serializer/Deserializer de message au format XML SenL
    """
    
    def write(self, message):
        return pickle.dumps(message)
    
    
    def read(self, buffer):
        pass