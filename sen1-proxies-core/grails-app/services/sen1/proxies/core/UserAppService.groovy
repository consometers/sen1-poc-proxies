package sen1.proxies.core

import org.springframework.beans.factory.annotation.Autowired

import grails.gorm.transactions.Transactional
import sen1.proxies.core.service.AbstractDataService

/**
 * Service UserAppService
 * 
 * Service métier pour la gestion des sen1.proxies.core.User
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Transactional(readOnly = true)
class UserAppService extends AbstractDataService<UserApp> {
	/**
	 * Service Data Délégué
	 * Tous les appels aux méthodes non présentes dans ce service sont délégués au service data
	 */
	//	@Autowired
	//	//@Delegate
	//	UserDataService userDataService

	/**
	 * Recherche unique par user et application
	 * 
	 * @param username
	 * @param applicationName
	 * @return
	 */
	UserApp findByUserAndApplication(String userName, String applicationName) {
		return UserApp.createCriteria().get {
			user { eq 'username', userName	 }
			app { eq 'name', applicationName	 }
		}
	}
}
