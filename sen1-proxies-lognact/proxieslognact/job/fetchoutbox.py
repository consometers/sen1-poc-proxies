"""
Module fetchoutbox

@author: Gregory Elleouet
"""

from proxieslognact import settings


class FetchOutboxJob(object):
    """
    Job pour charger les outbox en attente et les envoyer sur le réseau fédéré
    Ce job ne fait pas le travail final mais il se charge juste de charger les
    outbox par paquet et délègue le travail à un service
    """

    def __init__(self):
        """
        Constructor
        """
        self.outboxService = None
        self.proxyService = None
        
    
    def execute(self, scheduler):
        """
        Exécute le job depuis un scheduler
        
        TODO : paralléliser le traitement des consumers
        """
        
        # Charge tous les outbox par page pour éviter de saturer la mémoire
        # compte d'abord le nombre total de outbox et les charge par packet
        paginationMax = settings["persistance"]["paginationBackendMax"]
        nbOutbox = self.outboxService.count()
        nbPage = int(nbOutbox / paginationMax) + 1 if nbOutbox else 0
        self.logger.info("Running FetchOutboxJob... found {} outbox(s) [{} page(s)]".format(nbOutbox, nbPage))
        
        if (nbOutbox):
            for page in range(0, nbPage):
                outboxIds = self.outboxService.listIds(dict(offset=page*paginationMax, max=paginationMax))
                
                for outboxId in outboxIds:
                    try:
                        self.proxyService.federate_outbox_data(outboxId)
                        
                    except Exception as ex:
                        self.logger.error("ProxyService.federate_outbox_data : {}".format(ex))
