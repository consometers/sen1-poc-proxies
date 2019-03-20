package sen1.proxies.core

import org.springframework.beans.factory.annotation.Autowired

import grails.gorm.transactions.Transactional
import sen1.proxies.core.data.UserDataService
import sen1.proxies.core.service.AbstractDataService

/**
 * Service UserService
 * 
 * Service métier pour la gestion des sen1.proxies.core.User
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Transactional(readOnly = true)
class UserService extends AbstractDataService<User> {
	/**
	 * Service Data Délégué
	 * Tous les appels aux méthodes non présentes dans ce service sont délégués au service data
	 */
	//	@Autowired
	//	//@Delegate
	//	UserDataService userDataService
}
