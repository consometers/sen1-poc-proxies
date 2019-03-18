package sen1.proxies.pride.job

import org.grails.testing.GrailsUnitTest

import sen1.proxies.core.ApplicationUtils
import sen1.proxies.core.job.PushOutboxSubJob
import sen1.proxies.pride.PrideService
import spock.lang.Shared
import spock.lang.Specification

class PushOutboxSubJobSpec extends Specification implements GrailsUnitTest {

	@Shared
	PushOutboxSubJob pushOutboxSubJob



	@Override
	Closure doWithSpring() {
		def beans = {
			outboxConsumerService(sen1.proxies.core.OutboxConsumerService)
			outboxService(sen1.proxies.core.OutboxService)
			prideService(sen1.proxies.pride.PrideService)
			warp10(sen1.proxies.pride.warp10.impl.Warp10v0)
			outboxConverter(sen1.proxies.pride.io.GTSOutboxConverter)
			messageSerializer(sen1.proxies.core.io.serialiser.XmlMessageSerializer)
		}
		return beans
	}


	/**
	 * Exécuté avant chaque test
	 *
	 * @return
	 */
	def setup() {
		pushOutboxSubJob = new PushOutboxSubJob()
	}


	/**
	 * Exécuté après chaque test
	 * 
	 * @return
	 */
	def cleanup() {
	}


	/**
	 * On fait ce test pour s'assurer que la propriété "pushOutboxService" soit bien injecté avec le service 
	 * PrideService car par défaut l'injection se fait avec le name et non pas le type, or notre "pushOutboxService"
	 * est bien un objet de type PrideService
	 */
	void "injection service"() {
		when:
		ApplicationUtils.autowireBean(getGrailsApplication(), pushOutboxSubJob)

		then:
		pushOutboxSubJob.messageSerializer != null
		pushOutboxSubJob.outboxConsumerService != null
		pushOutboxSubJob.outboxService != null
		pushOutboxSubJob.outboxConverter != null
		pushOutboxSubJob.pushOutboxService != null
		pushOutboxSubJob.pushOutboxService instanceof PrideService
	}
}