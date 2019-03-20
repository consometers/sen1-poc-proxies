package sen1.proxies.core

import grails.gorm.transactions.Rollback
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import sen1.proxies.core.data.InboxDataService
import spock.lang.Specification

class InboxServiceSpec extends Specification implements ServiceUnitTest<InboxService>, DataTest{

	/**
	 * Setup avant chaque test
	 * 
	 * @return
	 */
	def setup() {
		mockDomains(Inbox)
		// le service data doit être injecté manuellement
		//service.inboxDataService = dataStore.getService(InboxDataService)
	}


	@Rollback
	def "test count"() {
		when:
		new Inbox(data: [], receivedDate: new Date()).save(flush: true)
		new Inbox(data: [], receivedDate: new Date()).save(flush: true)

		then:
		service.count() == 2
	}


	@Rollback
	def "test list(pagination)"() {
		when:
		new Inbox(data: [], receivedDate: new Date()).save(flush: true)
		new Inbox(data: [], receivedDate: new Date()).save(flush: true)

		then:
		service.list([:]).size() == 2
		service.list([max:1]).size() == 1
	}
}
