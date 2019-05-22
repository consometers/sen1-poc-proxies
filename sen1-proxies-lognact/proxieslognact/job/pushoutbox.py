"""
Module pushoutbox

@author: Gregory Elleouet
"""

from proxieslognact.api.lognact import GetData


class PushOutboxJob(object):
    """
    Job pour charger les données du système local (lognact) et les envoyer sur
    la boite "outbox" 
    """

    def __init__(self):
        """
        Constructor
        """
        self.lognact = None
        self.config = None
        
        
    
    def execute(self, scheduler):
        """
        Exécute le job depuis un scheduler
        Charge tous les consumers associés à un objet et prépare un message pour
        chacun d'eux à envoyer sur la boite "outbox"
        """
        self.logger.info("Running PushOutboxJob...")
        
        # construit un objet pour requêter les données
        command = GetData()\
            .serverUrl(self.config.value("LOGNACT_URL"))\
            .user(self.config.value("LOGNACT_USER"))\
            .password(self.config.value("LOGNACT_PASSWORD"))
            
        datas = self.lognact.fetch_data(command)
