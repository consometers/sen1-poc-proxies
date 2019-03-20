package sen1.proxies.core

import org.springframework.beans.factory.annotation.Autowired

import grails.gorm.transactions.Transactional
import sen1.proxies.core.data.ConsumerDataService
import sen1.proxies.core.service.AbstractDataService

/**
 * Service ConsumerService
 * 
 * Service métier pour la gestion des sen1.proxies.core.Consumer
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Transactional(readOnly = true)
class ConsumerService extends AbstractDataService<Consumer> {
	/**
	 * Service Data Délégué
	 * Tous les appels aux méthodes non présentes dans ce service sont délégués au service data
	 */
	//	@Autowired
	//	//@Delegate
	//	ConsumerDataService consumerDataService
}
