package sen1.proxies.core

import org.springframework.beans.factory.annotation.Autowired

import grails.gorm.transactions.Transactional
import sen1.proxies.core.data.OutboxDataService
import sen1.proxies.core.service.AbstractDataService


/**
 * Service OutboxService
 * 
 * Service métier pour la gestion des sen1.proxies.core.Outbox
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class OutboxService extends AbstractDataService<Outbox> {
	/**
	 * Service Data Délégué
	 * Tous les appels aux méthodes non présentes dans ce service sont délégués au service data
	 */
	@Autowired
	@Delegate
	OutboxDataService outboxDataService
}
