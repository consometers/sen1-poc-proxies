# -*- coding: utf-8 -*-

"""
Module proxy

@author: Gregory Elleouet
"""

import time
import signal
import sys
import schedule
from application import applicationContext


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
    
    
    def _exit_handler(self, signum, flag):
        """
        Handler pour l'arrêt "propre" du serveur
        """
        print(f"Handle exit signal : {signum}")
        self.suspended = True
        
    
    def start(self):
        """
        Démarrage du proxy
        """
        print("Starting proxy server...")
        
        # Exécution du job pushoutbox toutes les 5 minutes
        schedule.every(1).minutes.do(self.pushoutboxjob.execute, schedule)
    
    
    def stop(self):
        """
        Arrête le proxy
        """
        print("Stopping proxy server...")
        
        
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
        
        print("Listening proxy server...")
        
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
    
    
# exécution autoname
if __name__ == "__main__":
    main()
    
