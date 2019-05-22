"""
Module config

@author: Gregory Elléouet
"""

from proxieslognact.model.configvalue import ConfigValue
from proxieslognact.util.datasource import transactional


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
        pass
    
    
    @transactional()
    def value(self, name, session=None):
        """
        Change une config par son nom
        @param name: le nom de la config à retrouver
        @param session: auto injecté par le decorator transactionel. L'argument
        est optionnel pour le masquer du code appelant
        """
        configValue = session.query(ConfigValue).\
                filter(ConfigValue.name == name).\
                one()
        
        # si aucne value, le one() a déclenchée une exception
        # donc le return renvoit forcément quelquechose
        return configValue.value
        
        