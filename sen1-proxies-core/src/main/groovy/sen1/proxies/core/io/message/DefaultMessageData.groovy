package sen1.proxies.core.io.message

import sen1.proxies.core.io.MessageData

/**
 * Implémentation standard d'un MessageData sous la forme d'un datapoint( timestamp / valeur)
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class DefaultMessageData implements MessageData<String> {
	/**
	 * Date donnée
	 */
	Date timestamp
}
