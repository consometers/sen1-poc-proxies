package sen1.proxies.core

import org.grails.testing.GrailsUnitTest

import grails.gorm.transactions.Rollback
import grails.testing.gorm.DataTest
import spock.lang.Specification

class ConsumerServiceSpec extends Specification implements GrailsUnitTest, DataTest {

	/**
	 * Setup avant chaque test
	 * 
	 * @return
	 */
	def setup() {
		mockDomains(Consumer)
		mockDomains(App)
		mockDomains(User)
		mockDomains(UserApp)
	}


	@Rollback
	def "test fetch userApp.user|userApp.app"() {
		when:
		App app = new App(name: 'app1', jid: 'jid1').save(flush: true)
		User user = new User(username: 'user1').save(flush: true)
		UserApp userApp = new UserApp(user: user, app: app).save(flush: true)
		Consumer consumer = new Consumer(consumerApp: app, userApp: userApp, name: 'consumer1').save(flush: true)
		long consumerId = consumer.id
		consumer = service.findByIdFetchAll(consumerId)

		then:
		consumer != null
		consumer.consumerApp.name == app.name
		consumer.userApp.app.name == app.name
		consumer.userApp.user.username == user.username
	}
}