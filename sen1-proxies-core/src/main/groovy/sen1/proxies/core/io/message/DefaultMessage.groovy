package sen1.proxies.core.io.message

import sen1.proxies.core.io.Message
import sen1.proxies.core.io.MessageData

/**
 * Implémentation standard d'un message sur réseau fédéré avec liste de datapoints
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class DefaultMessage implements Message {

	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.core.io.Message#newMessageDataInstance()
	 */
	@Override
	public MessageData newMessageDataInstance() {
		return new DefaultMessageData()
	}
}
