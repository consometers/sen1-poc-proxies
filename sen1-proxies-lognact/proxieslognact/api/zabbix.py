"""

@author: Gregory
"""

from datetime import datetime

from protobix import SampleProbe, DataContainer, ZabbixAgentConfig
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
        
        self.logger.info("Pushing datas to Zabbix {}:{}...".format(self.serverHost, self.serverPort))
        
        zabbixConfig = ZabbixAgentConfig()
        zabbixConfig.server_active = self.serverHost
        zabbixConfig.server_port = int(self.serverPort)
        zabbixConfig.log_type = 'console'
        
        zabbixContainer = DataContainer(config = zabbixConfig, logger = self.logger)
        zabbixContainer.data_type = 'items'
        
        for data in command.datas:
            self.logger.debug("Add zabbix item hostname={}, key={}, value={}".format(command.hostname, 
                                                                              command.itemKey,
                                                                              data.value))
            zabbixContainer.add_item(command.hostname, command.itemKey,
                                     data.value,
                                     int(data.timestamp.timestamp()))
            
        zabbixContainer.send()
    
    
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
            self.logger.info("Try connecting to {}... Zabbix v{}...".format(self.serverUrl, zapi.api_version()))
        except (ZabbixAPIException, ReadTimeout, ValueError) as ex:
            self.logger.error("Try connecting to {} : {}".format(self.serverUrl, ex))
        
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
            detailHistory = "{} to {}".format(command.dateStart, command.dateEnd)
        else:
            # en mode limit, il faut inverser le tri pour récupérer les valeurs
            # les plus récentes
            apiParams["limit"] = command.limit
            apiParams["sortorder"] = "DESC"
            detailHistory = "{} last values".format(command.limit)
          
        try:
            datas = zapi.history.get(**apiParams)
            self.logger.info("Fetching history from {}...{} value(s)".format(detailHistory, len(datas)))
        except (ZabbixAPIException, ReadTimeout, ValueError) as ex:
            self.logger.error("Fetching history from {} : {}".format(detailHistory, ex))
        
        return datas
        