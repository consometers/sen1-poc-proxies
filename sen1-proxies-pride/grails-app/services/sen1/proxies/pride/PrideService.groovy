package sen1.proxies.pride

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
import sen1.proxies.pride.warp10.Warp10
import sen1.proxies.pride.warp10.script.Warp10FetchScript

/**
 * Service métier dédié à Pride
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Transactional(readOnly = true)
class PrideService extends AbstractService implements ProxyService<JSONArray> {

	private static final PROXY_URL_CONFIG = "PROXY_URL"

	/**
	 * Instance Warp10 injectée par Spring
	 * Pas de souci singleton car implémentation thread-safe
	 */
	@Autowired
	Warp10 warp10

	@Autowired
	ConfigService configService

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
	List<JSONArray> fetchData(Consumer consumer) throws Exception {
		// Warp10 utilise un nom de série (selector ou classname) commun à plusieurs objets.
		// le consumer est configuré pour des objets multi-value, donc on inverse le raisonnement
		// le label de Warp10 devient notre name/identifiant de l'objet et le classname/selector sera envoyé dans
		// notre sous-valeur metaname. Le metavalue ne nous sert qu'à préciser le label
		Warp10FetchScript fetchScript = new Warp10FetchScript()
				.token(consumer.userApp.token)
				.selector("=${consumer.metaname}{${consumer.metavalue}=${consumer.name}}")

		// soit cette donnée a déjà été lue, et on ne charge que les données plus récentes au dernier chargement
		if (consumer.dateLastValue) {
			// la dernière date correspond au timestamp d'un datapoint
			// le champ start est inclusif, donc on doit décaler la date début d'au moins 1 second
			// pour ne pas recharger 2x le même datapoint
			use(TimeCategory) {
				fetchScript.start(consumer.dateLastValue + 1.second).end(new Date())
			}
		} else {
			// sinon on essaye de lire les MAX dernières valeurs
			fetchScript.end(new Date()).count(firstMaxValue)
		}

		// appel API Warp10 via script
		JSONElement warp10Result = warp10.exec(configService.value(PROXY_URL_CONFIG), fetchScript)

		// parse le contenu de la réponse Warp10 pour récupérer les valeurs
		// le selector demandé est unique, donc on prend la 1ère série du json retourné
		JSONArray datapoints = warp10.datapoints(warp10Result, 0)
		return datapoints
	}


	/** 
	 * Envoit de données vers Pride
	 *
	 * @see sen1.proxies.core.service.ProxyService#pushData(sen1.proxies.core.io.Message)
	 */
	@Override
	void pushData(Message message) throws Exception {
		// TODO Auto-generated method stub

	}
}
