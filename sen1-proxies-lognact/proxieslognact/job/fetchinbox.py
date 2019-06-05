"""
Module fetchoutbox

@author: Gregory Elleouet
"""

from proxieslognact import settings


class FetchInboxJob(object):
    """
    Job pour charger les inbox en attente et les envoyer sur le système local
    Ce job ne fait pas le travail final mais il se charge juste de charger les
    inbox par paquet et délègue le travail à un service
    """

    def __init__(self):
        """
        Constructor
        """
        self.inboxService = None
        self.proxyService = None
        
    
    def execute(self, scheduler):
        """
        Exécute le job depuis un scheduler
        
        TODO : paralléliser le traitement des consumers
        """
        
        # Charge tous les inbox par page pour éviter de saturer la mémoire
        # compte d'abord le nombre total de inbox et les charge par packet
        paginationMax = settings["persistance"]["paginationBackendMax"]
        nbInbox = self.inboxService.count()
        nbPage = int(nbInbox / paginationMax) + 1 if nbInbox else 0
        self.logger.info("Running FetchInboxJob... found {} inbox(s) [{} page(s)]".format(nbInbox, nbPage))
        
        if (nbInbox):
            for page in range(0, nbPage):
                inboxIds = self.inboxService.listIds(dict(offset=page*paginationMax, max=paginationMax))
                
                for inboxId in inboxIds:
                    try:
                        self.proxyService.push_inbox_data(inboxId)
                        
                    except Exception as ex:
                        self.logger.error("ProxyService.push_inbox_data : {}".format(ex))
