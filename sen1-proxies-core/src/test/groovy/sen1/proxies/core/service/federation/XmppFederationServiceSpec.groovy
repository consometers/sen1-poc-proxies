package sen1.proxies.core.service.federation

import java.util.function.Consumer

import org.grails.testing.GrailsUnitTest

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import sen1.proxies.core.Config
import sen1.proxies.core.OutboxService
import sen1.proxies.core.io.Message
import spock.lang.Ignore
import spock.lang.Specification

class XmppFederationServiceSpec extends Specification implements ServiceUnitTest<XmppFederationService>,DataTest {


	@Override
	Closure doWithSpring() {
		def beans = {
			configService(sen1.proxies.core.ConfigService)
		}
		return beans
	}


	/**
	 * Setup avant chaque test
	 *
	 * @return
	 */
	def setup() {
		mockDomains(Config)

		service.messageConsumer = new Consumer<Message>() {

					@Override
					public void accept(Message arg0) {
					}
				}
	}


	/**
	 * Cleanup après chaque test
	 *
	 * @return
	 */
	def cleanup() {
		service.close()
	}


	@Ignore
	void "connect"() {
		when:
		service.initListener()
		service.initSender()
		service.connect()

		then:
		notThrown Exception
	}
}