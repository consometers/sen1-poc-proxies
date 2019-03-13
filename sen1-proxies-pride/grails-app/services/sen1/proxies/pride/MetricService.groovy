package sen1.proxies.pride

import org.springframework.beans.factory.annotation.Autowired
import grails.gorm.services.Service
import sen1.proxies.core.service.AbstractDataService
import sen1.proxies.core.service.AbstractService
import sen1.proxies.pride.data.MetricDataService

/**
 * Service métier/data Metric
 * La partie data est gérée par le sous-service sen1.proxies.pride.data.MetricDataService
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class MetricService extends AbstractDataService<Metric> {

	@Autowired
	@Delegate
	MetricDataService metricDataService


	/**
	 * Calcule le nombre de metric avec au moins un consumer
	 * 
	 * @return nb metric
	 */
	long countWithConsumer() {
		return Metric.createCriteria().get {
			sizeGt "consumers", 0
			projections { rowCount() }
		}
	}
}
