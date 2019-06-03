"""

@author: Gregory
"""

from datetime import datetime

from protobix import SampleProbe
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
        
        :param command: critère de sélection et connexion
        """
        super()._load_config()
        
        command.asserts()
        
        zabbixSender = ZabbixSender()
        result = zabbixSender.run()
        
        if (result == 1):
            raise Exception("Step 1: probe initialization")
        elif (result == 2):
            raise Exception("Step 2 : probe data collection")
        elif (result == 3):
            raise Exception("Step 3 : add data to DataContainer")
        elif (result == 4):
            raise Exception("Step 4 : send data to Zabbix")
    
    
    def datapoint_value(self, data):
        """
        Extrait la valeur d'une data d'un fetch
        
        :param data: 
        :return value dans son type d'origine
        """
        return data["value"] if data else None
    
    
    def datapoint_timestamp(self, data):
        """
        Extrait le timestamp d'une data d'un fetch
        
        :param data: 
        :return datetime
        """
        return datetime.fromtimestamp(int(data["clock"])) if (data and "clock" in data) else None
    
    
    def fetch_data(self, command):
        """
        Récupère des données depuis le serveur à partir des critères de sélection
        spécifiés en paramètre
        
        :param command: critère de sélection et connexion
        """
        super()._load_config()
        
        command.asserts()
        
        zapi = ZabbixAPI(server = self.serverUrl, timeout = 2.5)
        datas = None
        
        try:
            zapi.login(command.user, command.password)
            self.logger.info(f"Try connecting to {command.serverUrl}... Zabbix v{zapi.api_version()}...")
        except (ZabbixAPIException, ReadTimeout, ValueError) as ex:
            self.logger.error(f"Try connecting to {command.serverUrl} : {ex}")
        
        # adapte les params en fonction du mode d'utilisation
        apiParams = dict(
            itemids = command.itemIds,
            output = 'extend',
            sortfield = 'clock'
        )
        
        if (command.dateEnd and command.dateStart):
            apiParams["time_from"] = int(command.dateStart.timestamp())
            apiParams["time_till"] = int(command.dateEnd.timestamp())
            apiParams["sortorder"] = "ASC"
            detailHistory = f"{command.dateStart} to {command.dateEnd}"
        else:
            # en mode limit, il faut inverser le tri pour récupérer les valeurs
            # les plus récentes
            apiParams["limit"] = command.limit
            apiParams["sortorder"] = "DESC"
            detailHistory = f"{command.limit} last values"
          
        try:
            datas = zapi.history.get(**apiParams)
            self.logger.info(f"Fetching history from {detailHistory}...{len(datas)} value(s)")
        except (ZabbixAPIException, ReadTimeout, ValueError) as ex:
            self.logger.error(f"Fetching history from {detailHistory} : {ex}")
        
        return datas
        


class ZabbixSender(SampleProbe):
    
    def _get_metrics(self):
        # mandatory method
        pass
    
    
    def _get_discovery(self):
        # mandatory method
        pass