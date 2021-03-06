package sen1.proxies.core

import grails.gorm.transactions.Rollback
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import sen1.proxies.core.data.OutboxDataService
import spock.lang.Specification

class OutboxServiceSpec extends Specification implements ServiceUnitTest<OutboxService>, DataTest{

	/**
	 * Setup avant chaque test
	 * 
	 * @return
	 */
	def setup() {
		mockDomains(Outbox)
		// le service data doit être injecté manuellement
		//service.outboxDataService = dataStore.getService(OutboxDataService)
	}


	@Rollback
	def "test count"() {
		when:
		new Outbox(data: [], receivedDate: new Date()).save(flush: true)
		new Outbox(data: [], receivedDate: new Date()).save(flush: true)

		then:
		service.count() == 2
	}


	@Rollback
	def "test list(pagination)"() {
		when:
		new Outbox(data: [], receivedDate: new Date()).save(flush: true)
		new Outbox(data: [], receivedDate: new Date()).save(flush: true)

		then:
		service.list([:]).size() == 2
		service.list([max:1]).size() == 1
	}
}
