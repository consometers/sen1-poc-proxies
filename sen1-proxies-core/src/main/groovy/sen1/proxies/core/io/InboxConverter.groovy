package sen1.proxies.core.io

/**
 * Converter Inbox
 * 
 * Convertit un message reçu depuis le réseau fédéré vers le format du système local 
 * Chaque proxy devra définir son propre converter
 * 
 * Réseau fédéré --> Inbox --> Système local
 *                         |
 *                     InboxConverter
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
interface InboxConverter<T, U extends MessageData> {
	/**
	 * Lecture d'une donnée reçue du réseau fédéré et conversion en donnée du système local
	 * 
	 * @param message réseau fédéré
	 * @return <T>
	 * @throws Exception
	 */
	T convert(Message<U> message) throws Exception
}
