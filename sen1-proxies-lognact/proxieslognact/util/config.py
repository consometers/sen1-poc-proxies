"""
Module config

@author: Gregory Elléouet
"""

from proxieslognact.model.configvalue import ConfigValue


class Config(object):
    """
    Charge les éléments de configuration depuis une base pour être injecté
    dans d'autres objets
    Ces configs sont externalisées car soit confidentuelles (password, token, etc)
    soit parce qu'elles changent d'un projet à un autre (url, etc...)
    Ex : url, etc...
    """
    def __init__(self):
        '''
        Constructor
        '''
        self.datasource = None
    
    
    def value(self, name):
        """
        Change une config par son nom
        """
        configValue = None
        
        with self.datasource.with_session() as session:
            configValue = session.query(ConfigValue).\
                filter(ConfigValue.name == name).one().value
        
        # si aucne value, le one() a déclenchée une exception
        # donc le return renvoit forcément quelquechose
        return configValue
        
        