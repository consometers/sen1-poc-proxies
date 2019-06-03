"""
Module user_app

@author: Gregory Elléouet
"""

from proxieslognact.model.user_app import UserApp
from proxieslognact.model.user import User
from proxieslognact.model.app import App
from proxieslognact.persistance.datasource import transactional
from macpath import join


class UserAppService(object):
    
    @transactional()
    def findByUserAndApplication(self, username, appname, session=None):
        """
        Recherche d'une application par son nom
        
        :param username: le nom du user
        :param appname: le nom de l'application émettrice
        :param session: auto injecté par le decorator transactional
        
        :return App
        """
        return session.query(UserApp) \
                .join(UserApp.app) \
                .join(UserApp.user) \
                .filter(User.username == username) \
                .filter(App.name == appname) \
                .one()
        
        