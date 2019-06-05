package sen1.proxies.bmhs.api.impl

import org.grails.web.json.JSONElement

import grails.converters.JSON
import sen1.proxies.bmhs.api.BmhsApi
import sen1.proxies.bmhs.api.BmhsFetchMessage
import sen1.proxies.bmhs.api.BmhsMessageData
import sen1.proxies.bmhs.api.BmhsPushMessage
import sen1.proxies.core.DateUtils
import sen1.proxies.core.http.Http
import sen1.proxies.core.http.transformer.JsonResponseTransformer

/**
 * Implémentation VO BmhsAPi
 * 
 * @see https://github.com/gelleouet/smarthome-application/wiki/API
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class BmhsApiv0 implements BmhsApi {

	/** 
	 * (non-Javadoc)
	 *
	 * @see sen1.proxies.bmhs.api.BmhsApi#push(java.lang.String, sen1.proxies.bmhs.api.BmhsMessage)
	 */
	@Override
	void push(String url, BmhsPushMessage message) throws Exception {
		Http http = Http.Post("${url}/api/device/push")
				.header("Authorization", message.token)
				.header("Content-Type", "application/json")
				.bodyString(new JSON(message).toString(true))

		http.execute()
	}

	/** 
	 * (non-Javadoc)
	 *
	 * @see sen1.proxies.bmhs.api.BmhsApi#fetch(java.lang.String, sen1.proxies.bmhs.api.BmhsFetchMessage)
	 */
	@Override
	List<JSONElement> fetch(String url, BmhsFetchMessage message) throws Exception {
		def body = new JSON(message)

		// on remplace les dates par le format ISO
		if (body.start) {
			body.start = DateUtils.formatDateTimeIso(body.start)
		}
		if (body.end) {
			body.end = DateUtils.formatDateTimeIso(body.end)
		}

		Http http = Http.Post("${url}/api/device/fetch")
				.header("Authorization", message.token)
				.header("Content-Type", "application/json")
				.bodyString(body.toString(true))

		JSONElement response = http.execute(new JsonResponseTransformer())?.content

		if (!response || !response.datas) {
			throw new Exception("Fetch response is empty !")
		}

		return response.datas
	}
}
