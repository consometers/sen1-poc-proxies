"""
Module builder

@author: Gregory Elléouet
"""


class Builder(object):
    """
    Builder sur tout type d'objet
    Permet de chainer les appels des setters sur la même instance et créer une
    API Fluent
    """
    def __init__(self, instanceType):
        '''
        PostConstruct
        
        :param instanceType: le type de l'instance à builder
        '''
        self.instanceType = instanceType
        self._instance = instanceType()
        
    
    def __getattr__(self, attr):
        """
        Appel dynamique sur les propriétés de l'objet type
        Passe par un wrapper pour récupérer l'argument value
        """
        def wrapper(*args):
            if (len(args) == 1):
                setattr(self._instance, attr, args[0])
                # Important : retourner le builder pour le mode Fluent
                return self
            else:
                raise RuntimeError("Setter must have one parameter !")
            
        return wrapper
    
    
    def build(self):
        """
        Retourne l'instance buildée
        """
        return self._instance