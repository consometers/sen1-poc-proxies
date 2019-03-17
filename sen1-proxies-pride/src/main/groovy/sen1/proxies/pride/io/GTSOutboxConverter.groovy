package sen1.proxies.pride.io

import org.grails.web.json.JSONArray

import sen1.proxies.core.OutboxConsumer
import sen1.proxies.core.io.Message
import sen1.proxies.core.io.OutboxConverter
import sen1.proxies.core.io.message.DefaultMessage
import sen1.proxies.core.io.message.DefaultMessageData

/**
 * Converter GTSOutboxConverter
 * 
 * Implémentation sen1.proxies.core.io.OutboxConverter pour les datapoints GTS
 * 
 * @see https://www.warp10.io/content/03_Documentation/03_Interacting_with_Warp_10/04_Fetching_data/02_GTS_output_format
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class GTSOutboxConverter implements OutboxConverter<JSONArray, DefaultMessageData> {

	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.core.io.OutboxConverter#convert(java.lang.Object)
	 */
	@Override
	Message<DefaultMessageData> convert(OutboxConsumer outboxConsumer, JSONArray data) throws Exception {
		Message message = new DefaultMessage()
		return null
	}
}
