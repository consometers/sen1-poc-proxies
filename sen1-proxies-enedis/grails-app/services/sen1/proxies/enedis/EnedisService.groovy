package sen1.proxies.enedis

import org.grails.web.json.JSONArray
import org.grails.web.json.JSONElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

import grails.gorm.transactions.Transactional
import groovy.time.TimeCategory
import sen1.proxies.core.ConfigService
import sen1.proxies.core.Consumer
import sen1.proxies.core.io.Message
import sen1.proxies.core.service.AbstractService
import sen1.proxies.core.service.ProxyService
import sen1.proxies.enedis.api.DataConnect
import sen1.proxies.enedis.api.MetricRequest

/**
 * Service métier dédié à Enedis
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Transactional(readOnly = true)
class PrideService extends AbstractService implements ProxyService<JSONElement> {

	private static final PROXY_URL_CONFIG = "PROXY_URL"

	/**
	 * Instance DataConnect injectée par Spring
	 * Pas de souci singleton car implémentation thread-safe
	 */
	@Autowired
	DataConnect dataConnect

	@Autowired
	ConfigService configService

	/**
	 * Injecté depuis les properties
	 * @see application.yml
	 */
	@Value('${sen1.proxies.enedis.dataconnect.maxMonthPeriod}')
	int maxMonthPeriod


	/** 
	 * Charge les données récentes depuis Enedis via DataConnect
	 * Si data n'a pas encore été chargée au moins une fois (dateLastValue = null),
	 * alors on va essayer de remonter les MAX dernières valeurs
	 *
	 * @see sen1.proxies.core.service.PushOutboxService#fetchData(sen1.proxies.core.OutboxConsumer)
	 */
	@Override
	List<JSONElement> fetchData(Consumer consumer) throws Exception {
		MetricRequest metricRequest = new MetricRequest()
		
		metricRequest.token = consumer.userApp.token
		metricRequest.end = new Date().clearTime()
		metricRequest.usagePointId = consumer.name
		
		use(TimeCategory) {
			if (consumer.dateLastValue) {
				// les données sont envoyées sur une journée complète
				// donc il faut passer à la journée suivante
				metricRequest.start = consumer.dateLastValue + 1.day
			} else {
				// si aucune donnée, on essaye de remonter les derniers mois
				metricRequest.start = metricRequest.end - 7.days
			}
		}
		
		return dataConnect.consumptionLoadCurve(metricRequest)
	}


	/** 
	 * Envoit de données vers Pride
	 *
	 * @see sen1.proxies.core.service.ProxyService#pushData(sen1.proxies.core.io.Message)
	 */
	@Override
	void pushData(Message message) throws Exception {
		throw new Exception('Enedis DataConnect not support this operation !')
	}
}
