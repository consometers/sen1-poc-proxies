package sen1.proxies.core

import org.springframework.beans.factory.annotation.Autowired

import grails.gorm.transactions.Transactional
import sen1.proxies.core.data.InboxDataService
import sen1.proxies.core.service.AbstractDataService

/**
 * Service InboxService
 * 
 * Service métier pour la gestion des sen1.proxies.core.Inbox
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Transactional(readOnly = true)
class InboxService extends AbstractDataService<Inbox> {
	/**
	 * Service Data Délégué
	 * Tous les appels aux méthodes non présentes dans ce service sont délégués au service data
	 */
	//	@Autowired
	//	//@Delegate
	//	InboxDataService inboxDataService
}
