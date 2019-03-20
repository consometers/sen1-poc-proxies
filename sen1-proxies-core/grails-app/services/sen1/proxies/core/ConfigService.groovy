package sen1.proxies.core

import org.springframework.beans.factory.annotation.Autowired

import grails.gorm.transactions.Transactional
import sen1.proxies.core.data.ConfigDataService
import sen1.proxies.core.service.AbstractDataService

/**
 * Service ConfigService
 * 
 * Service métier pour la gestion des sen1.proxies.core.Config
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Transactional(readOnly = true)
class ConfigService extends AbstractDataService<Config> {
	/**
	 * Service Data Délégué
	 * Tous les appels aux méthodes non présentes dans ce service sont délégués au service data
	 */
	//	@Autowired
	//	//@Delegate
	//	ConfigDataService configDataService


	/**
	 * Renvoit la valeur d'une config
	 * 
	 * @param name
	 * @return
	 */
	String value(String name) {
		return Config.findByName(name)?.value
	}
}
