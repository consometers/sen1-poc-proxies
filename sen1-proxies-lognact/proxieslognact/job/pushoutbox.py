"""
Module pushoutbox

@author: Gregory Elleouet
"""

from proxieslognact import settings


class PushOutboxJob(object):
    """
    Job pour charger les données du système local (lognact) et les traiter
    par un gestionnaire de message
    Ce job ne fait pas le travail final mais il se charge juste de charger les
    consumers par paquet et délègue le travail à handler
    """

    def __init__(self):
        """
        Constructor
        """
        self.consumerService = None
        self.proxyService = None
        self.messageHandler = None
        
    
    def execute(self, scheduler):
        """
        Exécute le job depuis un scheduler
        Charge tous les consumers associés à un objet et prépare un message pour
        chacun d'eux à déléguer au handler
        
        TODO : paralléliser le traitement des consumers
        """
        
        # Charge tous les consumers par page pour éviter de saturer la mémoire
        # compte d'abord le nombre total de consumers et les charge par packet
        paginationMax = settings["persistance"]["paginationBackendMax"]
        nbConsumer = self.consumerService.count()
        nbPage = int(nbConsumer / paginationMax) + 1 if nbConsumer else 0
        self.logger.info(f"Running PushOutboxJob... found {nbConsumer} consumer(s) [{nbPage} page(s)]")
        
        if (nbConsumer):
            for page in range(0, nbPage):
                consumerIds = self.consumerService.listIds(dict(offset=page*paginationMax, max=paginationMax))
                
                for consumerId in consumerIds:
                    try:
                        self.proxyService.handle_consumer_data(consumerId, self.messageHandler)
                        
                    except Exception as ex:
                        self.logger.error(f"ProxyService.handle_consumer_data : {ex}")
