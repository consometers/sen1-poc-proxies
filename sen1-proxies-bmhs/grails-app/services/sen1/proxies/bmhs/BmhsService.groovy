package sen1.proxies.bmhs

import org.grails.web.json.JSONElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

import grails.gorm.transactions.Transactional
import groovy.time.TimeCategory
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy
import sen1.proxies.bmhs.api.BmhsApi
import sen1.proxies.bmhs.api.BmhsFetchMessage
import sen1.proxies.bmhs.api.BmhsPushMessage
import sen1.proxies.core.ConfigService
import sen1.proxies.core.Consumer
import sen1.proxies.core.UserApp
import sen1.proxies.core.UserAppService
import sen1.proxies.core.io.Message
import sen1.proxies.core.io.MessageData
import sen1.proxies.core.service.AbstractService
import sen1.proxies.core.service.ProxyService

/**
 * Service métier dédié à BMHS
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Transactional(readOnly = true)
class BmhsService extends AbstractService implements ProxyService<JSONElement> {

	private static final PROXY_URL_CONFIG = "PROXY_URL"

	@Autowired
	BmhsApi bmhsApi

	@Autowired
	ConfigService configService

	@Autowired
	UserAppService userAppService

	/**
	 * Injecté depuis les properties
	 * @see application.yml
	 */
	@Value('${sen1.proxies.bmhs.firstMaxValue}')
	long firstMaxValue


	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.core.service.ProxyService#fetchData(sen1.proxies.core.Consumer)
	 */
	@Override
	List<JSONElement> fetchData(Consumer consumer) throws Exception {
		BmhsFetchMessage request = new BmhsFetchMessage()

		request.application = consumer.consumerApp.name
		request.token = consumer.userApp.token
		request.name = consumer.name
		request.metaname = consumer.metaname

		if (consumer.dateLastValue) {
			// on décale d'au moins 1sec pour ne pas récupérer la dernière
			// donnée déjà récupérée
			use(TimeCategory) {
				request.start = consumer.dateLastValue + 1.second
			}
		} else {
			// si aucune donnée, on essaye de remonter les X derniers jours
			request.order = "desc"
			request.limit = firstMaxValue
		}

		return bmhsApi.fetch(configService.value(PROXY_URL_CONFIG), request)
	}


	/** 
	 * Envoit des données vers BMHS
	 * Utilise l'API publique @see https://github.com/gelleouet/smarthome-application/wiki/API
	 *
	 * @see sen1.proxies.core.service.ProxyService#pushData(sen1.proxies.core.io.Message)
	 */
	@Override
	void pushData(Message message) throws Exception {
		// recherche du user associé au message pour récupérer son token de connexion
		UserApp userApp = userAppService.findByUserAndApplication(message.username, message.applicationSrc)

		if (!userApp) {
			throw new Exception("Cannot push data : userApp[${message.username}:${message.applicationSrc}] not found !")
		}

		// construction du message BMHS à partir du message proxy
		BmhsPushMessage bmhsMessage = new BmhsPushMessage()
				.token(userApp.token)
				.application(message.applicationSrc)
				.name(message.name)
				.metaname(message.metaname)
				.unite(message.unite)

		// charge les données dans le message
		for (MessageData data : message.datas) {
			bmhsMessage.addData(data.value, data.timestamp)
		}

		// envoit des données sur BMHS
		bmhsApi.push(configService.value(PROXY_URL_CONFIG), bmhsMessage)
	}


	/** 
	 * (non-Javadoc)
	 *
	 * @see sen1.proxies.core.service.ProxyService#convertData(sen1.proxies.core.io.Message, java.lang.Object)
	 */
	@Override
	public MessageData convertData(Message message, JSONElement data) throws Exception {
		return message.newMessageDataInstance(data.value, new Date(data.timestamp as Long))
	}
}
