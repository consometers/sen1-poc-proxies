/**
 * 
 */
package sen1.proxies.enedis.io

import org.grails.web.json.JSONArray
import org.grails.web.json.JSONElement

import sen1.proxies.core.io.Message
import sen1.proxies.core.io.MessageData
import sen1.proxies.core.io.OutboxConverter

/**
 * DataConnectOutboxConverter
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class DataConnectOutboxConverter implements OutboxConverter<JSONElement> {

	/**
	 * @see sen1.proxies.core.io.OutboxConverter#convert(sen1.proxies.core.io.Message, java.lang.Object)
	 */
	@Override
	MessageData convert(Message message, JSONElement data) throws Exception {
		assert data != null
		return message.newMessageDataInstance(data.value, data.timestamp)
	}
}
