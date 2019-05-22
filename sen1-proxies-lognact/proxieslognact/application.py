# -*- coding: utf-8 -*-

'''
Module application

Contexte applicatif
Déclare les objets de l'application et les créé dynamiquement à la demande
Gère aussi la configuration globale du projet
Ceci permet aussi des dépendances cycliques entre module car les modules sont
importées dynamiquement

@author: Gregory Elléouet
'''

from importlib import import_module
import logging


class ApplicationContext(object):
    """
    Contexte applicatif
    Instancie les beans à la demande à partir du dictionnaire beans
    """
    def __init__(self, beans, settings):
        """
        Constructor
        """
        self.beans = beans
        self.settings = settings
        self.instances = {}
        
    
    def _set_attrs(self, bean, attrs):
        """
        Injecte les propriétés d'un bean
        Une propriété peut être une référence vers un aure bean
        :param bean
        :param attrs
        """
        if attrs != None:
            for key, value in attrs.items():
                if key != "class":
                    # injection en cascade d'un autre bean
                    if value.startswith("bean:"):
                        setattr(bean, key, self.bean(value.replace("bean:", "")))
                    # injection d'une valeur simple
                    else:
                        setattr(bean, key, value)
            
        
        
    def bean(self, name):
        """
        Récupère un bean dans le contexte
        Crée une instance si l'objet n'existe pas mais qu'il est référencé dans
        le dictionnaire beans
        """
        bean = None

        # l'objet n'a pas encore été instancié, on le créé à la demande   
        # s'il existe dans le dictionnaire     
        if not name in self.instances:
            beanDict = self.beans[name]
            
            if beanDict == None:
                raise RuntimeError(f"bean {name} is not declared !")
            
            module_path, class_name = beanDict["class"].rsplit('.', 1)
            module = import_module(module_path)
            beanClass = getattr(module, class_name)
            
            # instancie le bean avec la classe trouvée
            # et le conserve dans la map des instances
            # inject aussi auto un logger
            bean = beanClass()
            self._set_attrs(bean, beanDict)
            self.instances[name] = bean
            bean.logger = logging.getLogger(beanDict["class"])
        else:
            bean = self.instances[name]
        
        return bean
    
    

# ------------------------------------------------------------------------------
# beans : dictionnaire des beans de l'application    
# ------------------------------------------------------------------------------

beans = {
    "proxy": {
        "class": "proxieslognact.proxy.Proxy",
        "pushoutboxjob": "bean:pushoutboxjob",
    },
    "lognact": {
        "class": "proxieslognact.api.zabbix.Zabbix",
    },
    "pushoutboxjob": {
        "class": "proxieslognact.job.pushoutbox.PushOutboxJob",
        "lognact": "bean:lognact",
        "config": "bean:config"
    },
    "config": {
        "class": "proxieslognact.service.config.Config"
    },
    "datasource": {
        "class": "proxieslognact.util.datasource.Datasource"
    }
}


# ------------------------------------------------------------------------------
# settings : dictionnaire config du projet
# ------------------------------------------------------------------------------

settings = {
    
}


# le conteneur principal de l'application
applicationContext = ApplicationContext(beans, settings)
