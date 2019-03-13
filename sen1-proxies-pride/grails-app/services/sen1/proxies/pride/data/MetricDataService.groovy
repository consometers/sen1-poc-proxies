package sen1.proxies.pride.data

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import sen1.proxies.pride.Metric

/**
 * Interface DataService pour l'entité Metric
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Service(Metric)
interface MetricDataService {
	/**
	 * Nombre total d'enités
	 * 
	 * @return
	 */
	long count()
}
