package sen1.proxies.core.io.message

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement

import sen1.proxies.core.io.MessageData

/**
 * Implémentation standard d'un MessageData
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
class DefaultMessageData implements MessageData {
	Object value
	Date timestamp
}
