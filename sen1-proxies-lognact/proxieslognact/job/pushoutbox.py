"""
Module pushoutbox

@author: Gregory Elleouet
"""

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
        
        
    def execute(self, scheduler):
        """
        Exécute le job depuis un scheduler
        Charge tous les consumers associés à un objet et prépare un message pour
        chacun d'eux à envoyer sur la boite "outbox"
        """
        print("Execute PushOutboxJob")
        