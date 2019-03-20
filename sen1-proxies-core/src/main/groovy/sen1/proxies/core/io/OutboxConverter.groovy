package sen1.proxies.core.io

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
interface OutboxConverter<T> {
	/**
	 * Convertit une donnée du système local vers une donnée du réseau fédéré
	 * Le message parent est déjà construit.
	 * Ce converter doit juste s'occuper de remplir les datas du message et compléter si besoin le message
	 * mais le message est logiquement déjà prêt
	 * 
	 * @param message
	 * @param data donnée au format <T> à convertir
	 * @return MessageData
	 * @throws Exception
	 */
	MessageData convert(Message message, T data) throws Exception
}
