package sen1.proxy.bmhs

import org.grails.web.json.JSONArray
import org.springframework.beans.factory.annotation.Autowired

import grails.gorm.transactions.Transactional
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy
import sen1.proxies.bmhs.api.BmhsApi
import sen1.proxies.bmhs.api.BmhsMessage
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
class BmhsService extends AbstractService implements ProxyService<JSONArray> {

	private static final PROXY_URL_CONFIG = "PROXY_URL"

	@Autowired
	BmhsApi bmhsApi

	@Autowired
	ConfigService configService

	@Autowired
	UserAppService userAppService


	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.core.service.ProxyService#fetchData(sen1.proxies.core.Consumer)
	 */
	@Override
	List<JSONArray> fetchData(Consumer consumer) throws Exception {
		throw new Exception("Not implemented !")
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
		BmhsMessage bmhsMessage = new BmhsMessage()
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
}
