# -*- coding: utf-8 -*-

"""
Module proxy

@author: Gregory Elleouet
"""

import time
import signal
import sys
import schedule

from proxieslognact.application import applicationContext
from proxieslognact import settings


__all__ = ['Proxy', 'main']


class Proxy(object):
    """
    Objet principal permettant de démarrer un serveur proxy avec toutes les
    fonctionnalités :
        * écoute des messages sur la fédération
        * envoit des nouvelles données vers les consumers
    """
    
    def __init__(self):
        '''
        Constructor
        '''
        self.suspended = False
        self.pushoutboxjob = None
        self.fetchoutboxjob = None
        self.fetchinboxjob = None
        self.federationProtocol = None
    
    
    def _exit_handler(self, signum, flag):
        """
        Handler pour l'arrêt "propre" du serveur
        """
        self.logger.info(f"Handle exit signal : {signum}")
        self.suspended = True
    
    
    def start(self):
        """
        Démarrage du proxy
        """
        env = applicationContext.environnement
        self.logger.info(f"Starting proxy server... [{env}]")
        
        self.federationProtocol.start()
        
        # Planification des jobs (en prod, sinon exécution directe)
        if (env == "prod"):
            self.logger.info(f"Scheduling 'pushoutboxjob' every {settings['jobs']['pushoutboxjob']['interval']} minutes...")
            schedule.every(settings['jobs']['pushoutboxjob']['interval']).minutes.do(self.pushoutboxjob.execute, schedule)
            
            self.logger.info(f"Scheduling 'fetchoutboxjob' every {settings['jobs']['fetchoutboxjob']['interval']} minutes...")
            schedule.every(settings['jobs']['fetchoutboxjob']['interval']).minutes.do(self.fetchoutboxjob.execute, schedule)
            
            self.logger.info(f"Scheduling 'fetchinboxjob' every {settings['jobs']['fetchinboxjob']['interval']} minutes...")
            schedule.every(settings['jobs']['fetchinboxjob']['interval']).minutes.do(self.fetchinboxjob.execute, schedule)
        else:
            self.pushoutboxjob.execute(None)
            self.fetchoutboxjob.execute(None)
            self.fetchinboxjob.execute(None)
            pass

    
    def stop(self):
        """
        Arrête le proxy
        """
        self.logger.info("Stopping proxy server...")
        self.federationProtocol.stop()
        
        
    def run(self):
        """
        Démarre le proxy :
            * écoute les messages de la fédération
            * cron pour charger les nouvelles données locales et les envoyer sur la
                fédération à chaque consumer
        """
        self.start()
        
         # intercepte les signaux d'arrêt du process
        signal.signal(signal.SIGTERM, self._exit_handler)
        signal.signal(signal.SIGINT, self._exit_handler)
        
        while not self.suspended:
            schedule.run_pending()
            time.sleep(1)
        
        self.stop()
        sys.exit(0)
        


def main():
    """
    Entrypoint : démarre un sever proxy
    """
    proxy = applicationContext.bean("proxy")
    proxy.run()
    
    
    
# exécution autonome
if __name__ == "__main__":
    main()
    
