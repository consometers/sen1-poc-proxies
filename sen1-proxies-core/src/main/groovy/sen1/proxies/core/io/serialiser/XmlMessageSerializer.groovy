package sen1.proxies.core.io.serialiser

import grails.converters.XML
import sen1.proxies.core.io.Message
import sen1.proxies.core.io.MessageSerializer

/**
 * Implémentation XML d'un message
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class XmlMessageSerializer implements MessageSerializer {

	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.core.io.MessageSerializer#read(byte[])
	 */
	@Override
	Message read(byte[] buffer) throws Exception {
		return new XML().parse(new String(buffer))
	}


	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.core.io.MessageSerializer#write(sen1.proxies.core.io.Message)
	 */
	@Override
	byte[] write(Message message) throws Exception {
		return new XML(message).toString().getBytes()
	}
}
