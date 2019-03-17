package sen1.proxies.pride

import org.grails.web.json.JSONArray
import org.grails.web.json.JSONElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

import grails.gorm.transactions.Transactional
import sen1.proxies.core.OutboxConsumer
import sen1.proxies.core.service.AbstractService
import sen1.proxies.core.service.PushOutboxService
import sen1.proxies.pride.warp10.Warp10
import sen1.proxies.pride.warp10.script.Warp10FetchScript

/**
 * Service métier dédié à Pride
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class PrideService extends AbstractService implements PushOutboxService {

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
	 * Injecté depuis les properties
	 * @see application.yml
	 */
	@Value('${sen1.proxies.pride.warp10.firstMaxValue}')
	int firstMaxValue


	/** 
	 * Charge les données récentes depuis le système Pride
	 * Si data n'a pas encore été chargée au moins une fois (dateLastValue = null), alors on va essayer de remonter
	 * les MAX dernières valeurs
	 *
	 * @see sen1.proxies.core.service.PushOutboxService#fetchData(sen1.proxies.core.OutboxConsumer)
	 */
	@Override
	List fetchData(OutboxConsumer outboxConsumer) throws Exception {
		Warp10FetchScript fetchScript = new Warp10FetchScript()
				.token(readToken)
				.selector("=${outboxConsumer.name}{${outboxConsumer.metaname}=${outboxConsumer.metavalue}}")

		// soit cette donnée a déjà été lue, et on ne charge que les données plus récentes au dernier chargement
		if (outboxConsumer.dateLastValue) {
			fetchScript.start(outboxConsumer.dateLastValue).end(new Date())
		} else {
			// sinon on essaye de lire les MAX dernières valeurs
			fetchScript.end(new Date()).count(firstMaxValue)
		}

		// appel API Warp10 via script
		JSONElement warp10Result = warp10.exec(fetchScript)

		// parse le contenu de la réponse Warp10 pour récupérer les valeurs
		// le selector demandé est unique, donc on prend la 1ère série du json retourné
		return warp10.datapoints(warp10Result, 0)
	}
}
