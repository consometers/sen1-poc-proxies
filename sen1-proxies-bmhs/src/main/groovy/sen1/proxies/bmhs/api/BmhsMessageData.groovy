package sen1.proxies.bmhs.api

import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy

/**
 * Les données d'un message
 * 
 * @see https://github.com/gelleouet/smarthome-application/wiki/API
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Builder(builderStrategy=SimpleStrategy, prefix="")
class BmhsMessageData {
	/**
	 * [required] La valeur typée
	 */
	Object value

	/**
	 * [required] millisecondes depuis le 01/01/1970
	 */
	long timestamp
}
