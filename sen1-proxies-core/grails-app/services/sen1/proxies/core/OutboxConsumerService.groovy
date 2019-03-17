package sen1.proxies.core

import org.springframework.beans.factory.annotation.Autowired

import grails.gorm.transactions.Transactional
import sen1.proxies.core.data.OutboxConsumerDataService
import sen1.proxies.core.service.AbstractDataService

/**
 * Service OutboxConsumerService
 * 
 * Service métier pour la gestion des sen1.proxies.core.OutboxConsumer
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class OutboxConsumerService extends AbstractDataService<OutboxConsumer> {
	/**
	 * Service Data Délégué
	 * Tous les appels aux méthodes non présentes dans ce service sont délégués au service data
	 */
	@Autowired
	@Delegate
	OutboxConsumerDataService outboxConsumerDataService
}
