"""

@author: Gregory
"""

from proxieslognact.api.lognact import LogNAct
from pyzabbix import ZabbixAPI
from pyzabbix.api import ZabbixAPIException


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
        Récupère des données depuis le serveur à partir des critères de sélection
        spécifiés en paramètre
        @param command: critère de sélection et connexion
        """
        self.logger.info(f"Try connecting to {command._serverUrl}...")
        zapi = ZabbixAPI(command._serverUrl)
        datas = None
        
        try:
            zapi.login(command._user, command._password)
            
            datas = zapi.history.get(itemids = command._itemIds,
                           time_from = command._dateStart,
                           time_till = command._dateEnd,
                           output = 'extend',
                           sortfield = 'clock')
        except ZabbixAPIException as ex:
            self.logger.error(ex)
        
        return datas
        
        