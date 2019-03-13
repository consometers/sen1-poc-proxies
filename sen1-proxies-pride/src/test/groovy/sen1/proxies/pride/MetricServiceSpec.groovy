package sen1.proxies.pride

import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.MessageSource

import grails.config.Config
import grails.core.GrailsApplication
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import sen1.proxies.pride.data.MetricDataService
import spock.lang.Specification

class MetricServiceSpec extends Specification implements ServiceUnitTest<MetricService>, DataTest{

	def setup() {
		mockDomains(Metric, MetricConsumer, MetricValue)
		// le service data doit être injecté manuellement
		service.metricDataService = dataStore.getService(MetricDataService)
	}


	@Rollback
	void "test countWithConsumer"() {
		when:"insert 3 metric"
		new Metric(classname: "classname", labelname: "labelname", labelvalue: "labelvalue1",
		consumers: [
			new MetricConsumer(application: "consumer1")
		]).save(flush: true)
		new Metric(classname: "classname", labelname: "labelname", labelvalue: "labelvalue2",
		consumers: [
			new MetricConsumer(application: "consumer2")
		]).save(flush: true)
		new Metric(classname: "classname", labelname: "labelname", labelvalue: "labelvalue3",
		consumers: []).save(flush: true)

		then:"2 of 3 metric with consumer"
		service.count() == 3
		service.countWithConsumer() == 2
	}
}
