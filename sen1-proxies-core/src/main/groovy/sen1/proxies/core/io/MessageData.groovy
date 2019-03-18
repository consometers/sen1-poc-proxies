package sen1.proxies.core.io

/**
 * Représentation d'une valeur de données dans un message sur le réseau fédéré
 * 
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
trait MessageData {
	/**
	 * Valeur de la donnée 
	 */
	Object value

	/**
	 * Date donnée
	 */
	Date timestamp
}
