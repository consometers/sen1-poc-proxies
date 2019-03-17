package sen1.proxies.core.io

import sen1.proxies.core.OutboxConsumer

/**
 * Converter Outbox
 * 
 * Convertit une data reçue du système local en une donnée du réseau fédéré
 * Chaque proxy devra définir son propre converter
 * 
 *  Système local --> Outbox --> Réseau fédéré
 *                 |
 *             OutboxConverter
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
interface OutboxConverter<T, U extends MessageData> {
	/**
	 * Convertir une donnée du système local vers une donnée du réseau fédéré
	 * 
	 * @param outboxConsumer
	 * @param data
	 * @return message
	 * @throws Exception
	 */
	Message<U> convert(OutboxConsumer outboxConsumer, T data) throws Exception
}
