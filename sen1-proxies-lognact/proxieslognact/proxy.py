# -*- coding: utf-8 -*-

"""
Module Proxy

Démarre un serveur pour écouter les messages de la fédération et les envoyer
vers l'instance lognact et inversement allimente les consumers de la fédération
à partir des données du serveur lognact

@author: Gregory Elleouet
"""

import time
import signal
import sys


__all__ = ['start', 'stop', 'main']


_suspended=False


def _exit_handler(signum, flag):
    """
    hander de sortie du process
    :param signnum:
    :param flag:
    """
    print("Handle kill signal")
    global _suspended
    _suspended=True


def start():
    """
    Démarre le proxy :
        * écoute les messages de la fédération
        * cron pour charger les nouvelles données locales et les envoyer sur la
            fédération à chaque consumer
    """
    print("Starting proxy...")
    
    
def stop():
    """
    Arrête le proxy et libère les ressources
    """
    print("Stopping proxy...")
    
    
def main():
    """
    Entrypoint : démarre un proxy
    """
    start()
    
    # Signaux d'arrêt du process
    signal.signal(signal.SIGTERM, _exit_handler)
    signal.signal(signal.SIGINT, _exit_handler)
    
    while(not _suspended):
        time.sleep(1)
        
    stop()
    sys.exit(0)
    

# Execution autonome du module   
if __name__ == "__main__":
    main()
