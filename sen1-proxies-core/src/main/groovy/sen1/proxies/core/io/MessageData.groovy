package sen1.proxies.core.io

/**
 * Représentation d'une valeur de données dans un message sur le réseau fédéré
 * 
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
trait MessageData<T> {
	/**
	 * Valeur de la donnée 
	 */
	T value
}
