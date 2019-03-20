package sen1.proxies.core

import org.springframework.beans.factory.annotation.Autowired

import grails.gorm.transactions.Transactional
import sen1.proxies.core.data.AppDataService
import sen1.proxies.core.service.AbstractDataService

/**
 * Service AppService
 * 
 * Service métier pour la gestion des sen1.proxies.core.App
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Transactional(readOnly = true)
class AppService extends AbstractDataService<App> {
	/**
	 * Service Data Délégué
	 * Tous les appels aux méthodes non présentes dans ce service sont délégués au service data
	 */
	//@Autowired
	//@Delegate
	//AppDataService appDataService


	/**
	 * Recherche d'une application par son nom
	 * 
	 * @param name
	 * @return App
	 */
	App findByName(String name) {
		App.findByName(name)
	}
}
