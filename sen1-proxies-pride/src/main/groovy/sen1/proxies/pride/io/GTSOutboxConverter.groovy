package sen1.proxies.pride.io

import org.grails.web.json.JSONArray
import org.springframework.beans.factory.annotation.Autowired

import sen1.proxies.core.OutboxConsumer
import sen1.proxies.core.io.Message
import sen1.proxies.core.io.MessageData
import sen1.proxies.core.io.OutboxConverter
import sen1.proxies.core.io.message.DefaultMessage
import sen1.proxies.core.io.message.DefaultMessageData
import sen1.proxies.pride.warp10.Warp10

/**
 * Converter GTSOutboxConverter
 * 
 * Implémentation sen1.proxies.core.io.OutboxConverter pour les datapoints GTS
 * 
 * @see https://www.warp10.io/content/03_Documentation/03_Interacting_with_Warp_10/04_Fetching_data/02_GTS_output_format
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class GTSOutboxConverter implements OutboxConverter<JSONArray> {

	@Autowired
	Warp10 warp10


	/**
	 * (non-Javadoc)
	 *
	 * @see sen1.proxies.core.io.OutboxConverter#convert(sen1.proxies.core.OutboxConsumer, sen1.proxies.core.io.Message, java.lang.Object)
	 */
	@Override
	MessageData convert(OutboxConsumer outboxConsumer, Message message, JSONArray data) throws Exception {
		assert data != null
		MessageData messageData = message.newMessageDataInstance()
		messageData.value = warp10.datapointValue(data)
		messageData.timestamp = warp10.datapointTimestamp(data)
		return messageData
	}
}
