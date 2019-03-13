package sen1.proxies.pride.data

import grails.gorm.transactions.Rollback
import grails.test.hibernate.HibernateSpec
import grails.testing.gorm.DataTest
import sen1.proxies.pride.Metric
import spock.lang.Shared

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class MetricDataServiceSpec extends HibernateSpec implements DataTest {
	@Shared
	MetricDataService metricDataService


	def setup()  {
		mockDomain(Metric)
		metricDataService = hibernateDatastore.getService(MetricDataService)
	}


	@Rollback
	def "test count"() {
		when:
		new Metric(classname: "classname", labelname: "labelname", labelvalue: "labelvalue1").save(flush: true)
		new Metric(classname: "classname", labelname: "labelname", labelvalue: "labelvalue2").save(flush: true)

		then:
		metricDataService.count() == 2
	}
}
