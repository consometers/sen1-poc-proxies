"""
Module config

@author: Gregory Elléouet
"""

from proxieslognact.model.config import Config
from proxieslognact.persistance.datasource import transactional


class ConfigService(object):
    
    @transactional()
    def value(self, name, session=None):
        """
        Change une config par son nom
        @param name: le nom de la config à retrouver
        @param session: auto injecté par le decorator transactionel. L'argument
        est optionnel pour le masquer du code appelant
        """
        config = session.query(Config) \
                .filter(Config.name == name) \
                .one()
        
        # si aucne value, le one() a déclenchée une exception
        # donc le return renvoit forcément quelquechose
        return config.value
        
        