package sen1.proxies.bmhs.api.impl

import grails.converters.JSON
import sen1.proxies.bmhs.api.BmhsApi
import sen1.proxies.bmhs.api.BmhsMessage
import sen1.proxies.core.http.Http

/**
 * Implémentation VO BmhsAPi
 * 
 * @see https://github.com/gelleouet/smarthome-application/wiki/API
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class BmhsApiv0 implements BmhsApi {

	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.bmhs.api.BmhsApi#push(java.lang.String, sen1.proxies.bmhs.api.BmhsMessage)
	 */
	@Override
	void push(String url, BmhsMessage message) throws Exception {
		Http http = Http.Post("${url}/api/device/push")
				.header("Authorization", message.token)
				.header("Content-Type", "application/json")
				.bodyString(new JSON(message).toString(true))

		http.execute()
	}
}
