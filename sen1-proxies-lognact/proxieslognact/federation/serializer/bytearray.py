'''
Created on 24 mai 2019

@author: Gregory
'''

import pickle

from proxieslognact.federation.message import MessageSerializer


class ByteArrayMessageSerializer(MessageSerializer):
    """
    Serializer/Deserializer de message en bytearray
    """
    
    def write(self, message):
        return pickle.dumps(message)
    
    
    def read(self, buffer):
        return pickle._loads(buffer)