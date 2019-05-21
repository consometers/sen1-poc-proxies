"""

@author: Gregory
"""

from proxieslognact.api.lognact import LogNAct, PushData, GetData


_all__ = ['Zabbix']


class Zabbix(LogNAct):
    """
    Implémentation Zabbix LogNAct
    """


    def __init__(self):
        '''
        Constructor
        '''
        super().__init__()
        
        
    def push_data(self, command):
        """
        Envoit des données vers le serveur
        """
        pass
    
    
    def fetch_data(self, command):
        """
        Récupère des données depuis le serveur
        """
        pass
