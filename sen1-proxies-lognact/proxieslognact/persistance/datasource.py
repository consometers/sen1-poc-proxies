"""
Module datasource

@author: Gregory Elléouet
"""

import os
import logging

import sqlalchemy
from sqlalchemy.orm import sessionmaker
from sqlalchemy.orm import scoped_session

from proxieslognact import applicationContext


class Datasource(object):
    """
    Surcouche d'un ORM pour factoriser le code de préparation d'une query
    à partir d'une session et gérer les transactions
    Gère la connexion à la base depuis les infos fournies en variable env
    
    Utilise les décorateurs pour démarrer une session/transaction au niveau
    méthode ou dans le corps d'une méthode
    """
    def __init__(self):
        '''
        Constructor
        '''
        self.engine = None
        assert "DATASOURCE_URL" in os.environ, "Environment DATASOURCE_URL is required !"
        
    
    def _init_datasource(self):
        """
        Prépare le datasource après que toutes les propriétés soient injectées
        A ne faire qu'une fois pour démarrerle Engine
        """
        if (self.engine == None):
            showSql = False
            
            if (logging.getLogger("sqlachemy.sql") and logging.getLogger("sqlachemy.sql").level == logging.DEBUG):
                showSql = True
                
            self.logger.info(f"Start SQLAlchemy {sqlalchemy.__version__} engine...")
            self.engine = sqlalchemy.create_engine(os.environ["DATASOURCE_URL"],
                                                   echo = showSql)
            self.sessionFactory = sessionmaker(bind=self.engine)
            self.threadLocalSessionFactory = scoped_session(self.sessionFactory)
        
        
    def __del__(self):
        """
        Destructor
        Libère la connexion
        """
        if (self.engine != None):
            self.logger.info("Closing datasource...")
            #self.engine.dispose()
    
    
    def currentSession(self):
        """
        Récupère la session courante dans le thread loca. Si aucune session
        existe, une nouvelle est créé
        """
        self._init_datasource()
        return self.threadLocalSessionFactory()
    
    
    
    def closeCurrentSession(self):
        """
        Ferme la session courante. si la transaction n'a pas été commitée, elle
        sera rollbackée automatiquement
        """
        self.threadLocalSessionFactory.remove()
    
    
    
def transactional(readonly = True):
    def transactional_decorator(function):
        """
        Decorateur methode : démarre une transaction à partir d'une session du 
        thread local. Ouvre une nouvelle session s'il n'en existe pas.
        
        En fin de méthode, la transaction est commitée si aucune erreur n'a été levée.
        elle est rollbackée dans le cas contraire
        """
        def wrapper(*args):
            # récupère la session courante depuis le datasource
            datasource = applicationContext.bean("datasource")
            session = datasource.currentSession()
            response = None
            
            # inject session in argument
            # !! il faut que la méthode ait déclaté un objet session optionnel en 
            # dernier argument !!
            newargs = args + (session,)
            
            try:
                response = function(*newargs)
                
                if (not readonly):
                    session.commit()
            except:
                session.rollback()
                raise
            finally:
                datasource.closeCurrentSession()
                
            return response
        
        return wrapper
    
    return transactional_decorator
        
        
        
        
