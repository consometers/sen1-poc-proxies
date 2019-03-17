package sen1.proxies.core

import grails.gorm.transactions.Rollback
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import sen1.proxies.core.data.OutboxConsumerDataService
import spock.lang.Specification

class OutboxConsumerServiceSpec extends Specification implements ServiceUnitTest<OutboxConsumerService>, DataTest{

	/**
	 * Setup avant chaque test
	 * 
	 * @return
	 */
	def setup() {
		mockDomains(OutboxConsumer)
		// le service data doit être injecté manuellement
		service.outboxConsumerDataService = dataStore.getService(OutboxConsumerDataService)
	}


	@Rollback
	def "test count"() {
		when:
		new OutboxConsumer(consumer: "consumer", name: "name1").save(flush: true)
		new OutboxConsumer(consumer: "consumer", name: "name2").save(flush: true)

		then:
		service.count() == 2
	}


	@Rollback
	def "test list(pagination)"() {
		when:
		new OutboxConsumer(consumer: "consumer", name: "name1").save(flush: true)
		new OutboxConsumer(consumer: "consumer", name: "name2").save(flush: true)

		then:
		service.list([:]).size() == 2
		service.list([max:1]).size() == 1
	}
}
