package sen1.proxies.core.io

/**
 * Représentation d'une valeur de données dans un message sur le réseau fédéré
 * 
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
interface MessageData {
	/**
	 * Valeur de la donnée 
	 */
	Object getValue()
	void setValue(Object value)

	/**
	 * Date donnée
	 */
	Date getTimestamp()
	void setTimestamp(Date timestamp)
}
