"""
Module datasource

@author: Gregory Elléouet
"""

import sqlalchemy
from sqlalchemy.orm import sessionmaker
import os
from contextlib import contextmanager


class Datasource(object):
    """
    Surcouche d'un ORM pour factoriser le code de préparation d'une query
    à partir d'une session et gérer les transactions
    Gère la connexion à la base depuis les infos fournies en variable env
    """
    def __init__(self):
        '''
        Constructor
        '''
        if not "DATASOURCE_URL" in os.environ:
            raise RuntimeError("Environment DATASOURCE_URL is required !")
        
        print(f"Use SQLAlchemy {sqlalchemy.__version__}")
        
        self.engine = sqlalchemy.create_engine(os.environ["DATASOURCE_URL"], echo=True)
        self.sessionFactory = sessionmaker(bind=self.engine)
        
    
    @contextmanager
    def with_new_session(self):
        """
        Création d'une nouvelle session et exécution du code dans la transaction
        La transaction est commitée ou rollbackée en fin de transaction
        """
        session = self.sessionFactory()
        
        try:
            yield session
            session.commit()
        except:
            session.rollback()
            raise
        finally:
            session.close()
            
            
    @contextmanager
    def with_session(self):
        """
        Création d'une nouvelle session si aucune session n'existe dans le thread
        local, sinon récupère cette session. Cela permet d'exécuter plusieurs query
        dans une même transaction.
        La transaction est commitée ou rollbackée en fin de transaction
        """
        session = self.sessionFactory()
        
        try:
            yield session
            session.commit()
        except:
            session.rollback()
            raise
        finally:
            session.close()
    
    
