"""

@author: Gregory
"""

from pyzabbix import ZabbixAPI, ZabbixAPIException
from requests.exceptions import ReadTimeout

from proxieslognact.api.lognact import LogNAct


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
        zapi = ZabbixAPI(server = command.serverUrl, timeout = 2.5)
        datas = None
        
        try:
            zapi.login(command.user, command.password)
            self.logger.info(f"Try connecting to {command.serverUrl}... Zabbix{zapi.api_version()}...")
        except (ZabbixAPIException, ReadTimeout, ValueError) as ex:
            self.logger.error(f"Try connecting to {command.serverUrl} : {ex}")
            
        try:
            datas = zapi.history.get(itemids = command.itemIds,
                           time_from = command.dateStart,
                           time_till = command.dateEnd,
                           output = 'extend',
                           sortfield = 'clock')
            self.logger.info(f"Fetching history from {command.dateStart} to {command.dateEnd}...{len(datas)}value(s)")
        except (ZabbixAPIException, ReadTimeout, ValueError) as ex:
            self.logger.error(f"Fetching history from {command.dateStart} to {command.dateEnd} : {ex}")
        
        return datas
        
        