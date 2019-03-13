package sen1.proxies.pride

import org.grails.web.json.JSONArray
import org.grails.web.json.JSONElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

import grails.gorm.transactions.Transactional
import sen1.proxies.core.service.AbstractService
import sen1.proxies.pride.warp10.Warp10
import sen1.proxies.pride.warp10.script.Warp10FetchScript

/**
 * Service métier dédié à Pride
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class PrideService extends AbstractService {

	/**
	 * Instance Warp10 injectée par Spring
	 * Pas de souci singleton car implémentation thread-safe
	 */
	@Autowired
	Warp10 warp10

	/**
	 * Injecté depuis les properties 
	 * @see application.yml
	 */
	@Value('${sen1.proxies.pride.warp10.readToken}')
	String readToken


	/**
	 * Charge les données récentes d'une metric depuis le système Pride et les passe à un consommateur
	 * Si la metric n'a pas encore été chargée au moins une fois (dateLastValue = null), alors on va seulement démarrer
	 * le transfert des données depuis le jour même (on ne remonte pas en arrière).
	 * 
	 * @param metric
	 * @param consume
	 * @throws Exception
	 */
	void consumeValues(Metric metric, Closure consume) throws Exception {
		// date début transfert, soit le début du jour si jamais utilisé, soit la date de la dernière valeur
		Date dateStart = metric.dateLastValue ?: new Date().clearTime()
		Date dateEnd = new Date()

		// charge les infos depuis pride avec warp10
		// utilisation du script fetch pour un retour des valeurs en json
		JSONElement resultScript = warp10.exec(new Warp10FetchScript()
				.token(readToken)
				.selector("=${metric.classname}{${metric.labelname}=${metric.labelvalue}}")
				.start(dateStart)
				.end(dateEnd))

		// parse le contenu de la réponse Warp10 pour récupérer les valeurs
		// le selector demandé est unique, donc on prend la 1ère série du json retourné
		List<JSONArray> datapoints = warp10.parseDatapoints(resultScript, 0)

		// passe ensuite chaque valeur dans la fonction de traitement avec 2 paramètres :
		// la metric et sa valeur convertie
		datapoints?.each { datapoint ->
			consume(metric, new MetricValue(date: warp10.parseDatapointTimestamp(datapoint),
			value: warp10.parseDatapointValue(datapoint)))
		}
	}
}
